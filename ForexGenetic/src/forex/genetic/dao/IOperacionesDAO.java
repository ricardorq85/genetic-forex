/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.util.Date;
import java.util.List;

import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public interface IOperacionesDAO extends IGeneticDAO<Order> {

	public List<DateInterval> consultarVigencias(Date fechaPeriodo) throws GeneticDAOException;

	public List<Individuo> consultarOperacionesXPeriodo(Date fechaInicial, Date fechaFinal) throws GeneticDAOException;

	public double consultarPipsXAgrupacion(String agrupador) throws GeneticDAOException;

	public Estadistica consultarEstadisticasIndividuo(Individuo individuo) throws GeneticDAOException;

	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase) throws GeneticDAOException;

	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase, int filas) throws GeneticDAOException;

	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase, Date fechaFin, int filas)
			throws GeneticDAOException;

	public int deleteOperaciones(String idIndividuo) throws GeneticDAOException;

	public void updateOperacion(Individuo individuo, Order operacion, Date fechaApertura) throws GeneticDAOException;

	public void updateMaximosReprocesoOperacion(Individuo individuo, Order operacion) throws GeneticDAOException;

	public void insertOperaciones(Individuo individuo, List<Order> operaciones) throws GeneticDAOException;

	public Individuo consultarOperacionesIndividuoRetroceso(Individuo ind, Date fechaMaximo) throws GeneticDAOException;

	public List<Individuo> consultarOperacionesIndividuoRetroceso(Date fechaMaximo) throws GeneticDAOException;

	public int cleanOperacionesPeriodo() throws GeneticDAOException;

	public int insertOperacionesPeriodo(ParametroOperacionPeriodo param) throws GeneticDAOException;

	public void actualizarOperacionesPositivasYNegativas() throws GeneticDAOException;

}
