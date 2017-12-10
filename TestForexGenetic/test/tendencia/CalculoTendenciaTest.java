package tendencia;

import org.junit.Assert;
import org.junit.Test;

import forex.genetic.entities.CalculoTendenciaEstadistica;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Order;
import genetic.test.GeneticTest;

public class CalculoTendenciaTest extends GeneticTest {

	@Test
	public final void siLosPipsActualesSonPositivosYSuperanElRetrocesoMaximoNegativoEntoncesLaProbabilidadPositivaPorRetrocesoDebeSer100()
			throws Exception {
		Estadistica e = new Estadistica();
		e.setPipsMaximosRetrocesoNegativos(811);
		Order ordenActual = new Order();
		ordenActual.setPips(1000);
		CalculoTendenciaEstadistica cte = new CalculoTendenciaEstadistica(e, ordenActual);
		double p = cte.calcularProbabilidadXRetrocesosPositivos();
		Assert.assertEquals(1.0D, p, 0.00001);
	}

	@Test
	public final void siLosPipsActualesSonNegativosYSuperanElRetrocesoMaximoNegativoEntoncesLaProbabilidadNegativaPorRetrocesoDebeSer100()
			throws Exception {
		Estadistica e = new Estadistica();
		e.setPipsMinimosRetrocesoPositivos(-894);
		Order ordenActual = new Order();
		ordenActual.setPips(-1000);
		CalculoTendenciaEstadistica cte = new CalculoTendenciaEstadistica(e, ordenActual);
		double p = cte.calcularProbabilidadXRetrocesosNegativos();
		Assert.assertEquals(1.0D, p, 0.00001);
	}

	@Test
	public final void siLosPipsActualesSonPositivosYSuperanElRetrocesoPromedioNegativoEntoncesLaProbabilidadPositivaPorRetrocesoDebeSer80()
			throws Exception {
		Estadistica e = new Estadistica();
		e.setPipsPromedioRetrocesoNegativos(273);
		Order ordenActual = new Order();
		ordenActual.setPips(1000);
		CalculoTendenciaEstadistica cte = new CalculoTendenciaEstadistica(e, ordenActual);
		double p = cte.calcularProbabilidadXRetrocesosPositivos();
		Assert.assertEquals(0.8D, p, 0.00001);
	}

	@Test
	public final void siLosPipsActualesSonNegativosYSuperanElRetrocesoPromedioNegativoEntoncesLaProbabilidadNegativaPorRetrocesoDebeSer80()
			throws Exception {
		Estadistica e = new Estadistica();
		e.setPipsPromedioRetrocesoPositivos(-340);
		Order ordenActual = new Order();
		ordenActual.setPips(-1000);
		CalculoTendenciaEstadistica cte = new CalculoTendenciaEstadistica(e, ordenActual);
		double p = cte.calcularProbabilidadXRetrocesosNegativos();
		Assert.assertEquals(0.8D, p, 0.00001);
	}

	@Test
	public final void siLosPipsActualesSonPositivosYSuperanElRetrocesoMaximoYElPromedioNegativoEntoncesLaProbabilidadPositivaPorRetrocesoDebeSer80()
			throws Exception {
		Estadistica e = new Estadistica();
		e.setPipsMaximosRetrocesoNegativos(811);
		e.setPipsPromedioRetrocesoNegativos(273);
		Order ordenActual = new Order();
		ordenActual.setPips(1000);
		CalculoTendenciaEstadistica cte = new CalculoTendenciaEstadistica(e, ordenActual);
		double p = cte.calcularProbabilidadXRetrocesosPositivos();
		Assert.assertEquals(1.0D, p, 0.00001);
	}

	@Test
	public final void siLosPipsActualesSonNegativosYSuperanElRetrocesoMinimoYElPromedioPostivosEntoncesLaProbabilidadNegativaPorRetrocesoDebeSer1()
			throws Exception {
		Estadistica e = new Estadistica();
		e.setPipsMinimosRetrocesoPositivos(-894);
		e.setPipsPromedioRetrocesoPositivos(-340);
		Order ordenActual = new Order();
		ordenActual.setPips(-1000);
		CalculoTendenciaEstadistica cte = new CalculoTendenciaEstadistica(e, ordenActual);
		double p = cte.calcularProbabilidadXRetrocesosNegativos();
		Assert.assertEquals(1.0D, p, 0.00001);
	}

	@Test
	public final void siLosPipsActualesSonPositivosYNoSuperanElRetrocesoPromedioNegativoEntoncesLaProbabilidadPositivaPorRetrocesoDebeSer50()
			throws Exception {
		Estadistica e = new Estadistica();
		e.setPipsPromedioRetrocesoNegativos(273);
		Order ordenActual = new Order();
		ordenActual.setPips(273);
		CalculoTendenciaEstadistica cte = new CalculoTendenciaEstadistica(e, ordenActual);
		double p = cte.calcularProbabilidadXRetrocesosPositivos();
		Assert.assertEquals(0.5D, p, 0.00001);
	}

	@Test
	public final void siLosPipsActualesSonNegativosYNoSuperanElRetrocesoPromedioPositivoEntoncesLaProbabilidadNegativaPorRetrocesoDebeSer50()
			throws Exception {
		Estadistica e = new Estadistica();
		e.setPipsPromedioRetrocesoPositivos(-340);
		Order ordenActual = new Order();
		ordenActual.setPips(-340);
		CalculoTendenciaEstadistica cte = new CalculoTendenciaEstadistica(e, ordenActual);
		double p = cte.calcularProbabilidadXRetrocesosPositivos();
		Assert.assertEquals(0.5D, p, 0.00001);
	}
	
	@Test
	public final void siLosPipsActualesSonPositivosYNoSuperanElRetrocesoMaximoPeroSiSuperanElPromedioNegativoEntoncesLaProbabilidadPositivaPorRetrocesoDebeSer80()
			throws Exception {
		Estadistica e = new Estadistica();
		e.setPipsMaximosRetrocesoNegativos(811);
		e.setPipsPromedioRetrocesoNegativos(273);
		Order ordenActual = new Order();
		ordenActual.setPips(400);
		CalculoTendenciaEstadistica cte = new CalculoTendenciaEstadistica(e, ordenActual);
		double p = cte.calcularProbabilidadXRetrocesosPositivos();
		Assert.assertEquals(0.8D, p, 0.00001);
	}
	

}
