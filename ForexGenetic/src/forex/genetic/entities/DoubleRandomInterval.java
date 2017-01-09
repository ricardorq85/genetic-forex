/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.ArrayList;
import java.util.List;

import forex.genetic.util.RandomUtil;

/**
 *
 * @author ricardorq85
 */
public class DoubleRandomInterval extends DoubleInterval implements RandomInterval<Double> {

	public static final long serialVersionUID = 201101451810L;

	public DoubleRandomInterval(Double i1, Double i2, Double i3) {
		if (i3 != null) {
			List<Double> l = new ArrayList<>();
			l.add(i1);
			l.add(i2);
			l.add(i3);
			int index1 = RandomUtil.nextInt(l.size());
			int index2 = RandomUtil.nextInt(l.size());
			int index3 = RandomUtil.nextInt(l.size());
			double value1 = Math.min(l.get(index1), l.get(index2));
			double value2 = l.get(index3);
			this.lowInterval = Math.min(value1, value2);
			this.highInterval = Math.max(value1, value2);
		} else {
			this.lowInterval = i1;
			this.highInterval = i2;
		}
	}

	public Double generateRandom() {
		Double g = RandomUtil.generateNegativePositive(this.lowInterval, this.highInterval);
		return g;
	}

}
