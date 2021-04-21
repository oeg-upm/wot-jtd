package kehio.toRDF.datatype.tests;

import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.junit.Assert;
import org.junit.Test;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfId;
import kehio.mapper.Kehio;

public class DatatypeCollectionTest {

	private static final String OUTPUT = "<http://example-instance.com/resource> <http://xmlns.com/foaf/0.1/age> \"21\" , \"42\"^^<http://www.w3.org/2001/XMLSchema#int> , \"32\"^^<http://www.w3.org/2001/XMLSchema#int> , \"12\"^^<http://www.w3.org/2001/XMLSchema#int> , \"20\" , \"31\"^^<http://www.w3.org/2001/XMLSchema#int> , \"11\"^^<http://www.w3.org/2001/XMLSchema#int> , \"41\"^^<http://www.w3.org/2001/XMLSchema#int> ;\n" + 
													"        <http://xmlns.com/foaf/0.1/text>  \"text 12\"@en , \"Texto@algo mas 21\"@es , \"Text@ 21\"@en , \"text 11\"@en .";
	private static Model expectedModel = ModelFactory.createDefaultModel();
	static {
		expectedModel.read(new ByteArrayInputStream(OUTPUT.getBytes()), null, "Turtle");
	}

	@Test
	public void testDatatype1()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection1 testingObject = new ORMTestDtypeCollection1();
		Model model = Kehio.deserializeClass(testingObject);
		Assert.assertTrue(expectedModel.containsAll(model));
	}

	@Test
	public void testDatatype2()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection2 testingObject = new ORMTestDtypeCollection2();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		} catch (Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}

	@Test
	public void testDatatype3()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection3 testingObject = new ORMTestDtypeCollection3();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		} catch (Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}

	@Test
	public void testDatatype4()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection4 testingObject = new ORMTestDtypeCollection4();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		} catch (Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}

	@Test
	public void testDatatype5()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection5 testingObject = new ORMTestDtypeCollection5();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		} catch (Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}

	@Test
	public void testDatatype6()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection6 testingObject = new ORMTestDtypeCollection6();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		} catch (Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}

	@Test
	public void testDatatype7()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection7 testingObject = new ORMTestDtypeCollection7();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		} catch (Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}

	@Test
	public void testDatatype8()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection8 testingObject = new ORMTestDtypeCollection8();
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age, ResourceFactory.createPlainLiteral("1"));
		Assert.assertTrue(correct);
	}

	@Test
	public void testDatatype9()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection9 testingObject = new ORMTestDtypeCollection9();
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age,
				ResourceFactory.createLangLiteral("1", "http://www.w3.org/2001/XMLSchema#double"));
		Assert.assertTrue(correct);
	}

	@Test
	public void testDatatype10()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection10 testingObject = new ORMTestDtypeCollection10();
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age, ResourceFactory.createPlainLiteral("1"));
		Assert.assertTrue(correct);
	}

	@Test
	public void testDatatype11()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection11 testingObject = new ORMTestDtypeCollection11();
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age, ResourceFactory.createPlainLiteral("1"));
		Assert.assertTrue(correct);
	}
	
	@Test
	public void testDatatype12()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection12 testingObject = new ORMTestDtypeCollection12();
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age, ResourceFactory.createPlainLiteral("1"));
		Assert.assertTrue(correct);
	}
	
	@Test
	public void testDatatype13()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection13 testingObject = new ORMTestDtypeCollection13();
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age, ResourceFactory.createPlainLiteral("1"));
		Assert.assertTrue(correct);
	}
	
	@Test
	public void testDatatype14()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection14 testingObject = new ORMTestDtypeCollection14();
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age, ResourceFactory.createPlainLiteral("1"));
		Assert.assertTrue(correct);
	}
	
	@Test
	public void testDatatype15()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection15 testingObject = new ORMTestDtypeCollection15();
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age, ResourceFactory.createPlainLiteral("1"));
		Assert.assertTrue(correct);
	}
	
	
	@Test
	public void testDatatype16()
			throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection16 testingObject = new ORMTestDtypeCollection16();
		Model model = Kehio.deserializeClass(testingObject);
		boolean correct = model.contains(null, FOAF.age, ResourceFactory.createPlainLiteral("1"));
		Assert.assertTrue(correct);
	}
	
	@Test
	public void testDatatype17() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		ORMTestDtypeCollection17 testingObject = new ORMTestDtypeCollection17();
		Boolean correct = false;
		try {
			Kehio.deserializeClass(testingObject);
		} catch (Exception e) {
			correct = true;
		}
		Assert.assertTrue(correct);
	}

}

class ORMTestDtypeCollection1 {
	// Ok
	@RdfId
	public String id = "http://example-instance.com/resource";

	@RdfDatatype("http://xmlns.com/foaf/0.1/age")
	public Collection<Integer> age_1 = new ArrayList<>();

	@RdfDatatype("http://xmlns.com/foaf/0.1/age")
	public Collection<String> age_2 = new ArrayList<>();

	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", datatype = "http://www.w3.org/2001/XMLSchema#int")
	public Collection<String> age_3 = new ArrayList<>();

	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", sinkDatatype = true)
	public Collection<String> age_4 = new ArrayList<>();

	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/text", lang = "en")
	public Collection<String> text_1 = new ArrayList<>();

	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/text", sinkLang = true)
	public Collection<String> text_2 = new ArrayList<>();

