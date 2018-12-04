/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import forex.genetic.entities.Estadistica;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.entities.mongo.MongoEstadistica;

/**
 *
 * @author ricardorq85
 */
public interface IEstadisticaDAO<E extends Estadistica> extends IGeneticDAO<E> {

	public <I extends IndividuoEstrategia> MongoEstadistica getLast(I individuo);

	public <I extends IndividuoEstrategia, O extends Order> MongoEstadistica getLast(I individuo, O order);
}
