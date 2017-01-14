package estrategiaoperacionperiodo;

import static forex.genetic.manager.PropertiesManager.load;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import forex.genetic.dao.EstrategiaOperacionPeriodoDAO;
import forex.genetic.dao.OperacionesDAO;
import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.manager.InclusionesManager;
import forex.genetic.manager.IndividuosPeriodoManager;
import forex.genetic.util.RandomUtil;
import forex.genetic.util.jdbc.JDBCUtil;

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

	@Rule
	public MockitoRule mr = MockitoJUnit.rule();

	@Test
	public final void cuandoNoHayInclusionesDebeGenerarParametroAleatorios() {
		try {
			// IndividuosPeriodoManager mockManager =
			// mock(IndividuosPeriodoManager.class);
			// doThrow(new
			// IOException()).when(mockManager).procesarIndividuosXPeriodo();

			InclusionesManager manager = new InclusionesManager();
			EstrategiaOperacionPeriodoDAO mockDAO = mock(EstrategiaOperacionPeriodoDAO.class);
			when(mockDAO.consultarInclusiones()).thenReturn(new ArrayList<>());
			manager.setEstrategiaOperacionPeriodoDAO(mockDAO);
			int inclusiones = manager.consultarInclusiones(null, "SELL").size();
			System.out.println("cuandoNoHayInclusionesDebeGenerarParametroAleatorios=" + inclusiones);
			Assert.assertTrue("Inclusiones=" + inclusiones, inclusiones > 0);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void cuandoHayMenosDelNumeroDeInclusionesParametrizadasDebeGenerarParametroAleatoriosHastaCompletarlas() {
		try {
			List<ParametroOperacionPeriodo> inclusionesForMock = new ArrayList<>();
			inclusionesForMock.add(new ParametroOperacionPeriodo());
			InclusionesManager manager = new InclusionesManager();
			EstrategiaOperacionPeriodoDAO mockDAO = mock(EstrategiaOperacionPeriodoDAO.class);
			when(mockDAO.consultarInclusiones()).thenReturn(inclusionesForMock);
			manager.setEstrategiaOperacionPeriodoDAO(mockDAO);
			int inclusiones = manager.consultarInclusiones(null, "SELL").size();
			System.out.println(
					"cuandoHayMenosDelNumeroDeInclusionesParametrizadasDebeGenerarParametroAleatoriosHastaCompletarlas="
							+ inclusiones);
			Assert.assertTrue("Inclusiones=" + inclusiones,
					inclusiones >= manager.getMinimoInclusiones());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void lasInclusionesGeneranValoresAleatoriosParaR2() {
		try {
			List<ParametroOperacionPeriodo> inclusionesForMock = new ArrayList<>();
			inclusionesForMock.add(new ParametroOperacionPeriodo());
			InclusionesManager manager = new InclusionesManager();
			EstrategiaOperacionPeriodoDAO mockDAO = mock(EstrategiaOperacionPeriodoDAO.class);
			when(mockDAO.consultarInclusiones()).thenReturn(inclusionesForMock);
			manager.setEstrategiaOperacionPeriodoDAO(mockDAO);

			List<ParametroOperacionPeriodo> inclusiones = manager.consultarInclusiones(null, "SELL");
			ParametroOperacionPeriodo param = inclusiones.get(RandomUtil.nextInt(inclusiones.size()));
			double filtroR2Semana = param.getFiltroR2Semana();
			double filtroR2Mes = param.getFiltroR2Mes();
			double filtroR2Anyo = param.getFiltroR2Anyo();
			double filtroR2Totales = param.getFiltroR2Totales();
			System.out.println("filtroR2Semana=" + filtroR2Semana + ",filtroR2Mes=" + filtroR2Mes + ",filtroR2Anyo="
					+ filtroR2Anyo + ",filtroR2Totales=" + filtroR2Totales);

			Assert.assertTrue("R2 no generado",
					(filtroR2Semana != 0 || filtroR2Mes != 0 || filtroR2Anyo != 0 || filtroR2Totales != 0));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void lasInclusionesGeneranValoresAleatoriosParaPendiente() {
		try {
			List<ParametroOperacionPeriodo> inclusionesForMock = new ArrayList<>();
			inclusionesForMock.add(new ParametroOperacionPeriodo());
			InclusionesManager manager = new InclusionesManager();
			EstrategiaOperacionPeriodoDAO mockDAO = mock(EstrategiaOperacionPeriodoDAO.class);
			when(mockDAO.consultarInclusiones()).thenReturn(inclusionesForMock);
			manager.setEstrategiaOperacionPeriodoDAO(mockDAO);

			List<ParametroOperacionPeriodo> inclusiones = manager.consultarInclusiones(null, "SELL");
			ParametroOperacionPeriodo param = inclusiones.get(RandomUtil.nextInt(inclusiones.size()));
			double filtroPendienteSemana = param.getFiltroPendienteSemana();
			double filtroPendienteMes = param.getFiltroPendienteMes();
			double filtroPendienteAnyo = param.getFiltroPendienteAnyo();
			double filtroPendienteTotales = param.getFiltroPendienteTotales();
			System.out.println("filtroPendienteSemana=" + filtroPendienteSemana + ",filtroPendienteMes="
					+ filtroPendienteMes + ",filtroPendienteAnyo=" + filtroPendienteAnyo + ",filtroPendienteTotales="
					+ filtroPendienteTotales);

			Assert.assertTrue("Pendiente no generado", (filtroPendienteSemana != 0 || filtroPendienteMes != 0
					|| filtroPendienteAnyo != 0 || filtroPendienteTotales != 0));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/*@Test
	public final void cuandoInserteinsertOperacionesPeriodoTengaEnCuentaLosFiltrosR2YPendiente() {
		try {
			// IndividuosPeriodoManager manager = new
			// IndividuosPeriodoManager();
			Connection conn = JDBCUtil.getConnection();;
			OperacionesDAO operacionesDAO = new OperacionesDAO(conn);
			InclusionesManager manager = new InclusionesManager();
			List<ParametroOperacionPeriodo> inclusiones = manager.consultarInclusiones(null);
			ParametroOperacionPeriodo param = inclusiones.get(RandomUtil.nextInt(inclusiones.size()));
			operacionesDAO.cleanOperacionesPeriodo();
			int cantidad = operacionesDAO.insertOperacionesPeriodo(param);
			ResultadoOperacionPeriodo resultado = operacionesDAO.consultarOperacionesPeriodo();
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}*/

}
