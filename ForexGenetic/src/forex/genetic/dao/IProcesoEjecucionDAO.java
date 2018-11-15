/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.util.Date;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.dto.ProcesoEjecucionDTO;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public interface IProcesoEjecucionDAO extends IGeneticDAO<ProcesoEjecucionDTO> {

	public Date getFechaHistoricaProcesoMaxima() throws GeneticDAOException;

	public int getCountHistorico(Date fechaOperacion, int diasProceso) throws GeneticDAOException;

	public void insertOperacionBase(Date fechaOperacion, int diasProceso, String idIndividuo) throws GeneticDAOException;

	public void insertProceso(Date fechaOperacion, String idIndividuo) throws GeneticDAOException;

	public int updateProceso(Date fechaOperacion, String idIndividuo) throws GeneticDAOException;

	public void insertProcesoRepetidos(String idIndividuo, String tipoProceso) throws GeneticDAOException;

	public int deleteProceso(String idIndividuo) throws GeneticDAOException;

	public int hasMinimumOperations(Date fechaInicial, Date fechaOperacion, String idIndividuo) throws GeneticDAOException;

	public Date getFechaOperacion(Date fechaOperacion, int diasProceso, String idIndividuo) throws GeneticDAOException;

	public List<Individuo> getIndividuos(String filtroAdicional, Date fechaHistorico) throws GeneticDAOException;

	public void insertIndividuo(IndividuoEstrategia individuo) throws GeneticDAOException;
}
