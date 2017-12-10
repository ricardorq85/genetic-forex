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
public class RelacionPair implements Comparable<RelacionPair>, Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 201205232209L;
    
    private String pair = null;
    private OperationType operationType = null;
    private float value = 0.0F;

    /**
     *
     * @param pair
     * @param operationType
     */
    public RelacionPair(String pair, OperationType operationType) {
        this.pair = pair;
        this.operationType = operationType;
    }

    /**
     *
     */
    public RelacionPair() {
    }

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

    /**
     *
     * @return
     */
    public float getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RelacionPair other = (RelacionPair) obj;
        if ((this.pair == null) ? (other.pair != null) : !this.pair.equals(other.pair)) {
            return false;
        }
        if (this.operationType != other.operationType) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.pair != null ? this.pair.hashCode() : 0);
        hash = 31 * hash + (this.operationType != null ? this.operationType.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "RelacionPair{ operationType=" + operationType + ", pair=" + pair + ", value=" + value + '}';
    }

    @Override
    public int compareTo(RelacionPair o) {
        return Float.compare(this.getValue(), o.getValue());
    }
}
