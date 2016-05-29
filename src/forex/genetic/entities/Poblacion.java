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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author ricardorq85
 */
public class Poblacion implements Serializable, Cloneable {

    public static final long serialVersionUID = 201101251800L;
    private List<IndividuoEstrategia> individuos = new Vector<IndividuoEstrategia>();
    private OperationType operationType = null;
    private String pair = null;
    private int riskLevel = 0;
    private double dRiskLevel = Constants.MAX_RISK_LEVEL;

    public Poblacion() {
        this.operationType = PropertiesManager.getOperationType();
        this.pair = PropertiesManager.getPair();
        setRiskLevel(PropertiesManager.getRiskLevel() / dRiskLevel);
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

    public Poblacion getByProcessedUntil(int processedUntil, int processedFrom, IndividuoReadData individuoReadData) {
        Poblacion p = new Poblacion();
        for (int i = 0; i < this.getIndividuos().size(); i++) {
            IndividuoEstrategia individuoEstrategia = this.getIndividuos().get(i);
            individuoEstrategia.corregir(individuoReadData);
            p.getIndividuos().add(individuoEstrategia);
        }
        return p;
    }

    public Poblacion getFirst(int cantidad) {
        return this.getFirst(cantidad, 0);
    }

    public Poblacion getFirst(int cantidad, int fromIndex) {
        Poblacion p = new Poblacion();
        p.setIndividuos(CollectionUtil.subList(this.getIndividuos(), fromIndex,
                ((fromIndex + cantidad) < this.getIndividuos().size()) ? (fromIndex + cantidad) : this.getIndividuos().size()));

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
        Set set = new HashSet<IndividuoEstrategia>(this.individuos);
        set.addAll(poblacion.getIndividuos());
        /*        for (IndividuoEstrategia individuoEstrategia : poblacion.getIndividuos()) {
        this.add(individuoEstrategia);
        }*/
        this.individuos.clear();
        this.individuos.addAll(set);
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

    @Override
    public String toString() {
        return "Poblacion{" + "individuos=" + individuos.size() + ", operationType=" + operationType + ", pair=" + pair + '}';
    }

    @Override
    public Poblacion clone() {
        Poblacion cloned = new Poblacion();
        cloned.individuos = new Vector<IndividuoEstrategia>(this.individuos.size());
        for (Iterator<IndividuoEstrategia> it = this.individuos.iterator(); it.hasNext();) {
            IndividuoEstrategia individuoEstrategia = it.next();
            cloned.add(individuoEstrategia.clone());
        }
        cloned.operationType = this.operationType;
        cloned.pair = this.pair;
        cloned.riskLevel = this.riskLevel;
        cloned.dRiskLevel = this.dRiskLevel;
        return cloned;
    }
}
