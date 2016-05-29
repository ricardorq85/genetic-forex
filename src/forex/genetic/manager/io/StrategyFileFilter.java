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
public class StrategyFileFilter implements FileFilter {

    private long previousTo = 0L;
    private IndividuoEstrategia individuoEstrategia = null;
    private String idFilter = null;

    public StrategyFileFilter() {
        this(null);
    }

    public StrategyFileFilter(IndividuoEstrategia individuoEstrategia) {
        this.individuoEstrategia = individuoEstrategia;
        if (individuoEstrategia != null) {
            if (individuoEstrategia.getId() != null) {
                String str = individuoEstrategia.getIdParent1();
                String[] split = str.split("\\.");
                if ((split != null) && (split.length > 0)) {
                    idFilter = split[0];
                }
            }
        }
        if ((individuoEstrategia == null) || (individuoEstrategia.getCreationDate() == null)) {
            this.previousTo = System.currentTimeMillis();
        } else {
            this.previousTo = individuoEstrategia.getCreationDate().getTime();
        }
    }

    public boolean accept(File file) {
        boolean accept = false;
        String name = file.getName();
        if ((name.contains(".gfx")) && (file.lastModified() <= this.previousTo)) {
            if (idFilter != null) {
                if (name.contains(idFilter)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
        return accept;
    }
}
