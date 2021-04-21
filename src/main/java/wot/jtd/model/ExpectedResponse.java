package wot.jtd.model;

import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonInclude;

import kehio.annotations.RdfDatatype;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#expectedresponse">ExpectedResponse</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#expectedresponse">ExpectedResponse WoT documentation</a> 
 * @author Andrea Cimmino
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpectedResponse extends AbstractRdfObject{
	
	// -- attributes
	@RdfDatatype("https://www.w3.org/2019/wot/hypermedia#forContentType")
	@NotBlank(message="'contentType' is mandatory and must be a valid mime type from RDC 2046")
	protected String contentType; //from RFC2046

	// -- Getters & Setters
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
		
	// -- HashCode & Equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = superHashcode(result, prime);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		
		ExpectedResponse other = (ExpectedResponse) obj;
		return  sameSuperAttributes(other) && sameAttribute(this.getContentType(), other.getContentType());
	}




	
	
	
	
	
}
