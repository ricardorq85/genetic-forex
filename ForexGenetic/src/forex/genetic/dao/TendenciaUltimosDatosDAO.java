/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.sql.Connection;

/**
 *
 * @author ricardorq85
 */
public class TendenciaUltimosDatosDAO extends TendenciaDAO {

	public TendenciaUltimosDatosDAO(Connection connection) {
		super(connection, "TENDENCIA_ULTIMOSDATOS");
	}
}
