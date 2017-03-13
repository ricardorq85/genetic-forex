package tendencia.procesar;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.tendencia.manager.ExportarTendenciaManager;
import forex.genetic.util.Constants;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.DateUtil;
import genetic.test.GeneticTest;

public class ExportarTendenciaTest extends GeneticTest {

	private ProcesoTendenciaBuySell test(Date fechaBase) throws ClassNotFoundException, SQLException {
		ExportarTendenciaManager exporter = new ExportarTendenciaManager();
		String periodo = "2D";
		String tipoTendencia = "BUY_SELL_20170204-2";
		double tiempoTendencia = (24 * 2 / 24) * 24 * 60;
		ProcesoTendenciaBuySell paraProcesar = new ProcesoTendenciaBuySell(periodo, tipoTendencia, tiempoTendencia,
				fechaBase);
		exporter.setParaProcesar(paraProcesar);
		exporter.procesar();
		return paraProcesar;
	}

	@Test
	public final void siLaPendientePara2DEsNegativaDebeExportarOperacionDeVenta()
			throws ParseException, ClassNotFoundException, SQLException {
		Date fechaBase = DateUtil.obtenerFecha("2017/02/01 07:59");
		ProcesoTendenciaBuySell exported = this.test(fechaBase);
		Assert.assertEquals(Constants.OperationType.SELL, exported.getTipoOperacion());
	}

	@Test
	public final void siLaPendientePara2DEsPositivaDebeExportarOperacionDeCompra()
			throws ParseException, ClassNotFoundException, SQLException {
		Date fechaBase = DateUtil.obtenerFecha("2017/02/06 23:59");
		ProcesoTendenciaBuySell exported = this.test(fechaBase);
		Assert.assertEquals(Constants.OperationType.BUY, exported.getTipoOperacion());
	}

	@Test
	public final void siEsCompraYElPrecioCalculadoEs1p31219EntoncesElTakeProfitDebeSer1p31574() {
		// PRECIO_CALCULADO=1.31219,TAKE_PROFIT=1.31929,STOP_LOSS=1.30888
		TendenciaParaOperar ten = new TendenciaParaOperar();
		ten.setTipoOperacion(OperationType.BUY);
		ten.setPrecioCalculado(1.31219);
		ten.setTp(1.31929);

		Assert.assertEquals(1.31574, ten.getTp(), 0.0001);
	}

	@Test
	public final void siEsVentaYElPrecioCalculadoEs1p33087EntoncesElTakeProfitDebeSer1p33041() {
		// PRECIO_CALCULADO=1.33087,TAKE_PROFIT=1.32995,STOP_LOSS=1.33584
		TendenciaParaOperar ten = new TendenciaParaOperar();
		ten.setTipoOperacion(OperationType.SELL);
		ten.setPrecioCalculado(1.33087);
		ten.setTp(1.32995);

		Assert.assertEquals(1.33041, ten.getTp(), 0.0001);
	}

	@Test
	public final void siEsCompraYElPrecioCalculadoEs1p31219EntoncesElStopLossDebeSerELSLPorElFactor() {
		// PRECIO_CALCULADO=1.31219,TAKE_PROFIT=1.31929,STOP_LOSS=1.30888
		TendenciaParaOperar ten = new TendenciaParaOperar();
		ten.setTipoOperacion(OperationType.BUY);
		ten.setPrecioCalculado(1.31219);
		ten.setSl(1.30888);

		Assert.assertEquals(1.30419, ten.getSl(), 0.0001);
	}

	@Test
	public final void siEsVentaYElPrecioCalculadoEs1p33087EntoncesElStopLossDebeSerMultiplicadoPorElFactor() {
		// PRECIO_CALCULADO=1.33087,TAKE_PROFIT=1.32995,STOP_LOSS=1.33584
		TendenciaParaOperar ten = new TendenciaParaOperar();
		ten.setTipoOperacion(OperationType.SELL);
		ten.setPrecioCalculado(1.33087);
		ten.setSl(1.33584);

		Assert.assertEquals(1.33887, ten.getSl(), 0.0001);
	}

	@Test
	public final void siEsVentaYElPrecioCalculadoEs1p30301YSL1p30496EntoncesElStopLossDebeSerMultiplicadoPorElFactor() {
		// PRECIO_CALCULADO=1.30301,TAKE_PROFIT=1.2997,STOP_LOSS=1.30593
		TendenciaParaOperar ten = new TendenciaParaOperar();
		ten.setTipoOperacion(OperationType.SELL);
		ten.setPrecioCalculado(1.30301);
		ten.setSl(1.30496);

		Assert.assertEquals(1.31101, ten.getSl(), 0.0001);
	}

