package forex.genetic.factory;

import forex.genetic.facade.IGeneticFacade;
import forex.genetic.facade.MultipleDatoHistoricoFacadeImpl;
import forex.genetic.facade.PoblacionFacade;

public class FacadeFactory extends GeneticFactory {

	public static IGeneticFacade create(String entidad) {
		IGeneticFacade facade  = null;
		if ("datoHistorico".equals(entidad)) {
			facade = new MultipleDatoHistoricoFacadeImpl();
		}else if ("individuo".equals(entidad)) {
			facade = new PoblacionFacade();
		}
		return facade;
	}

}
