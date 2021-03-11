package kehio.datatype.tests;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.junit.Test;

import org.junit.Assert;
import kehio.annotations.done.RdfDatatype;
import kehio.annotations.done.RdfId;
import kehio.mapper.Kehio;

public class IdTest {

	private static final String OUTPUT = "<http://example-instance.com/resource> <http://xmlns.com/foaf/0.1/name> \"test\" .";
	private static Model expectedModel = ModelFactory.createDefaultModel();
	static{
		expectedModel.read(new ByteArrayInputStream(OUTPUT.getBytes()), null, "Turtle");
	}
	
	@Test
	public void testId1() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestId1 testingObject = new ORMTestId1();
		Model model = Kehio.deserializeClass(testingObject);		
		Assert.assertTrue(expectedModel.containsAll(model));
	}
	
	@Test
	public void testId2() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestId2 testingObject = new ORMTestId2();
		Model model = Kehio.deserializeClass(testingObject);
		Assert.assertTrue(expectedModel.containsAll(model));
	}
	
	@Test
	public void testId3() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestId3 testingObject = new ORMTestId3();
		Model model = Kehio.deserializeClass(testingObject);
		Resource subject = model.listSubjectsWithProperty(FOAF.name, "test").next();
		Assert.assertTrue(subject.isAnon() && model.contains(null, FOAF.name, "test"));
	}
	
	@Test
	public void testId4() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestId4 testingObject = new ORMTestId4();
		Model model = Kehio.deserializeClass(testingObject);
		Resource subject = model.listSubjectsWithProperty(FOAF.name, "test").next();
		Assert.assertTrue(subject.isAnon() && model.contains(null, FOAF.name, "test"));
	}
	
	@Test
	public void testId5() {
		ORMTestId5 testingObject = new ORMTestId5();
		Boolean test = false;
		try {
			Model model = Kehio.deserializeClass(testingObject);
			model.write(System.out, "Turtle");
		}catch(Exception e) {
			test = true;
		}
		Assert.assertTrue(test);
	}
	
	@Test
	public void testId6() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestId6 testingObject = new ORMTestId6();
		Model model = Kehio.deserializeClass(testingObject);
		Resource subject = model.listSubjectsWithProperty(FOAF.name, "test").next();
		Assert.assertTrue(subject.isAnon() && model.contains(null, FOAF.name, "test"));
	}

	@Test
	public void testId7() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestId7 testingObject = new ORMTestId7();
		Boolean test = false;
		try {
			Model model = Kehio.deserializeClass(testingObject);
			model.write(System.out, "Turtle");
		}catch(Exception e) {
			test = true;
		}
		Assert.assertTrue(test);
	}
	
}

class ORMTestId1 {
	// Ok
	@RdfId
	public String id = "http://example-instance.com/resource";
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/name")
	public String test = "test";
}

class ORMTestId2 {
	// Ok
	@RdfId
	public URI id;
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/name")
	public String test = "test";
	public ORMTestId2() {
		try {
			id = new URI("http://example-instance.com/resource");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}

class ORMTestId3 {
	// Ok
	@RdfId
	public URI id;
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/name")
	public String test = "test";
}

class ORMTestId4 {
	// Ok
	@RdfId
	public String id;
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/name")
	public String test = "test";
}

class ORMTestId5 {
	// Exception
	@RdfId
	public Integer id;
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/name")
	public String test = "test";
}

class ORMTestId6 {
	// Ok
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/name")
	public String test = "test";
}

class ORMTestId7 {
	// Exception
	@RdfId
	public String id_1 = "http://example-instance.com/resource";
	@RdfId
	public URI id_2;
	
	@RdfDatatype(value="http://xmlns.com/foaf/0.1/name")
	public String test = "test";
}
