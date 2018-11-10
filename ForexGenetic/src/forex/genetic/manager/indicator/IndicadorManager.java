/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import java.util.List;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Indicator;

/**
 *
 * @author ricardorq85
 * @param <E>
 *            Tipo indicador
 */
public abstract class IndicadorManager<E> {

	private boolean priceDependence = false;
	private boolean obligatory = false;

	/**
	 *
	 */
	protected String id = null;

	/**
	 *
	 * @param priceDependence
	 */
	protected IndicadorManager(boolean priceDependence) {
		this(priceDependence, false);
	}

	/**
	 *
	 * @param priceDependence
	 * @param obligatory
	 */
	protected IndicadorManager(boolean priceDependence, boolean obligatory) {
		this.priceDependence = priceDependence;
		this.obligatory = obligatory;
	}
	
	public String[] getNombreCalculado () {
		return new String[]{"calculado"};
	}

	/**
	 *
	 * @param individuo
	 * @param indicator
	 * @param points
	 * @param i
	 * @return
	 */
	public boolean operate(E individuo, E indicator, List<Point> points, int i) {
		return true;
	}

	/**
	 *
	 * @return
	 */
	public boolean isPriceDependence() {
		return priceDependence;
	}

	/**
	 *
	 * @param priceDependence
	 */
	public void setPriceDependence(boolean priceDependence) {
		this.priceDependence = priceDependence;
	}

	/**
	 *
	 * @return
	 */
	public boolean isObligatory() {
		return obligatory;
	}

	/**
	 *
	 * @param obligatory
	 */
	public void setObligatory(boolean obligatory) {
		this.obligatory = obligatory;
	}

	/**
	 *
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 *
	 * @param indicator
	 * @param point
	 * @return
	 */
	public abstract Indicator generate(E indicator, Point point);

	/**
	 *
	 * @param individuo
	 * @param indicator
	 * @param point
	 * @return
	 */
	public boolean operate(E individuo, E indicator, Point point) {
		return true;
	}

	/**
	 *
	 * @param individuo
	 * @param indicator
	 * @param currentPoint
	 * @param previousPoint
	 * @return
	 */
	public boolean operate(E individuo, E indicator, Point currentPoint, Point previousPoint) {
		return true;
	}

	/**
	 *
	 * @param individuo
	 * @param indicator
	 * @param point
	 * @return
	 */
	public abstract Interval calculateInterval(E individuo, E indicator, Point point);

	/**
	 *
	 * @param indicator1
	 * @param indicator2
	 * @return
	 */
	public abstract Indicator crossover(E indicator1, E indicator2);

	/**
	 *
	 * @param indicator
	 * @return
	 */
	public abstract Indicator mutate(E indicator);

	/**
	 *
	 * @param individuo
	 * @param optimizedIndividuo
	 * @param indicator
	 * @param prevPoint
	 * @param point
	 * @param pips
	 * @return
	 */
	public abstract Indicator optimize(E individuo, E optimizedIndividuo, E indicator, Point prevPoint, Point point,
			double pips);

	/**
	 *
	 * @param indicator
	 */
	public abstract void round(E indicator);

	/**
	 *
	 * @param indicator
	 * @param prevPoint
	 * @param point
	 * @return
	 */
	public abstract double getValue(E indicator, Point prevPoint, Point point);
}
