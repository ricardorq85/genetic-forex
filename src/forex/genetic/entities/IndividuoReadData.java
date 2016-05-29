/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import forex.genetic.util.Constants.OperationType;
import java.io.Serializable;

/**
 *
 * @author ricardorq85
 */
public class IndividuoReadData implements Serializable {

    public static final long serialVersionUID = 201205230720L;
    protected String pair;
    protected OperationType operationType;

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

}
