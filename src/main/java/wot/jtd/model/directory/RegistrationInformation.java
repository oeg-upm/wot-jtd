package wot.jtd.model.directory;

import com.fasterxml.jackson.annotation.JsonInclude;

import kehio.annotations.RdfDatatype;
import wot.jtd.model.AbstractRdfObject;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationInformation extends AbstractRdfObject {

	@RdfDatatype(value="http://schema.org/dateCreated", datatype="http://www.w3.org/2001/XMLSchema#dateTime")
	private String created;
	@RdfDatatype(value="http://schema.org/dateModified", datatype="http://www.w3.org/2001/XMLSchema#dateTime")
	private String modified;
	@RdfDatatype(value="http://schema.org/expires", datatype="http://www.w3.org/2001/XMLSchema#dateTime")
	private String expires;
	@RdfDatatype(value="https://www.w3.org/2021/wot/discovery#ttl", datatype="http://www.w3.org/2001/XMLSchema#unsignedInt")
	private String ttl;
	@RdfDatatype(value="https://www.w3.org/2021/wot/discovery#retrieved", datatype="http://www.w3.org/2001/XMLSchema#dateTime")
	private String retrieved;
	
	
	public RegistrationInformation() {
		super();
	}


	public String getCreated() {
		return created;
	}


	public void setCreated(String created) {
		this.created = created;
	}


	public String getModified() {
		return modified;
	}


	public void setModified(String modified) {
		this.modified = modified;
	}


	public String getExpires() {
		return expires;
	}


	public void setExpires(String expires) {
		this.expires = expires;
	}


	public String getTtl() {
		return ttl;
	}


	public void setTtl(String ttl) {
		this.ttl = ttl;
	}


	public String getRetrieved() {
		return retrieved;
	}


	public void setRetrieved(String retrieved) {
		this.retrieved = retrieved;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((expires == null) ? 0 : expires.hashCode());
		result = prime * result + ((modified == null) ? 0 : modified.hashCode());
		result = prime * result + ((retrieved == null) ? 0 : retrieved.hashCode());
		result = prime * result + ((ttl == null) ? 0 : ttl.hashCode());
		result = superHashcode(result, prime);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		
		RegistrationInformation other = (RegistrationInformation) obj;
		Boolean sameClass = sameSuperAttributes(other);
		// Primitive or Object attributes
		sameClass &= sameAttribute(this.getCreated(), other.getCreated());
		sameClass &= sameAttribute(this.getModified(), other.getModified());
		sameClass &= sameAttribute(this.getExpires(), other.getExpires());
		sameClass &= sameAttribute(this.getTtl(), other.getTtl());
		sameClass &= sameAttribute(this.getRetrieved(), other.getRetrieved());
		
		return sameClass;
	}

	

	
}
