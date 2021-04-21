package kehio.toRDF.datatype.tests;

import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Assert;
import org.junit.Test;

import kehio.mapper.Kehio;
import kehio.models.test.TestPerson1;
import kehio.models.test.TestPerson2;

public class ObjectTest {

	
	private static final String TEST1 = "[ <http://xmlns.com/foaf/0.1/name>\n" + 
			"          \"John\" ;\n" + 
			"  <http://xmlns.com/foaf/0.1/phone>\n" + 
			"          [ <http://xmlns.com/foaf/0.1/number>\n" + 
			"                    \"+34 54 65 23\" ] ;\n" + 
			"  <http://xmlns.com/foaf/0.1/surnames>\n" + 
			"          \"Fold\" , \"Doe\"\n" + 
			"] .";

	
	@Test
	public void test1() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		TestPerson1 person = new TestPerson1(true);
		Model model = Kehio.deserializeClass(person);
		Model expectedModel = ModelFactory.createDefaultModel();
		expectedModel.read(new ByteArrayInputStream(TEST1.getBytes()), null, "Turtle");
		Assert.assertTrue(expectedModel.getGraph().isIsomorphicWith(model.getGraph()));
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
	public void test2() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		TestPerson2 person = new TestPerson2(true);
		Model model = Kehio.deserializeClass(person);
		Model expectedModel = ModelFactory.createDefaultModel();
		expectedModel.read(new ByteArrayInputStream(TEST2.getBytes()), null, "Turtle");
		
		Assert.assertTrue(expectedModel.getGraph().isIsomorphicWith(model.getGraph()));
	}
	
	
}

