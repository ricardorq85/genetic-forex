/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ricardorq85
 */
public class PeriodoAgrupador extends IntervalIndicator {

    /**
     *
     */
    public static final long serialVersionUID = 201101251823L;
    private int periodo;

    /**
     *
     * @param name
     */
    public PeriodoAgrupador(String name) {
        super(name);
    }

    /**
     *
     * @return
     */
    public int getPeriodo() {
        return periodo;
    }

    /**
     *
     * @param periodo
     */
    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.interval.getName()).append("Lower=").append(this.interval.getLowInterval()).append(",");
        buffer.append(this.interval.getName()).append("Higher=").append(this.interval.getHighInterval());

        return buffer.toString();
    }

	@Override
	public Map<String, Object> valuesToMap() {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		return objectMap;
	}
    
}
