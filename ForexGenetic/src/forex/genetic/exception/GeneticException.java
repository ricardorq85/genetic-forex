/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.exception;

/**
 *
 * @author ricardorq85
 */
public class GeneticException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     *
     * @param message
     */
    public GeneticException(String message) {
        super(message);
    }
    
    public GeneticException(String message, Throwable cause) {
        super(message, cause);
    }    
}
