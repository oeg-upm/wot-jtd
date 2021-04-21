package kehio.models.test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import kehio.annotations.RdfDatatypeGroup;

public class TestPost1 {
	
	private static final String OUTPUT = "[ <http://xmlns.com/foaf/0.1/description>  \"esto es un ejemplo\"@es , \"Ceci est un exemple\"@fr , \"questo é un esempio\"@it , \"this is an example\"@en ] .";
	public static Model expectedModel = ModelFactory.createDefaultModel();
	static{
		expectedModel.read(new ByteArrayInputStream(OUTPUT.getBytes()), null, "Turtle");
	}
	
	@RdfDatatypeGroup(value="http://xmlns.com/foaf/0.1/description", byLang=true)
	private Map<String,String> post;
	
	public TestPost1() {
		
	}
	
	public TestPost1(boolean init) {
		if(init) {
			post = new HashMap<>();
			post.put("en", "this is an example");
			post.put("es", "esto es un ejemplo");
			post.put("it", "questo é un esempio");
			post.put("fr", "Ceci est un exemple");
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
		if (!(obj instanceof TestPost1))
			return false;
		TestPost1 other = (TestPost1) obj;
		if (post == null) {
			if (other.post != null)
				return false;
		} else if (!post.equals(other.post))
			return false;
		return true;
	}
	
	
}
