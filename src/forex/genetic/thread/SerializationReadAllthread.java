/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.entities.Poblacion;
import forex.genetic.manager.io.SerializationManager;

/**
 *
 * @author ricardorq85
 */
public class SerializationReadAllthread extends Thread {

    private String path;
    private int counter;
    private int processedUntil;
    private SerializationManager sm = null;
    private Poblacion poblacion = null;

    public SerializationReadAllthread(String path, int counter, int processedUntil, SerializationManager sm, Poblacion poblacion) {
        this.path = path;
        this.counter = counter;
        this.processedUntil = processedUntil;
        this.poblacion = poblacion;
    }

    public void run() {
        try {
            Poblacion p = sm.readAll(path, counter, processedUntil);
            poblacion.addAll(p);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Poblacion getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(Poblacion poblacion) {
        this.poblacion = poblacion;
    }

    public int getProcessedUntil() {
        return processedUntil;
    }

    public void setProcessedUntil(int processedUntil) {
        this.processedUntil = processedUntil;
    }

    public SerializationManager getSm() {
        return sm;
    }

    public void setSm(SerializationManager sm) {
        this.sm = sm;
    }
    
    
}
