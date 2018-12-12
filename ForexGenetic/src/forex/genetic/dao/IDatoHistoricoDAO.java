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

	public Point consultarXFecha(Date fechaBase) throws GeneticDAOException;

	public int consultarCantidadPuntos() throws GeneticDAOException;

	public int consultarCantidadPuntos(DateInterval interval) throws GeneticDAOException;

	public Date getFechaHistoricaMinima() throws GeneticDAOException;

	public Date getFechaHistoricaMinima(Date fechaMayorQue) throws GeneticDAOException;

	public Date getFechaHistoricaMaxima(Date fecha) throws GeneticDAOException;

	public Date getFechaHistoricaMaxima() throws GeneticDAOException;

	public List<? extends Point> consultarHistorico(Date fechaBase1, Date fechaBase2) throws GeneticDAOException;

	public List<? extends Point> consultarHistoricoOrderByPrecio(Date fechaBase1, Date fechaBase2)
			throws GeneticDAOException;

	public List<? extends Point> consultarHistorico(Date fechaBase) throws GeneticDAOException;

	public List<? extends Point> consultarPuntoByLow(Date fechaBase1, Date fechaBase2, double base)
			throws GeneticDAOException;

	public List<? extends Point> consultarPuntoByHigh(Date fechaBase1, Date fechaBase2, double base)
			throws GeneticDAOException;

	public DoubleInterval consultarMaximoMinimo(Date fecha1, Date fecha2) throws GeneticDAOException;

	public Point consultarRetroceso(Order orden) throws GeneticDAOException;

	public double consultarMaximaDiferencia(Date fecha, String formatoAgrupador) throws GeneticDAOException;

	public double consultarPrecioPonderado(Date fecha) throws GeneticDAOException;

	public <H extends Order> Point consultarPuntoCierreByTakeOrStop(H order, DateInterval rango)
			throws GeneticDAOException;

	public Point consultarPuntoAnterior(Date fecha) throws GeneticDAOException;

	public List<Date> consultarPuntosApertura(Date fechaMayorQue, String idIndividuo) throws GeneticDAOException;

	public List<Date> consultarPuntosApertura(DateInterval rango, String idIndividuo) throws GeneticDAOException;

	public List<Point> consultarProximosPuntosApertura(IndividuoEstrategia individuo, DateInterval rango);

	public List<Point> consultarPuntosCierre(IndividuoEstrategia individuo, DateInterval rango);
}
