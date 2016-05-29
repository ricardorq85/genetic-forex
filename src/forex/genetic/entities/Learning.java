/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants.OperationType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class Learning implements Serializable {

    public static final long serialVersionUID = 201205200756L;
    private String pair = null;
    private OperationType operationType = null;
    private List<RelacionPair> relacionMonedas = new ArrayList<RelacionPair>();
    private List<RelacionGeneraciones> relacionMutation = new ArrayList<RelacionGeneraciones>();
    private List<RelacionGeneraciones> relacionOptimization = new ArrayList<RelacionGeneraciones>();
    private IntegerInterval takeProfitInterval = null;
    private IntegerInterval stopLossInterval = null;

    public Learning() {
        pair = PropertiesManager.getPair();
        operationType = PropertiesManager.getOperationType();
        this.createTakeProfitInterval();
        this.createStopLossInterval();
    }

    public IntegerInterval createTakeProfitInterval() {
        this.takeProfitInterval = new IntegerInterval("takeProfitInterval");
        this.takeProfitInterval.setLowInterval(Integer.MAX_VALUE);
        this.takeProfitInterval.setHighInterval(Integer.MIN_VALUE);
        return this.takeProfitInterval;
    }

    public IntegerInterval createStopLossInterval() {
        this.stopLossInterval = new IntegerInterval("stopLossInterval");
        this.stopLossInterval.setLowInterval(Integer.MAX_VALUE);
        this.stopLossInterval.setHighInterval(Integer.MIN_VALUE);
        return this.stopLossInterval;
    }

    public IntegerInterval getStopLossInterval() {
        return stopLossInterval;
    }

    public void setStopLossInterval(IntegerInterval stopLossInterval) {
        this.stopLossInterval = stopLossInterval;
    }

    public IntegerInterval getTakeProfitInterval() {
        return takeProfitInterval;
    }

    public void setTakeProfitInterval(IntegerInterval takeProfitInterval) {
        this.takeProfitInterval = takeProfitInterval;
    }

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

    public List<RelacionPair> getRelacionMonedas() {
        return relacionMonedas;
    }

    public void setRelacionMonedas(List<RelacionPair> relacionMonedas) {
        this.relacionMonedas = relacionMonedas;
    }

    public List<RelacionGeneraciones> getRelacionMutation() {
        return relacionMutation;
    }

    public void setRelacionMutation(List<RelacionGeneraciones> relacionMutation) {
        this.relacionMutation = relacionMutation;
    }

    public List<RelacionGeneraciones> getRelacionOptimization() {
        return relacionOptimization;
    }

    public void setRelacionOptimization(List<RelacionGeneraciones> relacionOptimization) {
        this.relacionOptimization = relacionOptimization;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Learning other = (Learning) obj;
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
        int hash = 5;
        hash = 61 * hash + (this.pair != null ? this.pair.hashCode() : 0);
        hash = 61 * hash + (this.operationType != null ? this.operationType.hashCode() : 0);
        return hash;
    }
  
    @Override
    public String toString() {
        return "Learning{" + "pair=" + pair + ", operationType=" + operationType + ","
                + "\n relacionMonedas=" + relacionMonedas + ","
                + "\n relacionMutation=" + relacionMutation + ","
                + "\n relacionOptimization=" + relacionOptimization + ","
                + "\n takeProfitInterval=" + takeProfitInterval + ","
                + "\n stopLossInterval=" + stopLossInterval + '}';
    }
}
