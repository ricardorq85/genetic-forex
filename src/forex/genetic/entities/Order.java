/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author rrojasq
 */
public class Order implements Serializable {

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

    public boolean isCloseImmediate() {
        return closeImmediate;
    }

    public void setCloseImmediate(boolean closeImmediate) {
        this.closeImmediate = closeImmediate;
    }

    public Constants.OperationType getTipo() {
        return tipo;
    }

    public void setTipo(Constants.OperationType tipo) {
        this.tipo = tipo;
    }

    public double getLot() {
        return lot;
    }

    public void setLot(double lot) {
        this.lot = lot;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(double stopLoss) {
        this.stopLoss = stopLoss;
    }

    public double getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(double takeProfit) {
        this.takeProfit = takeProfit;
    }

    public boolean isCloseByTakeStop() {
        return closeByTakeStop;
    }

    public void setCloseByTakeStop(boolean closeByTakeStop) {
        this.closeByTakeStop = closeByTakeStop;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public int getCloseOperationIndex() {
        return closeOperationIndex;
    }

    public void setCloseOperationIndex(int closeOperationIndex) {
        this.closeOperationIndex = closeOperationIndex;
    }

    public int getCloseOperationPoblacionIndex() {
        return closeOperationPoblacionIndex;
    }

    public void setCloseOperationPoblacionIndex(int closeOperationPoblacionIndex) {
        this.closeOperationPoblacionIndex = closeOperationPoblacionIndex;
    }

    public Point getClosePoint() {
        return closePoint;
    }

    public void setClosePoint(Point closePoint) {
        this.closePoint = closePoint;
    }

    public double getCloseSpread() {
        return closeSpread;
    }

    public void setCloseSpread(double closeSpread) {
        this.closeSpread = closeSpread;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public int getOpenOperationIndex() {
        return openOperationIndex;
    }

    public void setOpenOperationIndex(int openOperationIndex) {
        this.openOperationIndex = openOperationIndex;
    }

    public int getOpenOperationPoblacionIndex() {
        return openOperationPoblacionIndex;
    }

    public void setOpenOperationPoblacionIndex(int openOperationPoblacionIndex) {
        this.openOperationPoblacionIndex = openOperationPoblacionIndex;
    }

    public double getOpenOperationValue() {
        return openOperationValue;
    }

    public void setOpenOperationValue(double openOperationValue) {
        this.openOperationValue = openOperationValue;
    }

    public Point getOpenPoint() {
        return openPoint;
    }

    public void setOpenPoint(Point openPoint) {
        this.openPoint = openPoint;
    }

    public double getOpenSpread() {
        return openSpread;
    }

    public void setOpenSpread(double openSpread) {
        this.openSpread = openSpread;
    }

    public double getPips() {
        return pips;
    }

    public void setPips(double pips) {
        this.pips = pips;
    }

    public String toString2() {
        //return "Order{" + "openOperationValue=" + openOperationValue + ", openOperationPoblacionIndex=" + openOperationPoblacionIndex + ", openOperationIndex=" + openOperationIndex + ", openPoint=" + openPoint + ", openSpread=" + openSpread + ", openDate=" + openDate + ", closeOperationPoblacionIndex=" + closeOperationPoblacionIndex + ", closeOperationIndex=" + closeOperationIndex + ", closePoint=" + closePoint + ", closeSpread=" + closeSpread + ", pips=" + pips + ", closeByTakeStop=" + closeByTakeStop + ", closeDate=" + closeDate + '}';
        return "Order{openDate=" + openDate + ";openOPerationValue=" + openOperationValue + ";pips=" + pips + "}";
    }

    @Override
    public String toString() {
        return "Order{" + "openOperationValue=" + openOperationValue
                + ", openDate=" + DateUtil.getDateString(openDate) + ", takeProfit=" + takeProfit + ", stopLoss=" + stopLoss
                + "\n, pips=" + pips + ", closeDate=" + DateUtil.getDateString(closeDate) + ", tipo=" + tipo + ", closeImmediate=" + closeImmediate + '}';
    }

    public boolean comparePattern(Order other) {
        if (other == null) {
            return false;
        }
        return ((Double.compare(this.pips, 0.0D) == Double.compare(other.pips, 0.0D)));
    }
}
