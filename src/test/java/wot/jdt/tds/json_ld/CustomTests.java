package wot.jdt.tds.json_ld;

import java.io.IOException;

import org.junit.Test;

import wot.jtd.JTD;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.model.Thing;

public class CustomTests {

	
	public static String JSON_LD = "{\n" + 
			"    \"@context\": \"https://www.w3.org/2019/wot/td/v1\",\n" + 
			"    \"id\": \"urn:dev:ops:32473-WoTLamp-1234\",\n" + 
			"    \"title\": \"MyLampThing\",\n" + 
			"    \"securityDefinitions\": {\n" + 
			"        \"basic_sc\": {\n" + 
			"            \"scheme\": \"basic\",\n" + 
			"            \"in\": \"header\"\n" + 
			"        }\n" + 
			"    },\n" + 
			"    \"security\": [\n" + 
			"        \"basic_sc\"\n" + 
			"    ],\n" + 
			"    \"properties\": {\n" + 
			"        \"status\": {\n" + 
			"            \"type\": \"string\",\n" + 
			"            \"readOnly\" : false,\n" + 
			"            \"writeOnly\" : false\n" + 
			"        },\n" + 
			"      \"temperature\": {\n" + 
			"            \"type\": \"string\",\n" + 
			"            \"readOnly\" : true\n" + 
			"        }\n" + 
			"    }\n" + 
			"}";
	@Test
	public void test() throws IOException, SchemaValidationException {
		Thing thing = Thing.fromJson(JTD.parseJson(JSON_LD));
		thing.getProperties().entrySet().forEach(entry -> System.out.println(entry));
	}
}
