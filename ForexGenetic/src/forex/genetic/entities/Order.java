/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.io.Serializable;
import java.util.Date;

import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;

/**
 *
 * @author rrojasq
 */
public class Order implements Serializable, Cloneable {

	public static final long serialVersionUID = 201207182112L;
	private double openOperationValue = 0.0D;
	private int openOperationPoblacionIndex = 0;
	private int openOperationIndex = 0;
	private Point openPoint = null;
	private double openSpread = 0.0D;
	private Date openDate = null;
	private double takeProfit = 0.0D;
	private double stopLoss = 0.0D;
	private double lot = 0.0D;
	private int closeOperationPoblacionIndex = 0;
	private int closeOperationIndex = 0;
	private Point closePoint = null;
	private double closeSpread = 0.0D;
	private double pips = 0.0D;
	private double profit = 0.0D;
	private boolean closeByTakeStop = false;
	private Date closeDate = null;
	private Constants.OperationType tipo = Constants.OperationType.SELL;
	private boolean closeImmediate = false;
	private double maxPipsRetroceso = 0.0D;
	private double maxValueRetroceso = 0.0D;
	private Date maxFechaRetroceso = null;
	DoubleInterval maxMinHistoriaApertura = null;
	private long duracionMinutos;

	/**
	 *
	 * @return
	 */
	public DoubleInterval getMaxMinHistoriaApertura() {
		return maxMinHistoriaApertura;
	}

	/**
	 *
	 * @param maxMinHistoriaApertura
	 */
	public void setMaxMinHistoriaApertura(DoubleInterval maxMinHistoriaApertura) {
		this.maxMinHistoriaApertura = maxMinHistoriaApertura;
	}

	/**
	 *
	 * @return
	 */
	public double getMaxPipsRetroceso() {
		return maxPipsRetroceso;
	}

	/**
	 *
	 * @param maxPipsRetroceso
	 */
	public void setMaxPipsRetroceso(double maxPipsRetroceso) {
		this.maxPipsRetroceso = maxPipsRetroceso;
	}

	/**
	 *
	 * @return
	 */
	public double getMaxValueRetroceso() {
		return maxValueRetroceso;
	}

	/**
	 *
	 * @param maxValueRetroceso
	 */
	public void setMaxValueRetroceso(double maxValueRetroceso) {
		this.maxValueRetroceso = maxValueRetroceso;
	}

	/**
	 *
	 * @return
	 */
	public Date getMaxFechaRetroceso() {
		return (this.maxFechaRetroceso = maxFechaRetroceso != null ? new Date(maxFechaRetroceso.getTime()) : null);
	}

	/**
	 *
	 * @param maxFechaRetroceso
	 */
	public void setMaxFechaRetroceso(Date maxFechaRetroceso) {
		this.maxFechaRetroceso = maxFechaRetroceso != null ? new Date(maxFechaRetroceso.getTime()) : null;
	}

	/**
	 *
	 * @return
	 */
	public boolean isCloseImmediate() {
		return closeImmediate;
	}

	/**
	 *
	 * @param closeImmediate
	 */
	public void setCloseImmediate(boolean closeImmediate) {
		this.closeImmediate = closeImmediate;
	}

	/**
	 *
	 * @return
	 */
	public Constants.OperationType getTipo() {
		return tipo;
	}

	/**
	 *
	 * @param tipo
	 */
	public void setTipo(Constants.OperationType tipo) {
		this.tipo = tipo;
	}

	/**
	 *
	 * @return
	 */
	public double getLot() {
		return lot;
	}

	/**
	 *
	 * @param lot
	 */
	public void setLot(double lot) {
		this.lot = lot;
	}

	/**
	 *
	 * @return
	 */
	public double getProfit() {
		return profit;
	}

	/**
	 *
	 * @param profit
	 */
	public void setProfit(double profit) {
		this.profit = profit;
	}

