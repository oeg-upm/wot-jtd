package kehio.models.test;

import java.util.Map;

import kehio.annotations.RdfDatatypeGroup;

public class TestPost4 {
	
	// -- RDF representation
	// throws exception
	// ----
	
	@RdfDatatypeGroup(value="http://xmlns.com/foaf/0.1/numbers", byDatatype=true)
	private Map<String,Boolean> post;
	
	public TestPost4() {
		
	}
	
	public TestPost4(boolean init) {
		
	}
}
