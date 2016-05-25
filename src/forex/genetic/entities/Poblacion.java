/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author ricardorq85
 */
public class Poblacion {

    private List<IndividuoEstrategia> individuos = new Vector<IndividuoEstrategia>();

    public Poblacion getFirst() {
        return getFirst(1);
    }

    public Poblacion getFirst(int cantidad) {
        Poblacion p = new Poblacion();
        p.setIndividuos(this.getIndividuos().subList(0, (cantidad < this.getIndividuos().size()) ? cantidad : this.getIndividuos().size()));

        return p;
    }

    public void removeAll(List<IndividuoEstrategia> individuos) {
        this.individuos.removeAll(individuos);
    }

    public void addAll(Poblacion poblacion) {
        this.individuos.addAll(poblacion.getIndividuos());
        Set<IndividuoEstrategia> set = new HashSet<IndividuoEstrategia>();
        set.addAll(individuos);
        individuos.clear();
        set.addAll(individuos);

        individuos.clear();
        individuos.addAll(set);

        //this.addAllHistoricos(poblacion.getIndividuos());
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
        return individuos;
    }

    public void setIndividuos(List<IndividuoEstrategia> individuos) {
        this.individuos = individuos;
        //this.addAllHistoricos(individuos);
    }
}