	/**
	 *
	 * @return
	 */
	public double getStopLoss() {
		return stopLoss;
	}

	/**
	 *
	 * @param stopLoss
	 */
	public void setStopLoss(double stopLoss) {
		this.stopLoss = stopLoss;
	}

	/**
	 *
	 * @return
	 */
	public double getTakeProfit() {
		return takeProfit;
	}

	/**
	 *
	 * @param takeProfit
	 */
	public void setTakeProfit(double takeProfit) {
		this.takeProfit = takeProfit;
	}

	/**
	 *
	 * @return
	 */
	public boolean isCloseByTakeStop() {
		return closeByTakeStop;
	}

	/**
	 *
	 * @param closeByTakeStop
	 */
	public void setCloseByTakeStop(boolean closeByTakeStop) {
		this.closeByTakeStop = closeByTakeStop;
	}

	/**
	 *
	 * @return
	 */
	public Date getCloseDate() {
		return ((this.closeDate != null) ? new Date(closeDate.getTime()) : null);
	}

	/**
	 *
	 * @param closeDate
	 */
	public void setCloseDate(Date closeDate) {
		this.closeDate = (closeDate != null) ? new Date(closeDate.getTime()) : null;
	}

	/**
	 *
	 * @return
	 */
	public int getCloseOperationIndex() {
		return closeOperationIndex;
	}

	/**
	 *
	 * @param closeOperationIndex
	 */
	public void setCloseOperationIndex(int closeOperationIndex) {
		this.closeOperationIndex = closeOperationIndex;
	}

	/**
	 *
	 * @return
	 */
	public int getCloseOperationPoblacionIndex() {
		return closeOperationPoblacionIndex;
	}

	/**
	 *
	 * @param closeOperationPoblacionIndex
	 */
	public void setCloseOperationPoblacionIndex(int closeOperationPoblacionIndex) {
		this.closeOperationPoblacionIndex = closeOperationPoblacionIndex;
	}

	/**
	 *
	 * @return
	 */
	public Point getClosePoint() {
		return closePoint;
	}

	/**
	 *
	 * @param closePoint
	 */
	public void setClosePoint(Point closePoint) {
		this.closePoint = closePoint;
	}

	/**
	 *
	 * @return
	 */
	public double getCloseSpread() {
		return closeSpread;
	}

	/**
	 *
	 * @param closeSpread
	 */
	public void setCloseSpread(double closeSpread) {
		this.closeSpread = closeSpread;
	}

	/**
	 *
	 * @return
	 */
	public Date getOpenDate() {
		return ((this.openDate != null) ? new Date(openDate.getTime()) : null);
	}

	/**
	 *
	 * @param openDate
	 */
	public void setOpenDate(Date openDate) {
		this.openDate = (openDate != null) ? new Date(openDate.getTime()) : null;
	}

	/**
	 *
	 * @return
	 */
	public int getOpenOperationIndex() {
		return openOperationIndex;
	}

	/**
	 *
	 * @param openOperationIndex
	 */
	public void setOpenOperationIndex(int openOperationIndex) {
		this.openOperationIndex = openOperationIndex;
	}

	/**
	 *
	 * @return
	 */
	public int getOpenOperationPoblacionIndex() {
		return openOperationPoblacionIndex;
	}

	/**
	 *
	 * @param openOperationPoblacionIndex
	 */
	public void setOpenOperationPoblacionIndex(int openOperationPoblacionIndex) {
		this.openOperationPoblacionIndex = openOperationPoblacionIndex;
	}

	/**
	 *
	 * @return
	 */
	public double getOpenOperationValue() {
		return openOperationValue;
	}

	/**
	 *
	 * @param openOperationValue
	 */
	public void setOpenOperationValue(double openOperationValue) {
		this.openOperationValue = openOperationValue;
	}

	/**
	 *
	 * @return
	 */
	public Point getOpenPoint() {
		return openPoint;
	}

