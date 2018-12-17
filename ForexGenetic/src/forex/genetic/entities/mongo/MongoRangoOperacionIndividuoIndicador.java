/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.mongo;

import forex.genetic.entities.RangoOperacionIndividuoIndicador;

/**
 *
 * @author ricardorq85
 */
public class MongoRangoOperacionIndividuoIndicador extends RangoOperacionIndividuoIndicador {

	public boolean cumplePorcentajeIndicador() {
		return (getPorcentajeCumplimiento() > 0.8);
		/*
		 * && (porcentajeCumplimiento < 0.8)
		 */
	}

}
