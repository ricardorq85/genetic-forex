/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.util.Date;

import forex.genetic.bo.Parametro;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public interface IParametroDAO extends IGeneticDAO<Parametro> {

	public int getIntValorParametro(String nombre) throws GeneticDAOException;

	public float getFloatValorParametro(String nombre) throws GeneticDAOException;

	public boolean getBooleanValorParametro(String nombre) throws GeneticDAOException;

	public String getValorParametro(String nombre) throws GeneticDAOException;

	public String[] getArrayStringParametro(String nombre) throws GeneticDAOException;

	public Date getDateValorParametro(String nombre) throws GeneticDAOException;

	public void updateDateValorParametro(String nombre, Date valor) throws GeneticDAOException;

	public void updateValorParametro(String nombre, String valor) throws GeneticDAOException;
}
