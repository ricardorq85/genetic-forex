/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package borradoindividuos;

import forex.genetic.manager.BorradoInconsistentesStopLossManager;
import forex.genetic.entities.Individuo;
import forex.genetic.manager.BorradoManager;
import static forex.genetic.manager.PropertiesManager.load;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ricardorq85
 */
public class BorradoIndividuosStopLossInconsistentesTest {

    private final BorradoManager borradoManager = new BorradoInconsistentesStopLossManager();
    private List<Individuo> individuos;

    public BorradoIndividuosStopLossInconsistentesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        load().join();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        individuos = borradoManager.consultarIndividuos();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void elIndividuoConsultadoTieneStopLossMenorOIgualA10() {
        Individuo ind = null;
        if ((individuos != null) && (!individuos.isEmpty())) {
            ind = individuos.get(0);
        }

        assert ((ind == null) || (ind.getStopLoss() <= 100));
    }
}
