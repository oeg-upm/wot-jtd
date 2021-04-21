package kehio.fromRDF.datatype.tests;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;

import kehio.mapper.Kehio;
import kehio.models.test.TestPerson1;
import kehio.models.test.TestPerson2;

public class ObjectRdfTest {

	
	private static final String TEST1 = "<http://kehio-tests.com/instance1> <http://xmlns.com/foaf/0.1/name>\n" + 
			"          \"John\" ;\n" + 
			"  <http://xmlns.com/foaf/0.1/phone>\n" + 
			"          [ <http://xmlns.com/foaf/0.1/number>\n" + 
			"                    \"+34 54 65 23\" ] ;\n" + 
			"  <http://xmlns.com/foaf/0.1/surnames>\n" + 
			"          \"Fold\" , \"Doe\"\n" + 
			" .";

	
	@Test
	public void test1() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		Model model = ModelFactory.createDefaultModel();
		model.read(new ByteArrayInputStream(TEST1.getBytes()), null, "Turtle");
		TestPerson1 person = (TestPerson1) Kehio.serializeClass(TestPerson1.class, model, ResourceFactory.createResource("http://kehio-tests.com/instance1"));
		
		TestPerson1 expectedPerson = new TestPerson1(true);
		Assert.assertTrue(expectedPerson.equals(person));
	}

	private static final String TEST2 = "[ <http://xmlns.com/foaf/0.1/name>\n" + 
			"          \"John\" ;\n" + 
			"  <http://xmlns.com/foaf/0.1/phone>\n" + 
			"          [ <http://xmlns.com/foaf/0.1/number>\n" + 
			"                    \"+34 54 65 23\" ] ;\n" + 
			"  <http://xmlns.com/foaf/0.1/phone>\n" + 
			"          [ <http://xmlns.com/foaf/0.1/number>\n" + 
			"                    \"+34 54 65 23\" ] ;\n" + 
			"  <http://xmlns.com/foaf/0.1/surnames>\n" + 
			"          \"Fold\" , \"Doe\"\n" + 
			"] .";

	@Test
	public void test2() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Model model = ModelFactory.createDefaultModel();
		model.read(new ByteArrayInputStream(TEST2.getBytes()), null, "Turtle");
		Resource subject = model.listSubjectsWithProperty(ResourceFactory.createProperty("http://xmlns.com/foaf/0.1/name")).next();
		TestPerson2 person = (TestPerson2) Kehio.serializeClass(TestPerson2.class, model, subject);
		
	
		TestPerson2 expectedPerson = new TestPerson2(true);
		System.out.println(person.toString());
		System.out.println(expectedPerson.toString());
		Assert.assertTrue(expectedPerson.equals(person));
	}
	
	
}
