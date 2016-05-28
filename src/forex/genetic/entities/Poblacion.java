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

    public Poblacion() {
        this.operationType = PropertiesManager.getOperationType();
        this.pair = PropertiesManager.getPropertyString(Constants.PAIR);
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
    
    public Poblacion getByProcessedUntil(int cantidad, int processedUntil) {
        Poblacion p = new Poblacion();
        for (IndividuoEstrategia individuoEstrategia : this.getIndividuos()) {
            if (processedUntil == individuoEstrategia.getProcessedUntil()) {
                p.getIndividuos().add(individuoEstrategia);
            }
        }
        return p;
    }

    public Poblacion getFirst(int cantidad) {
        Poblacion p = new Poblacion();
        p.setIndividuos(CollectionUtil.subList(this.getIndividuos(), 0, (cantidad < this.getIndividuos().size()) ? cantidad : this.getIndividuos().size()));

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
            if (!this.individuos.contains(individuoEstrategia)) {
                this.individuos.add(individuoEstrategia);
            }
        }
    }

    /*    public void addAllHistoricos(List<IndividuoEstrategia> individuos) {
    Set<IndividuoEstrategia> set = new HashSet<IndividuoEstrategia>();
    set.addAll(individuosHistoricos);
    individuosHistoricos.clear();
    set.addAll(individuos);

    individuosHistoricos.clear();
    individuosHistoricos.addAll(set);
    }
     */
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
