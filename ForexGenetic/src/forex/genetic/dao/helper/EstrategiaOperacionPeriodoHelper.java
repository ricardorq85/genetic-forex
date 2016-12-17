/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	public static List<ParametroOperacionPeriodo> inclusiones(ResultSet resultado) throws SQLException {
		List<ParametroOperacionPeriodo> inclusiones	= new ArrayList<>();	

		while (resultado.next()) {
			ParametroOperacionPeriodo param = new ParametroOperacionPeriodo();
			param.setFiltroPipsXSemana(resultado.getInt("R_SEMANA"));
			param.setFiltroPipsXMes(resultado.getInt("R_MES"));
			param.setFiltroPipsXAnyo(resultado.getInt("R_ANYO"));
			param.setFiltroPipsTotales(resultado.getInt("R_TOTALES"));
			
			param.setFiltroR2Semana(resultado.getDouble("R2_SEMANA"));
			param.setFiltroR2Mes(resultado.getDouble("R2_MES"));
			param.setFiltroR2Anyo(resultado.getDouble("R2_ANYO"));
			param.setFiltroR2Totales(resultado.getDouble("R2_TOTALES"));

			param.setFiltroPendienteSemana(resultado.getDouble("PENDIENTE_SEMANA"));
			param.setFiltroPendienteMes(resultado.getDouble("PENDIENTE_MES"));
			param.setFiltroPendienteAnyo(resultado.getDouble("PENDIENTE_ANYO"));
			param.setFiltroPendienteTotales(resultado.getDouble("PENDIENTE_TOTALES"));
			
			inclusiones.add(param);
		}
		return inclusiones;
	}	

}