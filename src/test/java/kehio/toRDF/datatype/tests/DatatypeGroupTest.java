package kehio.toRDF.datatype.tests;

import java.net.URISyntaxException;

import org.apache.jena.rdf.model.Model;
import org.junit.Assert;
import org.junit.Test;

import kehio.mapper.Kehio;
import kehio.models.test.TestPost1;
import kehio.models.test.TestPost2;
import kehio.models.test.TestPost3;
import kehio.models.test.TestPost4;

public class DatatypeGroupTest {

	
	
	@Test
	public void testDatatype1() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		TestPost1 testingObject = new TestPost1(true);
		Model model = Kehio.deserializeClass(testingObject);
		Assert.assertTrue(TestPost1.expectedModel.getGraph().isIsomorphicWith(model.getGraph()));
	}
	
	@Test
	public void testDatatype2() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		TestPost2 testingObject = new TestPost2(true);
		Model model = Kehio.deserializeClass(testingObject);
		Assert.assertTrue(TestPost2.expectedModel.getGraph().isIsomorphicWith(model.getGraph()));
	}
	

	@Test
	public void testDatatype3() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		Boolean correct = false;
		try {
			TestPost3 testingObject = new TestPost3(true);
			Kehio.deserializeClass(testingObject);
		}catch(Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}
	

	@Test
	public void testDatatype4() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		Boolean correct = false;
		try {
			TestPost4 testingObject = new TestPost4(true);
			Kehio.deserializeClass(testingObject);
		}catch(Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}
	
	

}