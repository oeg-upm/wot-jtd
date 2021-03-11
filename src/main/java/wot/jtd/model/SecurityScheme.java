package wot.jtd.model;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
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

import kehio.annotations.done.RdfDatatype;
import kehio.annotations.done.RdfDatatypeCollection;
import kehio.annotations.done.RdfDatatypeContainer;
import kehio.annotations.done.RdfObject;
import wot.jtd.JTD;
import wot.jtd.exception.SchemaValidationException;
import wot.jtd.exception.SecuritySchemeValidationException;
import wot.jtd.vocabulary.Vocabulary;

/**
 * This class implements the object <a href="https://www.w3.org/TR/wot-thing-description/#securityscheme">SecurityScheme</a> (and its sub-classes) from a Thing Description as specified in the Web of Things (WoT) documentation.<p>
 * @see <a href="https://www.w3.org/TR/wot-thing-description/#securityscheme">SecurityScheme WoT documentation</a> 
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecurityScheme extends AbstractJTDObject{

	// TODO: añadir restricciones a los getters y setters basados en el schema actual
	
	// -- attributes

	private enum defaultSchemes {
		nosec, basic, digest, psk, oauth2, bearer, apikey
	}
	@RdfDatatype("https://www.w3.org/2019/wot/td#scheme")
	@NotBlank(message = "'scheme' must not be blank, e.g., nosec, basic, digest, bearer, psk, oauth2, or apikey")
	private String scheme;
	@RdfDatatype("http://purl.org/dc/terms/description")
	private String description;

	@RdfDatatypeContainer(value="http://purl.org/dc/terms/description",byLang=true)
	private Map<String, String> descriptions;
	
	@RdfObject("https://www.w3.org/2019/wot/td#proxy")
	private URI proxy;
	
	// digest
	@RdfDatatype("https://www.w3.org/2019/wot/td#qop")
	@Pattern(regexp = "auth|auth-int", flags = Pattern.Flag.CASE_INSENSITIVE, message="'qop' must be one of: 'auth' or 'auth-int'.")
	private String qop;
	// psk
	@RdfDatatype("https://www.w3.org/2019/wot/td#identity")
	private String identity;
	// oauth2
	@RdfObject("https://www.w3.org/2019/wot/td#token")
	private URI token;
	@RdfObject("https://www.w3.org/2019/wot/td#refresh")
	private URI refresh;
	@RdfDatatype("https://www.w3.org/2019/wot/td#flow")
	private String flow;
	@RdfDatatypeCollection("https://www.w3.org/2019/wot/td#scopes")
	private Collection<String> scopes;
	// bearer
	@RdfDatatype("https://www.w3.org/2019/wot/td#alg")
	private String alg; 
	@RdfDatatype("https://www.w3.org/2019/wot/td#format")
	private String format; 
	// oauth2 + bearer
	@RdfObject("https://www.w3.org/2019/wot/td#authorization")
	private URI authorization;
	// basic + digest + apikey + bearer
	@RdfDatatype("https://www.w3.org/2019/wot/td#in")
	@Pattern(regexp = "header|query|body|cookie", flags = Pattern.Flag.CASE_INSENSITIVE, message="'in' must be one of: 'header', 'query', 'body', or 'cookie'")
	private String in;
	@RdfDatatype("https://www.w3.org/2019/wot/td#name")
	private String name;
	
	// -- constructors and validation methods (static)

	/**
	 * This method creates a validated instance of {@link SecurityScheme}, if default values are enabled in {@link JTD} some attributes from the {@link SecurityScheme} will be initialized <a href="https://www.w3.org/TR/wot-thing-description/#sec-default-values">depending on the schema</a>.
	 * @param scheme a valid security scheme, e.g., nosec, basic, digest, psk, oauth2, bearer, or apikey 
	 * @return an instantiated and validated  {@link SecurityScheme}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static SecurityScheme create(String scheme) throws SchemaValidationException {
		// Create form
		SecurityScheme securityScheme = new SecurityScheme();
		securityScheme.setScheme(scheme);
		// TODO: initiaise default values based on the scheme provided
		// Validate created form
		validate(securityScheme);
		return securityScheme;
	}
	
	/**
	 * This method creates a validated instance of {@link SecurityScheme} with 'nosec' as 'scheme'
	 * @return an instantiated and validated  {@link SecurityScheme}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static SecurityScheme createNoSecurityScheme() throws SchemaValidationException {
		return create(defaultSchemes.nosec.toString());
	}
	/**
	 * This method creates a validated instance of {@link SecurityScheme} with 'basic' as 'scheme', if default values are enabled in {@link JTD} the attribute 'in' is initialized with 'header'
	 * @return an instantiated and validated  {@link SecurityScheme}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static SecurityScheme createBasicSecurityScheme() throws SchemaValidationException {
		SecurityScheme scheme = create(defaultSchemes.basic.toString());
		if(JTD.getDefaultValues())
			scheme.setIn(Vocabulary.HEADER);
		return scheme;
	}
	/**
	 * This method creates a validated instance of {@link SecurityScheme} with 'digest' as 'scheme', if default values are enabled in {@link JTD} the attribute 'in' is initialized with 'header' and 'qop' with 'auth'
	 * @return an instantiated and validated  {@link SecurityScheme}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static SecurityScheme createDigestSecurityScheme() throws SchemaValidationException {
		SecurityScheme scheme = create(defaultSchemes.digest.toString());
		if(JTD.getDefaultValues()) {
			scheme.setIn(Vocabulary.HEADER);
			scheme.setQop("auth");
		}
		return scheme;
	}
	/**
	 * This method creates a validated instance of {@link SecurityScheme} with 'bearer' as 'scheme', if default values are enabled in {@link JTD} the attribute 'in' is initialized with 'header', 'alg' with 'ES256', and 'format' with 'jwt'
	 * @return an instantiated and validated  {@link SecurityScheme}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static SecurityScheme createBearerSecurityScheme() throws SchemaValidationException {
		SecurityScheme scheme = create(defaultSchemes.bearer.toString());
		if(JTD.getDefaultValues()) {
			scheme.setIn(Vocabulary.HEADER);
			scheme.setAlg("ES256");
			scheme.setFormat("jwt");
		}
		return scheme;
	}
	/**
	 * This method creates a validated instance of {@link SecurityScheme} with 'apikey' as 'scheme', if default values are enabled in {@link JTD} the attribute 'in' is initialized with 'query'
	 * @return an instantiated and validated  {@link SecurityScheme}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static SecurityScheme createAPIKeySecurityScheme() throws SchemaValidationException {
		SecurityScheme scheme = create(defaultSchemes.apikey.toString());
		if(JTD.getDefaultValues()) 
			scheme.setIn("query");
		return scheme;
	}
	/**
	 * This method creates a validated instance of {@link SecurityScheme} with 'psk' as 'scheme'.
	 * @return an instantiated and validated  {@link SecurityScheme}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static SecurityScheme createPSKSecurityScheme() throws SchemaValidationException {
		return create(defaultSchemes.psk.toString());
	}
	/**
	 * This method creates a validated instance of {@link SecurityScheme} with 'oauth2' as 'scheme'.
	 * @return an instantiated and validated  {@link SecurityScheme}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static SecurityScheme createOAuth2SecurityScheme() throws SchemaValidationException {
		return create(defaultSchemes.oauth2.toString());
	}

	/**
	 * This method validates an instance a {@link SecurityScheme} object.
	 * @param securityScheme an instance of {@link SecurityScheme}
	 * @throws SchemaValidationException this exception is thrown when the syntax of the Thing Description as ORM is incorrect
	 */
	public static void validate(SecurityScheme securityScheme) throws SchemaValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<SecurityScheme>> violations = validator.validate(securityScheme);
		StringBuilder builder = new StringBuilder();
		if(!violations.isEmpty()) {
			violations.forEach(msg -> builder.append(msg.getMessage()).append("\n"));
			throw new SecuritySchemeValidationException(builder.toString());
		}

	}

	// -- serialization and deserialization

	/**
	 * This method transforms the current {@link SecurityScheme} object into a {@link JsonObject}.
	 * @return a {@link JsonObject}
	 * @throws JsonProcessingException this exception is thrown when the syntax of the Thing Description as {@link JsonObject} is incorrect
	 */
	public JsonObject toJson() throws JsonProcessingException {
		return JTD.toJson(this);
	}
	
	/**
	 * This method instantiates and validates a {@link SecurityScheme} object from a {@link JsonObject}.
	 * @param json a SecurityScheme expressed as a {@link JsonObject}
	 * @return a valid {@link SecurityScheme}
	 * @throws IOException this exception is thrown when the syntax of the {@link JsonObject} is incorrect
	 * @throws SchemaValidationException  this exception is thrown when the syntax of the Thing Description as {@link JsonObject} is incorrect
	 */
	public static SecurityScheme fromJson(JsonObject json) throws IOException, SchemaValidationException {
		SecurityScheme securityScheme = (SecurityScheme) JTD.instantiateFromJson(json, SecurityScheme.class);
		validate(securityScheme);
		return securityScheme;
	}
	

	// -- getters and setters

	@Override
	public Collection<String> getType() {
		return type;
	}

	@Override
	public void setType(Collection<String> type) {
		this.type = type;
	}

	
	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Map<String, String> descriptions) {
		this.descriptions = descriptions;
	}

	public URI getProxy() {
		return proxy;
	}

	public void setProxy(URI proxy) {
		this.proxy = proxy;
	}

	public String getQop() {
		return qop;
	}

	public void setQop(String qop)  {
		this.qop = qop;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public URI getToken() {
		return token;
	}

	public void setToken(URI token) {
		this.token = token;
	}

	public URI getRefresh() {
		return refresh;
	}

	public void setRefresh(URI refresh) {
		this.refresh = refresh;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public Collection<String> getScopes() {
		return scopes;
	}

	public void setScopes(Collection<String> scopes) {
		this.scopes = scopes;
	}
	
	public String getAlg() {
		return alg;
	}

	public void setAlg(String alg) {
		this.alg = alg;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public URI getAuthorization() {
		return authorization;
	}

	public void setAuthorization(URI authorization) {
		this.authorization = authorization;
	}

	public String getIn() {
		return in;
	}

	public void setIn(String in) {
		this.in = in;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	// hashcode and equals

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((alg == null) ? 0 : alg.hashCode());
		result = prime * result + ((authorization == null) ? 0 : authorization.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((descriptions == null) ? 0 : descriptions.hashCode());
		result = prime * result + ((flow == null) ? 0 : flow.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((identity == null) ? 0 : identity.hashCode());
		result = prime * result + ((in == null) ? 0 : in.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((proxy == null) ? 0 : proxy.hashCode());
		result = prime * result + ((qop == null) ? 0 : qop.hashCode());
		result = prime * result + ((refresh == null) ? 0 : refresh.hashCode());
		result = prime * result + ((scheme == null) ? 0 : scheme.hashCode());
		result = prime * result + ((scopes == null) ? 0 : scopes.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof SecurityScheme))
			return false;
		SecurityScheme other = (SecurityScheme) obj;
		if (alg == null) {
			if (other.alg != null)
				return false;
		} else if (!alg.equals(other.alg))
			return false;
		if (authorization == null) {
			if (other.authorization != null)
				return false;
		} else if (!authorization.equals(other.authorization))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (descriptions == null) {
			if (other.descriptions != null)
				return false;
		} else if (!descriptions.equals(other.descriptions))
			return false;
		if (flow == null) {
			if (other.flow != null)
				return false;
		} else if (!flow.equals(other.flow))
			return false;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (identity == null) {
			if (other.identity != null)
				return false;
		} else if (!identity.equals(other.identity))
			return false;
		if (in == null) {
			if (other.in != null)
				return false;
		} else if (!in.equals(other.in))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (proxy == null) {
			if (other.proxy != null)
				return false;
		} else if (!proxy.equals(other.proxy))
			return false;
		if (qop == null) {
			if (other.qop != null)
				return false;
		} else if (!qop.equals(other.qop))
			return false;
		if (refresh == null) {
			if (other.refresh != null)
				return false;
		} else if (!refresh.equals(other.refresh))
			return false;
		if (scheme == null) {
			if (other.scheme != null)
				return false;
		} else if (!scheme.equals(other.scheme))
			return false;
		if (scopes == null) {
			if (other.scopes != null)
				return false;
		} else if (!scopes.equals(other.scopes))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	

	
	

	
}
