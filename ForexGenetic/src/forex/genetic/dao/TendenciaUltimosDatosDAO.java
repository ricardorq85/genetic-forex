/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.sql.Connection;

import forex.genetic.dao.oracle.OracleTendenciaDAO;

/**
 *
 * @author ricardorq85
 */
public class TendenciaUltimosDatosDAO extends OracleTendenciaDAO {

	public TendenciaUltimosDatosDAO(Connection connection) {
		super(connection, "TENDENCIA_ULTIMOSDATOS");
	}
}
