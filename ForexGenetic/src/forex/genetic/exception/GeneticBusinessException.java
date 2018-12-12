package forex.genetic.exception;

public class GeneticBusinessException extends GeneticException {

	private static final long serialVersionUID = 1L;

	public GeneticBusinessException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public GeneticBusinessException(Throwable cause) {
		super(null, cause);
	}

}
