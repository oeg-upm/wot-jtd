package wot.jdt.tds.rdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

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

import thewebsemantic.Bean2RDF;
import wot.jtd.JTD;
import wot.jtd.RDFHandler;
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
			/*org.eclipse.rdf4j.model.Model aGraph = RDFMapper.create().writeValue(test);
			aGraph.forEach(st -> System.out.println(st.getSubject()+" "+ st.getPredicate()+" "+st.getObject()));
			
			ValueFactory vf = SimpleValueFactory.getInstance();
			IRI subject = vf.createIRI("tag:complexible:pinto:af626ae3c3aee94a4b3199d0392c5cf8");
			IRI pred = vf.createIRI("http://www.w3.org/2011/http#method");
			Literal obj = vf.createLiteral("GET");
			IRI graph = null;
			aGraph.add(subject, pred, obj, graph);
			IRI objectType = vf.createIRI("https://www.w3.org/2019/wot/td#VersionInfo");
			aGraph.remove(subject, RDF.TYPE, objectType, graph);
			System.out.println("_--------------------__");
			aGraph.forEach(st -> System.out.println(st.getSubject()+" "+ st.getPredicate()+" "+st.getObject()));
			
			VersionInfo version = RDFMapper.create().readValue(aGraph, VersionInfo.class);
			System.out.println(version.toJson());*/
			
			RDFHandler.serialize(); 
			
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