	/**
	 *
	 * @param openPoint
	 */
	public void setOpenPoint(Point openPoint) {
		this.openPoint = openPoint;
	}

	/**
	 *
	 * @return
	 */
	public double getOpenSpread() {
		return openSpread;
	}

	/**
	 *
	 * @param openSpread
	 */
	public void setOpenSpread(double openSpread) {
		this.openSpread = openSpread;
	}

	/**
	 *
	 * @return
	 */
	public double getPips() {
		return pips;
	}

	/**
	 *
	 * @param pips
	 */
	public void setPips(double pips) {
		this.pips = pips;
	}

	public long getDuracionMinutos() {
		return duracionMinutos;
	}

	public void setDuracionMinutos(long duracionMinutos) {
		this.duracionMinutos = duracionMinutos;
	}

	/**
	 *
	 * @return
	 */
	public String toString2() {
		// return "Order{" + "openOperationValue=" + openOperationValue + ",
		// openOperationPoblacionIndex=" + openOperationPoblacionIndex + ",
		// openOperationIndex=" + openOperationIndex + ", openPoint=" +
		// openPoint + ", openSpread=" + openSpread + ", openDate=" + openDate +
		// ", closeOperationPoblacionIndex=" + closeOperationPoblacionIndex + ",
		// closeOperationIndex=" + closeOperationIndex + ", closePoint=" +
		// closePoint + ", closeSpread=" + closeSpread + ", pips=" + pips + ",
		// closeByTakeStop=" + closeByTakeStop + ", closeDate=" + closeDate +
		// '}';
		return "Order{openDate=" + openDate + ";openOPerationValue=" + openOperationValue + ";pips=" + pips + "}";
	}

	@Override
	public String toString() {
		return "Order{" + "openOperationValue=" + openOperationValue + ", openDate=" + DateUtil.getDateString(openDate)
				+ ", takeProfit=" + takeProfit + ", stopLoss=" + stopLoss + "\n, pips=" + pips + ", closeDate="
				+ DateUtil.getDateString(closeDate) + ", tipo=" + tipo + ", closeImmediate=" + closeImmediate
				+ ", spread=" + openSpread + '}';
	}

	/**
	 *
	 * @param other
	 * @return
	 */
	public boolean comparePattern(Order other) {
		if (other == null) {
			return false;
		}
		return ((Double.compare(this.pips, 0.0D) == Double.compare(other.pips, 0.0D)));
	}

	@Override
	public Order clone() throws CloneNotSupportedException {
		Order clonned = new Order();
		clonned.openOperationValue = this.openOperationValue;
		clonned.openOperationPoblacionIndex = this.openOperationPoblacionIndex;
		clonned.openOperationIndex = this.openOperationIndex;
		clonned.openPoint = this.openPoint;
		clonned.openSpread = this.openSpread;
		clonned.openDate = this.openDate;
		clonned.takeProfit = this.takeProfit;
		clonned.stopLoss = this.stopLoss;
		clonned.lot = this.lot;
		clonned.closeOperationPoblacionIndex = this.closeOperationPoblacionIndex;
		clonned.closeOperationIndex = this.closeOperationIndex;
		clonned.closePoint = this.closePoint;
		clonned.closeSpread = this.closeSpread;
		clonned.pips = this.pips;
		clonned.profit = this.profit;
		clonned.closeByTakeStop = this.closeByTakeStop;
		clonned.closeDate = this.closeDate;
		clonned.tipo = this.tipo;
		clonned.closeImmediate = this.closeImmediate;
		clonned.maxPipsRetroceso = this.maxPipsRetroceso;
		clonned.maxValueRetroceso = this.maxValueRetroceso;
		clonned.maxFechaRetroceso = this.maxFechaRetroceso;
		clonned.maxMinHistoriaApertura = this.maxMinHistoriaApertura;
		clonned.duracionMinutos = this.duracionMinutos;
		return clonned;
	}

}
