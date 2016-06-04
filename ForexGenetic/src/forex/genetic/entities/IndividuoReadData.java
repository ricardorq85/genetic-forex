/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.io.Serializable;

import forex.genetic.util.Constants.OperationType;

/**
 *
 * @author ricardorq85
 */
public class IndividuoReadData implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 201205230720L;

    /**
     *
     */
    protected String pair;

    /**
     *
     */
    protected OperationType operationType;

    /**
     *
     * @return
     */
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     *
     * @param operationType
     */
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    /**
     *
     * @return
     */
    public String getPair() {
        return pair;
    }

    /**
     *
     * @param pair
     */
    public void setPair(String pair) {
        this.pair = pair;
    }

}
