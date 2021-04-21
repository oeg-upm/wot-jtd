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

public class DatatypeRDFTest {

	

	@Test
	public void testDatatypeGroup1() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, URISyntaxException {
		TestPost1 expectedObject = new TestPost1(true);
		Model model = TestPost1.expectedModel;
		Resource subject = model.listSubjectsWithProperty(ResourceFactory.createProperty("http://xmlns.com/foaf/0.1/description")).next();
		TestPost1 object = (TestPost1) Kehio.serializeClass(TestPost1.class, model, subject);
		Assert.assertTrue(expectedObject.equals(object));
	}
	
	@Test
	public void testDatatypeGroup2() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, URISyntaxException {
		TestPost2 expectedObject = new TestPost2(true);
		Model model = TestPost2.expectedModel;
		Resource subject = model.listSubjectsWithProperty(ResourceFactory.createProperty("http://xmlns.com/foaf/0.1/numbers")).next();
		TestPost2 object = (TestPost2) Kehio.serializeClass(TestPost2.class, model, subject);
		Assert.assertTrue(expectedObject.equals(object));
	}
}