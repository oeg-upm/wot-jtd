package wot.jdt.tds.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.apache.jena.rdf.model.Model;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.gson.JsonObject;
import wot.jtd.JTD;
import wot.jtd.JsonHandler;
import wot.jtd.model.Thing;

public class RDFWoTTest {

	private boolean performGenericTest(String file) throws JsonProcessingException {
		JsonObject td = readJsonFile(new File(file));
		Thing thing = null;
		Thing thingFromRDF = null;
		try {
			thing = Thing.fromJson(td);
			Model generatedModel = JTD.toRDF(td);
			//generatedModel.write(System.out, "Turtle");
			thingFromRDF = JTD.fromRDF(generatedModel).get(0);
			
			thingFromRDF.setContext(thing.getContext());
		} catch (UnrecognizedPropertyException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Boolean sameModels = thing != null && thingFromRDF != null && JsonHandler.compareJson(thing.toJson(), thingFromRDF.toJson()) && thing.equals(thingFromRDF) ;
		return sameModels;
	}
	
	

	public static JsonObject readJsonFile(File myObj) {
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
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-1.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test1() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-1.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test2() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-2.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test3() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-3.json");
		Assert.assertTrue(testResult);
	}



	@Test
	public void test6() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-6.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test7() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-7.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test8() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-8.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test9() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-9.json");
		Assert.assertTrue(testResult);
	}

	

	@Test
	public void test11() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-11.json");
		Assert.assertTrue(testResult);
	}



	@Test
	public void test13() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-13.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test14() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-14.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test15() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-15.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test16() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-16.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test17() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-17.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test18() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-18.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test19() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-19.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test20() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-20.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test21() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-21.json");
		Assert.assertTrue(testResult);
	}

	

	@Test
	public void test23() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-23.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test24() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-24.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test25() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-25.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test26() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-26.json");
		Assert.assertTrue(testResult);
	}


	@Test
	public void test28() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-28.json");
		Assert.assertTrue(testResult);
	}

	
	@Test
	public void test29() throws JsonProcessingException {
		Boolean testResult = false;
		try {
			testResult = performGenericTest("./src/test/resources/wot/example-29.json");
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
		Assert.assertTrue(testResult);
	}

	@Test
	public void test30() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-30.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test31() throws JsonProcessingException {
		JTD.setArrayCompactKey("ace:scopes");
		// Json representation are equal thanks to JTD.setArrayCompactKey("ace:scopes"); that compacts unitary values for ace:scopes outside an array
		// but as objects one thing will have ace:scopes as an array and another as a string value
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-31.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test32() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-32.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test33() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-33.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test34() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-34.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test35() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-35.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test36() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-36.json");
		Assert.assertTrue(testResult);
	}

	@Test
	public void test37() throws JsonProcessingException {
		Boolean testResult = performGenericTest("./src/test/resources/wot/example-37.json");
		Assert.assertTrue(testResult);
	}

}
