/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

/**
 *
 * @author ricardorq85
 */
public class Momentum extends IntervalIndicator {

    /**
     *
     */
    public static final long serialVersionUID = 201102142113L;
    private double momentum = 0.0;
    private double period = 0.0;

    /**
     *
     * @param name
     */
    public Momentum(String name) {
        super(name);
    }

    /**
     *
     * @return
     */
    public double getMomentum() {
        return momentum;
    }

    /**
     *
     * @param momentum
     */
    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    /**
     *
     * @return
     */
    public double getPeriod() {
        return period;
    }

    /**
     *
     * @param period
     */
    public void setPeriod(double period) {
        this.period = period;
    }

}
