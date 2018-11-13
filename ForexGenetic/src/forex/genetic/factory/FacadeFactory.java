package forex.genetic.factory;

import forex.genetic.facade.IGeneticFacade;
import forex.genetic.facade.MultipleDatoHistoricoFacadeImpl;

public class FacadeFactory extends GeneticFactory {

	public static IGeneticFacade create(String entidad) {
		IGeneticFacade facade  = null;
		if ("datoHistorico".equals(entidad)) {
			facade = new MultipleDatoHistoricoFacadeImpl();
		}
		return facade;
	}

}
