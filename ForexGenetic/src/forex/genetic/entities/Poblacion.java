/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;

import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.CollectionUtil;
import forex.genetic.util.Constants;
import forex.genetic.util.Constants.OperationType;

/**
 *
 * @author ricardorq85
 */
public class Poblacion implements Serializable, Cloneable {

    public static final long serialVersionUID = 201101251800L;
    private List<IndividuoEstrategia> individuos = null;
    private OperationType operationType = null;
    private String pair = null;
    private int riskLevel = 0;
    private double dRiskLevel = Constants.MAX_RISK_LEVEL;
    private Tendencia tendencia = new Tendencia();

    /**
     *
     */
    public Poblacion() {
        this.individuos = new Vector<IndividuoEstrategia>();
        this.operationType = PropertiesManager.getOperationType();
        this.pair = PropertiesManager.getPair();
        setRiskLevel(PropertiesManager.getRiskLevel() / dRiskLevel);
        if (riskLevel != 0) {
            setRiskLevel(riskLevel);
        }
    }

    /**
     *
     * @param tendencia
     */
    public void setTendencia(Tendencia tendencia) {
        this.tendencia = tendencia;
    }

    /**
     *
     * @return
     */
    public Tendencia getTendencia() {
        return tendencia;
    }

    /**
     *
     * @return
     */
    public double getRiskLevel() {
        return dRiskLevel;
    }

    /**
     *
     * @param riskLevel
     */
    public void setRiskLevel(double riskLevel) {
        this.dRiskLevel = riskLevel;
    }

    /**
     *
     * @return
     */
    public String getPair() {
        return pair;
    }

    /**
     *
     * @param pair
     */
    public void setPair(String pair) {
        this.pair = pair;
    }

    /**
     *
     * @return
     */
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     *
     * @param operationType
     */
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    /**
     *
     * @return
     */
    public Poblacion getFirst() {
        return getFirst(1);
    }

    /**
     *
     * @param processedUntil
     * @param processedFrom
     * @param individuoReadData
     * @return
     */
    public Poblacion getByProcessedUntil(int processedUntil, int processedFrom, IndividuoReadData individuoReadData) {
        Poblacion p = new Poblacion();
        for (int i = 0; i < this.getIndividuos().size(); i++) {
            IndividuoEstrategia individuoEstrategia = this.getIndividuos().get(i);
            individuoEstrategia.corregir(individuoReadData);
            p.getIndividuos().add(individuoEstrategia);
        }
        return p;
    }

    /**
     *
     * @param cantidad
     * @return
     */
    public Poblacion getFirst(int cantidad) {
        return this.getFirst(cantidad, 0);
    }

    /**
     *
     * @param cantidad
     * @param fromIndex
     * @return
     */
    public Poblacion getFirst(int cantidad, int fromIndex) {
        Poblacion p = new Poblacion();
        p.setIndividuos(CollectionUtil.subList(this.getIndividuos(), fromIndex,
                ((fromIndex + cantidad) < this.getIndividuos().size()) ? (fromIndex + cantidad) : this.getIndividuos().size()));

        return p;
    }

    /**
     *
     * @return
     */
    public Poblacion getLast() {
        return getLast(1);
    }

    /**
     *
     * @param cantidad
     * @return
     */
    public Poblacion getLast(int cantidad) {
        Poblacion p = new Poblacion();
        //p.setIndividuos(this.getIndividuos().subList((cantidad < this.getIndividuos().size())
        //      ? (this.getIndividuos().size() - cantidad) : 0, this.getIndividuos().size()));
        p.setIndividuos(CollectionUtil.subList(this.getIndividuos(), (cantidad < this.getIndividuos().size())
                ? (this.getIndividuos().size() - cantidad) : 0, this.getIndividuos().size()));

        return p;
    }

    /**
     *
     * @param individuos
     */
    public void removeAll(List<IndividuoEstrategia> individuos) {
        this.individuos.removeAll(individuos);
    }

    /**
     *
     * @param poblacion
     */
    public void addAll(Poblacion poblacion) {
        Set set = new HashSet<IndividuoEstrategia>(this.individuos);
        set.addAll(poblacion.getIndividuos());
        /*        for (IndividuoEstrategia individuoEstrategia : poblacion.getIndividuos()) {
         this.add(individuoEstrategia);
         }*/
        this.individuos.clear();
        this.individuos.addAll(set);
    }

    /**
     *
     * @param ie
     */
    public void add(IndividuoEstrategia ie) {
        if (!this.individuos.contains(ie)) {
            //if (this.individuos.size() < PropertiesManager.getPropertyInt(Constants.INDIVIDUOS)) {
            this.individuos.add(ie);
            //}
        }
    }

    /**
     *
     * @param poblacion
     * @param compare
     */
    public void addAll(Poblacion poblacion, Poblacion compare) {
        for (IndividuoEstrategia individuoEstrategia : poblacion.getIndividuos()) {
            this.add(individuoEstrategia, compare);
        }
    }

    /**
     *
     * @param ie
     * @param compare
     */
    public void add(IndividuoEstrategia ie, Poblacion compare) {
        if ((!this.individuos.contains(ie)) && (!compare.individuos.contains(ie))) {
            this.individuos.add(ie);
        }
    }

    /**
     *
     * @return
     */
    public List<IndividuoEstrategia> getIndividuos() {
        return this.individuos;
    }

    /**
     *
     * @param individuos
     */
    public void setIndividuos(List<IndividuoEstrategia> individuos) {
        this.individuos = individuos;
        //this.addAllHistoricos(individuos);
    }

    /**
     *
     * @param p
     * @return
     */
    public boolean equals2(Poblacion p) {
        return (this.operationType.equals(p.operationType)
                && this.pair.equals(p.pair)
                && this.individuos.equals(p.individuos));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Poblacion other = (Poblacion) obj;
        if (this.operationType != other.operationType) {
            return false;
        }
        if (!Objects.equals(this.pair, other.pair)) {
            return false;
        }
        if (!Objects.equals(this.individuos, other.individuos)) {
            return false;
        }
        return true;
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
        Poblacion cloned;
        try {
            cloned = (Poblacion) super.clone();
        } catch (CloneNotSupportedException ex) {
            cloned = new Poblacion();
        }
        cloned.individuos = new Vector<>(this.individuos.size());
        for (IndividuoEstrategia individuoEstrategia : this.individuos) {
            cloned.add(individuoEstrategia.clone());
        }
        cloned.operationType = this.operationType;
        cloned.pair = this.pair;
        cloned.riskLevel = this.riskLevel;
        cloned.dRiskLevel = this.dRiskLevel;
        return cloned;
    }
}
