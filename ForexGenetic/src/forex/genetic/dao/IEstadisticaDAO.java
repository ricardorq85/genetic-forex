/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.util.Date;

import forex.genetic.entities.Estadistica;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;

/**
 *
 * @author ricardorq85
 */
public interface IEstadisticaDAO<E extends Estadistica> extends IGeneticDAO<E> {

	public <I extends IndividuoEstrategia> E getLast(I individuo);

	public <I extends IndividuoEstrategia, O extends Order> E getLast(I individuo, Date fechaBase);
}
