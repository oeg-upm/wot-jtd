package kehio.models.test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import kehio.annotations.RdfDatatypeGroup;

public class TestPost2 {
	
	// -- RDF representation
	private static final String OUTPUT = "[ <http://xmlns.com/foaf/0.1/numbers> \"12\"^^<http://www.w3.org/2001/XMLSchema#int> , \"12.2\"^^<http://www.w3.org/2001/XMLSchema#double> , \"this is an example\" , \"2002-09-24\"^^<http://www.w3.org/2001/XMLSchema#date> ] .";
	public static Model expectedModel = ModelFactory.createDefaultModel();
	static{
		expectedModel.read(new ByteArrayInputStream(OUTPUT.getBytes()), null, "Turtle");
	}
	
	// ----
	
	
	@RdfDatatypeGroup(value="http://xmlns.com/foaf/0.1/numbers", byDatatype=true)
	private Map<String,String> post;
	
	public TestPost2() {
		
	}
	
	public TestPost2(boolean init) {
		if(init) {
			post = new HashMap<>();
			post.put("http://www.w3.org/2001/XMLSchema#string", "this is an example");
			post.put("http://www.w3.org/2001/XMLSchema#int", "12");
			post.put("http://www.w3.org/2001/XMLSchema#double", "12.2");
			post.put("http://www.w3.org/2001/XMLSchema#date", "2002-09-24");
		}
	}

	public Map<String, String> getPost() {
		return post;
	}

	public void setPost(Map<String, String> post) {
		this.post = post;
	}
	
	public String toString() {
		return this.post.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((post == null) ? 0 : post.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TestPost2))
			return false;
		TestPost2 other = (TestPost2) obj;
		if (post == null) {
			if (other.post != null)
				return false;
		} else if (!post.equals(other.post))
			return false;
		return true;
	}
	
	
}
