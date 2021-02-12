package wot.jtd.model;

import java.io.IOException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;
import wot.jtd.JTD;
import wot.jtd.exception.ExpectedResponseValidationException;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.vocabulary.Vocabulary;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#expectedresponse">ExpectedResponse</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#expectedresponse">ExpectedResponse WoT documentation</a> 
 * @author Andrea Cimmino
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpectedResponse extends AbstractJTDObject{
	
	// -- attributes

	@NotBlank(message="'contentType' is mandatory and must be a valid mime type from RDC 2046")
	private String contentType; //from RFC2046
	@JsonProperty(Vocabulary.STATUS_CODE_NUMBER)
	private Integer statusCodeNumber;
	private String description;
	
	// -- ExpectedResponse Object methods
	
	/**
	 * This method creates a validated instance of {@link ExpectedResponse}.
	 * @param contentType a valid mime type from the <a href="https://tools.ietf.org/html/rfc2046">RFC 2046</a> specification
	 * @return
	 * @throws SchemaValidationException
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
	 * @throws SchemaValidationException
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
	 * @param expectedResponse a {@link ExpectedResponse} object
	 * @return a {@link JsonObject}
	 * @throws JsonProcessingException
	 */
	public JsonObject toJson() throws JsonProcessingException {
		return JTD.toJson(this);
	}

	/**
	 * This method instantiates and validates a {@link ExpectedResponse} object from a {@link JsonObject}.
	 * @param json a ExpectedResponse expressed as a {@link JsonObject}
	 * @return a valid {@link ExpectedResponse}
	 * @throws IOException
	 * @throws ExpectedResponseValidationException 
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
		
	public Integer getStatusCodeNumber() {
		return statusCodeNumber;
	}

	public void setStatusCodeNumber(Integer statusCodeNumber) {
		this.statusCodeNumber = statusCodeNumber;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// -- hashCode and equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((statusCodeNumber == null) ? 0 : statusCodeNumber.hashCode());
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
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (statusCodeNumber == null) {
			if (other.statusCodeNumber != null)
				return false;
		} else if (!statusCodeNumber.equals(other.statusCodeNumber))
			return false;
		return true;
	}




	
	
	
	
	
}
