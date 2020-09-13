package net.jimblackler.jsonschematypes;

import static net.jimblackler.jsonschemafriend.Validator.validate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.jimblackler.jsonschemafriend.DocumentSource;
import net.jimblackler.jsonschemafriend.DocumentUtils;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.UrlRewriter;
import net.jimblackler.jsonschemafriend.ValidationError;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class SuiteTest {
  public static final FileSystem FILE_SYSTEM = FileSystems.getDefault();

  private static DynamicNode scan(Collection<Path> testDirs, Path remotes, URI metaSchema) {
    Collection<DynamicNode> allFileTests = new ArrayList<>();
    for (Path testDir : testDirs) {
      try (InputStream inputStream = SuiteTest.class.getResourceAsStream(testDir.toString());
           BufferedReader bufferedReader =
               new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        String resource;
        while ((resource = bufferedReader.readLine()) != null) {
          if (!resource.endsWith(".json")) {
            continue;
          }
          Collection<DynamicNode> nodes = new ArrayList<>();
          try (InputStream inputStream1 =
                   SuiteTest.class.getResourceAsStream(testDir.resolve(resource).toString())) {
            JSONArray data = (JSONArray) DocumentUtils.loadJson(inputStream1);
            for (int idx = 0; idx != data.length(); idx++) {
              JSONObject testSet = data.getJSONObject(idx);
              if (!testSet.has("schema")) {
                continue; // ever happens?
              }

              Collection<DynamicTest> everitTests = new ArrayList<>();
              Collection<DynamicTest> ownTests = new ArrayList<>();
              Object schema = testSet.get("schema");
              URL resource1 = SuiteTest.class.getResource(remotes.toString());
              UrlRewriter urlRewriter = in
                  -> URI.create(
                      in.toString().replace("http://localhost:1234", resource1.toString()));
              if (schema instanceof JSONObject) {
                JSONObject schema1 = (JSONObject) schema;
                schema1.put("$schema", metaSchema.toString());
              }
              JSONArray tests1 = testSet.getJSONArray("tests");
              for (int idx2 = 0; idx2 != tests1.length(); idx2++) {
                JSONObject test = tests1.getJSONObject(idx2);
                Object data1 = test.get("data");
                boolean valid = test.getBoolean("valid");
                String description =
                    test.optString("description", data1 + (valid ? " succeeds" : " fails"));

                ownTests.add(dynamicTest(description, () -> {
                  System.out.println("Schema:");
                  if (schema instanceof JSONObject) {
                    System.out.println(((JSONObject) schema).toString(2));
                  } else {
                    System.out.println(schema);
                  }
                  System.out.println();

                  DocumentSource documentSource = new DocumentSource(List.of(urlRewriter));
                  URI local = new URI("memory", "local", null, null);
                  documentSource.store(local, schema);
                  SchemaStore schemaStore = new SchemaStore(documentSource);
                  net.jimblackler.jsonschemafriend.Schema schema1 = schemaStore.loadSchema(local);

                  System.out.println("Test:");
                  System.out.println(test.toString(2));
                  System.out.println();

                  List<ValidationError> errors = new ArrayList<>();
                  validate(schema1, data1, URI.create(""), errors::add);

                  System.out.print("Expected to " + (valid ? "pass" : "fail") + " ... ");
                  if (errors.isEmpty()) {
                    System.out.println("Passed");
                  } else {
                    System.out.println("Failures:");
                    for (ValidationError error : errors) {
                      System.out.println(error);
                    }
                    System.out.println();
                  }

                  assertEquals(errors.isEmpty(), valid);
                }));
                if (schema instanceof JSONObject) {
                  everitTests.add(dynamicTest(description, () -> {
                    JSONObject schema1 = (JSONObject) schema;
                    System.out.println("Schema:");
                    System.out.println(schema1.toString(2));
                    System.out.println();

                    Schema everitSchema = SchemaLoader.load(schema1, url -> {
                      url = url.replace("http://localhost:1234", resource1.toString());
                      try {
                        return new URL(url).openStream();
                      } catch (IOException e1) {
                        throw new UncheckedIOException(e1);
                      }
                    });

                    System.out.println("Test:");
                    System.out.println(test.toString(2));
                    System.out.println();

                    List<String> failures = null;
                    try {
                      everitSchema.validate(data1);
                    } catch (ValidationException ex) {
                      System.out.println(ex.toJSON());
                      failures = ex.getAllMessages();
                    } catch (Exception e1) {
                      fail(e1);
                    }

                    if (failures != null) {
                      System.out.println("Failures:");
                      for (String message : failures) {
                        System.out.println(message);
                      }
                      System.out.println();
                    }

                    System.out.print("Expeced to " + (valid ? "pass" : "fail") + " ... ");
                    System.out.println((failures == null ? "passed" : "failed"));

                    assertEquals(failures == null, valid);
                  }));
                }
              }
              ownTests.add(dynamicTest("schema", () -> {
                System.out.println("Schema:");
                if (schema instanceof JSONObject) {
                  System.out.println(((JSONObject) schema).toString(2));
                } else {
                  System.out.println(schema);
                }
                System.out.println();

                DocumentSource documentSource = new DocumentSource(List.of(urlRewriter));
                SchemaStore schemaStore = new SchemaStore(documentSource);
                URI local = new URI("memory", "local", null, null);
                documentSource.store(local, schema);
                schemaStore.loadSchema(local);
              }));

              List<DynamicContainer> dynamicNodes = List.of(
                  dynamicContainer("everit", everitTests), dynamicContainer("own", ownTests));
              nodes.add(dynamicContainer(testSet.getString("description"), dynamicNodes));
            }
          }
          DynamicNode e = dynamicContainer(resource, nodes);
          allFileTests.add(e);
        }
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }

    return dynamicContainer("name", allFileTests);
  }

  @TestFactory
  DynamicNode own() {
    Path path = FILE_SYSTEM.getPath("/suites");
    Path own = path.resolve("own");
    return scan(List.of(own), path.resolve("own_remotes"),
        URI.create("http://json-schema.org/draft-07/schema#"));
  }

  @TestFactory
  DynamicNode draft3() {
    return test("draft3", "http://json-schema.org/draft-03/schema#");
  }

  @TestFactory
  DynamicNode draft4() {
    return test("draft4", "http://json-schema.org/draft-04/schema#");
  }

  @TestFactory
  DynamicNode draft6() {
    return test("draft6", "http://json-schema.org/draft-06/schema#");
  }

  @TestFactory
  DynamicNode draft7() {
    return test("draft7", "http://json-schema.org/draft-07/schema#");
  }

  public DynamicNode test(String set, String metaSchema) {
    Path suite = FILE_SYSTEM.getPath("/suites").resolve("JSON-Schema-Test-Suite");
    Path tests = suite.resolve("tests").resolve(set);
    return scan(List.of(tests, tests.resolve("optional")), suite.resolve("remotes"),
        URI.create(metaSchema));
  }
}
