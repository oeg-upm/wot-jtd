package wot.jtd;

public class Vocabulary {
	
	private Vocabulary() {
		super();
	}

	public static final String JSONLD_CONTEXT = "@context";
	public static final String JSONLD_TYPE = "@type";
	public static final String JSONLD_ID = "@id";
	public static final String JSONLD_TYPE_ALIAS = "type";
	
	public static final String JSONLD_REGISTRATION_ALIAS = "registration";
	
	public static final String STATUS_CODE_NUMBER = "htv:statusCodeNumber";
	public static final String STATUS_CODE_VALUE = "htv:statusCodeValue";
	public static final String METHOD_NAME = "htv:methodName";
	public static final String ENUM = "enum";
	public static final String SCOPES = "scopes";
	public static final String OP = "op";
	public static final String ITEMS = "items";
	public static final String CONS = "const";
	public static final String SECURITY = "security";
	
	public static final String HEADER = "header";
	
	// Default values
	
	public static final String MIME_JSON = "application/json";
	public static final String JSON_SCHEMA_URI = "https://www.w3.org/2019/wot/json-schema#";
	
	
	// Security schemes
	
	public static final String NO_SECURITY_SCHEME = "nosec";
	public static final String BASIC_SECURITY_SCHEME = "basic";
	public static final String DIGEST_SECURITY_SCHEME = "digest";
	public static final String API_KEY_SECURITY_SCHEME = "apikey";
	public static final String BEARER_SECURITY_SCHEME = "bearer";
	public static final String PSK_SECURITY_SCHEME = "psk";
	public static final String OAUTH2_SECURITY_SCHEME = "oauth2";

	
}
