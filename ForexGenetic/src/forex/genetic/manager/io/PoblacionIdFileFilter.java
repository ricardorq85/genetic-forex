/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author ricardorq85
 */
public class PoblacionIdFileFilter implements FileFilter {

    private String id = null;

    /**
     *
     * @param id
     */
    public PoblacionIdFileFilter(String id) {
        this.id = id;
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
    }

    @Override
    public boolean accept(File file) {
        boolean accept = false;
        String name = file.getName();
        accept = ((name.contains(".gfx")) && (name.contains(id)));
        return accept;
    }
}
