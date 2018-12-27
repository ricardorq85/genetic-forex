/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.util.Date;
import java.util.List;

import forex.genetic.entities.Estadistica;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.entities.mongo.MongoEstadistica;

/**
 *
 * @author ricardorq85
 */
public interface IEstadisticaDAO<E extends Estadistica> extends IGeneticDAO<E> {

	<I extends IndividuoEstrategia> E getLast(I individuo);
	
	List<E> consultarRandom(Date fechaInicial, Date fechaFinal, int cantidad);

	<I extends IndividuoEstrategia, O extends Order> E getLast(I individuo, Date fechaBase);

	List<E> consultarByDuracionPromedio(int duracionPromedioMinutosMinimos, int cantidad);
}
