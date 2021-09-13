package wot.jdt.tds.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Test;

import com.github.fge.jsonpatch.JsonPatchException;
import com.google.gson.JsonObject;

import wot.jtd.JTD;
import wot.jtd.Validation;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.model.Thing;

public class ValidationTests {

	private static final String JSON_SAMPLE = "{\n" + 
		//	"    \"title\": \"MyLampThing\",\n" + 
			"    \"security\": \"basic_sc\",\n" + 
			"    \"securityDefinitions\": {\n" + 
			"        \"basic_sc\": {\n" + 
			//"            \"scheme\": \"basic\",\n" + 
			"            \"in\": \"header\"\n" + 
			"        }\n" + 
			"    },\n" + 
			"    \"properties\": {\n" + 
			"        \"status\": {\n" + 
			"            \"forms\": [\n" + 
			"                {\n" + 
			"                    \"href\": \"https://mylamp.example.com/status\"\n" + 
			"                }\n" + 
			"            ],\n" + 
			"            \"type\": \"string\"\n" + 
			"        }\n" + 
			"    },\n" + 
			"    \"id\": \"urn:dev:ops:32473-WoTLamp-1234\",\n" + 
			"    \"@context\": \"https://www.w3.org/2019/wot/td/v1\"\n" + 
			"}";
	
	
	@Test
	public void test0() throws IOException, JsonPatchException {
		Thing thing = Thing.fromJson(JTD.parseJson(JSON_SAMPLE));
		JsonObject report = Validation.jsonSchemaValidation(thing.toJson(), JSON_SCHEMA);
		System.out.println(report);
		Assert.assertTrue(true);
	}
	
