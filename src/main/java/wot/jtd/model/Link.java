package wot.jtd.model;

import java.net.URI;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfObject;
import wot.jtd.Vocabulary;


/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#link">Link</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#link">Link WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Link extends AbstractRdfObject{

	// -- Attributes
	
	@RdfObject("https://www.w3.org/2019/wot/hypermedia#hasTarget")
	@NotEmpty(message= "'href' must be a valid non-empty URI")
	@NotNull(message = "'href' must be a valid non-null URI")
	protected URI href;
	
	@RdfDatatype("https://www.w3.org/2019/wot/hypermedia#hintsAtMediaType")
	@JsonProperty(Vocabulary.JSONLD_TYPE_ALIAS)
	protected String mediaType; //from RFC2046
	
	@RdfObject(value="https://www.w3.org/2019/wot/hypermedia#hasRelationType", base="https://www.w3.org/2019/wot/hypermedia#")
	protected String rel;
	
	@RdfObject("https://www.w3.org/2019/wot/hypermedia#hasAnchor")
	protected URI anchor;
	
	// -- Getters & Setters
	
	public URI getHref() {
		return href;
	}
	public void setHref(URI href) {
		this.href = href;
	}
	
	/**
	 * 
	 * @return a mime type from the <a href="https://tools.ietf.org/html/rfc2046">RFC 2046</a> specification
	 */
	public String getMediaType() {
		return mediaType;
	}
	
	/**
	 * 
	 * @param mediaType a valid mime type from the <a href="https://tools.ietf.org/html/rfc2046">RFC 2046</a> specification
	 */
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	
	public String getRel() {
		return rel;
	}
	
	public void setRel(String rel) {
		this.rel = rel;
	}
	
	public URI getAnchor() {
		return anchor;
	}
	
	public void setAnchor(URI anchor) {
		this.anchor = anchor;
	}
	
	// -- hashCode and equals

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((anchor == null) ? 0 : anchor.hashCode());
		result = prime * result + ((href == null) ? 0 : href.hashCode());
		result = prime * result + ((mediaType == null) ? 0 : mediaType.hashCode());
		result = prime * result + ((rel == null) ? 0 : rel.hashCode());
		result = superHashcode(result, prime);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		
		Link other = (Link) obj;
		Boolean sameClass = sameSuperAttributes(other);
		sameClass &= sameAttribute(this.getAnchor(), other.getAnchor());
		sameClass &= sameAttribute(this.getHref(), other.getHref());
		sameClass &= sameAttribute(this.getMediaType(), other.getMediaType());
		sameClass &= sameAttribute(this.getRel(), other.getRel());
		
		return sameClass;
	}


	
	
	
}
