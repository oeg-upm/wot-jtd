package wot.jtd.model;

import java.util.Collection;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfObject;
import wot.jtd.Vocabulary;

/**
 * This class implements the object
 * <a href="https://www.w3.org/TR/wot-thing-description/#form">Form</a> from a
 * Thing Description as specified in the Web of Things (WoT) documentation.
 * <p>
 * 
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#form">Form WoT
 *      documentation</a>
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Form extends AbstractRdfObject {

	// -- Attributes

	@RdfObject(value = "https://www.w3.org/2019/wot/hypermedia#hasOperationType", base = "https://www.w3.org/2019/wot/td#")
	@JsonProperty(Vocabulary.OP)
	@Pattern(regexp = "readproperty|writeproperty|observeproperty|unobserveproperty|invokeaction|subscribeevent|unsubscribeevent|readallproperties|writeallproperties|readmultipleproperties|writemultipleproperties", flags = Pattern.Flag.CASE_INSENSITIVE, message = "'op' must have as value of the following: readproperty, writeproperty, observeproperty, unobserveproperty, invokeaction, subscribeevent, unsubscribeevent, readallproperties, writeallproperties, readmultipleproperties, or writemultipleproperties")
	private Collection<String> op; // compactable array

	@RdfObject("https://www.w3.org/2019/wot/hypermedia#hasTarget")
	@NotBlank(message = "'href' must be a valid non-empty and non-null URI or URI template")
	private String href;

	@RdfDatatype("https://www.w3.org/2019/wot/hypermedia#forContentType")
	private String contentType; // from RFC2046, default value

	@RdfDatatype("https://www.w3.org/2019/wot/hypermedia#forContentCoding")
	private String contentEncoding;

	@RdfDatatype("https://www.w3.org/2019/wot/hypermedia#forSubProtocol")
	private String subprotocol;

	@RdfDatatype("https://www.w3.org/2019/wot/td#hasSecurityConfiguration")
	private Collection<String> security;

	@RdfDatatype("https://www.w3.org/2019/wot/security#scopes")
	private Collection<String> scopes;

	@RdfObject("https://www.w3.org/2019/wot/hypermedia#returns")
	private ExpectedResponse response;

	// -- Getters & Setters

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Collection<String> getOp() {
		return op;
	}

	public void setOp(Collection<String> op) {
		this.op = op;
	}

	/**
	 * 
	 * @return a mime type from the
	 *         <a href="https://tools.ietf.org/html/rfc2046">RFC 2046</a>
	 *         specification
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * 
	 * @param contentType
	 *            a valid mime type from the
	 *            <a href="https://tools.ietf.org/html/rfc2046">RFC 2046</a>
	 *            specification
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public String getSubprotocol() {
		return subprotocol;
	}

	public void setSubprotocol(String subprotocol) {
		this.subprotocol = subprotocol;
	}

	public Collection<String> getSecurity() {
		return security;
	}

	public void setSecurity(Collection<String> security) {
		this.security = security;
	}

	public Collection<String> getScopes() {
		return scopes;
	}

	public void setScopes(Collection<String> scopes) {
		this.scopes = scopes;
	}

	public ExpectedResponse getResponse() {
		return response;
	}

	public void setResponse(ExpectedResponse response) {
		this.response = response;
	}

	// HashCode & Equals

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentEncoding == null) ? 0 : contentEncoding.hashCode());
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((href == null) ? 0 : href.hashCode());
		result = prime * result + ((op == null) ? 0 : op.hashCode());
		result = prime * result + ((response == null) ? 0 : response.hashCode());
		result = prime * result + ((scopes == null) ? 0 : scopes.hashCode());
		result = prime * result + ((security == null) ? 0 : security.hashCode());
		result = prime * result + ((subprotocol == null) ? 0 : subprotocol.hashCode());
		result = superHashcode(result, prime);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;

		Form other = (Form) obj;
		Boolean sameClass = sameSuperAttributes(other);
		sameClass &= sameAttribute(this.getContentEncoding(), other.getContentEncoding());
		sameClass &= sameAttribute(this.getContentType(), other.getContentType());
		sameClass &= sameAttribute(this.getHref(), other.getHref());
		sameClass &= sameAttribute(this.getSubprotocol(), other.getSubprotocol());
		sameClass &= sameAttribute(this.getResponse(), other.getResponse());

		sameClass &= sameCollections(this.getOp(), other.getOp());
		sameClass &= sameCollections(this.getScopes(), other.getScopes());
		sameClass &= sameCollections(this.getSecurity(), other.getSecurity());


		return sameClass;
	}

}
