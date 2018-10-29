/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import forex.genetic.dao.helper.TendenciaProcesoBuySellHelper;
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class TestTendenciaDAO {

	private Connection connection = null;

	public TestTendenciaDAO(Connection connection) {
		this.connection = connection;
	}

	public List<TendenciaParaOperar> consultarDatosTendencia(ProcesoTendenciaBuySell procesoTendencia)
			throws SQLException {
		List<TendenciaParaOperar> tendencias = new ArrayList<>();
//		String sql = "SELECT FECHA_BASE,FECHA_TENDENCIA, 'X' PERIODO,PRECIO_CALCULADO FROM TENDENCIA TEN "
//				+ " WHERE WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<? " + " AND TEN.FECHA_BASE<=?"
//				+ " AND TEN.FECHA_TENDENCIA>? "
//				+ "ORDER BY FECHA_TENDENCIA ASC";
		String sql = "WITH" + "	PARAMETROS AS " + "(	SELECT "
				+ " 		? PERIODO, (?) TIEMPO_TENDENCIA, ? FECHA_PROCESO, ? TIPO_TENDENCIA "
				+ "		FROM DUAL	" + " 	), " + " TENDENCIA_CALCULADA AS" + " 	(SELECT"
				+ " 		PARAM.FECHA_PROCESO FECHA_BASE, TRUNC(TEN.FECHA_TENDENCIA, 'HH24') FECHA_TENDENCIA, ROUND(SUM("
				+ " 		TEN.PRECIO_CALCULADO*TEN.PROBABILIDAD)/SUM(TEN.PROBABILIDAD),5) PRECIO_CALCULADO, COUNT(*)"
				+ " 		CANTIDAD, (5+2*(PARAM.TIEMPO_TENDENCIA/60-WEEK_MINUTES(TRUNC(TEN.FECHA_TENDENCIA, 'HH24'),TRUNC("
				+ " 		PARAM.FECHA_PROCESO, 'HH24'))/60)) MAYORQ_EXIGIDA, MIN(TEN.FECHA_TENDENCIA) MINFETENDENCIA, MAX("
				+ " 		TEN.FECHA_TENDENCIA) MAXFETENDENCIA,"
				+ "			MIN(TEN.PRECIO_CALCULADO) MINPRECIO_CALCULADO, MAX(TEN.PRECIO_CALCULADO) MAXPRECIO_CALCULADO,"
				+ "			ROUND(AVG(TEN.PROBABILIDAD),5) PROBABILIDAD " + " 	FROM"
				+ " 		PARAMETROS PARAM, TENDENCIA TEN " 
			    + " 	WHERE"
				+ "		TEN.TIPO_TENDENCIA=PARAM.TIPO_TENDENCIA"
				+ "		AND WEEK_MINUTES(TEN.FECHA_TENDENCIA,TEN.FECHA_BASE)<PARAM.TIEMPO_TENDENCIA"
				+ "		AND TEN.FECHA_BASE<=PARAM.FECHA_PROCESO" 
				+ "		AND TEN.FECHA_TENDENCIA>PARAM.FECHA_PROCESO"
				+ "	GROUP BY" + "		TRUNC(TEN.FECHA_TENDENCIA, 'HH24'), PARAM.FECHA_PROCESO, PARAM.TIEMPO_TENDENCIA"
				+ "	)"
				+ " SELECT PARAM.PERIODO PERIODO, TC.* FROM PARAMETROS PARAM, TENDENCIA_CALCULADA TC ORDER BY TC.FECHA_TENDENCIA aSC";
		ResultSet resultado = null;
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			int count = 1;
			stmtConsulta.setString(count++, procesoTendencia.getPeriodo());
			stmtConsulta.setDouble(count++, procesoTendencia.getTiempoTendencia());
			stmtConsulta.setTimestamp(count++, new Timestamp(procesoTendencia.getFechaBase().getTime()));
			stmtConsulta.setString(count++, procesoTendencia.getTipoTendencia());
			resultado = stmtConsulta.executeQuery();
			tendencias = TendenciaProcesoBuySellHelper.helpTendencias(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return tendencias;
	}

}
