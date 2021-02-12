package wot.jtd.model;

import java.io.IOException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;
import wot.jtd.JTD;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.exception.VersionInfoValidationException;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#versioninfo">VersionInfo</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#versioninfo">VersionInfo WoT documentation</a>
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VersionInfo extends AbstractJTDObject{

	// -- attributes
	
	@Pattern(regexp = "^[0-9]\\.[0-9]\\.[0-9]$", message="The pattern of 'instance' must be three numbers separated by '.'; e.g., '1.0.3'")
	@NotBlank(message = "The 'instance' must be a non-blak string that follows the semantic versioning pattern from https://semver.org/")
	private String instance;
	
	// -- static constructors and validation method
	
	/**
	 * This method creates a validated instance of {@link VersionInfo}.
	 * @param instance a valid version number following the pattern '^[0-9]\.[0-9]\.[0-9]$', e.g., "1.0.3"
	 * @return an instantiated and validated  {@link VersionInfo}
	 * @throws SchemaValidationException
	 */
	public static VersionInfo create(String instance) throws SchemaValidationException {
		// Create version info
		VersionInfo versionInfo = new VersionInfo();
		versionInfo.setInstance(instance);
		// Validate version info
		validate(versionInfo);
		return versionInfo;
	}

	/**
	 * This method validates an instance of {@link VersionInfo}.
	 * @param versionInfo an instance of {@link VersionInfo}
	 * @throws SchemaValidationException
	 */
	public static void validate(VersionInfo versionInfo) throws SchemaValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<VersionInfo>> violations = validator.validate(versionInfo);
		if(!violations.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
			throw new VersionInfoValidationException(builder.toString());
		}
	}

	// -- serialization and de-serialization
	
	/**
	 * This method transforms the current {@link VersionInfo} object into a {@link JsonObject}.
	 * @return a {@link JsonObject}
	 * @throws JsonProcessingException
	 */
	public JsonObject toJson() throws JsonProcessingException{
		return JTD.toJson(this);
	}
	
	/**
	 * This method instantiates and validates a {@link VersionInfo} object from a {@link JsonObject}.
	 * @param json a VersionInfo expressed as a {@link JsonObject}
	 * @return a valid {@link VersionInfo}
	 * @throws IOException
	 * @throws SchemaValidationException
	 */
	public static VersionInfo fromJson(JsonObject json) throws IOException, SchemaValidationException {
		VersionInfo versionInfo = (VersionInfo) JTD.instantiateFromJson(json, VersionInfo.class);
		validate(versionInfo);
		return versionInfo;
	}
		
	// -- getters and setters
	
	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}
	
	// -- hashCode and equals

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((instance == null) ? 0 : instance.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof VersionInfo))
			return false;
		VersionInfo other = (VersionInfo) obj;
		if (instance == null) {
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		return true;
	}


	
	
	
	
	
}
