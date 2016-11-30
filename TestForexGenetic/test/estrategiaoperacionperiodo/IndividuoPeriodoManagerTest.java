package estrategiaoperacionperiodo;

import static forex.genetic.manager.PropertiesManager.load;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import forex.genetic.dao.EstrategiaOperacionPeriodoDAO;
import forex.genetic.manager.IndividuosPeriodoManager;

public class IndividuoPeriodoManagerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		load().join();
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
	public final void testIndividuosPeriodoManager() {
		fail("Not yet implemented"); // TODO
	}

	@Rule
	public MockitoRule mr = MockitoJUnit.rule();

	@Test
	public final void cuandoNoHayInclusionesDebeGenerarParametroAleatorios() {
		try {
			// IndividuosPeriodoManager mockManager =
			// mock(IndividuosPeriodoManager.class);
			// doThrow(new
			// IOException()).when(mockManager).procesarIndividuosXPeriodo();

			IndividuosPeriodoManager manager = new IndividuosPeriodoManager();
			EstrategiaOperacionPeriodoDAO mockDAO = mock(EstrategiaOperacionPeriodoDAO.class);
			when(mockDAO.consultarInclusiones()).thenReturn(new ArrayList<>());
			manager.setEstrategiaOperacionPeriodoDAO(mockDAO);
			manager.procesarIndividuosXPeriodo();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testProcesarIndividuosXPeriodoParametroOperacionPeriodo() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testEjecutarIndividuosXPeriodo() {
		fail("Not yet implemented"); // TODO
	}

}
