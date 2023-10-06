package net.jimblackler.jsonschemafriend;

import com.fasterxml.jackson.databind.ObjectMapper;

class MetaSchemaDraft06 {
    private static final String SCHEMA_JSON = "{\n" +
            "    \"$schema\": \"http://json-schema.org/draft-06/schema#\",\n" +
            "    \"$id\": \"http://json-schema.org/draft-06/schema#\",\n" +
            "    \"title\": \"Core schema meta-schema\",\n" +
            "    \"definitions\": {\n" +
            "        \"schemaArray\": {\n" +
            "            \"type\": \"array\",\n" +
            "            \"minItems\": 1,\n" +
            "            \"items\": { \"$ref\": \"#\" }\n" +
            "        },\n" +
            "        \"nonNegativeInteger\": {\n" +
            "            \"type\": \"integer\",\n" +
            "            \"minimum\": 0\n" +
            "        },\n" +
            "        \"nonNegativeIntegerDefault0\": {\n" +
            "            \"allOf\": [\n" +
            "                { \"$ref\": \"#/definitions/nonNegativeInteger\" },\n" +
            "                { \"default\": 0 }\n" +
            "            ]\n" +
            "        },\n" +
            "        \"simpleTypes\": {\n" +
            "            \"enum\": [\n" +
            "                \"array\",\n" +
            "                \"boolean\",\n" +
            "                \"integer\",\n" +
            "                \"null\",\n" +
            "                \"number\",\n" +
            "                \"object\",\n" +
            "                \"string\"\n" +
            "            ]\n" +
            "        },\n" +
            "        \"stringArray\": {\n" +
            "            \"type\": \"array\",\n" +
            "            \"items\": { \"type\": \"string\" },\n" +
            "            \"uniqueItems\": true,\n" +
            "            \"default\": []\n" +
            "        }\n" +
            "    },\n" +
            "    \"type\": [\"object\", \"boolean\"],\n" +
            "    \"properties\": {\n" +
            "        \"$id\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"format\": \"uri-reference\"\n" +
            "        },\n" +
            "        \"$schema\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"format\": \"uri\"\n" +
            "        },\n" +
            "        \"$ref\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"format\": \"uri-reference\"\n" +
            "        },\n" +
            "        \"title\": {\n" +
            "            \"type\": \"string\"\n" +
            "        },\n" +
            "        \"description\": {\n" +
            "            \"type\": \"string\"\n" +
            "        },\n" +
            "        \"default\": {},\n" +
            "        \"examples\": {\n" +
            "            \"type\": \"array\",\n" +
            "            \"items\": {}\n" +
            "        },\n" +
            "        \"multipleOf\": {\n" +
            "            \"type\": \"number\",\n" +
            "            \"exclusiveMinimum\": 0\n" +
            "        },\n" +
            "        \"maximum\": {\n" +
            "            \"type\": \"number\"\n" +
            "        },\n" +
            "        \"exclusiveMaximum\": {\n" +
            "            \"type\": \"number\"\n" +
            "        },\n" +
            "        \"minimum\": {\n" +
            "            \"type\": \"number\"\n" +
            "        },\n" +
            "        \"exclusiveMinimum\": {\n" +
            "            \"type\": \"number\"\n" +
            "        },\n" +
            "        \"maxLength\": { \"$ref\": \"#/definitions/nonNegativeInteger\" },\n" +
            "        \"minLength\": { \"$ref\": \"#/definitions/nonNegativeIntegerDefault0\" },\n" +
            "        \"pattern\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"format\": \"regex\"\n" +
            "        },\n" +
            "        \"additionalItems\": { \"$ref\": \"#\" },\n" +
            "        \"items\": {\n" +
            "            \"anyOf\": [\n" +
            "                { \"$ref\": \"#\" },\n" +
            "                { \"$ref\": \"#/definitions/schemaArray\" }\n" +
            "            ],\n" +
            "            \"default\": {}\n" +
            "        },\n" +
            "        \"maxItems\": { \"$ref\": \"#/definitions/nonNegativeInteger\" },\n" +
            "        \"minItems\": { \"$ref\": \"#/definitions/nonNegativeIntegerDefault0\" },\n" +
            "        \"uniqueItems\": {\n" +
            "            \"type\": \"boolean\",\n" +
            "            \"default\": false\n" +
            "        },\n" +
            "        \"contains\": { \"$ref\": \"#\" },\n" +
            "        \"maxProperties\": { \"$ref\": \"#/definitions/nonNegativeInteger\" },\n" +
            "        \"minProperties\": { \"$ref\": \"#/definitions/nonNegativeIntegerDefault0\" },\n" +
            "        \"required\": { \"$ref\": \"#/definitions/stringArray\" },\n" +
            "        \"additionalProperties\": { \"$ref\": \"#\" },\n" +
            "        \"definitions\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"additionalProperties\": { \"$ref\": \"#\" },\n" +
            "            \"default\": {}\n" +
            "        },\n" +
            "        \"properties\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"additionalProperties\": { \"$ref\": \"#\" },\n" +
            "            \"default\": {}\n" +
            "        },\n" +
            "        \"patternProperties\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"additionalProperties\": { \"$ref\": \"#\" },\n" +
            "            \"propertyNames\": { \"format\": \"regex\" },\n" +
            "            \"default\": {}\n" +
            "        },\n" +
            "        \"dependencies\": {\n" +
            "            \"type\": \"object\",\n" +
            "            \"additionalProperties\": {\n" +
            "                \"anyOf\": [\n" +
            "                    { \"$ref\": \"#\" },\n" +
            "                    { \"$ref\": \"#/definitions/stringArray\" }\n" +
            "                ]\n" +
            "            }\n" +
            "        },\n" +
            "        \"propertyNames\": { \"$ref\": \"#\" },\n" +
            "        \"const\": {},\n" +
            "        \"enum\": {\n" +
            "            \"type\": \"array\",\n" +
            "            \"minItems\": 1,\n" +
            "            \"uniqueItems\": true\n" +
            "        },\n" +
            "        \"type\": {\n" +
            "            \"anyOf\": [\n" +
            "                { \"$ref\": \"#/definitions/simpleTypes\" },\n" +
            "                {\n" +
            "                    \"type\": \"array\",\n" +
            "                    \"items\": { \"$ref\": \"#/definitions/simpleTypes\" },\n" +
            "                    \"minItems\": 1,\n" +
            "                    \"uniqueItems\": true\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        \"format\": { \"type\": \"string\" },\n" +
            "        \"allOf\": { \"$ref\": \"#/definitions/schemaArray\" },\n" +
            "        \"anyOf\": { \"$ref\": \"#/definitions/schemaArray\" },\n" +
            "        \"oneOf\": { \"$ref\": \"#/definitions/schemaArray\" },\n" +
            "        \"not\": { \"$ref\": \"#\" }\n" +
            "    },\n" +
            "    \"default\": {}\n" +
            "}";

    static final Object SCHEMA;

    static {
        Object schemaObject;
        try {
            schemaObject = new ObjectMapper().readValue(SCHEMA_JSON, Object.class);
        }
        catch (Throwable ignored) {
            schemaObject = null;
        }
        SCHEMA = schemaObject;
    }
}
