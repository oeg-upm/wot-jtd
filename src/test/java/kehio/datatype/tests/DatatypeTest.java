package kehio.datatype.tests;

import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.junit.Assert;
import org.junit.Test;

import kehio.annotations.done.RdfDatatype;
import kehio.annotations.done.RdfId;
import kehio.mapper.Kehio;

public class DatatypeTest {

	private static final String OUTPUT = "<http://example-instance.com/resource> <http://xmlns.com/foaf/0.1/age> \"4\"^^<http://www.w3.org/2001/XMLSchema#int> , \"3\"^^<http://www.w3.org/2001/XMLSchema#int>, \"2\", \"1\"^^<http://www.w3.org/2001/XMLSchema#int> ;\n" + 
										 " 										<http://xmlns.com/foaf/0.1/text>  \"Text@ 2\"@en , \"Text 1\"@en .";
	private static Model expectedModel = ModelFactory.createDefaultModel();
	static{
		expectedModel.read(new ByteArrayInputStream(OUTPUT.getBytes()), null, "Turtle");
	}
	
	@Test
	public void testDatatype1() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtype1 testingObject = new ORMTestDtype1();
		Model model = Kehio.deserializeClass(testingObject);
		Assert.assertTrue(expectedModel.containsAll(model));
	}
	
	@Test
	public void testDatatype2() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtype2 testingObject = new ORMTestDtype2();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		}catch(Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}
	
	@Test
	public void testDatatype3() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtype3 testingObject = new ORMTestDtype3();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		}catch(Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}
	
	@Test
	public void testDatatype4() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtype4 testingObject = new ORMTestDtype4();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		}catch(Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}
	
	@Test
	public void testDatatype5() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtype5 testingObject = new ORMTestDtype5();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		}catch(Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}
	
	@Test
	public void testDatatype6() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtype6 testingObject = new ORMTestDtype6();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		}catch(Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}
	
	@Test
	public void testDatatype7() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtype7 testingObject = new ORMTestDtype7();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		}catch(Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}
	
	@Test
	public void testDatatype8() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtype8 testingObject = new ORMTestDtype8();		
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age, ResourceFactory.createPlainLiteral("1"));
		Assert.assertTrue(correct);
	}
	@Test
	public void testDatatype9() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtype9 testingObject = new ORMTestDtype9();		
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age, ResourceFactory.createLangLiteral("1","http://www.w3.org/2001/XMLSchema#double"));
		Assert.assertTrue(correct);
	}
	@Test
	public void testDatatype10() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtype10 testingObject = new ORMTestDtype10();		
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age, ResourceFactory.createPlainLiteral("1"));
		Assert.assertTrue(correct);
	}
	@Test
	public void testDatatype11() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtype11 testingObject = new ORMTestDtype11();		
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age, ResourceFactory.createPlainLiteral("1"));
		Assert.assertTrue(correct);
	}
	
}
class ORMTestDtype1 {
	// Ok
	@RdfId
	public String id = "http://example-instance.com/resource";
	
	@RdfDatatype("http://xmlns.com/foaf/0.1/age")
	public Integer age_1 = 1;
	
	@RdfDatatype("http://xmlns.com/foaf/0.1/age")
	public String age_2 = "2";
	
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", datatype="http://www.w3.org/2001/XMLSchema#int")
	public String age_3= "3";
	
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", sinkDatatype=true)
	public String age_4 = "4^^<http://www.w3.org/2001/XMLSchema#int>";
	
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/text", lang="en")
	public String text_1 = "Text 1";
	
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/text", sinkLang=true)
	public String text_2 = "Text@ 2@en";
}

class ORMTestDtype2 {
	// Exception
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", datatype="http://www.w3.org/2001/XMLSchema#double", sinkLang=true)
	public String age_1 = "1@en";
}

class ORMTestDtype3 {
	// Exception
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", datatype="http://www.w3.org/2001/XMLSchema#double", sinkDatatype=true)
	public String age_1 = "1^^http://www.w3.org/2001/XMLSchema#double";
}


class ORMTestDtype4 {
	// Exception
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", lang="en", sinkDatatype=true)
	public String age_1 = "1^^http://www.w3.org/2001/XMLSchema#double";
}

class ORMTestDtype5 {
	// Exception
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", lang="en", sinkLang=true)
	public String age_1 = "1@en";
}

class ORMTestDtype6 {
	// Exception
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", datatype="http://www.w3.org/2001/XMLSchema#double", lang="en")
	public String age_1 = "1";
}

class ORMTestDtype7 {
	// Exception
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", sinkDatatype=true, sinkLang=true)
	public String age_1 = "1";
}

class ORMTestDtype8 {
	// Ok
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", datatype="es")
	public String age_1 = "1";
}

class ORMTestDtype9 {
	// Ok
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", lang="http://www.w3.org/2001/XMLSchema#double")
	public String age_1 = "1";
}

class ORMTestDtype10 {
	// Ok
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", lang=" ")
	public String age_1 = "1";
}

class ORMTestDtype11 {
	// Ok
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/age", datatype=" ")
	public String age_1 = "1";
}