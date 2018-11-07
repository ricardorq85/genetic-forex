/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

import java.util.HashMap;
import java.util.Map;

import forex.genetic.entities.Point;

/**
 *
 * @author ricardorq85
 */
public class Rsi extends IntervalIndicator {

    /**
     *
     */
    public static final long serialVersionUID = 201102142113L;
    private double rsi = 0.0;
    private double parameter1 = 0.0;

    /**
     *
     * @param name
     */
    public Rsi(String name) {
        super(name);
    }

    /**
     *
     * @return
     */
    public double getParameter1() {
        return parameter1;
    }

    /**
     *
     * @param parameter1
     */
    public void setParameter1(double parameter1) {
        this.parameter1 = parameter1;
    }

    /**
     *
     * @return
     */
    public double getRsi() {
        return rsi;
    }

    /**
     *
     * @param rsi
     */
    public void setRsi(double rsi) {
        this.rsi = rsi;
    }

	@Override
	public Map<String, Object> valuesToMap(Point datoHistorico) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		if (!Double.isInfinite(this.rsi) && !Double.isNaN(this.rsi)) {
			objectMap.put("rsi", this.rsi);
			objectMap.put("calculado", this.rsi);
		}

		return objectMap;		
	}


}
