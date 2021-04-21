package kehio.fromRDF.datatype.tests;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Assert;
import org.junit.Test;

import kehio.mapper.Kehio;
import kehio.models.test.TestPost1;
import kehio.models.test.TestPost2;

public class DatatypeGroupRDFTest {


	@Test
	public void testDatatype1() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Model model = TestPost1.expectedModel;
		Resource subject = model.listSubjectsWithProperty(ResourceFactory.createProperty("http://xmlns.com/foaf/0.1/description")).next();
		TestPost1 object = (TestPost1) Kehio.serializeClass(TestPost1.class, model, subject);
		TestPost1 expectedObject = new TestPost1(true);
		
		Assert.assertTrue(expectedObject.equals(object));
	}
	
	@Test
	public void testDatatype2() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Model model = TestPost2.expectedModel;
		Resource subject = model.listSubjectsWithProperty(ResourceFactory.createProperty("http://xmlns.com/foaf/0.1/numbers")).next();
		TestPost2 object = (TestPost2) Kehio.serializeClass(TestPost2.class, model, subject);
		TestPost2 expectedObject = new TestPost2(true);
		
		Assert.assertTrue(expectedObject.equals(object));
	}
}