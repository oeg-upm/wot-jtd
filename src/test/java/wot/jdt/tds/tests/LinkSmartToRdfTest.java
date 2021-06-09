package wot.jdt.tds.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.jena.graph.Graph;
import org.apache.jena.query.ARQ;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.lib.ShLib;
import org.apache.jena.sparql.mgt.Explain.InfoLevel;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.gson.JsonObject;

import wot.jtd.JTD;
import wot.jtd.JsonHandler;
import wot.jtd.model.Thing;

public class LinkSmartToRdfTest {

	static {
		ARQ.setExecutionLogging(InfoLevel.NONE);
	}
	
	private boolean performGenericTest(String file) throws JsonProcessingException {
		JsonObject td = readJsonFile(new File(file));
		Thing thing = null;
		Thing thingFromRDF = null;
		try {
			thing = Thing.fromJson(td);
			Model generatedModel = JTD.toRDF(td);
			//generatedModel.write(System.out, "Turtle");
			applyShape(generatedModel.getGraph());
			thingFromRDF = JTD.fromRDF(generatedModel).get(0);
			thingFromRDF.setContext(thing.getContext());
		} catch (UnrecognizedPropertyException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(thingFromRDF.toJson());
		System.out.println(thing.toJson());
		Boolean sameModels = thing != null && thingFromRDF != null && JsonHandler.compareJson(thing.toJson(), thingFromRDF.toJson()) && thing.equals(thingFromRDF) ;
		return sameModels;
	}
	
	private static void applyShape(Graph dataGraph) {
		Graph shapesGraph = RDFDataMgr.loadGraph("https://raw.githubusercontent.com/w3c/wot-thing-description/main/validation/td-validation.ttl", Lang.TURTLE);
		Shapes shapes = Shapes.parse(shapesGraph);
	    ValidationReport report = ShaclValidator.get().validate(shapes, dataGraph);
	    System.out.println(report.conforms());
	    ShLib.printReport(report);
	    System.out.println();
	    RDFDataMgr.write(System.out, report.getModel(), Lang.TTL);
	    System.out.println("_------");
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

	
	@Test
	public void test0() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-0-0.json");
		Assert.assertTrue(testResult);
	}
	/*
	@Test
	public void test1() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-1.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test2() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-2.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test3() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-3.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test4() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-4.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test5() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-5.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test6() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-6.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test7() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-7.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test8() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-8.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test9() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-9.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test10() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-10.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test11() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-11.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test12() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-12.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test13() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-13.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test14() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-14.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test15() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-15.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test16() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-16.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test17() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-17.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test18() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-18.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test19() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-19.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test20() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-20.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test21() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-21.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test22() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-22.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test23() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-23.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test24() throws JsonProcessingException {
		Boolean testResult = false;
		try {
		 performGenericTest("./src/test/resources/linksmart/framed/td-24.json");
		}catch(Exception e) {
			testResult = true;
		}
		Assert.assertTrue(testResult);
	}

	@Test
	public void test25() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-25.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test26() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-26.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test27() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-27.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test28() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-28.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test29() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-29.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test30() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-30.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test31() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-31.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test32() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-32.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test33() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-33.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test34() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-34.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test35() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-35.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test36() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-36.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test37() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-37.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test38() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-38.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test39() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-39.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test40() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-40.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test41() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-41.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test42() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-42.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test43() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-43.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test44() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-44.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test45() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-45.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test46() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-46.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test47() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-47.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test48() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-48.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test49() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-49.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test50() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-50.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test51() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-51.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test52() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-52.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test53() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-53.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test54() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-54.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test55() throws JsonProcessingException {
		Boolean testResult = false;
		try {
		 performGenericTest("./src/test/resources/linksmart/framed/td-55.json");
		}catch(Exception e) {
			testResult = true;
		}
		Assert.assertTrue(testResult);
	}

	@Test
	public void test56() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-56.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test57() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-57.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test58() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-58.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test59() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-59.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test60() throws JsonProcessingException {
		Boolean testResult = false;
		try {
		 performGenericTest("./src/test/resources/linksmart/framed/td-60.json");
		}catch(Exception e) {
			testResult = true;
		}
		Assert.assertTrue(testResult);
	}

	@Test
	public void test61() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-61.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test62() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-62.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test63() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-63.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test64() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-64.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test65() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/linksmart/framed/td-65.json");
		Assert.assertTrue(testResult);
	}
*/
}