	public ORMTestDtypeCollection1() {
		age_1.add(11);
		age_1.add(12);
		age_2.add("20");
		age_2.add("21");
		age_3.add("31");
		age_3.add("32");
		age_4.add("41^^<http://www.w3.org/2001/XMLSchema#int>");
		age_4.add("42^^<http://www.w3.org/2001/XMLSchema#int>");
		text_1.add("text 11");
		text_1.add("text 12");
		text_2.add("Text@ 21@en");
		text_2.add("Texto@algo mas 21@es");
	}
}

class ORMTestDtypeCollection2 {
	// Exception
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", datatype = "http://www.w3.org/2001/XMLSchema#double", sinkLang = true)
	public Collection<String> age_1 = new ArrayList<>();

	public ORMTestDtypeCollection2() {
		age_1.add("1@en");
		age_1.add("2@en");
	}

}

class ORMTestDtypeCollection3 {
	// Exception
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", datatype = "http://www.w3.org/2001/XMLSchema#double", sinkDatatype = true)
	public Collection<String> age_1 = new ArrayList<>();

	public ORMTestDtypeCollection3() {
		age_1.add("1^^http://www.w3.org/2001/XMLSchema#double");
		age_1.add("2^^http://www.w3.org/2001/XMLSchema#double");
	}
}

class ORMTestDtypeCollection4 {
	// Exception
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", lang = "en", sinkDatatype = true)
	public Collection<String> age_1 = new ArrayList<>();

	public ORMTestDtypeCollection4() {
		age_1.add("1^^http://www.w3.org/2001/XMLSchema#double");
		age_1.add("2^^http://www.w3.org/2001/XMLSchema#double");
	}
}

class ORMTestDtypeCollection5 {
	// Exception
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", lang = "en", sinkLang = true)
	public Collection<String> age_1 = new ArrayList<>();

	public ORMTestDtypeCollection5() {
		age_1.add("1@en");
		age_1.add("1@en");
	}
}

class ORMTestDtypeCollection6 {
	// Exception
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", datatype = "http://www.w3.org/2001/XMLSchema#double", lang = "en")
	public Collection<String> age_1 = new ArrayList<>();

	public ORMTestDtypeCollection6() {
		age_1.add("1");
		age_1.add("1");
	}
}

class ORMTestDtypeCollection7 {
	// Exception
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", sinkDatatype = true, sinkLang = true)
	public Collection<String> age_1 = new ArrayList<>();

	public ORMTestDtypeCollection7() {
		age_1.add("1");
		age_1.add("1");
	}
}

class ORMTestDtypeCollection8 {
	// Ok
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", datatype = "es")
	public Collection<String> age_1 = new ArrayList<>();

	public ORMTestDtypeCollection8() {
		age_1.add("1");
		age_1.add("1");
	}
}

class ORMTestDtypeCollection9 {
	// Ok
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", lang = "http://www.w3.org/2001/XMLSchema#double")
	public Collection<String> age_1 = new ArrayList<>();

	public ORMTestDtypeCollection9() {
		age_1.add("1");
		age_1.add("1");
	}
}

class ORMTestDtypeCollection10 {
	// Ok
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", lang = " ")
	public Collection<String> age_1 = new ArrayList<>();

	public ORMTestDtypeCollection10() {
		age_1.add("1");
		age_1.add("1");
	}
}

class ORMTestDtypeCollection11 {
	// Ok
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", datatype = " ")
	public Collection<String> age_1 = new ArrayList<>();

	public ORMTestDtypeCollection11() {
		age_1.add("1");
		age_1.add("1");
	}
}

class ORMTestDtypeCollection12 {
	// Ok
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", datatype = " ")
	public List<String> age_1 = new ArrayList<>();

	public ORMTestDtypeCollection12() {
		age_1.add("1");
		age_1.add("1");
	}
}

class ORMTestDtypeCollection13 {
	// Ok
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", datatype = " ")
	public Queue<String> age_1 = new LinkedList<>();

	public ORMTestDtypeCollection13() {
		age_1.add("1");
		age_1.add("1");
	}
}

class ORMTestDtypeCollection14 {
	// Ok
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", datatype = " ")
	public List<String> age_1 = new LinkedList<>();

	public ORMTestDtypeCollection14() {
		age_1.add("1");
		age_1.add("1");
	}
}

class ORMTestDtypeCollection15 {
	// Ok
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", datatype = " ")
	public Deque<String> age_1 = new LinkedList<>();

	public ORMTestDtypeCollection15() {
		age_1.add("1");
		age_1.add("1");
	}
}

class ORMTestDtypeCollection16 {
	// Ok
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", datatype = " ")
	public SortedSet<String> age_1 = new TreeSet<>();

	public ORMTestDtypeCollection16() {
		age_1.add("1");
		age_1.add("1");
	}
}

class ORMTestDtypeCollection17 {
	// Exception
	@RdfDatatype(value = "http://xmlns.com/foaf/0.1/age", datatype = " ")
	public Map<String, String> age_1 = new HashMap<>();

	public ORMTestDtypeCollection17() {
		age_1.put("1","1");
		age_1.put("1","1");
	}
}