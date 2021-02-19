package wot.jtd.model;

import java.io.IOException;
import java.net.URI;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;

import wot.jtd.JTD;
import wot.jtd.exception.LinkValidationException;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.vocabulary.Vocabulary;


/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#link">Link</a> from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#link">Link WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Link extends AbstractJTDObject{

	// -- attributes
	
	@NotEmpty(message= "'href' must be a valid non-empty URI")
	@NotNull(message = "'href' must be a valid non-null URI")
	private URI href;
	@JsonProperty(Vocabulary.TYPE)
	private String mediaType; //from RFC2046
	private String rel;
	private URI anchor;
	
	// -- static constructors and validation method
	
	/**
	 * This method creates a validated instance of {@link Link}.
	 * @param href a valid URI
	 * @return an instantiated and validated  {@link Link}
	 * @throws SchemaValidationException
	 */
	public static Link create(URI href) throws SchemaValidationException {
		// Create link
		Link link = new Link();
		link.setHref(href);
		// Validate link
		validate(link);
		return link;
	}
	
	/**
	 * This method validates an instance of {@link Link}.
	 * @param link an instance of {@link Link}
	 * @throws SchemaValidationException
	 */
	public static void validate(Link link) throws SchemaValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Link>> violations = validator.validate(link);
		if(!violations.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
			throw new LinkValidationException(builder.toString());
		}
	}
	
	// -- serialization and deserialization

	/**
	 * This method transforms the current {@link Link} object into a {@link JsonObject}.
	 * @return a {@link JsonObject}
	 * @throws JsonProcessingException
	 */
	public JsonObject toJson() throws JsonProcessingException{
		return JTD.toJson(this);
	}
	
	/**
	 * This method instantiates and validates a {@link Link} object from a {@link JsonObject}.
	 * @param json a Link expressed as a {@link JsonObject}
	 * @return a valid {@link Link}
	 * @throws IOException
	 * @throws SchemaValidationException 
	 */
	public static Link fromJson(JsonObject json) throws IOException, SchemaValidationException {
		Link link = (Link) JTD.instantiateFromJson(json, Link.class);
		validate(link);
		return link;
	}
	
	
	// -- getters and setters
	
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
	 * @param type a valid mime type from the <a href="https://tools.ietf.org/html/rfc2046">RFC 2046</a> specification
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
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Link))
			return false;
		Link other = (Link) obj;
		if (anchor == null) {
			if (other.anchor != null)
				return false;
		} else if (!anchor.equals(other.anchor))
			return false;
		if (href == null) {
			if (other.href != null)
				return false;
		} else if (!href.equals(other.href))
			return false;
		if (mediaType == null) {
			if (other.mediaType != null)
				return false;
		} else if (!mediaType.equals(other.mediaType))
			return false;
		if (rel == null) {
			if (other.rel != null)
				return false;
		} else if (!rel.equals(other.rel))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}


	


	
	
	
}
