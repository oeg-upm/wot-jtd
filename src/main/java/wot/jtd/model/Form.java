package wot.jtd.model;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;

import kehio.annotations.done.RdfDatatype;
import kehio.annotations.done.RdfDatatypeCollection;
import kehio.annotations.done.RdfObject;
import kehio.annotations.done.RdfObjectCollection;
import wot.jtd.JTD;
import wot.jtd.exception.FormValidationException;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.vocabulary.Vocabulary;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#form">Form</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#form">Form WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Form extends AbstractJTDObject{

	// -- attributes
	@RdfObjectCollection("https://www.w3.org/2019/wot/hypermedia#hasOperationType")
	@JsonProperty(Vocabulary.OP)
	@Pattern(regexp = "readproperty|writeproperty|observeproperty|unobserveproperty|invokeaction|subscribeevent|unsubscribeevent|readallproperties|writeallproperties|readmultipleproperties|writemultipleproperties", flags = Pattern.Flag.CASE_INSENSITIVE, message="'op' must have as value of the following: readproperty, writeproperty, observeproperty, unobserveproperty, invokeaction, subscribeevent, unsubscribeevent, readallproperties, writeallproperties, readmultipleproperties, or writemultipleproperties")
	private Collection<String> op; // compactable array
	
	@RdfObject("https://www.w3.org/2019/wot/hypermedia#hasTarget")
	@NotBlank(message = "'href' must be a valid non-empty and non-null URI or URI template")
	private String href;
	
	@RdfDatatype("https://www.w3.org/2019/wot/hypermedia#forContentType")
	private String contentType; //from RFC2046, default value
	@RdfDatatype("https://www.w3.org/2019/wot/hypermedia#forContentCoding")
	private String contentEncoding; 
	@RdfDatatype("https://www.w3.org/2019/wot/hypermedia#forSubProtocol")
	private String subprotocol;
	@RdfDatatypeCollection("https://www.w3.org/2019/wot/td#hasSecurityConfiguration")
	private Collection<String> security;
	@RdfDatatypeCollection("https://www.w3.org/2019/wot/security#scopes")
	private Collection<String> scopes;
	@RdfObject("https://www.w3.org/2019/wot/hypermedia#returns")
	private ExpectedResponse response;
	


	
	// -- constructors and validation methods (static)
	
	/**
	 * This method creates a validated instance of {@link Form}, if default values are enabled in {@link JTD} the {@link Form} will have as content type 'application/json'
	 * @param href a valid URI
	 * @return an instantiated and validated  {@link Form}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect 
	 */
	public static Form create(String href) throws SchemaValidationException {
		// Create form
		Form form = new Form();
		form.setHref(href);
		if(JTD.getDefaultValues())
			form.setContentType(Vocabulary.MIME_JSON);
		// Validate created form
		validate(form);
		return form;
	}
	
	/**
	 * This method validates an instance a {@link Form} object.
	 * @param form an instance of {@link Form}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static void validate(Form form) throws SchemaValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Form>> violations = validator.validate(form);
		StringBuilder builder = new StringBuilder();
		if(!violations.isEmpty()) {
			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
			throw new FormValidationException(builder.toString());
		}
		// TODO: check that op is one of: readproperty, writeproperty, observeproperty, unobserveproperty, invokeaction, subscribeevent, unsubscribeevent, readallproperties, writeallproperties, readmultipleproperties, or writemultiplepropertie
		// TODO: add to the documentation that OP can be only one of these values ==> transform op in enum?
		// TODO: check that methodName is one REST operation, check if there are more
		if(form.getResponse()!=null)
			ExpectedResponse.validate(form.getResponse());
	}
	
	// -- serialization and deserialization

	/**
	 * This method transforms the current {@link Form} object into a {@link JsonObject}.
	 * @return a {@link JsonObject}
	 * @throws JsonProcessingException this exception is thrown when the syntax of the Thing Description as {@link JsonObject} is incorrect
	 */
	public JsonObject toJson() throws JsonProcessingException {
		return JTD.toJson(this);
	}
	
	
	/**
	 * This method instantiates and validates a {@link Form} object from a {@link JsonObject}.
	 * @param json a Link expressed as a {@link JsonObject}
	 * @return a valid {@link Form}
	 * @throws IOException this exception is thrown when the syntax of the {@link JsonObject} is incorrect
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as {@link JsonObject} is incorrect
	*/
	public static Form fromJson(JsonObject json) throws IOException, SchemaValidationException {
		Form form = (Form) JTD.instantiateFromJson(json, Form.class);
		validate(form);
		return form;
	}
	
	// -- getters and setters
	
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
	 * @return a mime type from the <a href="https://tools.ietf.org/html/rfc2046">RFC 2046</a> specification
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * 
	 * @param contentType a valid mime type from the <a href="https://tools.ietf.org/html/rfc2046">RFC 2046</a> specification
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


	// hashcode and equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contentEncoding == null) ? 0 : contentEncoding.hashCode());
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((href == null) ? 0 : href.hashCode());
		result = prime * result + ((op == null) ? 0 : op.hashCode());
		result = prime * result + ((response == null) ? 0 : response.hashCode());
		result = prime * result + ((scopes == null) ? 0 : scopes.hashCode());
		result = prime * result + ((security == null) ? 0 : security.hashCode());
		result = prime * result + ((subprotocol == null) ? 0 : subprotocol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Form))
			return false;
		Form other = (Form) obj;
		if (contentEncoding == null) {
			if (other.contentEncoding != null)
				return false;
		} else if (!contentEncoding.equals(other.contentEncoding))
			return false;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (href == null) {
			if (other.href != null)
				return false;
		} else if (!href.equals(other.href))
			return false;
		
		if (op == null) {
			if (other.op != null)
				return false;
		} else if (!op.equals(other.op))
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		if (scopes == null) {
			if (other.scopes != null)
				return false;
		} else if (!scopes.equals(other.scopes))
			return false;
		if (security == null) {
			if (other.security != null)
				return false;
		} else if (!security.equals(other.security))
			return false;
		if (subprotocol == null) {
			if (other.subprotocol != null)
				return false;
		} else if (!subprotocol.equals(other.subprotocol))
			return false;
		return true;
	}
	
	


	

	
	
	
	
}
