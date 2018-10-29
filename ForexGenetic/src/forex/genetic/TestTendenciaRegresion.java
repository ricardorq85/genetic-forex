package forex.genetic;

import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.LogUtil.logTime;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.setErr;
import static java.lang.System.setOut;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import forex.genetic.dao.TestTendenciaDAO;
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class TestTendenciaRegresion {

	private int index = 0;

	public static void main(String[] args) throws ParseException, ClassNotFoundException, SQLException,
			InterruptedException, FileNotFoundException, UnsupportedEncodingException {
		long id = currentTimeMillis();
		load().join();
		logTime("TestTendencia: " + id, 1);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getPropertyString(LOG_PATH));
		stringBuilder.append("TestTendencia");
		stringBuilder.append(id);
		stringBuilder.append("_log.log");
		String name = stringBuilder.toString();
		PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Inicio: " + id, 1);
		setId(Long.toString(id));
		try {
			TestTendenciaRegresion tendenciaRegresion = new TestTendenciaRegresion();
			tendenciaRegresion.calcularRegresion();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		logTime("Fin: " + id, 1);

	}

	private void calcularRegresion() throws ParseException, ClassNotFoundException, SQLException {
		Date fechaBase = DateUtil.obtenerFecha("2018/10/01 00:06");
		ProcesoTendenciaBuySell procesoTendencia = new ProcesoTendenciaBuySell("1D", "BUY_SELL_20170204-2", 1440 * 1,
				fechaBase);
		Connection conn = JDBCUtil.getConnection();

		TestTendenciaDAO dao = new TestTendenciaDAO(conn);
		List<TendenciaParaOperar> results = dao.consultarDatosTendencia(procesoTendencia);

		double[] sdData = new double[results.size()];
		StandardDeviation sd = new StandardDeviation();
		SimpleRegression sr = new SimpleRegression();
		results.stream().forEach((tendencia) -> {
			long diffDias = DateUtil.diferenciaMinutos(fechaBase, tendencia.getFechaTendencia()); // / 60 / 24;
			sr.addData(diffDias, tendencia.getPrecioCalculado());
			sdData[index++] = tendencia.getPrecioCalculado();
		});
		double standardDeviation = sd.evaluate(sdData);
		System.out.println("Fecha base:" + DateUtil.getDateString(fechaBase));
		System.out.println("Pendiente:" + sr.getSlope());
		System.out.println("R2:" + sr.getRSquare());
		System.out.println("Intercept:" + sr.getIntercept());
		System.out.println("standardDeviation:" + standardDeviation);

	}

}
