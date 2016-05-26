/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import forex.genetic.manager.IndividuoManager;
import java.util.List;
import static forex.genetic.util.Constants.*;

/**
 *
 * @author ricardorq85
 */
public class IndividuoEstrategia implements Comparable<IndividuoEstrategia> {

    private int id = 0;
    private int generacion = -1;
    private IndividuoEstrategia parent1 = null;
    private IndividuoEstrategia parent2 = null;
    private IndividuoType individuoType = IndividuoType.INITIAL;
    private int takeProfit = 0;
    private int stopLoss = 0;
    private double lot = 0;
    private int initialBalance = 0;
    private List<? extends Indicator> openIndicators = null;
    private List<? extends Indicator> closeIndicators = null;
    private Fortaleza fortaleza = null;

    public IndividuoEstrategia() {
        this(0, null, null, IndividuoType.INITIAL);
    }

    public IndividuoEstrategia(IndividuoEstrategia parent1) {
        this(0, parent1, null, IndividuoType.INITIAL);
    }

    public IndividuoEstrategia(int generacion, IndividuoEstrategia parent1, IndividuoEstrategia parent2, IndividuoType individuoType) {
        setId(IndividuoManager.nextId());
        setGeneracion(generacion);
        setParent1(parent1);
        setParent2(parent2);
        setIndividuoType(individuoType);
    }

    public IndividuoType getIndividuoType() {
        return individuoType;
    }

    public void setIndividuoType(IndividuoType individuoType) {
        this.individuoType = individuoType;
    }

    public IndividuoEstrategia getParent1() {
        return parent1;
    }

    public void setParent1(IndividuoEstrategia parent1) {
        this.parent1 = parent1;
    }

    public IndividuoEstrategia getParent2() {
        return parent2;
    }

    public void setParent2(IndividuoEstrategia parent2) {
        this.parent2 = parent2;
    }

    public Fortaleza getFortaleza() {
        return fortaleza;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFortaleza(Fortaleza fortaleza) {
        this.fortaleza = fortaleza;
    }

    public List<? extends Indicator> getOpenIndicators() {
        return openIndicators;
    }

    public void setOpenIndicators(List<? extends Indicator> openIndicators) {
        this.openIndicators = openIndicators;
    }

    public List<? extends Indicator> getCloseIndicators() {
        return closeIndicators;
    }

    public void setCloseIndicators(List<? extends Indicator> closeIndicators) {
        this.closeIndicators = closeIndicators;
    }

    public int getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(int initialBalance) {
        this.initialBalance = initialBalance;
    }

    public int getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(int stopLoss) {
        this.stopLoss = stopLoss;
    }

    public int getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(int takeProfit) {
        this.takeProfit = takeProfit;
    }

    public double getLot() {
        return lot;
    }

    public void setLot(double lot) {
        this.lot = lot;
    }

    public int getGeneracion() {
        return generacion;
    }

    public void setGeneracion(int generacion) {
        this.generacion = generacion;
    }

    public int compareTo(IndividuoEstrategia o) {
        return (this.fortaleza.compareTo(o.getFortaleza()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IndividuoEstrategia) {
            IndividuoEstrategia objIndividuo = (IndividuoEstrategia) obj;
            return (this.openIndicators.equals(objIndividuo.openIndicators)
                    && this.closeIndicators.equals(objIndividuo.closeIndicators)
                    && (this.takeProfit - objIndividuo.takeProfit) / this.takeProfit < 0.03
                    && (this.stopLoss - objIndividuo.stopLoss) / this.stopLoss < 0.03);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(" Id=" + (this.id));
        buffer.append(" Generacion=" + (this.generacion));
        buffer.append("; IndividuoType=" + (this.individuoType) + ";");
        buffer.append("\n\t");
        buffer.append(((this.fortaleza == null) ? 0.0 : this.fortaleza.toString()));
        buffer.append("\n\t");
        if (parent1 != null) {
            buffer.append("; Padre 1=" + parent1.getId());
        }
        if (parent2 != null) {
            buffer.append("; Padre 2=" + parent2.getId());
        }
        buffer.append("; TakeProfit=" + this.takeProfit);
        buffer.append("; Stoploss=" + this.stopLoss);
        buffer.append("; Lot=" + this.lot);
        buffer.append("; Initial Balance=" + this.initialBalance);
        buffer.append("\n\t");
        buffer.append("; Open Indicadores=" + (this.openIndicators));
        buffer.append("\n\t");
        buffer.append("; Close Indicadores=" + (this.closeIndicators));

        return buffer.toString();
    }
}
