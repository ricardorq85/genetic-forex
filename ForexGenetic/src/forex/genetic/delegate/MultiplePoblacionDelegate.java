/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import java.io.FileNotFoundException;
import java.io.IOException;

import forex.genetic.entities.Poblacion;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.facade.DatoHistoricoFacade;
import forex.genetic.facade.PoblacionFacade;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.factory.FacadeFactory;
import forex.genetic.manager.PoblacionManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MultiplePoblacionDelegate {

	private DatoHistoricoFacade dhFacade;
	private PoblacionFacade poblacionFacade;

	public MultiplePoblacionDelegate() {
		super();
		dhFacade = (DatoHistoricoFacade) FacadeFactory.create("datoHistorico");
		poblacionFacade = (PoblacionFacade) FacadeFactory.create("individuo");
	}

	/**
	 *
	 * @param poblacion
	 */
	public void cargarPoblacion(Poblacion poblacion) {
		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		poblacionFacade.cargarPoblacion(indicadorController, poblacion);
	}

	/**
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void cargarDatosHistoricos() throws GeneticDAOException {
		int endPoblacion = PropertiesManager.getPropertyInt(Constants.END_POBLACION);
		for (int poblacionIndex = PropertiesManager
				.getPropertyInt(Constants.INITIAL_POBLACION); poblacionIndex <= endPoblacion
						&& !PropertiesManager.getPropertyBoolean(Constants.TERMINAR); poblacionIndex++) {
			LogUtil.logTime("Crear poblacion " + poblacionIndex, 1);
			PoblacionManager poblacionManager = new PoblacionManager();
			poblacionManager.load("" + poblacionIndex, false);
			PoblacionManager nextPoblacionManager = new PoblacionManager();
			if (poblacionIndex < endPoblacion) {
				nextPoblacionManager.load("" + (poblacionIndex + 1), false);
			}
			dhFacade.cargarDatoHistorico(poblacionManager.getPoints());
			LogUtil.logTime("Poblacion " + poblacionIndex + " Fecha = " + poblacionManager.getDateInterval(), 1);
		}
	}
}
