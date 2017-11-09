/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;

/**
 *
 * @author ricardorq85
 */
public class LearningParametrosIndividuo implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 201205270933L;
    private boolean takeProfit = false;
    private boolean stopLoss = false;
    private boolean lot = false;
    private boolean initialBalance = false;
    private List<Boolean> openIndicators = null;
    private List<Boolean> closeIndicators = null;
    private transient final IndicadorController indicadorController = ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.Individuo);

    /**
     *
     */
    public LearningParametrosIndividuo() {
        this.openIndicators = new ArrayList<>(indicadorController.getIndicatorNumber());
        this.closeIndicators = new ArrayList<>(indicadorController.getIndicatorNumber());
    }

    /**
     *
     * @return
     */
    public List<Boolean> getCloseIndicators() {
        return closeIndicators;
    }

    /**
     *
     * @param closeIndicators
     */
    public void setCloseIndicators(List<Boolean> closeIndicators) {
        this.closeIndicators = closeIndicators;
    }

    /**
     *
     * @return
     */
    public boolean isInitialBalance() {
        return initialBalance;
    }

    /**
     *
     * @param initialBalance
     */
    public void setInitialBalance(boolean initialBalance) {
        this.initialBalance = initialBalance;
    }

    /**
     *
     * @return
     */
    public boolean isLot() {
        return lot;
    }

    /**
     *
     * @param lot
     */
    public void setLot(boolean lot) {
        this.lot = lot;
    }

    /**
     *
     * @return
     */
    public List<Boolean> getOpenIndicators() {
        return openIndicators;
    }

    /**
     *
     * @param openIndicators
     */
    public void setOpenIndicators(List<Boolean> openIndicators) {
        this.openIndicators = openIndicators;
    }

    /**
     *
     * @return
     */
    public boolean isStopLoss() {
        return stopLoss;
    }

    /**
     *
     * @param stopLoss
     */
    public void setStopLoss(boolean stopLoss) {
        this.stopLoss = stopLoss;
    }

    /**
     *
     * @return
     */
    public boolean isTakeProfit() {
        return takeProfit;
    }

    /**
     *
     * @param takeProfit
     */
    public void setTakeProfit(boolean takeProfit) {
        this.takeProfit = takeProfit;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LearningParametrosIndividuo other = (LearningParametrosIndividuo) obj;
        if (this.takeProfit != other.takeProfit) {
            return false;
        }
        if (this.stopLoss != other.stopLoss) {
            return false;
        }
        if (this.lot != other.lot) {
            return false;
        }
        if (this.initialBalance != other.initialBalance) {
            return false;
        }
        if (this.openIndicators != other.openIndicators && (this.openIndicators == null || !this.openIndicators.equals(other.openIndicators))) {
            return false;
        }
        if (this.closeIndicators != other.closeIndicators && (this.closeIndicators == null || !this.closeIndicators.equals(other.closeIndicators))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.takeProfit ? 1 : 0);
        hash = 37 * hash + (this.stopLoss ? 1 : 0);
        hash = 37 * hash + (this.lot ? 1 : 0);
        hash = 37 * hash + (this.initialBalance ? 1 : 0);
        hash = 37 * hash + (this.openIndicators != null ? this.openIndicators.hashCode() : 0);
        hash = 37 * hash + (this.closeIndicators != null ? this.closeIndicators.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "LearningParametrosIndividuo{" + "takeProfit=" + takeProfit + ", stopLoss=" + stopLoss + ", lot=" + lot + ", initialBalance=" + initialBalance + ", openIndicators=" + openIndicators + ", closeIndicators=" + closeIndicators + '}';
    }
}
