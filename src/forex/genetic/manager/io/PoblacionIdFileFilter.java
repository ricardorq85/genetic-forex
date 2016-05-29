/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import forex.genetic.entities.IndividuoEstrategia;
import java.io.File;
import java.io.FileFilter;
import java.util.Date;

/**
 *
 * @author ricardorq85
 */
public class PoblacionIdFileFilter implements FileFilter {

    private String id = null;

    public PoblacionIdFileFilter(String id) {
        this.id = id;
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
    }

    public boolean accept(File file) {
        boolean accept = false;
        String name = file.getName();
        accept = ((name.contains(".gfx")) && (name.contains(id)));
        return accept;
    }
}
