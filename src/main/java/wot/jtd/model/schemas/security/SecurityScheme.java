package wot.jtd.model.schemas.security;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;

import kehio.annotations.RdfDatatype;
import kehio.annotations.RdfDatatypeGroup;
import kehio.annotations.RdfObject;
import wot.jtd.model.AbstractRdfObject;

/**
 * This class implements the object <a href=
 * "https://www.w3.org/TR/wot-thing-description/#securityscheme">SecurityScheme</a>
 * (and its sub-classes) from a Thing Description as specified in the Web of
 * Things (WoT) documentation.
 * <p>
 * 
 * @see <a href=
 *      "https://www.w3.org/TR/wot-thing-description/#securityscheme">SecurityScheme
 *      WoT documentation</a>
 * @author Andrea Cimmino
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecurityScheme extends AbstractRdfObject {

	// -- Attributes

	@RdfDatatype("https://www.w3.org/2019/wot/td#scheme")
	@NotBlank(message = "'scheme' must not be blank, e.g., nosec, basic, digest, bearer, psk, oauth2, or apikey")
	private String scheme;

	@RdfDatatype("http://purl.org/dc/terms/description")
	private String description;

	@RdfDatatypeGroup(value = "http://purl.org/dc/terms/description", byLang = true)
	private Map<String, String> descriptions;

	@RdfObject("https://www.w3.org/2019/wot/td#proxy")
	private URI proxy;

	// digest
	@RdfDatatype("https://www.w3.org/2019/wot/td#qop")
	@Pattern(regexp = "auth|auth-int", flags = Pattern.Flag.CASE_INSENSITIVE, message = "'qop' must be one of: 'auth' or 'auth-int'.")
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
	
	@RdfDatatype("https://www.w3.org/2019/wot/td#scopes")
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
	@Pattern(regexp = "header|query|body|cookie", flags = Pattern.Flag.CASE_INSENSITIVE, message = "'in' must be one of: 'header', 'query', 'body', or 'cookie'")
	private String in;

	@RdfDatatype("https://www.w3.org/2019/wot/td#name")
	private String name;

	// -- Getters & Setters

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

	public void setQop(String qop) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		result = superHashcode(result, prime);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;

		SecurityScheme other = (SecurityScheme) obj;
		Boolean sameClass = sameSuperAttributes(other);
		sameClass &= sameAttribute(this.getDescription(), other.getDescription());
		sameClass &= sameAttribute(this.getScheme(), other.getScheme());
		sameClass &= sameAttribute(this.getProxy(), other.getProxy());
		sameClass &= sameMap(this.getDescriptions(), other.getDescriptions());
		// apikey + basic + (~bearer) + (~digest)
		sameClass &= sameAttribute(this.getIn(), other.getIn());
		sameClass &= sameAttribute(this.getName(), other.getName());
		// (~bearer)  + (~oauth)
		sameClass &= sameAttribute(this.getAuthorization(), other.getAuthorization());
		sameClass &= sameAttribute(this.getAlg(), other.getAlg());
		sameClass &= sameAttribute(this.getFormat(), other.getFormat());
		// (~digest)
		sameClass &= sameAttribute(this.getQop(), other.getQop());
		// oauth
		sameClass &= sameAttribute(this.getToken(), other.getToken());
		sameClass &= sameAttribute(this.getRefresh(), other.getRefresh());
		sameClass &= sameCollections(this.getScopes(), other.getScopes());
		sameClass &= sameAttribute(this.getFlow(), other.getFlow());
		// psk
		sameClass &= sameAttribute(this.getIdentity(), other.getIdentity());
		return sameClass;
	}
	

}