	@Test
	public void test1() throws IOException, JsonPatchException, IllegalAccessException, ClassNotFoundException, URISyntaxException, SchemaValidationException {
		Thing thing = Thing.fromJson(JTD.parseJson(JSON_SAMPLE));
		Model shapes = ModelFactory.createDefaultModel();
		shapes.read("https://raw.githubusercontent.com/w3c/wot-thing-description/main/validation/td-validation.ttl", "TURTLE");
		Model thingModel = JTD.toRDF(thing);
		thingModel.write(System.out,"Turtle");
		Validation.shaclShapeValidation(ResourceFactory.createResource(thing.getId()), thingModel, shapes).write(System.out, "Turtle");
		Assert.assertTrue(true);
	}
	
	
	private static final String JSON_SCHEMA = "{\n" + 
			"    \"title\": \"WoT TD Schema - 16 October 2019\",\n" + 
			"    \"description\": \"JSON Schema for validating TD instances against the TD model. TD instances can be with or without terms that have default values\",\n" + 
			"    \"$schema \": \"http://json-schema.org/draft-07/schema#\",\n" + 
			"    \"definitions\": {\n" + 
			"        \"anyUri\": {\n" + 
			"            \"type\": \"string\",\n" + 
			"            \"format\": \"iri-reference\"\n" + 
			"        },\n" + 
			"        \"description\": {\n" + 
			"            \"type\": \"string\"\n" + 
			"        },\n" + 
			"        \"descriptions\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"additionalProperties\": {\n" + 
			"                \"type\": \"string\"\n" + 
			"            }\n" + 
			"        },\n" + 
			"        \"title\": {\n" + 
			"            \"type\": \"string\"\n" + 
			"        },\n" + 
			"        \"titles\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"additionalProperties\": {\n" + 
			"                \"type\": \"string\"\n" + 
			"            }\n" + 
			"        },\n" + 
			"        \"security\": {\n" + 
			"            \"oneOf\": [{\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"items\": {\n" + 
			"                        \"type\": \"string\"\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                }\n" + 
			"            ]\n" + 
			"        },\n" + 
			"        \"scopes\": {\n" + 
			"            \"oneOf\": [{\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"items\": {\n" + 
			"                        \"type\": \"string\"\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                }\n" + 
			"            ]\n" + 
			"        },\n" + 
			"        \"subprotocol\": {\n" + 
			"            \"type\": \"string\",\n" + 
			"            \"enum\": [\n" + 
			"                \"longpoll\",\n" + 
			"                \"websub\",\n" + 
			"                \"sse\"\n" + 
			"            ]\n" + 
			"        },\n" + 
			"        \"thing-context-w3c-uri\": {\n" + 
			"            \"type\": \"string\",\n" + 
			"            \"enum\": [\n" + 
			"                \"https://www.w3.org/2019/wot/td/v1\"\n" + 
			"            ]\n" + 
			"        },\n" + 
			"        \"thing-context\": {\n" + 
			"            \"oneOf\": [{\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"items\": [{\n" + 
			"                        \"$ref\": \"#/definitions/thing-context-w3c-uri\"\n" + 
			"                    }],\n" + 
			"                    \"additionalItems\": {\n" + 
			"                        \"anyOf\": [{\n" + 
			"                                \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                            },\n" + 
			"                            {\n" + 
			"                                \"type\": \"object\"\n" + 
			"                            }\n" + 
			"                        ]\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    \"$ref\": \"#/definitions/thing-context-w3c-uri\"\n" + 
			"                }\n" + 
			"            ]\n" + 
			"        },\n" + 
			"        \"type_declaration\": {\n" + 
			"            \"oneOf\": [{\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"items\": {\n" + 
			"                        \"type\": \"string\"\n" + 
			"                    }\n" + 
			"                }\n" + 
			"            ]\n" + 
			"        },\n" + 
			"        \"dataSchema\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"properties\": {\n" + 
			"                \"@type\": {\n" + 
			"                    \"$ref\": \"#/definitions/type_declaration\"\n" + 
			"                },\n" + 
			"                \"description\": {\n" + 
			"                    \"$ref\": \"#/definitions/description\"\n" + 
			"                },\n" + 
			"                \"title\": {\n" + 
			"                    \"$ref\": \"#/definitions/title\"\n" + 
			"                },\n" + 
			"                \"descriptions\": {\n" + 
			"                    \"$ref\": \"#/definitions/descriptions\"\n" + 
			"                },\n" + 
			"                \"titles\": {\n" + 
			"                    \"$ref\": \"#/definitions/titles\"\n" + 
			"                },\n" + 
			"                \"writeOnly\": {\n" + 
			"                    \"type\": \"boolean\"\n" + 
			"                },\n" + 
			"                \"readOnly\": {\n" + 
			"                    \"type\": \"boolean\"\n" + 
			"                },\n" + 
			"                \"oneOf\": {\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"items\": {\n" + 
			"                        \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                \"unit\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"enum\": {\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"minItems\": 1,\n" + 
			"                    \"uniqueItems\": true\n" + 
			"                },\n" + 
			"                \"format\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"const\": {},\n" + 
			"                \"type\": {\n" + 
			"                    \"type\": \"string\",\n" + 
			"                    \"enum\": [\n" + 
			"                        \"boolean\",\n" + 
			"                        \"integer\",\n" + 
			"                        \"number\",\n" + 
			"                        \"string\",\n" + 
			"                        \"object\",\n" + 
			"                        \"array\",\n" + 
			"                        \"null\"\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                \"items\": {\n" + 
			"                    \"oneOf\": [{\n" + 
			"                            \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                        },\n" + 
			"                        {\n" + 
			"                            \"type\": \"array\",\n" + 
			"                            \"items\": {\n" + 
			"                                \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                            }\n" + 
			"                        }\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                \"maxItems\": {\n" + 
			"                    \"type\": \"integer\",\n" + 
			"                    \"minimum\": 0\n" + 
			"                },\n" + 
			"                \"minItems\": {\n" + 
			"                    \"type\": \"integer\",\n" + 
			"                    \"minimum\": 0\n" + 
			"                },\n" + 
			"                \"minimum\": {\n" + 
			"                    \"type\": \"number\"\n" + 
			"                },\n" + 
			"                \"maximum\": {\n" + 
			"                    \"type\": \"number\"\n" + 
			"                },\n" + 
			"                \"properties\": {\n" + 
			"                    \"additionalProperties\": {\n" + 
			"                        \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                \"required\": {\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"items\": {\n" + 
			"                        \"type\": \"string\"\n" + 
			"                    }\n" + 
			"                }\n" + 
			"            }\n" + 
			"        },\n" + 
			"        \"form_element_property\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"properties\": {\n" + 
			"                \"op\": {\n" + 
			"                    \"oneOf\": [{\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"readproperty\",\n" + 
			"                                \"writeproperty\",\n" + 
			"                                \"observeproperty\",\n" + 
			"                                \"unobserveproperty\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        {\n" + 
			"                            \"type\": \"array\",\n" + 
			"                            \"items\": {\n" + 
			"                                \"type\": \"string\",\n" + 
			"                                \"enum\": [\n" + 
			"                                    \"readproperty\",\n" + 
			"                                    \"writeproperty\",\n" + 
			"                                    \"observeproperty\",\n" + 
			"                                    \"unobserveproperty\"\n" + 
			"                                ]\n" + 
			"                            }\n" + 
			"                        }\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                \"href\": {\n" + 
			"                    \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                },\n" + 
			"                \"contentType\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"contentCoding\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"subprotocol\": {\n" + 
			"                    \"$ref\": \"#/definitions/subprotocol\"\n" + 
			"                },\n" + 
			"                \"security\": {\n" + 
			"                    \"$ref\": \"#/definitions/security\"\n" + 
			"                },\n" + 
			"                \"scopes\": {\n" + 
			"                    \"$ref\": \"#/definitions/scopes\"\n" + 
			"                },\n" + 
			"                \"response\": {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"properties\": {\n" + 
			"                        \"contentType\": {\n" + 
			"                            \"type\": \"string\"\n" + 
			"                        }\n" + 
			"                    }\n" + 
			"                }\n" + 
			"            },\n" + 
			"            \"required\": [\n" + 
			"                \"href\"\n" + 
			"            ],\n" + 
			"            \"additionalProperties\": true\n" + 
			"        },\n" + 
			"        \"form_element_action\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"properties\": {\n" + 
			"                \"op\": {\n" + 
			"                    \"oneOf\": [{\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"invokeaction\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        {\n" + 
			"                            \"type\": \"array\",\n" + 
			"                            \"items\": {\n" + 
			"                                \"type\": \"string\",\n" + 
			"                                \"enum\": [\n" + 
			"                                    \"invokeaction\"\n" + 
			"                                ]\n" + 
			"                            }\n" + 
			"                        }\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                \"href\": {\n" + 
			"                    \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                },\n" + 
			"                \"contentType\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"contentCoding\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"subprotocol\": {\n" + 
			"                    \"$ref\": \"#/definitions/subprotocol\"\n" + 
			"                },\n" + 
			"                \"security\": {\n" + 
			"                    \"$ref\": \"#/definitions/security\"\n" + 
			"                },\n" + 
			"                \"scopes\": {\n" + 
			"                    \"$ref\": \"#/definitions/scopes\"\n" + 
			"                },\n" + 
			"                \"response\": {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"properties\": {\n" + 
			"                        \"contentType\": {\n" + 
			"                            \"type\": \"string\"\n" + 
			"                        }\n" + 
			"                    }\n" + 
			"                }\n" + 
			"            },\n" + 
			"            \"required\": [\n" + 
			"                \"href\"\n" + 
			"            ],\n" + 
			"            \"additionalProperties\": true\n" + 
			"        },\n" + 
			"        \"form_element_event\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"properties\": {\n" + 
			"                \"op\": {\n" + 
			"                    \"oneOf\": [{\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"subscribeevent\",\n" + 
			"                                \"unsubscribeevent\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        {\n" + 
			"                            \"type\": \"array\",\n" + 
			"                            \"items\": {\n" + 
			"                                \"type\": \"string\",\n" + 
			"                                \"enum\": [\n" + 
			"                                    \"subscribeevent\",\n" + 
			"                                    \"unsubscribeevent\"\n" + 
			"                                ]\n" + 
			"                            }\n" + 
			"                        }\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                \"href\": {\n" + 
			"                    \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                },\n" + 
			"                \"contentType\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"contentCoding\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"subprotocol\": {\n" + 
			"                    \"$ref\": \"#/definitions/subprotocol\"\n" + 
			"                },\n" + 
			"                \"security\": {\n" + 
			"                    \"$ref\": \"#/definitions/security\"\n" + 
			"                },\n" + 
			"                \"scopes\": {\n" + 
			"                    \"$ref\": \"#/definitions/scopes\"\n" + 
			"                },\n" + 
			"                \"response\": {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"properties\": {\n" + 
			"                        \"contentType\": {\n" + 
			"                            \"type\": \"string\"\n" + 
			"                        }\n" + 
			"                    }\n" + 
			"                }\n" + 
			"            },\n" + 
			"            \"required\": [\n" + 
			"                \"href\"\n" + 
			"            ],\n" + 
			"            \"additionalProperties\": true\n" + 
			"        },\n" + 
			"        \"form_element_root\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"properties\": {\n" + 
			"                \"op\": {\n" + 
			"                    \"oneOf\": [{\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"readallproperties\",\n" + 
			"                                \"writeallproperties\",\n" + 
			"                                \"readmultipleproperties\",\n" + 
			"                                \"writemultipleproperties\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        {\n" + 
			"                            \"type\": \"array\",\n" + 
			"                            \"items\": {\n" + 
			"                                \"type\": \"string\",\n" + 
			"                                \"enum\": [\n" + 
			"                                    \"readallproperties\",\n" + 
			"                                    \"writeallproperties\",\n" + 
			"                                    \"readmultipleproperties\",\n" + 
			"                                    \"writemultipleproperties\"\n" + 
			"                                ]\n" + 
			"                            }\n" + 
			"                        }\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                \"href\": {\n" + 
			"                    \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                },\n" + 
			"                \"contentType\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"contentCoding\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"subprotocol\": {\n" + 
			"                    \"$ref\": \"#/definitions/subprotocol\"\n" + 
			"                },\n" + 
			"                \"security\": {\n" + 
			"                    \"$ref\": \"#/definitions/security\"\n" + 
			"                },\n" + 
			"                \"scopes\": {\n" + 
			"                    \"$ref\": \"#/definitions/scopes\"\n" + 
			"                },\n" + 
			"                \"response\": {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"properties\": {\n" + 
			"                        \"contentType\": {\n" + 
			"                            \"type\": \"string\"\n" + 
			"                        }\n" + 
			"                    }\n" + 
			"                }\n" + 
			"            },\n" + 
			"            \"required\": [\n" + 
			"                \"href\"\n" + 
			"            ],\n" + 
			"            \"additionalProperties\": true\n" + 
			"        },\n" + 
			"        \"property_element\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"properties\": {\n" + 
			"                \"@type\": {\n" + 
			"                    \"$ref\": \"#/definitions/type_declaration\"\n" + 
			"                },\n" + 
			"                \"description\": {\n" + 
			"                    \"$ref\": \"#/definitions/description\"\n" + 
			"                },\n" + 
			"                \"descriptions\": {\n" + 
			"                    \"$ref\": \"#/definitions/descriptions\"\n" + 
			"                },\n" + 
			"                \"title\": {\n" + 
			"                    \"$ref\": \"#/definitions/title\"\n" + 
			"                },\n" + 
			"                \"titles\": {\n" + 
			"                    \"$ref\": \"#/definitions/titles\"\n" + 
			"                },\n" + 
			"                \"forms\": {\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"minItems\": 1,\n" + 
			"                    \"items\": {\n" + 
			"                        \"$ref\": \"#/definitions/form_element_property\"\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                \"uriVariables\": {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"additionalProperties\": {\n" + 
			"                        \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                \"observable\": {\n" + 
			"                    \"type\": \"boolean\"\n" + 
			"                },\n" + 
			"                \"writeOnly\": {\n" + 
			"                    \"type\": \"boolean\"\n" + 
			"                },\n" + 
			"                \"readOnly\": {\n" + 
			"                    \"type\": \"boolean\"\n" + 
			"                },\n" + 
			"                \"oneOf\": {\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"items\": {\n" + 
			"                        \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                \"unit\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"enum\": {\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"minItems\": 1,\n" + 
			"                    \"uniqueItems\": true\n" + 
			"                },\n" + 
			"                \"format\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"const\": {},\n" + 
			"                \"type\": {\n" + 
			"                    \"type\": \"string\",\n" + 
			"                    \"enum\": [\n" + 
			"                        \"boolean\",\n" + 
			"                        \"integer\",\n" + 
			"                        \"number\",\n" + 
			"                        \"string\",\n" + 
			"                        \"object\",\n" + 
			"                        \"array\",\n" + 
			"                        \"null\"\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                \"items\": {\n" + 
			"                    \"oneOf\": [{\n" + 
			"                            \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                        },\n" + 
			"                        {\n" + 
			"                            \"type\": \"array\",\n" + 
			"                            \"items\": {\n" + 
			"                                \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                            }\n" + 
			"                        }\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                \"maxItems\": {\n" + 
			"                    \"type\": \"integer\",\n" + 
			"                    \"minimum\": 0\n" + 
			"                },\n" + 
			"                \"minItems\": {\n" + 
			"                    \"type\": \"integer\",\n" + 
			"                    \"minimum\": 0\n" + 
			"                },\n" + 
			"                \"minimum\": {\n" + 
			"                    \"type\": \"number\"\n" + 
			"                },\n" + 
			"                \"maximum\": {\n" + 
			"                    \"type\": \"number\"\n" + 
			"                },\n" + 
			"                \"properties\": {\n" + 
			"                    \"additionalProperties\": {\n" + 
			"                        \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                \"required\": {\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"items\": {\n" + 
			"                        \"type\": \"string\"\n" + 
			"                    }\n" + 
			"                }\n" + 
			"            },\n" + 
			"            \"required\": [\n" + 
			"                \"forms\"\n" + 
			"            ],\n" + 
			"            \"additionalProperties\": true\n" + 
			"        },\n" + 
			"        \"action_element\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"properties\": {\n" + 
			"                \"@type\": {\n" + 
			"                    \"$ref\": \"#/definitions/type_declaration\"\n" + 
			"                },\n" + 
			"                \"description\": {\n" + 
			"                    \"$ref\": \"#/definitions/description\"\n" + 
			"                },\n" + 
			"                \"descriptions\": {\n" + 
			"                    \"$ref\": \"#/definitions/descriptions\"\n" + 
			"                },\n" + 
			"                \"title\": {\n" + 
			"                    \"$ref\": \"#/definitions/title\"\n" + 
			"                },\n" + 
			"                \"titles\": {\n" + 
			"                    \"$ref\": \"#/definitions/titles\"\n" + 
			"                },\n" + 
			"                \"forms\": {\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"minItems\": 1,\n" + 
			"                    \"items\": {\n" + 
			"                        \"$ref\": \"#/definitions/form_element_action\"\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                \"uriVariables\": {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"additionalProperties\": {\n" + 
			"                        \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                \"input\": {\n" + 
			"                    \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                },\n" + 
			"                \"output\": {\n" + 
			"                    \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                },\n" + 
			"                \"safe\": {\n" + 
			"                    \"type\": \"boolean\"\n" + 
			"                },\n" + 
			"                \"idempotent\": {\n" + 
			"                    \"type\": \"boolean\"\n" + 
			"                }\n" + 
			"            },\n" + 
			"            \"required\": [\n" + 
			"                \"forms\"\n" + 
			"            ],\n" + 
			"            \"additionalProperties\": true\n" + 
			"        },\n" + 
			"        \"event_element\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"properties\": {\n" + 
			"                \"@type\": {\n" + 
			"                    \"$ref\": \"#/definitions/type_declaration\"\n" + 
			"                },\n" + 
			"                \"description\": {\n" + 
			"                    \"$ref\": \"#/definitions/description\"\n" + 
			"                },\n" + 
			"                \"descriptions\": {\n" + 
			"                    \"$ref\": \"#/definitions/descriptions\"\n" + 
			"                },\n" + 
			"                \"title\": {\n" + 
			"                    \"$ref\": \"#/definitions/title\"\n" + 
			"                },\n" + 
			"                \"titles\": {\n" + 
			"                    \"$ref\": \"#/definitions/titles\"\n" + 
			"                },\n" + 
			"                \"forms\": {\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"minItems\": 1,\n" + 
			"                    \"items\": {\n" + 
			"                        \"$ref\": \"#/definitions/form_element_event\"\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                \"uriVariables\": {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"additionalProperties\": {\n" + 
			"                        \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                    }\n" + 
			"                },\n" + 
			"                \"subscription\": {\n" + 
			"                    \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                },\n" + 
			"                \"data\": {\n" + 
			"                    \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                },\n" + 
			"                \"cancellation\": {\n" + 
			"                    \"$ref\": \"#/definitions/dataSchema\"\n" + 
			"                }\n" + 
			"            },\n" + 
			"            \"required\": [\n" + 
			"                \"forms\"\n" + 
			"            ],\n" + 
			"            \"additionalProperties\": true\n" + 
			"        },\n" + 
			"        \"link_element\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"properties\": {\n" + 
			"                \"href\": {\n" + 
			"                    \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                },\n" + 
			"                \"type\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"rel\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                \"anchor\": {\n" + 
			"                    \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                }\n" + 
			"            },\n" + 
			"            \"required\": [\n" + 
			"                \"href\"\n" + 
			"            ],\n" + 
			"            \"additionalProperties\": true\n" + 
			"        },\n" + 
			"        \"securityScheme\": {\n" + 
			"            \"oneOf\": [{\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"properties\": {\n" + 
			"                        \"@type\": {\n" + 
			"                            \"$ref\": \"#/definitions/type_declaration\"\n" + 
			"                        },\n" + 
			"                        \"description\": {\n" + 
			"                            \"$ref\": \"#/definitions/description\"\n" + 
			"                        },\n" + 
			"                        \"descriptions\": {\n" + 
			"                            \"$ref\": \"#/definitions/descriptions\"\n" + 
			"                        },\n" + 
			"                        \"proxy\": {\n" + 
			"                            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                        },\n" + 
			"                        \"scheme\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"nosec\"\n" + 
			"                            ]\n" + 
			"                        }\n" + 
			"                    },\n" + 
			"                    \"required\": [\n" + 
			"                        \"scheme\"\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"properties\": {\n" + 
			"                        \"@type\": {\n" + 
			"                            \"$ref\": \"#/definitions/type_declaration\"\n" + 
			"                        },\n" + 
			"                        \"description\": {\n" + 
			"                            \"$ref\": \"#/definitions/description\"\n" + 
			"                        },\n" + 
			"                        \"descriptions\": {\n" + 
			"                            \"$ref\": \"#/definitions/descriptions\"\n" + 
			"                        },\n" + 
			"                        \"proxy\": {\n" + 
			"                            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                        },\n" + 
			"                        \"scheme\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"basic\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        \"in\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"header\",\n" + 
			"                                \"query\",\n" + 
			"                                \"body\",\n" + 
			"                                \"cookie\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        \"name\": {\n" + 
			"                            \"type\": \"string\"\n" + 
			"                        }\n" + 
			"                    },\n" + 
			"                    \"required\": [\n" + 
			"                        \"scheme\"\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"properties\": {\n" + 
			"                        \"@type\": {\n" + 
			"                            \"$ref\": \"#/definitions/type_declaration\"\n" + 
			"                        },\n" + 
			"                        \"description\": {\n" + 
			"                            \"$ref\": \"#/definitions/description\"\n" + 
			"                        },\n" + 
			"                        \"descriptions\": {\n" + 
			"                            \"$ref\": \"#/definitions/descriptions\"\n" + 
			"                        },\n" + 
			"                        \"proxy\": {\n" + 
			"                            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                        },\n" + 
			"                        \"scheme\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"digest\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        \"qop\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"auth\",\n" + 
			"                                \"auth-int\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        \"in\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"header\",\n" + 
			"                                \"query\",\n" + 
			"                                \"body\",\n" + 
			"                                \"cookie\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        \"name\": {\n" + 
			"                            \"type\": \"string\"\n" + 
			"                        }\n" + 
			"                    },\n" + 
			"                    \"required\": [\n" + 
			"                        \"scheme\"\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"properties\": {\n" + 
			"                        \"@type\": {\n" + 
			"                            \"$ref\": \"#/definitions/type_declaration\"\n" + 
			"                        },\n" + 
			"                        \"description\": {\n" + 
			"                            \"$ref\": \"#/definitions/description\"\n" + 
			"                        },\n" + 
			"                        \"descriptions\": {\n" + 
			"                            \"$ref\": \"#/definitions/descriptions\"\n" + 
			"                        },\n" + 
			"                        \"proxy\": {\n" + 
			"                            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                        },\n" + 
			"                        \"scheme\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"apikey\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        \"in\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"header\",\n" + 
			"                                \"query\",\n" + 
			"                                \"body\",\n" + 
			"                                \"cookie\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        \"name\": {\n" + 
			"                            \"type\": \"string\"\n" + 
			"                        }\n" + 
			"                    },\n" + 
			"                    \"required\": [\n" + 
			"                        \"scheme\"\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"properties\": {\n" + 
			"                        \"@type\": {\n" + 
			"                            \"$ref\": \"#/definitions/type_declaration\"\n" + 
			"                        },\n" + 
			"                        \"description\": {\n" + 
			"                            \"$ref\": \"#/definitions/description\"\n" + 
			"                        },\n" + 
			"                        \"descriptions\": {\n" + 
			"                            \"$ref\": \"#/definitions/descriptions\"\n" + 
			"                        },\n" + 
			"                        \"proxy\": {\n" + 
			"                            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                        },\n" + 
			"                        \"scheme\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"bearer\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        \"authorization\": {\n" + 
			"                            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                        },\n" + 
			"                        \"alg\": {\n" + 
			"                            \"type\": \"string\"\n" + 
			"                        },\n" + 
			"                        \"format\": {\n" + 
			"                            \"type\": \"string\"\n" + 
			"                        },\n" + 
			"                        \"in\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"header\",\n" + 
			"                                \"query\",\n" + 
			"                                \"body\",\n" + 
			"                                \"cookie\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        \"name\": {\n" + 
			"                            \"type\": \"string\"\n" + 
			"                        }\n" + 
			"                    },\n" + 
			"                    \"required\": [\n" + 
			"                        \"scheme\"\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"properties\": {\n" + 
			"                        \"@type\": {\n" + 
			"                            \"$ref\": \"#/definitions/type_declaration\"\n" + 
			"                        },\n" + 
			"                        \"description\": {\n" + 
			"                            \"$ref\": \"#/definitions/description\"\n" + 
			"                        },\n" + 
			"                        \"descriptions\": {\n" + 
			"                            \"$ref\": \"#/definitions/descriptions\"\n" + 
			"                        },\n" + 
			"                        \"proxy\": {\n" + 
			"                            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                        },\n" + 
			"                        \"scheme\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"psk\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        \"identity\": {\n" + 
			"                            \"type\": \"string\"\n" + 
			"                        }\n" + 
			"                    },\n" + 
			"                    \"required\": [\n" + 
			"                        \"scheme\"\n" + 
			"                    ]\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    \"type\": \"object\",\n" + 
			"                    \"properties\": {\n" + 
			"                        \"@type\": {\n" + 
			"                            \"$ref\": \"#/definitions/type_declaration\"\n" + 
			"                        },\n" + 
			"                        \"description\": {\n" + 
			"                            \"$ref\": \"#/definitions/description\"\n" + 
			"                        },\n" + 
			"                        \"descriptions\": {\n" + 
			"                            \"$ref\": \"#/definitions/descriptions\"\n" + 
			"                        },\n" + 
			"                        \"proxy\": {\n" + 
			"                            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                        },\n" + 
			"                        \"scheme\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"oauth2\"\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        \"authorization\": {\n" + 
			"                            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                        },\n" + 
			"                        \"token\": {\n" + 
			"                            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                        },\n" + 
			"                        \"refresh\": {\n" + 
			"                            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"                        },\n" + 
			"                        \"scopes\": {\n" + 
			"                            \"oneOf\": [{\n" + 
			"                                    \"type\": \"array\",\n" + 
			"                                    \"items\": {\n" + 
			"                                        \"type\": \"string\"\n" + 
			"                                    }\n" + 
			"                                },\n" + 
			"                                {\n" + 
			"                                    \"type\": \"string\"\n" + 
			"                                }\n" + 
			"                            ]\n" + 
			"                        },\n" + 
			"                        \"flow\": {\n" + 
			"                            \"type\": \"string\",\n" + 
			"                            \"enum\": [\n" + 
			"                                \"code\"\n" + 
			"                            ]\n" + 
			"                        }\n" + 
			"                    },\n" + 
			"                    \"required\": [\n" + 
			"                        \"scheme\"\n" + 
			"                    ]\n" + 
			"                }\n" + 
			"            ]\n" + 
			"        }\n" + 
			"    },\n" + 
			"    \"type\": \"object\",\n" + 
			"    \"properties\": {\n" + 
			"        \"id\": {\n" + 
			"            \"type\": \"string\",\n" + 
			"            \"format\": \"uri\"\n" + 
			"        },\n" + 
			"        \"title\": {\n" + 
			"            \"$ref\": \"#/definitions/title\"\n" + 
			"        },\n" + 
			"        \"titles\": {\n" + 
			"            \"$ref\": \"#/definitions/titles\"\n" + 
			"        },\n" + 
			"        \"properties\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"additionalProperties\": {\n" + 
			"                \"$ref\": \"#/definitions/property_element\"\n" + 
			"            }\n" + 
			"        },\n" + 
			"        \"actions\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"additionalProperties\": {\n" + 
			"                \"$ref\": \"#/definitions/action_element\"\n" + 
			"            }\n" + 
			"        },\n" + 
			"        \"events\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"additionalProperties\": {\n" + 
			"                \"$ref\": \"#/definitions/event_element\"\n" + 
			"            }\n" + 
			"        },\n" + 
			"        \"description\": {\n" + 
			"            \"$ref\": \"#/definitions/description\"\n" + 
			"        },\n" + 
			"        \"descriptions\": {\n" + 
			"            \"$ref\": \"#/definitions/descriptions\"\n" + 
			"        },\n" + 
			"        \"version\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"properties\": {\n" + 
			"                \"instance\": {\n" + 
			"                    \"type\": \"string\"\n" + 
			"                }\n" + 
			"            },\n" + 
			"            \"required\": [\n" + 
			"                \"instance\"\n" + 
			"            ]\n" + 
			"        },\n" + 
			"        \"links\": {\n" + 
			"            \"type\": \"array\",\n" + 
			"            \"items\": {\n" + 
			"                \"$ref\": \"#/definitions/link_element\"\n" + 
			"            }\n" + 
			"        },\n" + 
			"        \"forms\": {\n" + 
			"            \"type\": \"array\",\n" + 
			"            \"minItems\": 1,\n" + 
			"            \"items\": {\n" + 
			"                \"$ref\": \"#/definitions/form_element_root\"\n" + 
			"            }\n" + 
			"        },\n" + 
			"        \"base\": {\n" + 
			"            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"        },\n" + 
			"        \"securityDefinitions\": {\n" + 
			"            \"type\": \"object\",\n" + 
			"            \"minProperties\": 1,\n" + 
			"            \"additionalProperties\": {\n" + 
			"                \"$ref\": \"#/definitions/securityScheme\"\n" + 
			"            }\n" + 
			"        },\n" + 
			"        \"support\": {\n" + 
			"            \"$ref\": \"#/definitions/anyUri\"\n" + 
			"        },\n" + 
			"        \"created\": {\n" + 
			"            \"type\": \"string\",\n" + 
			"            \"format\": \"date-time\"\n" + 
			"        },\n" + 
			"        \"modified\": {\n" + 
			"            \"type\": \"string\",\n" + 
			"            \"format\": \"date-time\"\n" + 
			"        },\n" + 
			"        \"security\": {\n" + 
			"            \"oneOf\": [{\n" + 
			"                    \"type\": \"string\"\n" + 
			"                },\n" + 
			"                {\n" + 
			"                    \"type\": \"array\",\n" + 
			"                    \"minItems\": 1,\n" + 
			"                    \"items\": {\n" + 
			"                        \"type\": \"string\"\n" + 
			"                    }\n" + 
			"                }\n" + 
			"            ]\n" + 
			"        },\n" + 
			"        \"@type\": {\n" + 
			"            \"$ref\": \"#/definitions/type_declaration\"\n" + 
			"        },\n" + 
			"        \"@context\": {\n" + 
			"            \"$ref\": \"#/definitions/thing-context\"\n" + 
			"        }\n" + 
			"    },\n" + 
			"    \"required\": [\n" + 
			"        \"title\",\n" + 
			"        \"security\",\n" + 
			"        \"securityDefinitions\",\n" + 
			"        \"@context\"\n" + 
			"    ],\n" + 
			"    \"additionalProperties\": true\n" + 
			"}";
}
