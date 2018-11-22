/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.util.Date;
import java.util.List;

import forex.genetic.entities.DateInterval;
import forex.genetic.entities.ParametroTendenciaGenetica;
import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.entities.Tendencia;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public interface ITendenciaDAO extends IGeneticDAO<Tendencia> {

	public int deleteTendencia(String idIndividuo) throws GeneticDAOException;

	public void deleteTendencia(String idIndividuo, Date fechaBase) throws GeneticDAOException;

	public int deleteTendenciaMenorQue(Date fechaBase) throws GeneticDAOException;

	public List<Tendencia> consultarTendenciasActualizar() throws GeneticDAOException;

	public Date nextFechaBase(Date fecha) throws GeneticDAOException;

	public Date maxFechaBaseTendencia() throws GeneticDAOException;

	public Date maxFechaProcesoTendencia(DateInterval intervaloFechaBase) throws GeneticDAOException;

	public int count(java.util.Date fecha) throws GeneticDAOException;

	public ProcesoTendencia consultarProcesarTendencia(java.util.Date fecha, java.util.Date fecha2)
			throws GeneticDAOException;

	public ProcesoTendencia consultarProcesarTendencia(java.util.Date fecha, java.util.Date fecha2, String tipo)
			throws GeneticDAOException;

	public List<ProcesoTendencia> consultarProcesarTendenciaDetalle(java.util.Date fecha, java.util.Date fecha2,
			int groupByMinutes) throws GeneticDAOException;

	public List<ProcesoTendencia> consultarTendenciaGenetica(java.util.Date fecha, java.util.Date fecha2,
			ParametroTendenciaGenetica parametroTendenciaGenetica) throws GeneticDAOException;

	public List<Date> consultarXCantidadFechaBase(Date fechaInicio, int parametroMesesTendencia)
			throws GeneticDAOException;

	public Date dummyTendencia(Date fecha, int rownum) throws GeneticDAOException;

	public String getTabla();

	public void setTabla(String tabla);

}
