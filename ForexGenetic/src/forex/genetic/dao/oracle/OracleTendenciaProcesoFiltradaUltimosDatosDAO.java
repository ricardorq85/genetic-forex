/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.oracle;

import java.sql.Connection;

/**
 *
 * @author ricardorq85
 */
public class OracleTendenciaProcesoFiltradaUltimosDatosDAO extends OracleTendenciaProcesoFiltradaDAO {

	public OracleTendenciaProcesoFiltradaUltimosDatosDAO(Connection connection) {
		super(connection, "TENDENCIA_ULTIMOSDATOS");
	}

}
