package kehio.models.test;

import java.util.Map;

import kehio.annotations.RdfDatatypeGroup;

public class TestPost3 {
	
	// -- RDF representation
	// throws exception
	// ----
	
	@RdfDatatypeGroup(value="http://xmlns.com/foaf/0.1/numbers", byDatatype=true)
	private Map<Integer,String> post;
	
	public TestPost3() {
		
	}
	
	public TestPost3(boolean init) {
		
	}
}
