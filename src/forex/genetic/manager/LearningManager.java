/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.IndividuoReadData;
import forex.genetic.entities.IntegerInterval;
import forex.genetic.entities.Learning;
import forex.genetic.entities.LearningParametrosIndividuo;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.RelacionGeneraciones;
import forex.genetic.entities.RelacionPair;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class LearningManager {

    private static Learning learning = null;

    /**
     * @return the learning
     */
    public static Learning getLearning() {
        return learning;
    }

    /**
     * @param aLearning the learning to set
     */
    public static void setLearning(Learning aLearning) {
        learning = aLearning;
    }

    /**
     *
     * @param learning
     * @param poblacion
     * @param poblacionHija
     * @param poblacionPadre
     */
    public void processLearning(Learning learning, Poblacion poblacion,
            Poblacion poblacionHija, Poblacion poblacionPadre) {
        this.processRelacionMonedas(learning, poblacion);
        this.processRelacionGeneraciones(learning, poblacion, poblacionHija, poblacionPadre);
        this.processTakeStop(poblacion);
    }

    private void processTakeStop(Poblacion poblacion) {
        List<IndividuoEstrategia> individuosPoblacion = poblacion.getIndividuos();
        int numberLearning = PropertiesManager.getNumberLearning();
        IntegerInterval tpInterval = getLearning().getTakeProfitInterval();
        IntegerInterval slInterval = getLearning().getStopLossInterval();
        if (tpInterval == null) {
            tpInterval = getLearning().createTakeProfitInterval();
        }
        if (slInterval == null) {
            slInterval = getLearning().createStopLossInterval();
        }
        for (int i = 0; (i < individuosPoblacion.size()) && (i < numberLearning); i++) {
            IndividuoEstrategia individuoEstrategia = individuosPoblacion.get(i);
            if (individuoEstrategia.isActive()) {
                tpInterval.setLowInterval(Math.min(tpInterval.getLowInterval(), individuoEstrategia.getTakeProfit()));
                tpInterval.setHighInterval(Math.max(tpInterval.getHighInterval(), individuoEstrategia.getTakeProfit()) + 1);
                slInterval.setLowInterval(Math.min(slInterval.getLowInterval(), individuoEstrategia.getStopLoss()));
                slInterval.setHighInterval(Math.max(slInterval.getHighInterval(), individuoEstrategia.getStopLoss()) + 1);
            } else {
                i = numberLearning;
            }
        }
    }

    private void processRelacionGeneraciones(Learning learning, Poblacion poblacion,
            Poblacion poblacionHija, Poblacion poblacionPadre) {
        List<IndividuoEstrategia> individuosHijos = poblacionHija.getIndividuos();
        List<IndividuoEstrategia> individuosPadres = poblacionPadre.getIndividuos();
        List<RelacionGeneraciones> relacionMutation = learning.getRelacionMutation();
        if (relacionMutation == null) {
            relacionMutation = new ArrayList<RelacionGeneraciones>();
            learning.setRelacionMutation(relacionMutation);
        }
        List<RelacionGeneraciones> relacionOptimization = learning.getRelacionOptimization();
        if (relacionOptimization == null) {
            relacionOptimization = new ArrayList<RelacionGeneraciones>();
            learning.setRelacionOptimization(relacionOptimization);
        }
        for (Iterator<IndividuoEstrategia> itHijos = individuosHijos.iterator(); itHijos.hasNext();) {
            IndividuoEstrategia hijo = itHijos.next();
            //int indexPoblacion = individuosPoblacion.indexOf(hijo);
            IndividuoEstrategia padre1 = new IndividuoEstrategia();
            padre1.setId(hijo.getIdParent1());
            int indexPadre1 = individuosPadres.indexOf(padre1);
            if (indexPadre1 >= 0) {
                padre1 = individuosPadres.get(indexPadre1);
                if ((hijo.getIndividuoType().equals(Constants.IndividuoType.MUTATION))
                        || (hijo.getIndividuoType().equals(Constants.IndividuoType.OPTIMIZED))) {
                    if ((hijo.getFortaleza() != null) && (padre1.getFortaleza() != null)
                            && (Math.abs(hijo.getFortaleza().getPips() - padre1.getFortaleza().getPips()) > 0.0000001)) {
                        LearningParametrosIndividuo learningParametrosIndividuo = this.compareIndividuos(hijo, padre1);
                        int compare = hijo.compareTo(padre1);
                        if (compare > 0) {
                            if (hijo.getIndividuoType().equals(Constants.IndividuoType.MUTATION)) {
                                this.addRelacionGeneraciones(relacionMutation, learningParametrosIndividuo, (compare));
                            } else if (hijo.getIndividuoType().equals(Constants.IndividuoType.OPTIMIZED)) {
                                this.addRelacionGeneraciones(relacionOptimization, learningParametrosIndividuo, (compare));
                            }
                        }
                    }
                }
            }
        }
        individuosHijos.clear();
        individuosPadres.clear();

        Collections.sort(relacionMutation);
        Collections.reverse(relacionMutation);
        LogUtil.logTime("RelacionMutation size=" + relacionMutation.size(), 1);
        if (relacionMutation.size() > 0) {
            LogUtil.logTime("\n RelacionMutation 0: " + relacionMutation.get(0), 1);
            LogUtil.logTime("\n RelacionMutation " + (relacionMutation.size() - 1) + ": " + relacionMutation.get(relacionMutation.size() - 1), 1);
        }

        Collections.sort(relacionOptimization);
        Collections.reverse(relacionOptimization);
        LogUtil.logTime("RelacionOptimization size=" + relacionOptimization.size(), 1);
        if (relacionOptimization.size() > 0) {
            LogUtil.logTime("\n RelacionOptimization 0: " + relacionOptimization.get(0), 1);
            LogUtil.logTime("\n RelacionOptimization " + (relacionOptimization.size() - 1) + ": " + relacionOptimization.get(relacionOptimization.size() - 1), 1);
        }

//        for (int i = 0; i < relacionMutation.size(); i++) {
//            RelacionMutation relacionMutation1 = relacionMutation.get(i);
//            LogUtil.logTime("\n RelacionMutation " + i + ": " + relacionMutation1.toString(), 1);
//        }
    }

    private void processRelacionMonedas(Learning learning, Poblacion poblacion) {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        int size = individuos.size();
        for (int i = 0; i < size; i++) {
            IndividuoReadData individuoReadData = individuos.get(i).getIndividuoReadData();
            this.addRelacionMonedas(learning, individuoReadData, size - i);
        }
        List<RelacionPair> relacionMonedas = learning.getRelacionMonedas();
        Collections.sort(relacionMonedas);
        Collections.reverse(relacionMonedas);
    }

    private void addRelacionGeneraciones(List<RelacionGeneraciones> list, LearningParametrosIndividuo learningParametrosIndividuo, float value) {
        RelacionGeneraciones r = new RelacionGeneraciones(learningParametrosIndividuo);
        int index = list.indexOf(r);
        if (index >= 0) {
            r = list.get(index);
            int lastIndex = list.lastIndexOf(r);
            if (index != lastIndex) {
                list.remove(lastIndex);
            }
        }
        float result = r.getValue() + value;
        r.setValue(result);
        if (index < 0) {
            list.add(r);
        }
    }

    private void addRelacionMonedas(Learning learning, IndividuoReadData individuoReadData, float value) {
        RelacionPair r = new RelacionPair(individuoReadData.getPair(), individuoReadData.getOperationType());
        int index = learning.getRelacionMonedas().indexOf(r);
        if (index >= 0) {
            r = learning.getRelacionMonedas().get(index);
        }
        float result = r.getValue() + value / 1000;
        r.setValue(result);
        if (index < 0) {
            learning.getRelacionMonedas().add(r);
        }
    }

    private LearningParametrosIndividuo compareIndividuos(IndividuoEstrategia hijo, IndividuoEstrategia padre) {
        LearningParametrosIndividuo learningParametrosIndividuo = new LearningParametrosIndividuo();
        learningParametrosIndividuo.setTakeProfit((hijo.getTakeProfit() == padre.getTakeProfit()));
        learningParametrosIndividuo.setStopLoss((hijo.getStopLoss() == padre.getStopLoss()));
        //learningParametrosIndividuo.setLot((hijo.getLot() == padre.getLot()));
        //learningParametrosIndividuo.setInitialBalance((hijo.getInitialBalance() == padre.getInitialBalance()));
        IndicadorController indicadorController = ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.Individuo);
        for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
            Indicator openIndicatorHijo = null;
            if (hijo.getOpenIndicators().size() > i) {
                openIndicatorHijo = hijo.getOpenIndicators().get(i);
            }
            Indicator openIndicatorPadre = null;
            if (padre.getOpenIndicators().size() > i) {
                openIndicatorPadre = padre.getOpenIndicators().get(i);
            }
            if (openIndicatorHijo != null) {
                learningParametrosIndividuo.getOpenIndicators().add(openIndicatorHijo.equals(openIndicatorPadre));
            } else {
                if (openIndicatorPadre == null) {
                    learningParametrosIndividuo.getOpenIndicators().add(true);
                } else {
                    learningParametrosIndividuo.getOpenIndicators().add(false);
                }
            }
            Indicator closeIndicatorHijo = null;
            if (hijo.getCloseIndicators().size() > i) {
                closeIndicatorHijo = hijo.getCloseIndicators().get(i);
            }
            Indicator closeIndicatorPadre = null;
            if (padre.getCloseIndicators().size() > i) {
                closeIndicatorPadre = padre.getCloseIndicators().get(i);
            }
            if (closeIndicatorHijo != null) {
                learningParametrosIndividuo.getCloseIndicators().add(closeIndicatorHijo.equals(closeIndicatorPadre));
            } else {
                if (closeIndicatorPadre == null) {
                    learningParametrosIndividuo.getCloseIndicators().add(true);
                } else {
                    learningParametrosIndividuo.getCloseIndicators().add(false);
                }

            }
        }
        return learningParametrosIndividuo;
    }
}
