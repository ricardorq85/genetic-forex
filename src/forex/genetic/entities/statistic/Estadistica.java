/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.statistic;

/**
 *
 * @author ricardorq85
 */
public class Estadistica {

    private int individuosGenerados = 0;
    private int generaciones = 0;
    private int individuosLeidos = 0;
    private int archivosLeidos = 0;
    private int individuosCruzados = 0;
    private int individuosMutados = 0;
    private int individuosInvalidos = 0;
    private int individuosRemovedEqualsReal = 0;    
    
    public void addIndividuoGenerado(int count) {
        individuosGenerados += count;
    }

    public void addGeneracion(int count) {
        generaciones += count;
    }

    public void addIndividuoLeido(int count) {
        individuosLeidos += count;
    }

    public void addArchivoLeido(int count) {
        archivosLeidos += count;
    }

    public void addIndividuoCruzado(int count) {
        individuosCruzados += count;
    }

    public void addIndividuoMutado(int count) {
        individuosMutados += count;
    }

    public void addIndividuoInvalido(int count) {
        individuosInvalidos += count;
    }
    public void addIndividuoRemovedEqualsReal(int count) {
        individuosRemovedEqualsReal += count;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Estadisticas... ");
        buffer.append(" IndividuosGenerados ");
        buffer.append(this.individuosGenerados);
        buffer.append(" IndividuosLeidos ");
        buffer.append(this.individuosLeidos);
        buffer.append(" ArchivosLeidos ");
        buffer.append(this.archivosLeidos);
        buffer.append(" Generaciones ");
        buffer.append(this.generaciones);
        buffer.append("\n\t");
        buffer.append(" IndividuosCruzados ");
        buffer.append(this.individuosCruzados);
        buffer.append(" IndividuosMutados ");
        buffer.append(this.individuosMutados);
        buffer.append(" IndividuosInvalidos ");
        buffer.append(this.individuosInvalidos);
        buffer.append("\n\t");
        buffer.append(" IndividuosEqualsReal ");
        buffer.append(this.individuosRemovedEqualsReal);
        
        return buffer.toString();
    }
}
