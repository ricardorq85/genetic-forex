package tendencia;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import forex.genetic.dao.TendenciaDAO;
import forex.genetic.entities.CalculoTendenciaEstadistica;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.TendenciaEstadistica;
import forex.genetic.tendencia.manager.TendenciaBuySellManager;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import genetic.test.GeneticTest;

public class TendenciaTest extends GeneticTest {

	private static Date FECHA_TENDENCIA;
	private static Connection CONNECTION;
	private TendenciaBuySellManager tendenciaManager; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		GeneticTest.setUpBeforeClass();
	}

	@Before
	public void setUp() throws Exception {
		tendenciaManager = new TendenciaBuySellManager();
		CONNECTION = JDBCUtil.getConnection();
		FECHA_TENDENCIA = DateUtil.obtenerFecha("2016/11/18 23:59");
		String[] individuos = { "1477365411387.9040", "1454889600000.1019", "1454196071797.3183", "1484511934277.1534",
				"1484511934277.509", "1454196071797.2662" };
		TendenciaBuySellManager tendenciaManager = new TendenciaBuySellManager();
		for (String ind : individuos) {
			tendenciaManager.borrarTendencia(ind, FECHA_TENDENCIA);
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private TendenciaEstadistica testIndividuo(String idIndividuo)
			throws SQLException, ClassNotFoundException, ParseException, CloneNotSupportedException {
		return this.testIndividuo(FECHA_TENDENCIA, idIndividuo);
	}

	private TendenciaEstadistica testIndividuo(Date fechaBase, String idIndividuo)
			throws SQLException, ClassNotFoundException, ParseException, CloneNotSupportedException {		
		TendenciaEstadistica tendencia = tendenciaManager.calcularTendencia(fechaBase, idIndividuo);
		System.out.println(tendencia);
		return tendencia;
	}

	private TendenciaEstadistica testMockIndividuo(String idIndividuo)
			throws SQLException, ClassNotFoundException, ParseException, CloneNotSupportedException {
		CalculoTendenciaEstadistica calculoTendencia = mock(CalculoTendenciaEstadistica.class);
		when(calculoTendencia.getProbabilidadPositivos()).thenReturn(0.6D);
		when(calculoTendencia.getProbabilidadNegativos()).thenReturn(0.4D);
		Estadistica e1 = new Estadistica();
		e1.setCantidadTotal(1);
		when(calculoTendencia.getEstadistica()).thenReturn(e1);
		CalculoTendenciaEstadistica calculoTendenciaFiltradaActual = mock(CalculoTendenciaEstadistica.class);
		when(calculoTendenciaFiltradaActual.getProbabilidadPositivos()).thenReturn(0.0D);
		when(calculoTendenciaFiltradaActual.getProbabilidadNegativos()).thenReturn(0.0D);
		when(calculoTendenciaFiltradaActual.getProbabilidadNegativos()).thenReturn(0.0D);
		Estadistica e2 = new Estadistica();
		e2.setCantidadTotal(1);
		when(calculoTendenciaFiltradaActual.getEstadistica()).thenReturn(e2);
		TendenciaEstadistica tendenciaForMock = new TendenciaEstadistica(e1, e2, null);
		tendenciaForMock.setCalculoTendencia(calculoTendencia);
		tendenciaForMock.setCalculoTendenciaFiltradaActual(calculoTendenciaFiltradaActual);
		tendenciaForMock.setOrdenActual(new Order());

		TendenciaBuySellManager tendenciaManagerMock = mock(TendenciaBuySellManager.class);
		when(tendenciaManagerMock.crearTendencia(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(tendenciaForMock);
		Mockito.doCallRealMethod().when(tendenciaManagerMock).calcularTendencia(Mockito.any(Date.class),
				Mockito.anyString());
		Mockito.doCallRealMethod().when(tendenciaManagerMock).calcularTendencia(Mockito.any(Date.class),
				Mockito.any(Individuo.class));
		Mockito.doCallRealMethod().when(tendenciaManagerMock).calcularTendencia(Mockito.any(Point.class),
				Mockito.any(Individuo.class));
		Mockito.doCallRealMethod().when(tendenciaManagerMock).setup();
		tendenciaManagerMock.setup();

		TendenciaEstadistica tendencia = tendenciaManagerMock.calcularTendencia(FECHA_TENDENCIA, idIndividuo);
		System.out.println(tendencia);
		return tendencia;
	}

	@Test
	public final void paraElIndividuo1477365411387p9040El18Nov2016LaTendenciaNoDebeSerNull()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1477365411387.9040";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertNotNull("Tendencia NULL", tendencia);
	}

	@Test
	public final void paraElIndividuo1477365411387p9040El18Nov2016LaTendenciaNoDebeSerVacia()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1477365411387.9040";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertNotNull("Tendencia NULL", tendencia);
		Assert.assertFalse("Tendencia vacio", (tendencia.getFecha() == null));
	}

	@Test
	public final void paraElIndividuo1469622989774P00El18Nov2016LaTendenciaDebeSerNull()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1469622989774.00";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertNull("Tendencias NULL", tendencia);
	}

	@Test
	public final void paraElIndividuo1477365411387p9040El18Nov2016LaProbNegativaMayorQueProbPositiva()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1477365411387.9040";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertTrue(
				"Prob Pos(" + tendencia.getProbabilidadPositivos() + ") " + ">=Prob Neg("
						+ tendencia.getProbabilidadNegativos() + ")",
				tendencia.getProbabilidadPositivos() < tendencia.getProbabilidadNegativos());
	}

	@Test
	public final void paraElIndividuo1477365411387p9040El18Nov2016LaOrdenActualNoDebeSerNull()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1477365411387.9040";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertNotNull("Orden Actual NULL", tendencia.getOrdenActual());
	}

	@Test
	public final void paraElIndividuo1477365411387p9040El18Nov2016LaOrdenActualDebeTenerLosDatosDeRetrocesos()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1477365411387.9040";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertNotNull("Orden Actual NULL", tendencia.getOrdenActual().getMaxFechaRetroceso());
	}

	@Test
	public final void paraElIndividuo1477365411387p9040El18Nov2016LaDuracionDeLaOrdenActualDebeMayorQueCero()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1477365411387.9040";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertTrue("Duracion Orden Actual CERO", (tendencia.getOrdenActual().getDuracionMinutos() > 0));
	}

	@Test
	public final void paraElIndividuo1454889600000p1019El18Nov2016LaProbPositivasMayorQueProbNegativa()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1454889600000.1019";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertTrue(
				"Prob Pos(" + tendencia.getProbabilidadPositivos() + ") " + "<=Prob Neg("
						+ tendencia.getProbabilidadNegativos() + ")",
				tendencia.getProbabilidadPositivos() > tendencia.getProbabilidadNegativos());
	}

	@Test
	public final void paraElIndividuo1454889600000p1019El18Nov2016LosPipsCalculadosDebenSer1000Menos400()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		int tp = 1000;
		int sl = -1059;
		int pipsActuales = 400 - 11;
		String idIndividuo = "1454889600000.1019";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertEquals(tp-pipsActuales, tendencia.getPips(), 0.00001);
		//Assert.assertTrue("Pips=" + tendencia.getPips() + ")", (tendencia.getPips() == (sl - pipsActuales)));
	}

	@Test
	public final void laProbabilidadPositivaMasLaNegativaDebenSumar1()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1454196071797.3183";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertTrue(
				"Prob Pos(" + tendencia.getProbabilidadPositivos() + ") " + "+ Prob Neg("
						+ tendencia.getProbabilidadNegativos() + ")="
						+ (tendencia.getProbabilidadPositivos() + tendencia.getProbabilidadNegativos()),
				(NumberUtil
						.round(tendencia.getProbabilidadPositivos() + tendencia.getProbabilidadNegativos()) == 1.0D));
	}

	@Test
	public final void paraElIndividuo1477365411387p9040El18Nov2016LosPipsCalculadosDebenSerSLMenosPipsActuales()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		int tp = 1194;
		int pipsActuales = -869 - 13;
		int sl = -2000;
		String idIndividuo = "1477365411387.9040";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertEquals(sl-pipsActuales, tendencia.getPips(), 0.00001);
		//Assert.assertTrue("Pips=" + tendencia.getPips() + ")", (tendencia.getPips() == (tp - (pipsActuales))));
	}

	@Test
	public final void guardarElIndividuo1477365411387p9040El18Nov2016()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1477365411387.9040";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		TendenciaBuySellManager tendenciaManager = new TendenciaBuySellManager();
		tendenciaManager.guardarTendencia(tendencia);
		TendenciaDAO tendenciaDAO = new TendenciaDAO(CONNECTION);
		Assert.assertTrue("Tendencia NO guardada", (tendenciaDAO.exists(tendencia)));
	}

	@Test
	public final void noGuardarElIndividuo1477365411387p9040El18Nov2016()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1477365411387.9040";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		TendenciaDAO tendenciaDAO = new TendenciaDAO(CONNECTION);
		Assert.assertTrue("Tendencia guardada", (!tendenciaDAO.exists(tendencia)));
	}

	@Test
	public final void guardarElIndividuo1454889600000p1019El18Nov2016()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1454889600000.1019";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		TendenciaBuySellManager tendenciaManager = new TendenciaBuySellManager();
		tendenciaManager.guardarTendencia(tendencia);
		TendenciaDAO tendenciaDAO = new TendenciaDAO(CONNECTION);
		Assert.assertTrue("Tendencia NO guardada", (tendenciaDAO.exists(tendencia)));
	}

	@Test
	public final void siLaProbabilidadEsPositivaYEsBuyElPrecioCalculadoDebeSerMayorQueElPrecioBase()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1454889600000.1019";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		TendenciaBuySellManager tendenciaManager = new TendenciaBuySellManager();
		tendenciaManager.guardarTendencia(tendencia);
		Assert.assertTrue(
				"Precio calculado(" + tendencia.getPrecioCalculado() + ") menor que precio base("
						+ tendencia.getPrecioBase() + ")",
				(tendencia.getPrecioCalculado() > tendencia.getPrecioBase()));
	}

	@Test
	public final void siLaProbabilidadEsNegativaYEsSellElPrecioCalculadoDebeSerMayorQueElPrecioBase()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1477365411387.9040";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertTrue(
				"Precio calculado(" + tendencia.getPrecioCalculado() + ") menor que precio base("
						+ tendencia.getPrecioBase() + ")",
				(tendencia.getPrecioCalculado() > tendencia.getPrecioBase()));
	}

	@Test
	public final void paraElIndividuo1454196071797p3183El18Nov2016LaProbabilidadesNOPuedenSerNAN()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1454196071797.3183";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertFalse("Probabilidad is NAN", Double.isNaN(tendencia.getProbabilidadPositivos()));
	}

	@Test
	public final void losPipsActualesNoPuedenSerMayoresQueElTakeProfit()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1454196071797.3183";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertFalse(
				"Pips actuales (" + tendencia.getOrdenActual().getPips() + ") mayores que Take profit("
						+ tendencia.getOrdenActual().getTakeProfit() + ")",
				(tendencia.getOrdenActual().getPips() > tendencia.getOrdenActual().getTakeProfit()));
	}

	@Test
	public final void siLaEstadisticaFiltradaEstaVaciaDebeCalcularTodoConLaEstadisticaDelIndividuo()
			throws CloneNotSupportedException, ClassNotFoundException, SQLException, ParseException {
		String idIndividuo = "1454196071797.3183";
		TendenciaEstadistica tendencia = this.testMockIndividuo(idIndividuo);
		Assert.assertFalse(
				"Nan Prob Pos(" + tendencia.getProbabilidadPositivos() + ") " + "+ Prob Neg("
						+ tendencia.getProbabilidadNegativos() + ")="
						+ (tendencia.getProbabilidadPositivos() + tendencia.getProbabilidadNegativos()),
				(Double.isNaN(tendencia.getProbabilidadPositivos() + tendencia.getProbabilidadNegativos())));
	}

	@Test
	public final void siLaEstadisticaEstaVaciaLaProbabilidadNoDebeSerNaN() {
		Estadistica e = new Estadistica();
		CalculoTendenciaEstadistica cte = new CalculoTendenciaEstadistica(e, new Order());
		cte.calcularProbabilidades();
		Assert.assertFalse(
				"Nan Prob Pos(" + cte.getProbabilidadPositivos() + ") " + "+ Prob Neg(" + cte.getProbabilidadNegativos()
						+ ")=" + (cte.getProbabilidadPositivos() + cte.getProbabilidadNegativos()),
				(Double.isNaN(cte.getProbabilidadPositivos() + cte.getProbabilidadNegativos())));
	}

	@Test
	public final void paraElIndividuo1484511934277p1534LaDuracionDebeSerMayorQueCero()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1484511934277.1534";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertTrue("Duracion (" + tendencia.getDuracion() + ")", ((tendencia.getDuracion() > 0)));
	}

	@Test
	public final void paraElIndividuo1484511934277p509LaDuracionDebeSerMayorQueCero()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1484511934277.509";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertTrue("Duracion (" + tendencia.getDuracion() + ")", ((tendencia.getDuracion() > 0)));
	}

	@Test
	public final void paraElIndividuo1454196071797p2662LaDuracionDebeSerMayorQueCero()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1454196071797.2662";
		TendenciaEstadistica tendencia = this.testIndividuo(idIndividuo);
		Assert.assertTrue("Duracion (" + tendencia.getDuracion() + ")", ((tendencia.getDuracion() > 0)));
	}

	@Test
	public final void silaDuracionActualEsMayorQueLasDuracionesPromedioEntoncesLaDuracionCalculadaDebeSerPositiva() {
		Estadistica e = new Estadistica();
		e.setPipsPromedioPositivos(500);
		e.setPipsPromedioNegativos(-1200);
		e.setCantidadTotal(20);
		e.setCantidadNegativos(10);
		e.setCantidadPositivos(10);
		e.setPipsPositivos(100);
		e.setPipsNegativos(-1000);
		e.setDuracionPromedioPositivos(1440);
		e.setDuracionPromedioNegativos(1440);
		e.setDuracionPromedio(1440);
		Order ordenActual = new Order();
		ordenActual.setDuracionMinutos(2880);
		CalculoTendenciaEstadistica cte = new CalculoTendenciaEstadistica(e, ordenActual);
		cte.calcularProbabilidades();
		cte.calcular();
		Assert.assertTrue("Duracion calculada(" + cte.getDuracion() + ")", (cte.getDuracion() > 0));
	}

	@Test
	public final void silaDuracionActualEsMayorQueLasDuracionesPromedioCualDebeSerLaDuracionCalculada() {
		Estadistica e = new Estadistica();
		e.setPipsPromedioPositivos(500);
		e.setPipsPromedioNegativos(-1200);
		e.setCantidadTotal(20);
		e.setCantidadNegativos(10);
		e.setCantidadPositivos(10);
		e.setPipsPositivos(100);
		e.setPipsNegativos(-1000);
		e.setDuracionPromedioPositivos(1440);
		e.setDuracionPromedioNegativos(1440);
		e.setDuracionPromedio(1440);
		Order ordenActual = new Order();
		ordenActual.setPips(400);
		ordenActual.setDuracionMinutos(2880);
		CalculoTendenciaEstadistica cte = new CalculoTendenciaEstadistica(e, ordenActual);
		cte.calcularProbabilidades();
		cte.calcular();
		Assert.assertTrue("Duracion calculada(" + cte.getDuracion() + ")", (cte.getDuracion() > 1440));
	}

	@Test
	public final void paraElIndividuo1484511934277P112El1Dic2016ALas17p59LaDuracionDebeSerMayorQueCero()
			throws ClassNotFoundException, SQLException, ParseException, CloneNotSupportedException {
		String idIndividuo = "1484511934277.112";
		Date fechaBase = DateUtil.obtenerFecha("2016/12/01 17:59");
		tendenciaManager.borrarTendencia(idIndividuo, fechaBase);
		TendenciaEstadistica tendencia = this.testIndividuo(fechaBase, idIndividuo);
		Assert.assertTrue("Duracion (" + tendencia.getDuracion() + ")", ((tendencia.getDuracion() > 0)));
	}

	@Test
	public final void paraElIndividuo1484511934277p987laDuracionTieneQueSerMayorQueCero() throws Exception {
		
		String idIndividuo = "1484511934277.987";
		Date fechaBase = DateUtil.obtenerFecha("2016/01/20 09:00");
		tendenciaManager.borrarTendencia(idIndividuo, fechaBase);
		TendenciaEstadistica tendencia = this.testIndividuo(fechaBase, idIndividuo);
		Assert.assertNotEquals(0, (tendencia.getDuracion()));
	}

	@Test
	public final void paraElIndividuo1454196071797p3260LaDuracionTieneQueSerMayorQueCero() throws Exception {		
		String idIndividuo = "1454196071797.3260";
		Date fechaBase = DateUtil.obtenerFecha("2016/11/14 00:00");
		tendenciaManager.borrarTendencia(idIndividuo, fechaBase);
		TendenciaEstadistica tendencia = this.testIndividuo(fechaBase, idIndividuo);
		Assert.assertNotEquals(0, (tendencia.getDuracion()));
	}
	
	@Test
	public final void laFechaProyectadaNoPuedeSerSabadoNiDomingo() throws Exception {		
		String idIndividuo = "1452884740297.1";
		Date fechaBase = DateUtil.obtenerFecha("2016/12/02 11:59");
		tendenciaManager.borrarTendencia(idIndividuo, fechaBase);
		TendenciaEstadistica tendencia = this.testIndividuo(fechaBase, idIndividuo);
		boolean habil = DateUtil.isDiaHabil(tendencia.getFechaTendencia());
		Assert.assertTrue("Es sábado o domingo", habil);
	}

	@Test
	public final void individuo1484511934277p1495ValidarPipsProyectados() throws Exception {		
		String idIndividuo = "1484511934277.1495";
		Date fechaBase = DateUtil.obtenerFecha("2017/02/01 11:51");
		//tendenciaManager.borrarTendencia(idIndividuo, fechaBase);
		Constants.TIPO_TENDENCIA="TEST";
		TendenciaEstadistica tendencia = this.testIndividuo(fechaBase, idIndividuo);
		boolean habil = DateUtil.isDiaHabil(tendencia.getFechaTendencia());
		Assert.assertTrue("Es sábado o domingo", habil);
	}

}
