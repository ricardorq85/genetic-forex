/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.CollectionUtil;
import forex.genetic.util.Constants;
import forex.genetic.util.Constants.OperationType;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author ricardorq85
 */
public class Poblacion implements Serializable {

    public static final long serialVersionUID = 201101251800L;
    private List<IndividuoEstrategia> individuos = new Vector<IndividuoEstrategia>();
    private OperationType operationType = null;
    private String pair = null;
    private int riskLevel = 0;
    private double dRiskLevel = Constants.MAX_RISK_LEVEL;

    public Poblacion() {
        this.operationType = PropertiesManager.getOperationType();
        this.pair = PropertiesManager.getPropertyString(Constants.PAIR);
        setRiskLevel(PropertiesManager.getPropertyDouble(Constants.RISK_LEVEL) / Constants.MAX_RISK_LEVEL);
        if (riskLevel != 0) {
            setRiskLevel(riskLevel);
        }
    }

    public double getRiskLevel() {
        return dRiskLevel;
    }

    public void setRiskLevel(double riskLevel) {
        this.dRiskLevel = riskLevel;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Poblacion getFirst() {
        return getFirst(1);
    }

    public Poblacion getByProcessedUntil(int processedUntil, int processedFrom) {
        Poblacion p = new Poblacion();
        for (int i = 0; i < this.getIndividuos().size(); i++) {
            IndividuoEstrategia individuoEstrategia = this.getIndividuos().get(i);
            corregirIndividuo(individuoEstrategia);
            /*                if (((individuoEstrategia.getProcessedUntil() == processedUntil) && (individuoEstrategia.getProcessedFrom() == processedFrom))
            || (!individuoEstrategia.getFileId().equals(PropertiesManager.getPropertyString(Constants.FILE_ID)))) {
            if ((this.getRiskLevel() != PropertiesManager.getPropertyDouble(Constants.RISK_LEVEL) / Constants.MAX_RISK_LEVEL)
            || (individuoEstrategia.getProcessedFrom() != PropertiesManager.getPropertyInt(Constants.NUMBER_BACK_ROOT_POBLACION))
            || (!individuoEstrategia.getFileId().equals(PropertiesManager.getPropertyString(Constants.FILE_ID)))
            || (!Fortaleza.currentVersion.equals(individuoEstrategia.getFortaleza().getVersion()))
            || (!PropertiesManager.getFortalezaType().equals(individuoEstrategia.getFortaleza().getType()))) {
            individuoEstrategia.setFortaleza(null);
            individuoEstrategia.setListaFortaleza(null);
            individuoEstrategia.setProcessedUntil(0);
            individuoEstrategia.setProcessedFrom(0);
            }
            p.getIndividuos().add(individuoEstrategia);
            }*/
            individuoEstrategia.setFortaleza(null);
            individuoEstrategia.setListaFortaleza(null);
            individuoEstrategia.setProcessedUntil(0);
            individuoEstrategia.setProcessedFrom(0);
            p.getIndividuos().add(individuoEstrategia);

        }
        return p;
    }

    private void corregirIndividuo(IndividuoEstrategia ind) {
        if (ind.getTakeProfit() < PropertiesManager.getPropertyInt(Constants.MIN_TP)
                || (ind.getTakeProfit() > PropertiesManager.getPropertyInt(Constants.MAX_TP))) {
            ind.setTakeProfit(PropertiesManager.getPropertyInt(Constants.MIN_TP));
        }
        if (ind.getStopLoss() < PropertiesManager.getPropertyInt(Constants.MIN_SL)
                || (ind.getStopLoss() > PropertiesManager.getPropertyInt(Constants.MAX_SL))) {
            ind.setStopLoss(PropertiesManager.getPropertyInt(Constants.MAX_SL));
        }
        if (ind.getLot() < PropertiesManager.getPropertyDouble(Constants.MIN_LOT)
                || (ind.getLot() > PropertiesManager.getPropertyDouble(Constants.MAX_LOT))) {
            ind.setLot(PropertiesManager.getPropertyDouble(Constants.MIN_LOT));
        }
    }

    public Poblacion getFirst(int cantidad) {
        Poblacion p = new Poblacion();
        p.setIndividuos(CollectionUtil.subList(this.getIndividuos(), 0, (cantidad < this.getIndividuos().size()) ? cantidad : this.getIndividuos().size()));

        return p;
    }

    public Poblacion getFirst(int cantidad, int fromIndex) {
        Poblacion p = new Poblacion();
        p.setIndividuos(CollectionUtil.subList(this.getIndividuos(), fromIndex,
                (cantidad < this.getIndividuos().size()) ? cantidad : this.getIndividuos().size()));

        return p;
    }

    public Poblacion getLast() {
        return getLast(1);
    }

    public Poblacion getLast(int cantidad) {
        Poblacion p = new Poblacion();
        //p.setIndividuos(this.getIndividuos().subList((cantidad < this.getIndividuos().size())
        //      ? (this.getIndividuos().size() - cantidad) : 0, this.getIndividuos().size()));
        p.setIndividuos(CollectionUtil.subList(this.getIndividuos(), (cantidad < this.getIndividuos().size())
                ? (this.getIndividuos().size() - cantidad) : 0, this.getIndividuos().size()));

        return p;
    }

    public void removeAll(List<IndividuoEstrategia> individuos) {
        this.individuos.removeAll(individuos);
    }

    public void addAll(Poblacion poblacion) {
        for (IndividuoEstrategia individuoEstrategia : poblacion.getIndividuos()) {
            this.add(individuoEstrategia);
        }
    }

    public void add(IndividuoEstrategia ie) {
        if (!this.individuos.contains(ie)) {
            //if (this.individuos.size() < PropertiesManager.getPropertyInt(Constants.INDIVIDUOS)) {
            this.individuos.add(ie);
            //}
        }
    }

    public void addAll(Poblacion poblacion, Poblacion compare) {
        for (IndividuoEstrategia individuoEstrategia : poblacion.getIndividuos()) {
            this.add(individuoEstrategia, compare);
        }
    }

    public void add(IndividuoEstrategia ie, Poblacion compare) {
        if ((!this.individuos.contains(ie)) && (!compare.individuos.contains(ie))) {
            this.individuos.add(ie);
        }
    }

    public List<IndividuoEstrategia> getIndividuos() {
        return this.individuos;
    }

    public void setIndividuos(List<IndividuoEstrategia> individuos) {
        this.individuos = individuos;
        //this.addAllHistoricos(individuos);
    }

    public boolean equals(Poblacion p) {
        return (this.operationType.equals(p.operationType)
                && this.pair.equals(p.pair)
                && this.individuos.equals(p.individuos));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
