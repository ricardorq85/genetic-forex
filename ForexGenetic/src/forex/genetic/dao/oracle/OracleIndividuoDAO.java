package forex.genetic.dao.oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.IIndividuoDAO;
import forex.genetic.dao.helper.IndividuoHelper;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.util.Constants;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.RandomUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class OracleIndividuoDAO extends OracleGeneticDAO<IndividuoEstrategia>
		implements IIndividuoDAO<IndividuoEstrategia> {

	public OracleIndividuoDAO(Connection connection) {
		super(connection);
	}
	public void crearVistaIndicadoresIndividuo(String viewName, String idIndividuo) throws GeneticDAOException {
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
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(statement);
		}
	}

	public void insertarIndividuoIndicadoresColumnas(String idIndividuo) throws GeneticDAOException {
		String sql = "INSERT INTO INDIVIDUO_INDICADORES(ID, PARENT_ID_1, PARENT_ID_2, TAKE_PROFIT, STOP_LOSS, "
				+ "  	LOTE, INITIAL_BALANCE, CREATION_DATE, TIPO_OPERACION,TIPO_INDIVIDUO,MONEDA,"
				+ "  	OPEN_INFERIOR_MA,OPEN_SUPERIOR_MA,OPEN_INFERIOR_MACD,OPEN_SUPERIOR_MACD,OPEN_INFERIOR_COMPARE,OPEN_SUPERIOR_COMPARE,OPEN_INFERIOR_ADX,OPEN_SUPERIOR_ADX,OPEN_INFERIOR_BOLLINGER,OPEN_SUPERIOR_BOLLINGER,OPEN_INFERIOR_ICHISIGNAL,OPEN_SUPERIOR_ICHISIGNAL,OPEN_INFERIOR_ICHITREND,OPEN_SUPERIOR_ICHITREND,OPEN_INFERIOR_MOMENTUM,OPEN_SUPERIOR_MOMENTUM,OPEN_INFERIOR_RSI,OPEN_SUPERIOR_RSI,OPEN_INFERIOR_SAR,OPEN_SUPERIOR_SAR,OPEN_INFERIOR_MA1200,OPEN_SUPERIOR_MA1200,OPEN_INFERIOR_MACD20X,OPEN_SUPERIOR_MACD20X,OPEN_INFERIOR_COMPARE1200,OPEN_SUPERIOR_COMPARE1200,OPEN_INFERIOR_ADX168,OPEN_SUPERIOR_ADX168,OPEN_INFERIOR_BOLLINGER240,OPEN_SUPERIOR_BOLLINGER240,OPEN_INFERIOR_ICHISIGNAL6,OPEN_SUPERIOR_ICHISIGNAL6,OPEN_INFERIOR_ICHITREND6,OPEN_SUPERIOR_ICHITREND6,OPEN_INFERIOR_MOMENTUM1200,OPEN_SUPERIOR_MOMENTUM1200,OPEN_INFERIOR_RSI84,OPEN_SUPERIOR_RSI84,OPEN_INFERIOR_SAR1200,OPEN_SUPERIOR_SAR1200, "
				+ "		CLOSE_INFERIOR_MA,CLOSE_SUPERIOR_MA,CLOSE_INFERIOR_MACD,CLOSE_SUPERIOR_MACD,CLOSE_INFERIOR_COMPARE,CLOSE_SUPERIOR_COMPARE,CLOSE_INFERIOR_ADX,CLOSE_SUPERIOR_ADX,CLOSE_INFERIOR_BOLLINGER,CLOSE_SUPERIOR_BOLLINGER,CLOSE_INFERIOR_ICHISIGNAL,CLOSE_SUPERIOR_ICHISIGNAL,CLOSE_INFERIOR_ICHITREND,CLOSE_SUPERIOR_ICHITREND,CLOSE_INFERIOR_MOMENTUM,CLOSE_SUPERIOR_MOMENTUM,CLOSE_INFERIOR_RSI,CLOSE_SUPERIOR_RSI,CLOSE_INFERIOR_SAR,CLOSE_SUPERIOR_SAR,CLOSE_INFERIOR_MA1200,CLOSE_SUPERIOR_MA1200,CLOSE_INFERIOR_MACD20X,CLOSE_SUPERIOR_MACD20X,CLOSE_INFERIOR_COMPARE1200,CLOSE_SUPERIOR_COMPARE1200,CLOSE_INFERIOR_ADX168,CLOSE_SUPERIOR_ADX168,CLOSE_INFERIOR_BOLLINGER240,CLOSE_SUPERIOR_BOLLINGER240,CLOSE_INFERIOR_ICHISIGNAL6,CLOSE_SUPERIOR_ICHISIGNAL6,CLOSE_INFERIOR_ICHITREND6,CLOSE_SUPERIOR_ICHITREND6,CLOSE_INFERIOR_MOMENTUM1200,CLOSE_SUPERIOR_MOMENTUM1200,CLOSE_INFERIOR_RSI84,CLOSE_SUPERIOR_RSI84,CLOSE_INFERIOR_SAR1200,CLOSE_SUPERIOR_SAR1200) "
				+ "  SELECT IND.ID, IND.PARENT_ID_1, IND.PARENT_ID_2, IND.TAKE_PROFIT, IND.STOP_LOSS, "
				+ "  IND.LOTE, IND.INITIAL_BALANCE, IND.CREATION_DATE, IND.TIPO_OPERACION, IND.TIPO_INDIVIDUO, IND.MONEDA,"
				+ "  OPEN_II_MA.INTERVALO_INFERIOR OPEN_INFERIOR_MA, OPEN_II_MA.INTERVALO_SUPERIOR OPEN_SUPERIOR_MA,"
				+ "  OPEN_II_MACD.INTERVALO_INFERIOR OPEN_INFERIOR_MACD, OPEN_II_MACD.INTERVALO_SUPERIOR OPEN_SUPERIOR_MACD,"
				+ "  OPEN_II_COMPARE.INTERVALO_INFERIOR OPEN_INFERIOR_COMPARE, OPEN_II_COMPARE.INTERVALO_SUPERIOR OPEN_SUPERIOR_COMPARE,"
				+ "  OPEN_II_ADX.INTERVALO_INFERIOR OPEN_INFERIOR_ADX, OPEN_II_ADX.INTERVALO_SUPERIOR OPEN_SUPERIOR_ADX,"
				+ "  OPEN_II_BOLLINGER.INTERVALO_INFERIOR OPEN_INFERIOR_BOLLINGER, OPEN_II_BOLLINGER.INTERVALO_SUPERIOR OPEN_SUPERIOR_BOLLINGER,"
				+ "  OPEN_II_ICHISIGNAL.INTERVALO_INFERIOR OPEN_INFERIOR_ICHISIGNAL, OPEN_II_ICHISIGNAL.INTERVALO_SUPERIOR OPEN_SUPERIOR_ICHISIGNAL,"
				+ "  OPEN_II_ICHITREND.INTERVALO_INFERIOR OPEN_INFERIOR_ICHITREND, OPEN_II_ICHITREND.INTERVALO_SUPERIOR OPEN_SUPERIOR_ICHITREND,"
				+ "  OPEN_II_MOMENTUM.INTERVALO_INFERIOR OPEN_INFERIOR_MOMENTUM, OPEN_II_MOMENTUM.INTERVALO_SUPERIOR OPEN_SUPERIOR_MOMENTUM,"
				+ "  OPEN_II_RSI.INTERVALO_INFERIOR OPEN_INFERIOR_RSI, OPEN_II_RSI.INTERVALO_SUPERIOR OPEN_SUPERIOR_RSI,"
				+ "  OPEN_II_SAR.INTERVALO_INFERIOR OPEN_INFERIOR_SAR, OPEN_II_SAR.INTERVALO_SUPERIOR OPEN_SUPERIOR_SAR,"
				+ "  OPEN_II_MA1200.INTERVALO_INFERIOR OPEN_INFERIOR_MA1200, OPEN_II_MA1200.INTERVALO_SUPERIOR OPEN_SUPERIOR_MA1200,"
				+ "  OPEN_II_MACD20X.INTERVALO_INFERIOR OPEN_INFERIOR_MACD20X, OPEN_II_MACD20X.INTERVALO_SUPERIOR OPEN_SUPERIOR_MACD20X,"
				+ "  OPEN_II_COMPARE1200.INTERVALO_INFERIOR OPEN_INFERIOR_COMPARE1200, OPEN_II_COMPARE1200.INTERVALO_SUPERIOR OPEN_SUPERIOR_COMPARE1200,"
				+ "  OPEN_II_ADX168.INTERVALO_INFERIOR OPEN_INFERIOR_ADX168, OPEN_II_ADX168.INTERVALO_SUPERIOR OPEN_SUPERIOR_ADX168,"
				+ "  OPEN_II_BOLLINGER240.INTERVALO_INFERIOR OPEN_INFERIOR_BOLLINGER240, OPEN_II_BOLLINGER240.INTERVALO_SUPERIOR OPEN_SUPERIOR_BOLLINGER240,"
				+ "  OPEN_II_ICHISIGNAL6.INTERVALO_INFERIOR OPEN_INFERIOR_ICHISIGNAL6, OPEN_II_ICHISIGNAL6.INTERVALO_SUPERIOR OPEN_SUPERIOR_ICHISIGNAL6,"
				+ "  OPEN_II_ICHITREND6.INTERVALO_INFERIOR OPEN_INFERIOR_ICHITREND6, OPEN_II_ICHITREND6.INTERVALO_SUPERIOR OPEN_SUPERIOR_ICHITREND6,"
				+ "  OPEN_II_MOMENTUM1200.INTERVALO_INFERIOR OPEN_INFERIOR_MOMENTUM1200, OPEN_II_MOMENTUM1200.INTERVALO_SUPERIOR OPEN_SUPERIOR_MOMENTUM1200,"
				+ "  OPEN_II_RSI84.INTERVALO_INFERIOR OPEN_INFERIOR_RSI84, OPEN_II_RSI84.INTERVALO_SUPERIOR OPEN_SUPERIOR_RSI84,"
				+ "  OPEN_II_SAR1200.INTERVALO_INFERIOR OPEN_INFERIOR_SAR1200, OPEN_II_SAR1200.INTERVALO_SUPERIOR OPEN_SUPERIOR_SAR1200,  "
				+ "  CLOSE_II_MA.INTERVALO_INFERIOR CLOSE_INFERIOR_MA, CLOSE_II_MA.INTERVALO_SUPERIOR CLOSE_SUPERIOR_MA,"
				+ "  CLOSE_II_MACD.INTERVALO_INFERIOR CLOSE_INFERIOR_MACD, CLOSE_II_MACD.INTERVALO_SUPERIOR CLOSE_SUPERIOR_MACD,"
				+ "  CLOSE_II_COMPARE.INTERVALO_INFERIOR CLOSE_INFERIOR_COMPARE, CLOSE_II_COMPARE.INTERVALO_SUPERIOR CLOSE_SUPERIOR_COMPARE,"
				+ "  CLOSE_II_ADX.INTERVALO_INFERIOR CLOSE_INFERIOR_ADX, CLOSE_II_ADX.INTERVALO_SUPERIOR CLOSE_SUPERIOR_ADX,"
				+ "  CLOSE_II_BOLLINGER.INTERVALO_INFERIOR CLOSE_INFERIOR_BOLLINGER, CLOSE_II_BOLLINGER.INTERVALO_SUPERIOR CLOSE_SUPERIOR_BOLLINGER,"
				+ "  CLOSE_II_ICHISIGNAL.INTERVALO_INFERIOR CLOSE_INFERIOR_ICHISIGNAL, CLOSE_II_ICHISIGNAL.INTERVALO_SUPERIOR CLOSE_SUPERIOR_ICHISIGNAL,"
				+ "  CLOSE_II_ICHITREND.INTERVALO_INFERIOR CLOSE_INFERIOR_ICHITREND, CLOSE_II_ICHITREND.INTERVALO_SUPERIOR CLOSE_SUPERIOR_ICHITREND,"
				+ "  CLOSE_II_MOMENTUM.INTERVALO_INFERIOR CLOSE_INFERIOR_MOMENTUM, CLOSE_II_MOMENTUM.INTERVALO_SUPERIOR CLOSE_SUPERIOR_MOMENTUM,"
				+ "  CLOSE_II_RSI.INTERVALO_INFERIOR CLOSE_INFERIOR_RSI, CLOSE_II_RSI.INTERVALO_SUPERIOR CLOSE_SUPERIOR_RSI,"
				+ "  CLOSE_II_SAR.INTERVALO_INFERIOR CLOSE_INFERIOR_SAR, CLOSE_II_SAR.INTERVALO_SUPERIOR CLOSE_SUPERIOR_SAR,"
				+ "  CLOSE_II_MA1200.INTERVALO_INFERIOR CLOSE_INFERIOR_MA1200, CLOSE_II_MA1200.INTERVALO_SUPERIOR CLOSE_SUPERIOR_MA1200,"
				+ "  CLOSE_II_MACD20X.INTERVALO_INFERIOR CLOSE_INFERIOR_MACD20X, CLOSE_II_MACD20X.INTERVALO_SUPERIOR CLOSE_SUPERIOR_MACD20X,"
				+ "  CLOSE_II_COMPARE1200.INTERVALO_INFERIOR CLOSE_INFERIOR_COMPARE1200, CLOSE_II_COMPARE1200.INTERVALO_SUPERIOR CLOSE_SUPERIOR_COMPARE1200,"
				+ "  CLOSE_II_ADX168.INTERVALO_INFERIOR CLOSE_INFERIOR_ADX168, CLOSE_II_ADX168.INTERVALO_SUPERIOR CLOSE_SUPERIOR_ADX168,"
				+ "  CLOSE_II_BOLLINGER240.INTERVALO_INFERIOR CLOSE_INFERIOR_BOLLINGER240, CLOSE_II_BOLLINGER240.INTERVALO_SUPERIOR CLOSE_SUPERIOR_BOLLINGER240,"
				+ "  CLOSE_II_ICHISIGNAL6.INTERVALO_INFERIOR CLOSE_INFERIOR_ICHISIGNAL6, CLOSE_II_ICHISIGNAL6.INTERVALO_SUPERIOR CLOSE_SUPERIOR_ICHISIGNAL6,"
				+ "  CLOSE_II_ICHITREND6.INTERVALO_INFERIOR CLOSE_INFERIOR_ICHITREND6, CLOSE_II_ICHITREND6.INTERVALO_SUPERIOR CLOSE_SUPERIOR_ICHITREND6,"
				+ "  CLOSE_II_MOMENTUM1200.INTERVALO_INFERIOR CLOSE_INFERIOR_MOMENTUM1200, CLOSE_II_MOMENTUM1200.INTERVALO_SUPERIOR CLOSE_SUPERIOR_MOMENTUM1200,"
				+ "  CLOSE_II_RSI84.INTERVALO_INFERIOR CLOSE_INFERIOR_RSI84, CLOSE_II_RSI84.INTERVALO_SUPERIOR CLOSE_SUPERIOR_RSI84,"
				+ "  CLOSE_II_SAR1200.INTERVALO_INFERIOR CLOSE_INFERIOR_SAR1200, CLOSE_II_SAR1200.INTERVALO_SUPERIOR CLOSE_SUPERIOR_SAR1200                  "
				+ "  FROM INDIVIDUO IND"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_MA ON IND.ID=OPEN_II_MA.ID_INDIVIDUO AND OPEN_II_MA.ID_INDICADOR='MA' AND OPEN_II_MA.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_MACD ON IND.ID=OPEN_II_MACD.ID_INDIVIDUO AND OPEN_II_MACD.ID_INDICADOR='MACD' AND OPEN_II_MACD.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_COMPARE ON IND.ID=OPEN_II_COMPARE.ID_INDIVIDUO AND OPEN_II_COMPARE.ID_INDICADOR='COMPARE_MA' AND OPEN_II_COMPARE.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_ADX ON IND.ID=OPEN_II_ADX.ID_INDIVIDUO AND OPEN_II_ADX.ID_INDICADOR='ADX' AND OPEN_II_ADX.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_BOLLINGER ON IND.ID=OPEN_II_BOLLINGER.ID_INDIVIDUO AND OPEN_II_BOLLINGER.ID_INDICADOR='BOLLINGER' AND OPEN_II_BOLLINGER.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_ICHISIGNAL ON IND.ID=OPEN_II_ICHISIGNAL.ID_INDIVIDUO AND OPEN_II_ICHISIGNAL.ID_INDICADOR='ICHIMOKU_SIGNAL' AND OPEN_II_ICHISIGNAL.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_ICHITREND ON IND.ID=OPEN_II_ICHITREND.ID_INDIVIDUO AND OPEN_II_ICHITREND.ID_INDICADOR='ICHIMOKU_TREND' AND OPEN_II_ICHITREND.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_MOMENTUM ON IND.ID=OPEN_II_MOMENTUM.ID_INDIVIDUO AND OPEN_II_MOMENTUM.ID_INDICADOR='MOMENTUM' AND OPEN_II_MOMENTUM.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_RSI ON IND.ID=OPEN_II_RSI.ID_INDIVIDUO AND OPEN_II_RSI.ID_INDICADOR='RSI' AND OPEN_II_RSI.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_SAR ON IND.ID=OPEN_II_SAR.ID_INDIVIDUO AND OPEN_II_SAR.ID_INDICADOR='SAR' AND OPEN_II_SAR.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_MA1200 ON IND.ID=OPEN_II_MA1200.ID_INDIVIDUO AND OPEN_II_MA1200.ID_INDICADOR='MA1200' AND OPEN_II_MA1200.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_MACD20X ON IND.ID=OPEN_II_MACD20X.ID_INDIVIDUO AND OPEN_II_MACD20X.ID_INDICADOR='MACD20X' AND OPEN_II_MACD20X.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_COMPARE1200 ON IND.ID=OPEN_II_COMPARE1200.ID_INDIVIDUO AND OPEN_II_COMPARE1200.ID_INDICADOR='COMPARE_MA1200' AND OPEN_II_COMPARE1200.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_ADX168 ON IND.ID=OPEN_II_ADX168.ID_INDIVIDUO AND OPEN_II_ADX168.ID_INDICADOR='ADX168' AND OPEN_II_ADX168.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_BOLLINGER240 ON IND.ID=OPEN_II_BOLLINGER240.ID_INDIVIDUO AND OPEN_II_BOLLINGER240.ID_INDICADOR='BOLLINGER240' AND OPEN_II_BOLLINGER240.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_ICHISIGNAL6 ON IND.ID=OPEN_II_ICHISIGNAL6.ID_INDIVIDUO AND OPEN_II_ICHISIGNAL6.ID_INDICADOR='ICHIMOKU_SIGNAL6' AND OPEN_II_ICHISIGNAL6.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_ICHITREND6 ON IND.ID=OPEN_II_ICHITREND6.ID_INDIVIDUO AND OPEN_II_ICHITREND6.ID_INDICADOR='ICHIMOKU_TREND6' AND OPEN_II_ICHITREND6.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_MOMENTUM1200 ON IND.ID=OPEN_II_MOMENTUM1200.ID_INDIVIDUO AND OPEN_II_MOMENTUM1200.ID_INDICADOR='MOMENTUM1200' AND OPEN_II_MOMENTUM1200.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_RSI84 ON IND.ID=OPEN_II_RSI84.ID_INDIVIDUO AND OPEN_II_RSI84.ID_INDICADOR='RSI84' AND OPEN_II_RSI84.TIPO='OPEN'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO OPEN_II_SAR1200 ON IND.ID=OPEN_II_SAR1200.ID_INDIVIDUO AND OPEN_II_SAR1200.ID_INDICADOR='SAR1200' AND OPEN_II_SAR1200.TIPO='OPEN'                "
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_MA ON IND.ID=CLOSE_II_MA.ID_INDIVIDUO AND CLOSE_II_MA.ID_INDICADOR='MA' AND CLOSE_II_MA.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_MACD ON IND.ID=CLOSE_II_MACD.ID_INDIVIDUO AND CLOSE_II_MACD.ID_INDICADOR='MACD' AND CLOSE_II_MACD.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_COMPARE ON IND.ID=CLOSE_II_COMPARE.ID_INDIVIDUO AND CLOSE_II_COMPARE.ID_INDICADOR='COMPARE_MA' AND CLOSE_II_COMPARE.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_ADX ON IND.ID=CLOSE_II_ADX.ID_INDIVIDUO AND CLOSE_II_ADX.ID_INDICADOR='ADX' AND CLOSE_II_ADX.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_BOLLINGER ON IND.ID=CLOSE_II_BOLLINGER.ID_INDIVIDUO AND CLOSE_II_BOLLINGER.ID_INDICADOR='BOLLINGER' AND CLOSE_II_BOLLINGER.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_ICHISIGNAL ON IND.ID=CLOSE_II_ICHISIGNAL.ID_INDIVIDUO AND CLOSE_II_ICHISIGNAL.ID_INDICADOR='ICHIMOKU_SIGNAL' AND CLOSE_II_ICHISIGNAL.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_ICHITREND ON IND.ID=CLOSE_II_ICHITREND.ID_INDIVIDUO AND CLOSE_II_ICHITREND.ID_INDICADOR='ICHIMOKU_TREND' AND CLOSE_II_ICHITREND.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_MOMENTUM ON IND.ID=CLOSE_II_MOMENTUM.ID_INDIVIDUO AND CLOSE_II_MOMENTUM.ID_INDICADOR='MOMENTUM' AND CLOSE_II_MOMENTUM.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_RSI ON IND.ID=CLOSE_II_RSI.ID_INDIVIDUO AND CLOSE_II_RSI.ID_INDICADOR='RSI' AND CLOSE_II_RSI.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_SAR ON IND.ID=CLOSE_II_SAR.ID_INDIVIDUO AND CLOSE_II_SAR.ID_INDICADOR='SAR' AND CLOSE_II_SAR.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_MA1200 ON IND.ID=CLOSE_II_MA1200.ID_INDIVIDUO AND CLOSE_II_MA1200.ID_INDICADOR='MA1200' AND CLOSE_II_MA1200.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_MACD20X ON IND.ID=CLOSE_II_MACD20X.ID_INDIVIDUO AND CLOSE_II_MACD20X.ID_INDICADOR='MACD20X' AND CLOSE_II_MACD20X.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_COMPARE1200 ON IND.ID=CLOSE_II_COMPARE1200.ID_INDIVIDUO AND CLOSE_II_COMPARE1200.ID_INDICADOR='COMPARE_MA1200' AND CLOSE_II_COMPARE1200.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_ADX168 ON IND.ID=CLOSE_II_ADX168.ID_INDIVIDUO AND CLOSE_II_ADX168.ID_INDICADOR='ADX168' AND CLOSE_II_ADX168.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_BOLLINGER240 ON IND.ID=CLOSE_II_BOLLINGER240.ID_INDIVIDUO AND CLOSE_II_BOLLINGER240.ID_INDICADOR='BOLLINGER240' AND CLOSE_II_BOLLINGER240.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_ICHISIGNAL6 ON IND.ID=CLOSE_II_ICHISIGNAL6.ID_INDIVIDUO AND CLOSE_II_ICHISIGNAL6.ID_INDICADOR='ICHIMOKU_SIGNAL6' AND CLOSE_II_ICHISIGNAL6.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_ICHITREND6 ON IND.ID=CLOSE_II_ICHITREND6.ID_INDIVIDUO AND CLOSE_II_ICHITREND6.ID_INDICADOR='ICHIMOKU_TREND6' AND CLOSE_II_ICHITREND6.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_MOMENTUM1200 ON IND.ID=CLOSE_II_MOMENTUM1200.ID_INDIVIDUO AND CLOSE_II_MOMENTUM1200.ID_INDICADOR='MOMENTUM1200' AND CLOSE_II_MOMENTUM1200.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_RSI84 ON IND.ID=CLOSE_II_RSI84.ID_INDIVIDUO AND CLOSE_II_RSI84.ID_INDICADOR='RSI84' AND CLOSE_II_RSI84.TIPO='CLOSE'"
				+ "		INNER JOIN INDICADOR_INDIVIDUO CLOSE_II_SAR1200 ON IND.ID=CLOSE_II_SAR1200.ID_INDIVIDUO AND CLOSE_II_SAR1200.ID_INDICADOR='SAR1200' AND CLOSE_II_SAR1200.TIPO='CLOSE'    "
				+ "	WHERE IND.ID = ?"
				+ "	AND NOT EXISTS (SELECT INDINDIC.ID FROM INDIVIDUO_INDICADORES INDINDIC WHERE INDINDIC.ID=IND.ID)";
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, idIndividuo);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
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
	public List<Individuo> consultarIndividuosPadreRepetidos(String tipoProceso) throws GeneticDAOException {
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
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosStopLossInconsistente(int sl) throws GeneticDAOException {
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
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosStopLossInconsistente(int sl, String idIndividuo)
			throws GeneticDAOException {
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
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosCantidadLimite(double porcentajeLimite) throws GeneticDAOException {
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
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosCantidadLimite(double porcentajeLimite, String idIndividuo)
			throws GeneticDAOException {
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
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
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
	public void smartDelete(String idIndividuo, String causaBorrado, String idPadre) throws GeneticDAOException {
		CallableStatement cstmt = null;
		try {
			cstmt = this.connection.prepareCall("{call SMART_DELETE(?,?,?)}");
			cstmt.setString(1, idIndividuo);
			cstmt.setString(2, causaBorrado);
			cstmt.setString(3, idPadre);
			cstmt.execute();
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
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
	public List<Individuo> consultarIndividuosRepetidosOperaciones(Individuo individuoPadre)
			throws GeneticDAOException {
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
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return list;
	}

	public List<Individuo> consultarIndividuosRepetidos() throws GeneticDAOException {
		List<Individuo> list = null;
		String sql = "SELECT ID_INDIVIDUO2 ID_INDIVIDUO, ID_INDIVIDUO1 ID_INDIVIDUO_PADRE "
				+ " FROM INDIVIDUOS_REPETIDOS WHERE ROWNUM < 300";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return list;
	}

	public Individuo consultarIndividuo(String idIndividuo) throws GeneticDAOException {
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
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return individuo;
	}

	public List<Individuo> consultarIndividuoHijoRepetidoOperaciones(Individuo individuoHijo)
			throws GeneticDAOException {
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
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return list;
	}

	public List<Individuo> consultarIndividuoHijoRepetido(Individuo individuoHijo) throws GeneticDAOException {
		List<Individuo> list = null;
		String sql = "SELECT ID_INDIVIDUO2 ID_INDIVIDUO, ID_INDIVIDUO1 ID_INDIVIDUO_PADRE "
				+ " FROM INDIVIDUOS_REPETIDOS " + " WHERE ID_INDIVIDUO2 = ?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, individuoHijo.getId());
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
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
	public void consultarDetalleIndividuoProceso(Individuo individuo, Date fechaHistorico) throws GeneticDAOException {
		String sql = "SELECT IND2.ID ID_INDIVIDUO, IND2.PARENT_ID_1, IND2.PARENT_ID_2, IND2.TAKE_PROFIT, IND2.STOP_LOSS, "
				+ "IND2.LOTE, IND2.INITIAL_BALANCE, IND2.CREATION_DATE, IND2.TIPO_OPERACION TIPO_OPERACION_INDIVIDUO, IND_MAXIMOS.FECHA_HISTORICO,"
				+ "IIND3.ID_INDICADOR, IIND3.INTERVALO_INFERIOR, IIND3.INTERVALO_SUPERIOR, IIND3.TIPO TIPO_INDICADOR,"
				+ "OPER.FECHA_APERTURA, OPER.SPREAD, OPER.OPEN_PRICE, OPER.TIPO TIPO_OPERACION "
				+ " FROM INDIVIDUO IND2" + "  INNER JOIN ("
				+ "    SELECT IND.ID ID_INDIVIDUO, MAX(PROC.FECHA_HISTORICO) FECHA_HISTORICO, MAX(OPER.FECHA_APERTURA) FECHA_APERTURA"
				+ "    FROM INDIVIDUO IND " + "      LEFT JOIN PROCESO PROC ON IND.ID=PROC.ID_INDIVIDUO"
				+ "      LEFT JOIN OPERACION OPER ON IND.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_CIERRE IS NULL"
				+ "    WHERE "
				+ "    IND.ID NOT IN (SELECT DISTINCT PRO.ID_INDIVIDUO FROM PROCESO PRO WHERE PRO.FECHA_HISTORICO=?)    "
				+ "    GROUP BY IND.ID) IND_MAXIMOS ON IND2.ID=IND_MAXIMOS.ID_INDIVIDUO"
				+ "  INNER JOIN INDICADOR_INDIVIDUO IIND3 ON IND2.ID=IIND3.ID_INDIVIDUO"
				+ "  LEFT JOIN OPERACION OPER ON IND2.ID=OPER.ID_INDIVIDUO AND OPER.FECHA_APERTURA=IND_MAXIMOS.FECHA_APERTURA"
				+ " WHERE IND2.ID=?" + " ORDER BY IND2.ID DESC";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new java.sql.Timestamp(fechaHistorico.getTime()));
			stmtConsulta.setString(2, individuo.getId());
			resultado = stmtConsulta.executeQuery();

			IndividuoHelper.detalleIndividuo(individuo, resultado);

		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
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
			throws GeneticDAOException {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT IND2.ID ID_INDIVIDUO, IND2.PARENT_ID_1, IND2.PARENT_ID_2, IND2.TAKE_PROFIT, IND2.STOP_LOSS, ");
		sql.append(
				"IND2.LOTE, IND2.INITIAL_BALANCE, IND2.CREATION_DATE, IND2.TIPO_OPERACION TIPO_OPERACION_INDIVIDUO, ");
		sql.append(" IND3.ID_INDICADOR, IND3.INTERVALO_INFERIOR, IND3.INTERVALO_SUPERIOR, IND3.TIPO TIPO_INDICADOR ");
		sql.append(" FROM INDIVIDUO IND2");
		sql.append("  INNER JOIN ");
		sql.append(indicadorController.getNombreTabla());
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

		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
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
	public void insertIndividuo(IndividuoEstrategia individuo) throws GeneticDAOException {
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
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
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
			throws GeneticDAOException {
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
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @return @throws SQLException
	 */
	public List<IndividuoOptimo> consultarIndividuosOptimos() throws GeneticDAOException {
		List<IndividuoOptimo> list = null;

		StringBuilder sql = new StringBuilder();
		sql.append(PropertiesManager.getQueryIndividuosOptimos());
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql.toString());
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosOptimos(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
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
	public int getCountIndicadoresOpen(Individuo individuo) throws GeneticDAOException {
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
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return count;
	}

	public List<Individuo> consultarIndividuosResumenSemanal(Date fechaInicial, Date fechaFinal)
			throws GeneticDAOException {
		List<Individuo> list = null;
		String sql = "SELECT DISTINCT ID_INDIVIDUO, NULL ID_INDIVIDUO_PADRE " + " FROM FILTERED_PARA_OPERAR_SELL PTFS "
				+ " WHERE PTFS.FECHA_SEMANA BETWEEN ? AND ? " + " UNION ALL "
				+ " SELECT DISTINCT ID_INDIVIDUO, NULL ID_INDIVIDUO_PADRE" + " FROM FILTERED_PARA_OPERAR_BUY PTFS "
				+ " WHERE PTFS.FECHA_SEMANA BETWEEN ? AND ?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaInicial.getTime()));
			stmtConsulta.setTimestamp(2, new Timestamp(fechaFinal.getTime()));
			stmtConsulta.setTimestamp(3, new Timestamp(fechaInicial.getTime()));
			stmtConsulta.setTimestamp(4, new Timestamp(fechaFinal.getTime()));
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosRandom(Date fechaInicial, Date fechaFinal, int cantidad)
			throws GeneticDAOException {
		List<Individuo> list = null;
		String sql = "SELECT IND.ID ID_INDIVIDUO, NULL ID_INDIVIDUO_PADRE  FROM INDIVIDUO IND INNER JOIN OPERACION OPER ON OPER.ID_INDIVIDUO=IND.ID "
				+ "   AND OPER.FECHA_APERTURA BETWEEN ? AND ? AND IND.ID LIKE ? AND ROWNUM < ?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaInicial.getTime()));
			stmtConsulta.setTimestamp(2, new Timestamp(fechaFinal.getTime()));
			String likeId = "%" + RandomUtil.nextInt(10);
			stmtConsulta.setString(3, likeId);
			stmtConsulta.setInt(4, cantidad);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosRandom(int cantidad) throws GeneticDAOException {
		List<Individuo> list = null;
		String sql = "SELECT IND.ID ID_INDIVIDUO, NULL ID_INDIVIDUO_PADRE  FROM INDIVIDUO IND "
				+ "   WHERE IND.ID LIKE ? AND ROWNUM < ?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			String likeId = "%" + RandomUtil.nextInt(10);
			stmtConsulta.setString(1, likeId);
			stmtConsulta.setInt(2, cantidad);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosIndicadoresCloseMinimos(int minimo) throws GeneticDAOException {
		List<Individuo> list = null;
		String sql = "SELECT II.ID_INDIVIDUO, NULL ID_INDIVIDUO_PADRE FROM FOREX.INDICADOR_INDIVIDUO II"
				+ " WHERE II.TIPO='CLOSE' AND II.INTERVALO_INFERIOR IS NOT NULL" + " GROUP BY II.ID_INDIVIDUO "
				+ " HAVING COUNT(*)>0 AND COUNT(*)<?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setInt(1, minimo);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosIndicadoresCloseMinimos(int minimo, String id)
			throws GeneticDAOException {
		List<Individuo> list = null;
		String sql = "SELECT II.ID_INDIVIDUO, NULL ID_INDIVIDUO_PADRE FROM FOREX.INDICADOR_INDIVIDUO II"
				+ " WHERE II.TIPO='CLOSE' AND II.INTERVALO_INFERIOR IS NOT NULL " + " AND II.ID_INDIVIDUO=?"
				+ " GROUP BY II.ID_INDIVIDUO " + " HAVING COUNT(*)>0 AND COUNT(*)<?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, id);
			stmtConsulta.setInt(2, minimo);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;

	}

	public List<Individuo> consultarIndividuosIntervaloIndicadores() throws GeneticDAOException {
		List<Individuo> list = null;
		String sql = "SELECT DISTINCT II.ID_INDIVIDUO, NULL ID_INDIVIDUO_PADRE " + " FROM INDICADOR_INDIVIDUO II "
				+ " INNER JOIN INTERVALO_INDICADOR_PROCESADOS II_PROC ON II.ID_INDICADOR=II_PROC.ID_INDICADOR AND II.TIPO=II_PROC.TIPO "
				+ " INNER JOIN INTERVALO_INDICADOR_SIN_OPER II_SINOPER ON II.ID_INDICADOR=II_SINOPER.ID_INDICADOR AND II.TIPO=II_SINOPER.TIPO "
				+ "    AND II_SINOPER.DIFFMIN<II_PROC.DIFFMIN "
				+ " WHERE II.INTERVALO_INFERIOR IS NOT NULL AND ROUND((II.INTERVALO_SUPERIOR-II.INTERVALO_INFERIOR),5)<=II_SINOPER.DIFFMIN "
				+ " AND ROWNUM<100";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public List<Individuo> consultarIndividuosIntervaloIndicadores(String idIndividuo) throws GeneticDAOException {
		List<Individuo> list = null;
		String sql = "SELECT DISTINCT II.ID_INDIVIDUO, NULL ID_INDIVIDUO_PADRE " + " FROM INDICADOR_INDIVIDUO II "
				+ " INNER JOIN INTERVALO_INDICADOR_PROCESADOS II_PROC ON II.ID_INDICADOR=II_PROC.ID_INDICADOR AND II.TIPO=II_PROC.TIPO "
				+ " INNER JOIN INTERVALO_INDICADOR_SIN_OPER II_SINOPER ON II.ID_INDICADOR=II_SINOPER.ID_INDICADOR AND II.TIPO=II_SINOPER.TIPO "
				+ "    AND II_SINOPER.DIFFMIN<II_PROC.DIFFMIN "
				+ " WHERE II.INTERVALO_INFERIOR IS NOT NULL AND ROUND((II.INTERVALO_SUPERIOR-II.INTERVALO_INFERIOR),5)<=II_SINOPER.DIFFMIN "
				+ " AND II.ID_INDIVIDUO=? " + " AND ROWNUM<100";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	@Override
	public boolean exists(IndividuoEstrategia obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insert(IndividuoEstrategia obj) throws GeneticDAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(IndividuoEstrategia obj) throws GeneticDAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<? extends IndividuoEstrategia> getListByProcesoEjecucion(String filtroAdicional, Date fechaHistorico)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosParaBorrar(Date fechaLimite) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosParaBorrar(String idIndividuo, Date fechaLimite)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosParaBorrar(int minutos) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosParaBorrar(String idIndividuo, int minutos) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void insertIfNoExists(IndividuoEstrategia obj) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}
}
