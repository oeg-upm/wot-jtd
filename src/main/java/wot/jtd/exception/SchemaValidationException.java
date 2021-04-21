package wot.jtd.exception;

/**
 * This exception is thrown when the syntax of the Thing Description is incorrect
 * @author Andrea Cimmino
 *
 */
public class SchemaValidationException extends Exception{

	private static final long serialVersionUID = 1L;

	public SchemaValidationException(String message) {
		super(message);
	}


}
