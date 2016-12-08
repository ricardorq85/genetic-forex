package util;

import java.text.ParseException;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import forex.genetic.util.DateUtil;

public class DateUtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void paraAnyo2008IgualesDebeRetornarFalse() throws ParseException {
		Date fecha1 = DateUtil.obtenerFecha("2008/01/09 00:00");
		Date fecha2 = DateUtil.obtenerFecha("2008/10/09 00:00");

		Assert.assertFalse("Años iguales", DateUtil.anyoMayorQue(fecha1, fecha2));
	}

	@Test
	public final void paraAnyoMes200809IgualesDebeRetornarFalse() throws ParseException {
		Date fecha1 = DateUtil.obtenerFecha("2008/09/09 00:00");
		Date fecha2 = DateUtil.obtenerFecha("2008/09/09 00:00");

		Assert.assertFalse("Meses iguales", DateUtil.anyoMesMayorQue(fecha1, fecha2));
	}

	@Test
	public final void paraAnyo2009y2010DebeRetornarFalse() throws ParseException {
		Date fecha1 = DateUtil.obtenerFecha("2009/03/09 00:00");
		Date fecha2 = DateUtil.obtenerFecha("2010/01/09 00:00");

		Assert.assertTrue("Años iguales", DateUtil.anyoMayorQue(fecha1, fecha2));
	}

	@Test
	public final void paraAnyoMes200809DebeRetornar200809() throws ParseException {
		Date fecha1 = DateUtil.obtenerFecha("2008/09/09 00:00");

		Assert.assertEquals("Año Mes errado", 200809, DateUtil.obtenerAnyoMes(fecha1));
	}
	
	@Test
	public final void paraAnyoMes200906y201006DebeRetornarTrue() throws ParseException {
		Date fechaMenor = DateUtil.obtenerFecha("2009/06/09 00:00");
		Date fechaMayor = DateUtil.obtenerFecha("2010/06/09 00:00");

		Assert.assertTrue("Año mes mayor", DateUtil.anyoMesMayorQue(fechaMenor, fechaMayor));
	}
	
	@Test
	public final void paraAnyoMes201106y201006DebeRetornarTrue() throws ParseException {
		Date fechaMenor = DateUtil.obtenerFecha("2011/06/09 00:00");
		Date fechaMayor = DateUtil.obtenerFecha("2010/06/09 00:00");

		Assert.assertFalse("Año mes mayor", DateUtil.anyoMesMayorQue(fechaMenor, fechaMayor));
	}	

	@Test
	public final void paraAnyoMes201107y201107DebeRetornarTrue() throws ParseException {
		Date fechaMenor = DateUtil.obtenerFecha("2011/07/09 00:00");
		Date fechaMayor = DateUtil.obtenerFecha("2011/07/09 00:00");

		Assert.assertFalse("Año mes mayor", DateUtil.anyoMesMayorQue(fechaMenor, fechaMayor));
	}	
}
