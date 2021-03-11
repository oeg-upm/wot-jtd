package wot.jtd.model;

import java.io.IOException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;

import kehio.annotations.done.RdfDatatype;
import wot.jtd.JTD;
import wot.jtd.exception.ExpectedResponseValidationException;
import wot.jtd.exception.SchemaValidationException;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#expectedresponse">ExpectedResponse</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#expectedresponse">ExpectedResponse WoT documentation</a> 
 * @author Andrea Cimmino
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpectedResponse extends AbstractJTDObject{
	
	// -- attributes
	@RdfDatatype("https://www.w3.org/2019/wot/hypermedia#forContentType")
	@NotBlank(message="'contentType' is mandatory and must be a valid mime type from RDC 2046")
	private String contentType; //from RFC2046

	// -- ExpectedResponse Object methods
	
	/**
	 * This method creates a validated instance of {@link ExpectedResponse}.
	 * @param contentType a valid mime type from the <a href="https://tools.ietf.org/html/rfc2046">RFC 2046</a> specification
	 * @return a valid {@link ExpectedResponse}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static ExpectedResponse create(String contentType) throws SchemaValidationException {
		// Create version info
		ExpectedResponse expectedResponse = new ExpectedResponse();
		expectedResponse.setContentType(contentType);
		// Validate version info
		validate(expectedResponse);
		return expectedResponse;
	}
	
	/**
	 * This method validates an instance of {@link ExpectedResponse}.
	 * @param expectedResponse an instance of {@link ExpectedResponse}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static void validate(ExpectedResponse expectedResponse) throws SchemaValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<ExpectedResponse>> violations = validator.validate(expectedResponse);
		if(!violations.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
			throw new ExpectedResponseValidationException(builder.toString());
		}
	}

	// -- serialization and deserialization

	/**
	 * This method transforms the current {@link ExpectedResponse} object into a {@link JsonObject}.
	 * @return a {@link JsonObject}
	 * @throws JsonProcessingException this exception is thrown when the syntax of the Thing Description as {@link JsonObject} is incorrect
	 */
	public JsonObject toJson() throws JsonProcessingException {
		return JTD.toJson(this);
	}

	/**
	 * This method instantiates and validates a {@link ExpectedResponse} object from a {@link JsonObject}.
	 * @param json a ExpectedResponse expressed as a {@link JsonObject}
	 * @return a valid {@link ExpectedResponse}
	 * @throws IOException this exception is thrown when the syntax of the {@link JsonObject} is incorrect
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as {@link JsonObject} is incorrect
	*/
	public static ExpectedResponse fromJson(JsonObject json) throws IOException, SchemaValidationException {
		ExpectedResponse expectedResponse = (ExpectedResponse) JTD.instantiateFromJson(json, ExpectedResponse.class);
		validate(expectedResponse);
		return expectedResponse;
	}
	// -- getters and setters
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
		

	// -- hashCode and equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ExpectedResponse))
			return false;
		ExpectedResponse other = (ExpectedResponse) obj;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		return true;
	}




	
	
	
	
	
}
