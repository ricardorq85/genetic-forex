/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.mongo;

import java.io.Serializable;
import java.util.Date;

import forex.genetic.entities.Order;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;

/**
 *
 * @author rrojasq
 */
public class MongoOrder extends Order implements Serializable, Cloneable {

	private static final long serialVersionUID = 507389146839932118L;

	private double closePriceByTakeProfit, closePriceByStopLoss;
	private Constants.CloseType tipoCierre;
	private Date fechaRegistro;
	private String idIndividuo;

	public void setCloseDate(Date closeDate) {
		if (getOpenDate() == null) {
			throw new IllegalStateException("Order.setCloseDate: Debe setear primero la fecha de apertura");
		}
		if (closeDate == null) {
			setDuracionMinutos(0);
		} else {
			setDuracionMinutos(DateUtil.calcularDuracionMinutos(getOpenDate(), closeDate));
		}
		super.setCloseDate(closeDate);
	}

	public double getClosePriceByTakeProfit() {
		return closePriceByTakeProfit;
	}

	public void setClosePriceByTakeProfit(double closePriceByTakeProfit) {
		this.closePriceByTakeProfit = closePriceByTakeProfit;
	}

	public double getClosePriceByStopLoss() {
		return closePriceByStopLoss;
	}

	public void setClosePriceByStopLoss(double closePriceByStopLoss) {
		this.closePriceByStopLoss = closePriceByStopLoss;
	}

	public Constants.CloseType getTipoCierre() {
		return tipoCierre;
	}

	public void setTipoCierre(Constants.CloseType tipoCierre) {
		this.tipoCierre = tipoCierre;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getIdIndividuo() {
		return idIndividuo;
	}

	public void setIdIndividuo(String idIndividuo) {
		this.idIndividuo = idIndividuo;
	}
	
}
