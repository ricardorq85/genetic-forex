/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.helper.IndividuoHelper;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.util.Constants;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class IndividuoDAO {

	protected Connection connection = null;

	/**
	 *
	 * @param connection
	 */
	public IndividuoDAO(Connection connection) {
		this.connection = connection;
	}

	public int duracionPromedioMinutos(String idIndividuo) throws SQLException {
		String sql = "SELECT ROUND(AVG(OPER.FECHA_CIERRE-OPER.FECHA_APERTURA)*24*60) DURACION FROM OPERACION OPER\n"
				+ " WHERE ID_INDIVIDUO = ? ";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		int duracion = 0;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				if (resultado.getObject("DURACION") != null) {
					duracion = resultado.getInt("DURACION");
				}
			}
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return duracion;
	}

	public List<Date> consultarPuntosApertura(Date fechaMayorQue, String idIndividuo) throws SQLException {
		List<Date> fechas;
		String sql = "SELECT DH.FECHA-1/24/60 FECHA " + " FROM DATOHISTORICO DH"
				+ "  INNER JOIN INDIVIDUO_INDICADORES IC ON IC.ID=? AND "
				+ "      (IC.INFERIOR_MA IS NULL OR IC.SUPERIOR_MA IS NULL OR "
				+ "     ROUND(DH.AVERAGE-DH.LOW, 5) BETWEEN IC.INFERIOR_MA AND IC.SUPERIOR_MA OR ROUND(DH.AVERAGE-DH.HIGH,5) BETWEEN IC.INFERIOR_MA AND IC.SUPERIOR_MA) "
				+ "    AND (IC.INFERIOR_MACD IS NULL OR IC.SUPERIOR_MACD IS NULL OR ROUND(DH.MACD_VALUE-DH.MACD_SIGNAL, 5) BETWEEN IC.INFERIOR_MACD AND IC.SUPERIOR_MACD) "
				+ "    AND (IC.INFERIOR_COMPARE IS NULL OR IC.SUPERIOR_COMPARE IS NULL OR ROUND(DH.AVERAGE_COMPARE-DH.COMPARE_VALUE, 5) BETWEEN IC.INFERIOR_COMPARE AND IC.SUPERIOR_COMPARE) "
				+ "    AND (IC.INFERIOR_ADX IS NULL OR IC.SUPERIOR_ADX IS NULL OR ROUND(DH.ADX_VALUE*(DH.ADX_PLUS-DH.ADX_MINUS), 5) BETWEEN IC.INFERIOR_ADX AND IC.SUPERIOR_ADX) "
				+ "    AND (IC.INFERIOR_SAR IS NULL OR IC.SUPERIOR_SAR IS NULL OR ROUND(DH.SAR-DH.LOW, 5) BETWEEN IC.INFERIOR_SAR AND IC.SUPERIOR_SAR OR DH.SAR-DH.HIGH BETWEEN IC.INFERIOR_SAR AND IC.SUPERIOR_SAR) "
				+ "    AND (IC.INFERIOR_RSI IS NULL OR IC.SUPERIOR_RSI IS NULL OR ROUND(DH.RSI, 5) BETWEEN IC.INFERIOR_RSI AND IC.SUPERIOR_RSI) "
				+ "    AND (IC.INFERIOR_BOLLINGER IS NULL OR IC.SUPERIOR_BOLLINGER IS NULL OR ROUND(DH.BOLLINGER_UPPER-DH.BOLLINGER_LOWER, 5) BETWEEN IC.INFERIOR_BOLLINGER AND IC.SUPERIOR_BOLLINGER) "
				+ "    AND (IC.INFERIOR_MOMENTUM IS NULL OR IC.SUPERIOR_MOMENTUM IS NULL OR ROUND(DH.MOMENTUM, 5) BETWEEN IC.INFERIOR_MOMENTUM AND IC.SUPERIOR_MOMENTUM) "
				+ "    AND (IC.INFERIOR_ICHISIGNAL IS NULL OR IC.SUPERIOR_ICHISIGNAL IS NULL OR ROUND(DH.ICHIMOKUCHINKOUSPAN*(DH.ICHIMOKUTENKANSEN-DH.ICHIMOKUKIJUNSEN),5) BETWEEN IC.INFERIOR_ICHISIGNAL AND IC.SUPERIOR_ICHISIGNAL) "
				+ "    AND (IC.INFERIOR_ICHITREND IS NULL OR IC.SUPERIOR_ICHITREND IS NULL OR ROUND(DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-DH.LOW, 5) BETWEEN IC.INFERIOR_ICHITREND AND IC.SUPERIOR_ICHITREND "
				+ "          OR ROUND(DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-DH.HIGH, 5) BETWEEN IC.INFERIOR_ICHITREND AND IC.SUPERIOR_ICHITREND) "
				+ "    AND (IC.INFERIOR_MA1200 IS NULL OR IC.SUPERIOR_MA1200 IS NULL OR "
				+ "     ROUND(DH.AVERAGE-DH.LOW, 5) BETWEEN IC.INFERIOR_MA1200 AND IC.SUPERIOR_MA1200 OR ROUND(DH.AVERAGE-DH.HIGH,5) BETWEEN IC.INFERIOR_MA1200 AND IC.SUPERIOR_MA1200) "
				+ "    AND (IC.INFERIOR_MACD20X IS NULL OR IC.SUPERIOR_MACD20X IS NULL OR ROUND(DH.MACD20X_VALUE+DH.MACD20X_SIGNAL, 5) BETWEEN IC.INFERIOR_MACD20X AND IC.SUPERIOR_MACD20X) "
				+ "    AND (IC.INFERIOR_COMPARE1200 IS NULL OR IC.SUPERIOR_COMPARE1200 IS NULL OR ROUND(DH.AVERAGE_COMPARE1200-DH.COMPARE_VALUE, 5) BETWEEN IC.INFERIOR_COMPARE1200 AND IC.SUPERIOR_COMPARE1200) "
				+ "    AND (IC.INFERIOR_ADX168 IS NULL OR IC.SUPERIOR_ADX168 IS NULL OR ROUND(DH.ADX_VALUE168*(DH.ADX_PLUS168-DH.ADX_MINUS168), 5) BETWEEN IC.INFERIOR_ADX168 AND IC.SUPERIOR_ADX168) "
				+ "    AND (IC.INFERIOR_SAR1200 IS NULL OR IC.SUPERIOR_SAR1200 IS NULL OR ROUND(DH.SAR1200-DH.LOW, 5) BETWEEN IC.INFERIOR_SAR1200 AND IC.SUPERIOR_SAR1200 OR DH.SAR1200-DH.HIGH BETWEEN IC.INFERIOR_SAR1200 AND IC.SUPERIOR_SAR1200) "
				+ "    AND (IC.INFERIOR_RSI84 IS NULL OR IC.SUPERIOR_RSI84 IS NULL OR ROUND(DH.RSI84, 5) BETWEEN IC.INFERIOR_RSI84 AND IC.SUPERIOR_RSI84) "
				+ "    AND (IC.INFERIOR_BOLLINGER240 IS NULL OR IC.SUPERIOR_BOLLINGER240 IS NULL OR ROUND(DH.BOLLINGER_UPPER240-DH.BOLLINGER_LOWER240, 5) BETWEEN IC.INFERIOR_BOLLINGER240 AND IC.SUPERIOR_BOLLINGER240) "
				+ "    AND (IC.INFERIOR_MOMENTUM1200 IS NULL OR IC.SUPERIOR_MOMENTUM1200 IS NULL OR ROUND(DH.MOMENTUM1200, 5) BETWEEN IC.INFERIOR_MOMENTUM1200 AND IC.SUPERIOR_MOMENTUM1200) "
				+ "    AND (IC.INFERIOR_ICHISIGNAL6 IS NULL OR IC.SUPERIOR_ICHISIGNAL6 IS NULL OR ROUND(DH.ICHIMOKUCHINKOUSPAN6*(DH.ICHIMOKUTENKANSEN6-DH.ICHIMOKUKIJUNSEN6),5) BETWEEN IC.INFERIOR_ICHISIGNAL6 AND IC.SUPERIOR_ICHISIGNAL6) "
				+ "    AND (IC.INFERIOR_ICHITREND6 IS NULL OR IC.SUPERIOR_ICHITREND6 IS NULL OR ROUND(DH.ICHIMOKUSENKOUSPANA6-DH.ICHIMOKUSENKOUSPANB6-DH.LOW, 5) BETWEEN IC.INFERIOR_ICHITREND6 AND IC.SUPERIOR_ICHITREND6 "
				+ "          OR ROUND(DH.ICHIMOKUSENKOUSPANA6-DH.ICHIMOKUSENKOUSPANB6-DH.HIGH, 5) BETWEEN IC.INFERIOR_ICHITREND6 AND IC.SUPERIOR_ICHITREND6) "
				+ " WHERE DH.FECHA>NVL(?,(SELECT MIN(FECHA) FROM DATOHISTORICO)) " + " ORDER BY FECHA ASC ";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			if (fechaMayorQue != null) {
				stmtConsulta.setTimestamp(2, new Timestamp(fechaMayorQue.getTime()));
			} else {
				stmtConsulta.setTimestamp(2, null);
			}
			resultado = stmtConsulta.executeQuery();
			fechas = IndividuoHelper.createFechas(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return fechas;
	}

	public void crearVistaIndicadoresIndividuo(String viewName, String idIndividuo) throws SQLException {
		String sql = "CREATE OR REPLACE VIEW " + viewName + " AS " + "SELECT IND.*,  "
				+ "  II_MA.INTERVALO_INFERIOR INFERIOR_MA, II_MA.INTERVALO_SUPERIOR SUPERIOR_MA, "
				+ "  II_MACD.INTERVALO_INFERIOR INFERIOR_MACD, II_MACD.INTERVALO_SUPERIOR SUPERIOR_MACD, "
				+ "  II_COMPARE.INTERVALO_INFERIOR INFERIOR_COMPARE, II_COMPARE.INTERVALO_SUPERIOR SUPERIOR_COMPARE, "
				+ "  II_ADX.INTERVALO_INFERIOR INFERIOR_ADX, II_ADX.INTERVALO_SUPERIOR SUPERIOR_ADX, "
				+ "  II_BOLLINGER.INTERVALO_INFERIOR INFERIOR_BOLLINGER, II_BOLLINGER.INTERVALO_SUPERIOR SUPERIOR_BOLLINGER, "
				+ "  II_ICHISIGNAL.INTERVALO_INFERIOR INFERIOR_ICHISIGNAL, II_ICHISIGNAL.INTERVALO_SUPERIOR SUPERIOR_ICHISIGNAL, "
				+ "  II_ICHITREND.INTERVALO_INFERIOR INFERIOR_ICHITREND, II_ICHITREND.INTERVALO_SUPERIOR SUPERIOR_ICHITREND, "
				+ "  II_MOMENTUM.INTERVALO_INFERIOR INFERIOR_MOMENTUM, II_MOMENTUM.INTERVALO_SUPERIOR SUPERIOR_MOMENTUM, "
				+ "  II_RSI.INTERVALO_INFERIOR INFERIOR_RSI, II_RSI.INTERVALO_SUPERIOR SUPERIOR_RSI, "
				+ "  II_SAR.INTERVALO_INFERIOR INFERIOR_SAR, II_SAR.INTERVALO_SUPERIOR SUPERIOR_SAR, "
				+ "  II_MA1200.INTERVALO_INFERIOR INFERIOR_MA1200, II_MA1200.INTERVALO_SUPERIOR SUPERIOR_MA1200, "
				+ "  II_MACD20X.INTERVALO_INFERIOR INFERIOR_MACD20X, II_MACD20X.INTERVALO_SUPERIOR SUPERIOR_MACD20X, "
				+ "  II_COMPARE1200.INTERVALO_INFERIOR INFERIOR_COMPARE1200, II_COMPARE1200.INTERVALO_SUPERIOR SUPERIOR_COMPARE1200, "
				+ "  II_ADX168.INTERVALO_INFERIOR INFERIOR_ADX168, II_ADX168.INTERVALO_SUPERIOR SUPERIOR_ADX168, "
				+ "  II_BOLLINGER240.INTERVALO_INFERIOR INFERIOR_BOLLINGER240, II_BOLLINGER240.INTERVALO_SUPERIOR SUPERIOR_BOLLINGER240, "
				+ "  II_ICHISIGNAL6.INTERVALO_INFERIOR INFERIOR_ICHISIGNAL6, II_ICHISIGNAL6.INTERVALO_SUPERIOR SUPERIOR_ICHISIGNAL6, "
				+ "  II_ICHITREND6.INTERVALO_INFERIOR INFERIOR_ICHITREND6, II_ICHITREND6.INTERVALO_SUPERIOR SUPERIOR_ICHITREND6, "
				+ "  II_MOMENTUM1200.INTERVALO_INFERIOR INFERIOR_MOMENTUM1200, II_MOMENTUM1200.INTERVALO_SUPERIOR SUPERIOR_MOMENTUM1200, "
				+ "  II_RSI84.INTERVALO_INFERIOR INFERIOR_RSI84, II_RSI84.INTERVALO_SUPERIOR SUPERIOR_RSI84, "
				+ "  II_SAR1200.INTERVALO_INFERIOR INFERIOR_SAR1200, II_SAR1200.INTERVALO_SUPERIOR SUPERIOR_SAR1200 "
				+ " FROM INDIVIDUO IND "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_MA ON IND.ID=II_MA.ID_INDIVIDUO AND II_MA.ID_INDICADOR='MA' AND II_MA.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_MACD ON IND.ID=II_MACD.ID_INDIVIDUO AND II_MACD.ID_INDICADOR='MACD' AND II_MACD.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_COMPARE ON IND.ID=II_COMPARE.ID_INDIVIDUO AND II_COMPARE.ID_INDICADOR='COMPARE_MA' AND II_COMPARE.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_ADX ON IND.ID=II_ADX.ID_INDIVIDUO AND II_ADX.ID_INDICADOR='ADX' AND II_ADX.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_BOLLINGER ON IND.ID=II_BOLLINGER.ID_INDIVIDUO AND II_BOLLINGER.ID_INDICADOR='BOLLINGER' AND II_BOLLINGER.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_ICHISIGNAL ON IND.ID=II_ICHISIGNAL.ID_INDIVIDUO AND II_ICHISIGNAL.ID_INDICADOR='ICHIMOKU_SIGNAL' AND II_ICHISIGNAL.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_ICHITREND ON IND.ID=II_ICHITREND.ID_INDIVIDUO AND II_ICHITREND.ID_INDICADOR='ICHIMOKU_TREND' AND II_ICHITREND.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_MOMENTUM ON IND.ID=II_MOMENTUM.ID_INDIVIDUO AND II_MOMENTUM.ID_INDICADOR='MOMENTUM' AND II_MOMENTUM.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_RSI ON IND.ID=II_RSI.ID_INDIVIDUO AND II_RSI.ID_INDICADOR='RSI' AND II_RSI.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_SAR ON IND.ID=II_SAR.ID_INDIVIDUO AND II_SAR.ID_INDICADOR='SAR' AND II_SAR.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_MA1200 ON IND.ID=II_MA1200.ID_INDIVIDUO AND II_MA1200.ID_INDICADOR='MA1200' AND II_MA1200.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_MACD20X ON IND.ID=II_MACD20X.ID_INDIVIDUO AND II_MACD20X.ID_INDICADOR='MACD20X' AND II_MACD20X.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_COMPARE1200 ON IND.ID=II_COMPARE1200.ID_INDIVIDUO AND II_COMPARE1200.ID_INDICADOR='COMPARE_MA1200' AND II_COMPARE1200.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_ADX168 ON IND.ID=II_ADX168.ID_INDIVIDUO AND II_ADX168.ID_INDICADOR='ADX168' AND II_ADX168.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_BOLLINGER240 ON IND.ID=II_BOLLINGER240.ID_INDIVIDUO AND II_BOLLINGER240.ID_INDICADOR='BOLLINGER240' AND II_BOLLINGER240.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_ICHISIGNAL6 ON IND.ID=II_ICHISIGNAL6.ID_INDIVIDUO AND II_ICHISIGNAL6.ID_INDICADOR='ICHIMOKU_SIGNAL6' AND II_ICHISIGNAL6.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_ICHITREND6 ON IND.ID=II_ICHITREND6.ID_INDIVIDUO AND II_ICHITREND6.ID_INDICADOR='ICHIMOKU_TREND6' AND II_ICHITREND6.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_MOMENTUM1200 ON IND.ID=II_MOMENTUM1200.ID_INDIVIDUO AND II_MOMENTUM1200.ID_INDICADOR='MOMENTUM1200' AND II_MOMENTUM1200.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_RSI84 ON IND.ID=II_RSI84.ID_INDIVIDUO AND II_RSI84.ID_INDICADOR='RSI84' AND II_RSI84.TIPO='OPEN' "
				+ "  INNER JOIN INDICADOR_INDIVIDUO II_SAR1200 ON IND.ID=II_SAR1200.ID_INDIVIDUO AND II_SAR1200.ID_INDICADOR='SAR1200' AND II_SAR1200.TIPO='OPEN' "
				+ " WHERE IND.ID = '" + idIndividuo + "'";
		Statement statement = null;
		ResultSet resultado = null;

		try {
			statement = this.connection.createStatement();
			statement.execute(sql);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(statement);
		}
	}

	public void insertarIndividuoIndicadoresColumnas(String idIndividuo) throws SQLException {
		String sql = "INSERT INTO INDIVIDUO_INDICADORES(" + "  	ID, PARENT_ID_1, PARENT_ID_2, TAKE_PROFIT, STOP_LOSS, "
				+ "  	LOTE, INITIAL_BALANCE, CREATION_DATE, TIPO_OPERACION,TIPO_INDIVIDUO,MONEDA,"
				+ "  	INFERIOR_MA,SUPERIOR_MA,INFERIOR_MACD,SUPERIOR_MACD,INFERIOR_COMPARE,SUPERIOR_COMPARE,INFERIOR_ADX,SUPERIOR_ADX,INFERIOR_BOLLINGER,SUPERIOR_BOLLINGER,INFERIOR_ICHISIGNAL,SUPERIOR_ICHISIGNAL,INFERIOR_ICHITREND,SUPERIOR_ICHITREND,INFERIOR_MOMENTUM,SUPERIOR_MOMENTUM,INFERIOR_RSI,SUPERIOR_RSI,INFERIOR_SAR,SUPERIOR_SAR,INFERIOR_MA1200,SUPERIOR_MA1200,INFERIOR_MACD20X,SUPERIOR_MACD20X,INFERIOR_COMPARE1200,SUPERIOR_COMPARE1200,INFERIOR_ADX168,SUPERIOR_ADX168,INFERIOR_BOLLINGER240,SUPERIOR_BOLLINGER240,INFERIOR_ICHISIGNAL6,SUPERIOR_ICHISIGNAL6,INFERIOR_ICHITREND6,SUPERIOR_ICHITREND6,INFERIOR_MOMENTUM1200,SUPERIOR_MOMENTUM1200,INFERIOR_RSI84,SUPERIOR_RSI84,INFERIOR_SAR1200,SUPERIOR_SAR1200)"
				+ "  SELECT IND.ID, IND.PARENT_ID_1, IND.PARENT_ID_2, IND.TAKE_PROFIT, IND.STOP_LOSS, "
				+ "  IND.LOTE, IND.INITIAL_BALANCE, IND.CREATION_DATE, IND.TIPO_OPERACION, IND.TIPO_INDIVIDUO, IND.MONEDA,"
				+ "  II_MA.INTERVALO_INFERIOR INFERIOR_MA, II_MA.INTERVALO_SUPERIOR SUPERIOR_MA,"
				+ "  II_MACD.INTERVALO_INFERIOR INFERIOR_MACD, II_MACD.INTERVALO_SUPERIOR SUPERIOR_MACD,"
				+ "  II_COMPARE.INTERVALO_INFERIOR INFERIOR_COMPARE, II_COMPARE.INTERVALO_SUPERIOR SUPERIOR_COMPARE,"
				+ "  II_ADX.INTERVALO_INFERIOR INFERIOR_ADX, II_ADX.INTERVALO_SUPERIOR SUPERIOR_ADX,"
				+ "  II_BOLLINGER.INTERVALO_INFERIOR INFERIOR_BOLLINGER, II_BOLLINGER.INTERVALO_SUPERIOR SUPERIOR_BOLLINGER,"
				+ "  II_ICHISIGNAL.INTERVALO_INFERIOR INFERIOR_ICHISIGNAL, II_ICHISIGNAL.INTERVALO_SUPERIOR SUPERIOR_ICHISIGNAL,"
				+ "  II_ICHITREND.INTERVALO_INFERIOR INFERIOR_ICHITREND, II_ICHITREND.INTERVALO_SUPERIOR SUPERIOR_ICHITREND,"
				+ "  II_MOMENTUM.INTERVALO_INFERIOR INFERIOR_MOMENTUM, II_MOMENTUM.INTERVALO_SUPERIOR SUPERIOR_MOMENTUM,"
				+ "  II_RSI.INTERVALO_INFERIOR INFERIOR_RSI, II_RSI.INTERVALO_SUPERIOR SUPERIOR_RSI,"
				+ "  II_SAR.INTERVALO_INFERIOR INFERIOR_SAR, II_SAR.INTERVALO_SUPERIOR SUPERIOR_SAR,"
				+ "  II_MA1200.INTERVALO_INFERIOR INFERIOR_MA1200, II_MA1200.INTERVALO_SUPERIOR SUPERIOR_MA1200,"
				+ "  II_MACD20X.INTERVALO_INFERIOR INFERIOR_MACD20X, II_MACD20X.INTERVALO_SUPERIOR SUPERIOR_MACD20X,"
				+ "  II_COMPARE1200.INTERVALO_INFERIOR INFERIOR_COMPARE1200, II_COMPARE1200.INTERVALO_SUPERIOR SUPERIOR_COMPARE1200,"
				+ "  II_ADX168.INTERVALO_INFERIOR INFERIOR_ADX168, II_ADX168.INTERVALO_SUPERIOR SUPERIOR_ADX168,"
				+ "  II_BOLLINGER240.INTERVALO_INFERIOR INFERIOR_BOLLINGER240, II_BOLLINGER240.INTERVALO_SUPERIOR SUPERIOR_BOLLINGER240,"
				+ "  II_ICHISIGNAL6.INTERVALO_INFERIOR INFERIOR_ICHISIGNAL6, II_ICHISIGNAL6.INTERVALO_SUPERIOR SUPERIOR_ICHISIGNAL6,"
				+ "  II_ICHITREND6.INTERVALO_INFERIOR INFERIOR_ICHITREND6, II_ICHITREND6.INTERVALO_SUPERIOR SUPERIOR_ICHITREND6,"
				+ "  II_MOMENTUM1200.INTERVALO_INFERIOR INFERIOR_MOMENTUM1200, II_MOMENTUM1200.INTERVALO_SUPERIOR SUPERIOR_MOMENTUM1200,"
				+ "  II_RSI84.INTERVALO_INFERIOR INFERIOR_RSI84, II_RSI84.INTERVALO_SUPERIOR SUPERIOR_RSI84,"
				+ "  II_SAR1200.INTERVALO_INFERIOR INFERIOR_SAR1200, II_SAR1200.INTERVALO_SUPERIOR SUPERIOR_SAR1200                "
				+ "  FROM INDIVIDUO IND"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_MA ON IND.ID=II_MA.ID_INDIVIDUO AND II_MA.ID_INDICADOR='MA' AND II_MA.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_MACD ON IND.ID=II_MACD.ID_INDIVIDUO AND II_MACD.ID_INDICADOR='MACD' AND II_MACD.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_COMPARE ON IND.ID=II_COMPARE.ID_INDIVIDUO AND II_COMPARE.ID_INDICADOR='COMPARE_MA' AND II_COMPARE.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_ADX ON IND.ID=II_ADX.ID_INDIVIDUO AND II_ADX.ID_INDICADOR='ADX' AND II_ADX.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_BOLLINGER ON IND.ID=II_BOLLINGER.ID_INDIVIDUO AND II_BOLLINGER.ID_INDICADOR='BOLLINGER' AND II_BOLLINGER.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_ICHISIGNAL ON IND.ID=II_ICHISIGNAL.ID_INDIVIDUO AND II_ICHISIGNAL.ID_INDICADOR='ICHIMOKU_SIGNAL' AND II_ICHISIGNAL.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_ICHITREND ON IND.ID=II_ICHITREND.ID_INDIVIDUO AND II_ICHITREND.ID_INDICADOR='ICHIMOKU_TREND' AND II_ICHITREND.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_MOMENTUM ON IND.ID=II_MOMENTUM.ID_INDIVIDUO AND II_MOMENTUM.ID_INDICADOR='MOMENTUM' AND II_MOMENTUM.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_RSI ON IND.ID=II_RSI.ID_INDIVIDUO AND II_RSI.ID_INDICADOR='RSI' AND II_RSI.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_SAR ON IND.ID=II_SAR.ID_INDIVIDUO AND II_SAR.ID_INDICADOR='SAR' AND II_SAR.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_MA1200 ON IND.ID=II_MA1200.ID_INDIVIDUO AND II_MA1200.ID_INDICADOR='MA1200' AND II_MA1200.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_MACD20X ON IND.ID=II_MACD20X.ID_INDIVIDUO AND II_MACD20X.ID_INDICADOR='MACD20X' AND II_MACD20X.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_COMPARE1200 ON IND.ID=II_COMPARE1200.ID_INDIVIDUO AND II_COMPARE1200.ID_INDICADOR='COMPARE_MA1200' AND II_COMPARE1200.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_ADX168 ON IND.ID=II_ADX168.ID_INDIVIDUO AND II_ADX168.ID_INDICADOR='ADX168' AND II_ADX168.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_BOLLINGER240 ON IND.ID=II_BOLLINGER240.ID_INDIVIDUO AND II_BOLLINGER240.ID_INDICADOR='BOLLINGER240' AND II_BOLLINGER240.TIPO='OPEN'"
				+ " 	INNER JOIN INDICADOR_INDIVIDUO II_ICHISIGNAL6 ON IND.ID=II_ICHISIGNAL6.ID_INDIVIDUO AND II_ICHISIGNAL6.ID_INDICADOR='ICHIMOKU_SIGNAL6' AND II_ICHISIGNAL6.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_ICHITREND6 ON IND.ID=II_ICHITREND6.ID_INDIVIDUO AND II_ICHITREND6.ID_INDICADOR='ICHIMOKU_TREND6' AND II_ICHITREND6.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_MOMENTUM1200 ON IND.ID=II_MOMENTUM1200.ID_INDIVIDUO AND II_MOMENTUM1200.ID_INDICADOR='MOMENTUM1200' AND II_MOMENTUM1200.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_RSI84 ON IND.ID=II_RSI84.ID_INDIVIDUO AND II_RSI84.ID_INDICADOR='RSI84' AND II_RSI84.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO II_SAR1200 ON IND.ID=II_SAR1200.ID_INDIVIDUO AND II_SAR1200.ID_INDICADOR='SAR1200' AND II_SAR1200.TIPO='OPEN'                "
				+ "	WHERE IND.ID = ?"
				+ "	AND NOT EXISTS (SELECT INDINDIC.ID FROM INDIVIDUO_INDICADORES INDINDIC WHERE INDINDIC.ID=IND.ID)";

		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, idIndividuo);
			statement.executeUpdate();
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @param tipoProceso
	 * @return
	 * @throws SQLException
	 */
	public List<Individuo> consultarIndividuosPadreRepetidos(String tipoProceso) throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT * FROM (SELECT IND.ID ID_INDIVIDUO, IND.PARENT_ID_1 ID_INDIVIDUO_PADRE "
				+ " FROM INDIVIDUO IND "
				+ "WHERE IND.ID NOT IN (SELECT PR.ID_INDIVIDUO_PADRE FROM PROCESO_REPETIDOS PR WHERE TIPO_PROCESO=?)"
				+ " ORDER BY IND.ID DESC) " + " WHERE ROWNUM<1000";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, tipoProceso);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosStopLossInconsistente(int sl) throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT IND.* FROM INDIVIDUO IND " + " WHERE IND.STOP_LOSS<=? "
				+ " AND IND.TAKE_PROFIT>IND.STOP_LOSS*4 " + " AND EXISTS ( "
				+ " SELECT 1 FROM INDICADOR_INDIVIDUO II WHERE II.ID_INDIVIDUO=IND.ID AND II.TIPO='OPEN') "
				+ " AND ROWNUM<100";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setInt(1, sl);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosBase(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosStopLossInconsistente(int sl, String idIndividuo) throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT IND.* FROM INDIVIDUO IND " + " WHERE IND.STOP_LOSS<=? "
				+ " AND IND.TAKE_PROFIT>IND.STOP_LOSS*4 " + " AND EXISTS ( "
				+ " SELECT 1 FROM INDICADOR_INDIVIDUO II WHERE II.ID_INDIVIDUO=IND.ID AND II.TIPO='OPEN') "
				+ " AND IND.ID=?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setInt(1, sl);
			stmtConsulta.setString(2, idIndividuo);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosBase(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosCantidadLimite(double porcentajeLimite) throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT IND.* " + " FROM INDIVIDUO IND "
				+ " INNER JOIN (SELECT OPER.ID_INDIVIDUO, COUNT(*) CANT FROM FOREX.OPERACION OPER GROUP BY OPER.ID_INDIVIDUO) OP ON OP.ID_INDIVIDUO=IND.ID "
				+ " INNER JOIN (SELECT COUNT(*) CANT FROM DATOHISTORICO DH) PUNTOS ON 1=1 "
				+ " WHERE (OP.CANT/PUNTOS.CANT)>? ";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setDouble(1, porcentajeLimite);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosBase(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosCantidadLimite(double porcentajeLimite, String idIndividuo)
			throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT IND.* " + " FROM INDIVIDUO IND "
				+ " INNER JOIN (SELECT OPER.ID_INDIVIDUO, COUNT(*) CANT FROM FOREX.OPERACION OPER GROUP BY OPER.ID_INDIVIDUO) OP ON OP.ID_INDIVIDUO=IND.ID "
				+ " INNER JOIN (SELECT COUNT(*) CANT FROM DATOHISTORICO DH) PUNTOS ON 1=1 "
				+ " WHERE (OP.CANT/PUNTOS.CANT)>? AND IND.ID=? ";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setDouble(1, porcentajeLimite);
			stmtConsulta.setString(2, idIndividuo);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosBase(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosYaProcesadosSinOperaciones(Date fechaLimite) throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT IND.* FROM INDIVIDUO IND"
				+ " INNER JOIN PROCESO P ON P.ID_INDIVIDUO=IND.ID AND P.FECHA_HISTORICO>=?"
				+ " WHERE (SELECT COUNT(*) FROM OPERACION OPER WHERE OPER.ID_INDIVIDUO=IND.ID)<10"
				+ " AND ROWNUM<1000";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new java.sql.Timestamp(fechaLimite.getTime()));
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosBase(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosYaProcesadosSinOperaciones(Date fechaLimite, String idIndividuo)
			throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT IND.* FROM INDIVIDUO IND"
				+ " INNER JOIN PROCESO P ON P.ID_INDIVIDUO=IND.ID AND P.FECHA_HISTORICO>=?"
				+ " WHERE (SELECT COUNT(*) FROM OPERACION OPER WHERE OPER.ID_INDIVIDUO=IND.ID)<10" 
				+ " AND IND.ID=?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new java.sql.Timestamp(fechaLimite.getTime()));
			stmtConsulta.setString(2, idIndividuo);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosBase(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	/**
	 *
	 * @param idIndividuo
	 * @param causaBorrado
	 * @param idPadre
	 * @throws SQLException
	 */
	public void smartDelete(String idIndividuo, String causaBorrado, String idPadre) throws SQLException {
		CallableStatement cstmt = null;
		try {
			cstmt = this.connection.prepareCall("{call SMART_DELETE(?,?,?)}");
			cstmt.setString(1, idIndividuo);
			cstmt.setString(2, causaBorrado);
			cstmt.setString(3, idPadre);
			cstmt.execute();
		} finally {
			JDBCUtil.close(cstmt);
		}
	}

	/**
	 *
	 * @param individuoPadre
	 * @return
	 * @throws SQLException
	 */
	public List<Individuo> consultarIndividuosRepetidos(Individuo individuoPadre) throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT ID_INDIVIDUO2 ID_INDIVIDUO, ID_INDIVIDUO1 ID_INDIVIDUO_PADRE "
				+ " FROM INDIVIDUOS_REPETIDOS_OPER"
				// + "INDIVIDUOS_REPETIDOS "
				+ " WHERE ID_INDIVIDUO1 = ? AND ROWNUM < 100";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, individuoPadre.getId());
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return list;
	}

	public Individuo consultarIndividuo(String idIndividuo) throws SQLException {
		Individuo individuo = null;
		String sql = "SELECT IND.ID ID_INDIVIDUO, IND.PARENT_ID_1 ID_INDIVIDUO_PADRE FROM INDIVIDUO IND WHERE IND.ID = ?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			resultado = stmtConsulta.executeQuery();

			List<Individuo> list = IndividuoHelper.createIndividuosById(resultado);
			if (!list.isEmpty()) {
				individuo = list.get(0);
			}
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return individuo;
	}

	public List<Individuo> consultarIndividuoHijoRepetido(Individuo individuoHijo) throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT ID_INDIVIDUO2 ID_INDIVIDUO, ID_INDIVIDUO1 ID_INDIVIDUO_PADRE "
				+ " FROM INDIVIDUOS_REPETIDOS_OPER"
				// + "INDIVIDUOS_REPETIDOS "
				+ " WHERE ID_INDIVIDUO2 = ?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, individuoHijo.getId());
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return list;
	}

	/**
	 *
	 * @param individuo
	 * @throws SQLException
	 */
	public void consultarDetalleIndividuoProceso(Individuo individuo) throws SQLException {
		String sql = "SELECT IND2.ID ID_INDIVIDUO, IND2.PARENT_ID_1, IND2.PARENT_ID_2, IND2.TAKE_PROFIT, IND2.STOP_LOSS, "
				+ "IND2.LOTE, IND2.INITIAL_BALANCE, IND2.CREATION_DATE, IND2.TIPO_OPERACION TIPO_OPERACION_INDIVIDUO, IND_MAXIMOS.FECHA_HISTORICO,"
				+ "IIND3.ID_INDICADOR, IIND3.INTERVALO_INFERIOR, IIND3.INTERVALO_SUPERIOR, IIND3.TIPO TIPO_INDICADOR,"
				+ "OPER.FECHA_APERTURA, OPER.SPREAD, OPER.OPEN_PRICE, OPER.TIPO TIPO_OPERACION "
				+ " FROM INDIVIDUO IND2" + "  INNER JOIN ("
				+ "    SELECT IND.ID ID_INDIVIDUO, MAX(PROC.FECHA_HISTORICO) FECHA_HISTORICO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA"
				+ "    FROM INDIVIDUO IND " + "      LEFT JOIN PROCESO PROC ON IND.ID=PROC.ID_INDIVIDUO"
				+ "      LEFT JOIN OPERACION OPER ON IND.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_CIERRE IS NULL"
				+ "    WHERE "
				+ "    IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=(SELECT MAX(FECHA) FROM DATOHISTORICO))    "
				+ "    GROUP BY IND.ID) IND_MAXIMOS ON IND2.ID=IND_MAXIMOS.ID_INDIVIDUO"
				+ "  INNER JOIN INDICADOR_INDIVIDUO IIND3 ON IND2.ID=IIND3.ID_INDIVIDUO"
				+ "  LEFT JOIN OPERACION OPER ON IND2.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_APERTURA=IND_MAXIMOS.FECHA_APERTURA"
				+ " WHERE IND2.ID=?" + " ORDER BY IND2.ID DESC";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, individuo.getId());
			resultado = stmtConsulta.executeQuery();

			IndividuoHelper.detalleIndividuo(individuo, resultado);

		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
	}

	/**
	 *
	 * @param indicadorController
	 * @param individuo
	 * @throws SQLException
	 */
	public void consultarDetalleIndividuo(IndicadorController indicadorController, Individuo individuo)
			throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT IND2.ID ID_INDIVIDUO, IND2.PARENT_ID_1, IND2.PARENT_ID_2, IND2.TAKE_PROFIT, IND2.STOP_LOSS, ");
		sql.append("IND2.LOTE, IND2.INITIAL_BALANCE, IND2.CREATION_DATE, ");
		sql.append("IND3.ID_INDICADOR, IND3.INTERVALO_INFERIOR, IND3.INTERVALO_SUPERIOR, IND3.TIPO ");
		sql.append(" FROM INDIVIDUO IND2");
		sql.append("  INNER JOIN ");
		// sql.append(indicadorController.getNombreTabla());
		sql.append(" IND3 ON IND2.ID=IND3.ID_INDIVIDUO");
		sql.append(" WHERE IND2.ID=?");
		sql.append(" ORDER BY IND2.ID DESC");

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql.toString());
			stmtConsulta.setString(1, individuo.getId());
			resultado = stmtConsulta.executeQuery();

			IndividuoHelper.detalleIndividuo(individuo, resultado, indicadorController);

		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
	}

	/**
	 *
	 * @param individuo
	 * @throws SQLException
	 */
	public void insertIndividuo(IndividuoEstrategia individuo) throws SQLException {
		String sql = "INSERT INTO INDIVIDUO(ID, PARENT_ID_1, PARENT_ID_2, "
				+ " TAKE_PROFIT, STOP_LOSS, LOTE, INITIAL_BALANCE, CREATION_DATE, "
				+ " TIPO_OPERACION, TIPO_INDIVIDUO, MONEDA) " + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, individuo.getId());
			statement.setString(2, individuo.getIdParent1());
			statement.setString(3, individuo.getIdParent2());
			statement.setDouble(4, individuo.getTakeProfit());
			statement.setDouble(5, individuo.getStopLoss());
			statement.setDouble(6, individuo.getLot());
			statement.setDouble(7, individuo.getInitialBalance());
			if (individuo.getCreationDate() != null) {
				statement.setTimestamp(8, new java.sql.Timestamp(individuo.getCreationDate().getTime()));
			} else {
				statement.setNull(8, java.sql.Types.DATE);
			}
			if (individuo.getTipoOperacion() != null) {
				statement.setString(9, individuo.getTipoOperacion().name());
			} else {
				statement.setString(9, Constants.OperationType.SELL.name());
			}
			statement.setString(10, individuo.getTipoIndividuo());
			statement.setString(11, individuo.getMoneda().getMoneda());

			statement.executeUpdate();
		} finally {
			JDBCUtil.close(statement);
		}

	}

	/**
	 *
	 * @param indicadorController
	 * @param individuo
	 * @throws SQLException
	 */
	public void insertIndicadorIndividuo(IndicadorController indicadorController, IndividuoEstrategia individuo)
			throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(indicadorController.getNombreTabla());
		sql.append(" (ID_INDICADOR, ID_INDIVIDUO, INTERVALO_INFERIOR, INTERVALO_SUPERIOR, TIPO) ");
		sql.append(" VALUES (?,?,?,?, ?)");

		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql.toString());
			for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
				IndicadorManager<?> indicatorManager = indicadorController.getManagerInstance(i);
				IntervalIndicator indicator = null;
				if (individuo.getOpenIndicators().size() > i) {
					indicator = (IntervalIndicator) individuo.getOpenIndicators().get(i);
				}
				statement.setString(1, indicatorManager.getId());
				statement.setString(2, individuo.getId());
				if ((indicator != null) && (!Double.isNaN(indicator.getInterval().getLowInterval()))
						&& (!Double.isNaN(indicator.getInterval().getHighInterval()))) {
					statement.setDouble(3, NumberUtil.round(indicator.getInterval().getLowInterval()));
					statement.setDouble(4, NumberUtil.round(indicator.getInterval().getHighInterval()));
				} else {
					statement.setNull(3, java.sql.Types.DOUBLE);
					statement.setNull(4, java.sql.Types.DOUBLE);
				}
				statement.setString(5, "OPEN");
				statement.executeUpdate();

				indicator = null;
				if (individuo.getCloseIndicators().size() > i) {
					indicator = (IntervalIndicator) individuo.getCloseIndicators().get(i);
				}
				statement.setString(1, indicatorManager.getId());
				statement.setString(2, individuo.getId());
				if ((indicator != null) && (!Double.isNaN(indicator.getInterval().getLowInterval()))
						&& (!Double.isNaN(indicator.getInterval().getHighInterval()))) {
					statement.setDouble(3, NumberUtil.round(indicator.getInterval().getLowInterval()));
					statement.setDouble(4, NumberUtil.round(indicator.getInterval().getHighInterval()));
				} else {
					statement.setNull(3, java.sql.Types.DOUBLE);
					statement.setNull(4, java.sql.Types.DOUBLE);
				}
				statement.setString(5, "CLOSE");
				statement.executeUpdate();
			}
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @return @throws SQLException
	 */
	public List<IndividuoOptimo> consultarIndividuosOptimos() throws SQLException {
		List<IndividuoOptimo> list = null;

		StringBuilder sql = new StringBuilder();
		sql.append(PropertiesManager.getQueryIndividuosOptimos());
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql.toString());
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosOptimos(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	/**
	 *
	 * @param individuo
	 * @return
	 * @throws SQLException
	 */
	public int getCountIndicadoresOpen(Individuo individuo) throws SQLException {
		int count = 0;
		String sql = "SELECT COUNT(*) FROM INDICADOR_INDIVIDUO II "
				+ " WHERE II.TIPO='OPEN' AND II.INTERVALO_INFERIOR IS NOT NULL " + " AND II.ID_INDIVIDUO=?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, individuo.getId());
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				count = resultado.getInt(1);
			}
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return count;
	}

}
