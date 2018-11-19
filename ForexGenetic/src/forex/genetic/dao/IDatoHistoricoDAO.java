/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.util.Date;
import java.util.List;

import forex.genetic.entities.DateInterval;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public interface IDatoHistoricoDAO extends IGeneticDAO<Point> {

	public int consultarCantidadPuntos() throws GeneticDAOException;

	public int consultarCantidadPuntos(DateInterval interval) throws GeneticDAOException;

	public Date getFechaHistoricaMinima() throws GeneticDAOException;

	public Date getFechaHistoricaMinima(Date fechaMayorQue) throws GeneticDAOException;

	public Date getFechaHistoricaMaxima(Date fecha) throws GeneticDAOException;

	public Date getFechaHistoricaMaxima() throws GeneticDAOException;

	public List<Point> consultarHistorico(Date fechaBase1, Date fechaBase2) throws GeneticDAOException;

	public List<Point> consultarHistoricoOrderByPrecio(Date fechaBase1, Date fechaBase2) throws GeneticDAOException;

	public List<Point> consultarHistorico(Date fechaBase) throws GeneticDAOException;

	public List<Point> consultarPuntoByLow(Date fechaBase1, Date fechaBase2, double base) throws GeneticDAOException;

	public List<Point> consultarPuntoByHigh(Date fechaBase1, Date fechaBase2, double base) throws GeneticDAOException;

	public DoubleInterval consultarMaximoMinimo(Date fecha1, Date fecha2) throws GeneticDAOException;

	public Point consultarRetroceso(Order orden) throws GeneticDAOException;

	public double consultarMaximaDiferencia(Date fecha, String formatoAgrupador) throws GeneticDAOException;

	public double consultarPrecioPonderado(Date fecha) throws GeneticDAOException;

	public Point consultarProximoPuntoApertura(IndividuoEstrategia individuo, DateInterval rango)
			throws GeneticDAOException;

	public Point consultarPuntoCierre(IndividuoEstrategia individuo, Date fechaBase) throws GeneticDAOException;

	public Point consultarPuntoAnterior(Date fecha) throws GeneticDAOException;

	public List<Date> consultarPuntosApertura(Date fechaMayorQue, String idIndividuo) throws GeneticDAOException;

	public List<Date> consultarPuntosApertura(DateInterval rango, String idIndividuo) throws GeneticDAOException;
}
