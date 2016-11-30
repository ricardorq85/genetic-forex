package mutation;
import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.manager.PropertiesManager.load;
import static java.lang.System.currentTimeMillis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import forex.genetic.dao.IndividuoDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.EspecificMutationManager;
import forex.genetic.manager.MutationIndividuoManager;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import junit.framework.TestCase;

public class MutationTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		load().join();
	}

	@Test
	public final void testMutateNegativoSinMinimo() {
		double d1 = 0.024;
		EspecificMutationManager manager = EspecificMutationManager.getInstance();
		double mutated = manager.mutate(d1);
		double porcDiferencia = Math.abs((mutated - d1) / d1);
		System.out.println(
				"testMutateNegativoSinMinimo: d1=" + d1 + ",mutated=" + mutated + ",porcDiferencia=" + porcDiferencia);
		Assert.assertTrue(porcDiferencia < 0.5);
	}

	@Test
	public final void testMutateNegativoConMinimoRealMaximoInfinito() {
		double d1 = 0.024;
		double min = -0.006;
		double max = Double.POSITIVE_INFINITY;
		EspecificMutationManager manager = EspecificMutationManager.getInstance();
		double mutated = manager.mutate(d1, min, max);
		double porcDiferencia = Math.abs((mutated - d1) / d1);
		System.out.println("testMutateNegativoConMinimoRealMaximoInfinito: d1=" + d1 + ",mutated=" + mutated
				+ ",porcDiferencia=" + porcDiferencia);
		Assert.assertTrue(porcDiferencia < 0.5);
	}

	@Test
	public final void testMutateNegativoConMinimoInfinitoMaximoReal() {
		double d1 = 0.024;
		double min = Double.NEGATIVE_INFINITY;
		double max = 2.04;
		EspecificMutationManager manager = EspecificMutationManager.getInstance();
		double mutated = manager.mutate(d1, min, max);
		double porcDiferencia = Math.abs((mutated - d1) / d1);
		System.out.println("testMutateNegativoConMinimoInfinitoMaximoReal: d1=" + d1 + ",mutated=" + mutated
				+ ",porcDiferencia=" + porcDiferencia);
		Assert.assertTrue(porcDiferencia < 0.5);
	}

	@Test
	public final void testMutateNegativoConMinimoInfinitoMaximoInfinito() {
		double d1 = 0.024;
		double min = Double.NEGATIVE_INFINITY;
		double max = Double.POSITIVE_INFINITY;
		EspecificMutationManager manager = EspecificMutationManager.getInstance();
		double mutated = manager.mutate(d1, min, max);
		double porcDiferencia = Math.abs((mutated - d1) / d1);
		System.out.println("testMutateNegativoConMinimoInfinitoMaximoReal: d1=" + d1 + ",mutated=" + mutated
				+ ",porcDiferencia=" + porcDiferencia);
		Assert.assertTrue(porcDiferencia < 0.5);
	}

	@Test
	public final void testMutateIndividuo() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		try {
			IndicadorController indicadorController = ControllerFactory
					.createIndicadorController(ControllerFactory.ControllerType.Individuo);
			long id = currentTimeMillis();
			setId(Long.toString(id));
			String idIndividuo = "1476325323261.2";
			Individuo individuo = new Individuo(idIndividuo);
			connection = JDBCUtil.getConnection();
			IndividuoDAO individuoDAO = new IndividuoDAO(connection);
			individuoDAO.consultarDetalleIndividuoProceso(individuo, new Date());
			Poblacion poblacion = new Poblacion();
			poblacion.add(individuo);

			MutationIndividuoManager mutator = new MutationIndividuoManager();
			Poblacion[] mutacion = mutator.mutate(1, poblacion, 0);
			List<IndividuoEstrategia> mutados = mutacion[1].getIndividuos();
			Assert.assertNotNull(mutados);
//			for (IndividuoEstrategia individuoMutado : mutados) {
//				individuoDAO.insertIndividuo(individuoMutado);
//				individuoDAO.insertIndicadorIndividuo(indicadorController, individuoMutado);
//				individuoDAO.insertarIndividuoIndicadoresColumnas(individuoMutado.getId());
//				connection.commit();
//				System.out.println("Individuo insertado a BD:" + individuoMutado.getId());
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(connection);
		}
	}

	@Test
	public final void testMutateIndividuoAtributosValidos() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		try {
			IndicadorController indicadorController = ControllerFactory
					.createIndicadorController(ControllerFactory.ControllerType.Individuo);
			long id = currentTimeMillis();
			setId(Long.toString(id));
			String idIndividuo = "1476739789133.2";
			Individuo individuo = new Individuo(idIndividuo);
			connection = JDBCUtil.getConnection();
			IndividuoDAO individuoDAO = new IndividuoDAO(connection);
			individuoDAO.consultarDetalleIndividuoProceso(individuo, new Date());
			Poblacion poblacion = new Poblacion();
			poblacion.add(individuo);

			MutationIndividuoManager mutator = new MutationIndividuoManager();
			Poblacion[] mutacion = mutator.mutate(1, poblacion, 10000);

			List<IndividuoEstrategia> mutados = mutacion[1].getIndividuos();
			Assert.assertNotNull(mutados);
			for (IndividuoEstrategia individuoMutado : mutados) {
				//System.out.println("testMutateIndividuoAtributosValidos: " + individuoMutado.toString());
				Assert.assertTrue(individuoMutado.getId().length() <= 50);
				Assert.assertTrue(Integer.toString(individuoMutado.getTakeProfit()).length() <= 5);
				Assert.assertTrue(individuoMutado.getTakeProfit() >= 300);
				Assert.assertTrue(Integer.toString(individuoMutado.getStopLoss()).length() <= 5);
				Assert.assertTrue(individuoMutado.getStopLoss() >= 300);
				Assert.assertTrue(Double.toString(individuoMutado.getLot()).length() <= 5);
				Assert.assertTrue(Integer.toString(individuoMutado.getInitialBalance()).length() <= 5);
				for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
					IntervalIndicator openIndicator = (IntervalIndicator) individuoMutado.getOpenIndicators().get(i);
					IntervalIndicator closeIndicator = (IntervalIndicator) individuoMutado.getCloseIndicators().get(i);
					System.out.println("ManagerInstance: " + indicadorController.getManagerInstance(i).getId());
					if (openIndicator != null) {
						Assert.assertTrue(
								Double.toString(NumberUtil.round(openIndicator.getInterval().getLowInterval()))
										.length() <= 10);
						Assert.assertTrue(
								Double.toString(NumberUtil.round(openIndicator.getInterval().getHighInterval()))
										.length() <= 10);
					}
					if (closeIndicator != null) {
						Assert.assertTrue(
								Double.toString(NumberUtil.round(closeIndicator.getInterval().getLowInterval()))
										.length() <= 10);
						Assert.assertTrue(
								Double.toString(NumberUtil.round(closeIndicator.getInterval().getHighInterval()))
										.length() <= 10);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(connection);
		}
	}

}
