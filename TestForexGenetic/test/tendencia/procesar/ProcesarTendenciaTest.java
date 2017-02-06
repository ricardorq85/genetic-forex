package tendencia.procesar;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import forex.genetic.entities.Order;
import forex.genetic.tendencia.manager.ProcesarTendenciasBuySellManager;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import genetic.test.GeneticTest;

public class ProcesarTendenciaTest extends GeneticTest {

	@Test
	//public final void siLaTendenciaDeLaProximaHoraSubeMasDe10PipsEnTodasLasProyeccionesYElPrecioEsMenorQueElPrecioBaseActualEntoncesDebeComprar() throws ParseException, ClassNotFoundException, SQLException {
	public final void siLaTendenciaDeLaProximaHoraSubeMasDe10PipsEnTodasLasProyeccionesYElPrecioEsMenorQueElPrecioBaseActualEntoncesDebeCrearUnaOrden() throws ParseException, ClassNotFoundException, SQLException {
		ProcesarTendenciasBuySellManager manager = new ProcesarTendenciasBuySellManager();		
		Date fechaBase = DateUtil.obtenerFecha("2016/11/18 23:59");
		Order orden = manager.procesarTendencias(fechaBase);
		Assert.assertNotNull("Orden NULL", (orden));
	}

	@Test
	public final void siLaTendenciaDeLaProximaHoraSubeMasDe10PipsEnTodasLasProyeccionesYElPrecioEsMenorQueElPrecioBaseActualEntoncesDebeComprar() throws ParseException, ClassNotFoundException, SQLException {
		ProcesarTendenciasBuySellManager manager = new ProcesarTendenciasBuySellManager();
		
		Date fechaBase = DateUtil.obtenerFecha("2016/11/18 23:59");
		Order orden = manager.procesarTendencias(fechaBase);
		Assert.assertEquals(Constants.OperationType.BUY, orden.getTipo());
	}

	@Test
	public final void siLaTendenciaDeLaProximaHoraSubeMasDe10PipsEnTodasLasProyeccionesYElPrecioEsMenorQueElPrecioBaseActualEntoncesDebeComprarConTPDe15Pips() throws ParseException, ClassNotFoundException, SQLException {
		ProcesarTendenciasBuySellManager manager = new ProcesarTendenciasBuySellManager();
		
		Date fechaBase = DateUtil.obtenerFecha("2016/11/18 23:59");
		Order orden = manager.procesarTendencias(fechaBase);
		Assert.assertEquals(15.0D, orden.getTakeProfit(), 0.00001D);
	}
	
	@Test
	public final void siLaTendenciaDeLaProximaHoraSubeMasDe10PipsEnTodasLasProyeccionesYElPrecioEsMenorQueElPrecioBaseActualEntoncesDebeComprarConSLDe25Pips() throws ParseException, ClassNotFoundException, SQLException {
		ProcesarTendenciasBuySellManager manager = new ProcesarTendenciasBuySellManager();
		
		Date fechaBase = DateUtil.obtenerFecha("2016/11/18 23:59");
		Order orden = manager.procesarTendencias(fechaBase);
		Assert.assertEquals(25.0D, orden.getStopLoss(), 0.00001D);
	}
}
