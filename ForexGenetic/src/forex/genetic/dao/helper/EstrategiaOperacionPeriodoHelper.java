/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import forex.genetic.entities.ParametroOperacionPeriodo;

/**
 *
 * @author ricardorq85
 */
public class EstrategiaOperacionPeriodoHelper {

	public static ParametroOperacionPeriodo estrategiaOperacionPeriodo(ResultSet resultado) throws SQLException {
		ParametroOperacionPeriodo param = null;

		if (resultado.next()) {
			param = new ParametroOperacionPeriodo();
			param.setFiltroPipsXSemana(resultado.getInt("FILTRO_PIPS_X_SEMANA"));
			param.setFiltroPipsXMes(resultado.getInt("FILTRO_PIPS_X_MES"));
			param.setFiltroPipsXAnyo(resultado.getInt("FILTRO_PIPS_X_ANYO"));
			param.setFiltroPipsTotales(resultado.getInt("FILTRO_PIPS_TOTALES"));
			param.setFirstOrder(resultado.getString("FIRST_ORDER"));
			param.setSecondOrder(resultado.getString("SECOND_ORDER"));
			param.setPipsTotales(resultado.getDouble("PIPS_TOTALES"));
			param.setCantidad(resultado.getInt("CANTIDAD"));
			param.setFecha(new Date(resultado.getTimestamp("FECHA").getTime()));
			param.setFechaInicial(new Date(resultado.getTimestamp("FECHA_INICIAL").getTime()));
			param.setFechaFinal(new Date(resultado.getTimestamp("FECHA_FINAL").getTime()));
		}
		return param;
	}

}