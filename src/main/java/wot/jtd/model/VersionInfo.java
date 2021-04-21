package wot.jtd.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;

import kehio.annotations.RdfDatatype;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#versioninfo">VersionInfo</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#versioninfo">VersionInfo WoT documentation</a>
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VersionInfo extends AbstractRdfObject{

	// -- attributes
	@RdfDatatype(value="https://www.w3.org/2019/wot/td#instance")
	@Pattern(regexp = "^[0-9]\\.[0-9]\\.[0-9]$", message="The pattern of 'instance' must be three numbers separated by '.'; e.g., '1.0.3'")
	@NotBlank(message = "The 'instance' must be a non-blak string that follows the semantic versioning pattern from https://semver.org/")
	protected String instance;
	
	
	// -- Getters & Setters
	
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
		// Super class hashCode
		result = superHashcode(result, prime);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		
		VersionInfo other = (VersionInfo) obj;
		Boolean sameClass = sameSuperAttributes(other);
		sameClass &= sameAttribute(this.instance, other.getInstance());
		
		return sameClass;
	}
	
}
