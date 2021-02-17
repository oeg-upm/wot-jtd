package wot.jdt.tds.rdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.spi.PersistenceProvider;

import org.apache.jena.graph.Graph;
import org.apache.jena.query.ARQ;
import org.apache.jena.rdf.model.Model;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.mgt.Explain.InfoLevel;
import org.junit.Assert;
import org.junit.Test;

import com.apicatalog.jsonld.JsonLdError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.gson.JsonObject;
import com.hp.hpl.jena.ontology.OntModel;

import io.fogsy.empire.core.empire.Empire;
import io.fogsy.empire.pinto.MappingOptions;
import io.fogsy.empire.pinto.RDFMapper;
import thewebsemantic.Bean2RDF;
import wot.jtd.JTD;
import wot.jtd.model.Thing;
import wot.jtd.model.VersionInfo;

public class LinkSmartTest {


	
	@Test
	public void test1() throws JsonProcessingException, JsonLdError {
		JTD.setShowExternalValuesWarnings(false);
		JsonObject td = readJsonFile(new File("./src/test/resources/linksmart/td-1.json"));
		JsonObject instance = new JsonObject();
		instance.addProperty("instance", "1.2.3");
		td.add("version", instance);
		Model modelTD = JTD.toRDF(td);
		Thing thing = null;
		try {
			//modelTD.write(System.out, "TURTLE");
			//thing = JTD.fromRDF(modelTD);	
			//JTD.fromRDF(modelTD);
			ObjectMapper mapper = new ObjectMapper();
			
			//VersionInfo version = RDFMapper.create().readValue(modelTD, VersionInfo.class.getClass());

			VersionInfo test = new VersionInfo();
			test.setInstance("3.21");
			org.eclipse.rdf4j.model.Model aGraph = RDFMapper.create().writeValue(test);
			aGraph.forEach(st -> System.out.println(st.getSubject()+" "+ st.getPredicate()+" "+st.getObject()));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		Assert.assertTrue(thing!=null && thing.isEquivalent(td));

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
