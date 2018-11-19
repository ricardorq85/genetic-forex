/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.util.Date;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.controller.IndicadorController;

/**
 *
 * @author ricardorq85
 */
public interface IIndividuoDAO<E extends IndividuoEstrategia> extends IGeneticDAO<E> {

	public List<? extends IndividuoEstrategia> getListByProcesoEjecucion(String filtroAdicional, Date fechaHistorico)
			throws GeneticDAOException;

	public void crearVistaIndicadoresIndividuo(String viewName, String idIndividuo) throws GeneticDAOException;

	public void insertarIndividuoIndicadoresColumnas(String idIndividuo) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosPadreRepetidos(String tipoProceso) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosStopLossInconsistente(int sl) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosStopLossInconsistente(int sl, String idIndividuo)
			throws GeneticDAOException;

	public List<Individuo> consultarIndividuosCantidadLimite(double porcentajeLimite) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosCantidadLimite(double porcentajeLimite, String idIndividuo)
			throws GeneticDAOException;

	public void smartDelete(String idIndividuo, String causaBorrado, String idPadre) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosRepetidosOperaciones(Individuo individuoPadre) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosRepetidos() throws GeneticDAOException;

	public Individuo consultarIndividuo(String idIndividuo) throws GeneticDAOException;

	public List<Individuo> consultarIndividuoHijoRepetidoOperaciones(Individuo individuoHijo)
			throws GeneticDAOException;

	public List<Individuo> consultarIndividuoHijoRepetido(Individuo individuoHijo) throws GeneticDAOException;

	public void consultarDetalleIndividuoProceso(Individuo individuo, Date fechaHistorico) throws GeneticDAOException;

	public void consultarDetalleIndividuo(IndicadorController indicadorController, Individuo individuo)
			throws GeneticDAOException;

	public void insertIndividuo(IndividuoEstrategia individuo) throws GeneticDAOException;

	public void insertIndicadorIndividuo(IndicadorController indicadorController, IndividuoEstrategia individuo)
			throws GeneticDAOException;

	public List<IndividuoOptimo> consultarIndividuosOptimos() throws GeneticDAOException;

	public int getCountIndicadoresOpen(Individuo individuo) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosResumenSemanal(Date fechaInicial, Date fechaFinal)
			throws GeneticDAOException;

	public List<Individuo> consultarIndividuosRandom(Date fechaInicial, Date fechaFinal, int cantidad)
			throws GeneticDAOException;

	public List<Individuo> consultarIndividuosRandom(int cantidad) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosIndicadoresCloseMinimos(int minimo) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosIndicadoresCloseMinimos(int minimo, String id) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosIntervaloIndicadores() throws GeneticDAOException;

	public List<Individuo> consultarIndividuosIntervaloIndicadores(String idIndividuo) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosParaBorrar(Date fechaLimite) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosParaBorrar(String idIndividuo, Date fechaLimite)
			throws GeneticDAOException;

	public List<Individuo> consultarIndividuosParaBorrar(int minutos) throws GeneticDAOException;

	public List<Individuo> consultarIndividuosParaBorrar(String idIndividuo, int minutos) throws GeneticDAOException;
}
