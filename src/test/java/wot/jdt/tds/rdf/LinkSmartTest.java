package wot.jdt.tds.rdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.jena.rdf.model.Model;
import org.junit.Assert;
import org.junit.Test;

import com.apicatalog.jsonld.JsonLdError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;

import kehio.mapper.Kehio;
import wot.jtd.JTD;
import wot.jtd.model.Thing;

public class LinkSmartTest {

	
	@Test
	public void test1() throws JsonProcessingException, JsonLdError {
		JTD.setShowExternalValuesWarnings(false);
		JsonObject td = readJsonFile(new File("./src/test/resources/linksmart/td-1.json"));
		Boolean condition = false;
		
		try {
			Thing thing = Thing.fromJson(td);
			Model model = Kehio.deserializeClass(thing);
			model.write(System.out, "TTL");
			//String id = td.get("id").getAsString();
			//Thing thing =  (Thing) Kehio.serializeClass(Thing.class, modelTD, ResourceFactory.createResource(id));
			
			condition = thing!=null && thing.isEquivalent(td);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		Assert.assertTrue(condition);

	}
	
	private static JsonObject readJsonFile(File myObj) {
		StringBuilder builder = new StringBuilder(); 
		try {
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        builder.append(data);
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return JTD.parseJson(builder.toString());
	}
	
}