	@Test
	public final void siEsVentaTPEsMenorQue10PipsDebeRemoverloDeLaLista() throws ClassNotFoundException, SQLException {
		TendenciaParaOperar ten1 = new TendenciaParaOperar(OperationType.SELL, 1.32404, 1.32279);
		TendenciaParaOperar ten2 = new TendenciaParaOperar(OperationType.SELL, 1.30500, 1.30495);
		List<TendenciaParaOperar> tendencias = new ArrayList<>();
		tendencias.add(ten1);
		tendencias.add(ten2);
		ProcesoTendenciaBuySell paraProcesar = new ProcesoTendenciaBuySell(null, null, 0.0D, null);
		paraProcesar.setTendencias(tendencias);

		Assert.assertEquals(1, tendencias.size());
	}

	@Test
	public final void siEsCompraTPEsMenorQue10PipsDebeRemoverloDeLaLista() throws ClassNotFoundException, SQLException {
		TendenciaParaOperar ten1 = new TendenciaParaOperar(OperationType.BUY, 1.30300, 1.30600);
		TendenciaParaOperar ten2 = new TendenciaParaOperar(OperationType.BUY, 1.30500, 1.30515);
		List<TendenciaParaOperar> tendencias = new ArrayList<>();
		tendencias.add(ten1);
		tendencias.add(ten2);
		ProcesoTendenciaBuySell paraProcesar = new ProcesoTendenciaBuySell(null, null, 0.0D, null);
		paraProcesar.setTendencias(tendencias);

		Assert.assertEquals(1, tendencias.size());
	}

	@Test
	public final void siEsCompraYElPuntoConocidoInicialEsMayorQueElPrimerPuntoDeLaTendenciaDebeModificarElTPSegunLosPuntosDiferenciaInicial()
			throws ClassNotFoundException, SQLException {
		TendenciaParaOperar ten1 = new TendenciaParaOperar(OperationType.BUY, 1.30945, 0.0D);
		double puntoConocidoInicial = 1.31366;
		double primerPuntoTendencia = 1.30916;
		double puntosDiferenciaInicial = (puntoConocidoInicial - primerPuntoTendencia);
		ten1.setPuntosDiferenciaInicial(puntosDiferenciaInicial);
		ten1.setTp(1.31491);

		Assert.assertEquals(1.31041, ten1.getTp(), 0.0001);
	}

	@Test
	public final void siEsVentaYElPuntoConocidoInicialEsMenorQueElPrimerPuntoDeLaTendenciaDebeModificarElTPSegunLosPuntosDiferenciaInicial()
			throws ClassNotFoundException, SQLException {
		TendenciaParaOperar ten1 = new TendenciaParaOperar(OperationType.SELL, 1.30945, 0.0D);
		double puntoConocidoInicial = 1.30645;
		double primerPuntoTendencia = 1.30955;
		double puntosDiferenciaInicial = (puntoConocidoInicial - primerPuntoTendencia);
		ten1.setPuntosDiferenciaInicial(puntosDiferenciaInicial);
		ten1.setTp(1.30000);

		Assert.assertEquals(1.3031, ten1.getTp(), 0.0001);
	}

	@Test
	public final void siEsCompraYElPuntoConocidoInicialEsMenorQueElPrimerPuntoDeLaTendenciaNODebeModificarElTPSegunLosPuntosDiferenciaInicial()
			throws ClassNotFoundException, SQLException {
		TendenciaParaOperar ten1 = new TendenciaParaOperar(OperationType.BUY, 1.30945, 0.0D);
		double puntoConocidoInicial = 1.30645;
		double primerPuntoTendencia = 1.30916;
		double puntosDiferenciaInicial = (puntoConocidoInicial - primerPuntoTendencia);
		ten1.setPuntosDiferenciaInicial(puntosDiferenciaInicial);
		ten1.setTp(1.31491);

		Assert.assertEquals(1.31491, ten1.getTp(), 0.0001);
	}

	@Test
	public final void siEsVentaYElPuntoConocidoInicialEsMayorQueElPrimerPuntoDeLaTendenciaNODebeModificarElTPSegunLosPuntosDiferenciaInicial()
			throws ClassNotFoundException, SQLException {
		TendenciaParaOperar ten1 = new TendenciaParaOperar(OperationType.SELL, 1.30945, 0.0D);
		double puntoConocidoInicial = 1.31645;
		double primerPuntoTendencia = 1.30955;
		double puntosDiferenciaInicial = (puntoConocidoInicial - primerPuntoTendencia);
		ten1.setPuntosDiferenciaInicial(puntosDiferenciaInicial);
		ten1.setTp(1.30000);

		Assert.assertEquals(1.30000, ten1.getTp(), 0.0001);
	}

}
