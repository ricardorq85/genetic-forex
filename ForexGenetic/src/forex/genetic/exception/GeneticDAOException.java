package forex.genetic.exception;

public class GeneticDAOException extends GeneticException {

	private static final long serialVersionUID = 1L;

	public GeneticDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeneticDAOException(Throwable cause) {
		super(null, cause);

	}
}
