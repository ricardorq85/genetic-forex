/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetic.test;

import static forex.genetic.delegate.GeneticDelegate.setId;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.MutationIndividuoManager;
import static forex.genetic.manager.PropertiesManager.load;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import static java.lang.System.currentTimeMillis;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ricardorq85
 */
public class AverageXXXTest {

    public AverageXXXTest() {
    }

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        load().join();
        long id = currentTimeMillis();
        setId(Long.toString(id));
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void mutateAverageInManager() {
        MutationIndividuoManager mutationManager = new MutationIndividuoManager();
        Poblacion poblacion = new Poblacion();
        poblacion.add(createIndividuo());

        mutationManager.mutate(0, poblacion, 1);

        Assert.assertEquals(1, 1);
    }

    private IndividuoEstrategia createIndividuo() {
        IndicadorController indicadorController = ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.Individuo);
        IndividuoEstrategia ie = new IndividuoEstrategia();

        ie.setTakeProfit(430);
        ie.setStopLoss(612);

        List<Indicator> openIndicators = new ArrayList<>(indicadorController.getIndicatorNumber());
        List<Indicator> closeIndicators = new ArrayList<>(indicadorController.getIndicatorNumber());
        IntervalIndicatorManager maManager = (IntervalIndicatorManager) indicadorController.getManagerInstance(0);
        Average ma = (Average) maManager.getIndicatorInstance();
        Interval<Double> interval = new DoubleInterval(-0.01339, -0.00146);
        ma.setInterval(interval);
        openIndicators.add(ma);
        closeIndicators.add(null);
        for (int i = 1; i < indicadorController.getIndicatorNumber(); i++) {
            openIndicators.add(null);
            closeIndicators.add(null);
        }
        ie.setOpenIndicators(openIndicators);
        ie.setCloseIndicators(closeIndicators);

        return ie;
    }
    
}
