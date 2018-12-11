/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.util.Date;
import java.util.List;

import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public interface ITendenciaParaOperarDAO extends IGeneticDAO<TendenciaParaOperar> {

	public List<? extends TendenciaParaOperar> consultar(Date fechaInicio) throws GeneticDAOException;

}
