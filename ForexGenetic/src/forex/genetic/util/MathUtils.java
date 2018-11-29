package forex.genetic.util;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class MathUtils {

	private MathUtils() {
	}

	public static double calcularStandardDeviation(List<Double> data) {
		StandardDeviation standardDeviation = new StandardDeviation();
		standardDeviation.setData(ArrayUtils.toPrimitive(data.toArray(new Double[0])));
		double value = standardDeviation.evaluate();
		return value;
	}

	public static double calcularModa(List<Double> data) {
		double[] results = StatUtils.mode(ArrayUtils.toPrimitive(data.toArray(new Double[0])));
		double value = results[results.length - 1];
		return value;
	}

}
