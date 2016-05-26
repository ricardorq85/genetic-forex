/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.Point;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public abstract class BasePointManager {

    public static BasePointManager getBasePointManager() {
        //return new BasePointManager1Minuto();
        //return new BasePointManager10Minutos();
        return new BasePointManagerFile();
    }

    protected BasePointManager() {
    }

    public abstract List<Point> process();

}
