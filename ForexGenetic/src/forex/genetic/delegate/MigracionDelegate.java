/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import forex.genetic.entities.Poblacion;
import forex.genetic.facade.DatoHistoricoFacade;
import forex.genetic.facade.PoblacionFacade;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.MigracionManager;
import forex.genetic.manager.PoblacionManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MigracionDelegate {

	public void cargarDatosHistoricos() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
		MigracionManager migracionManager = new MigracionManager();
		migracionManager.migrate();
	}
}
