/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.util.io.PropertiesManager;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Fortaleza;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.IndividuoReadData;
import forex.genetic.entities.Order;
import forex.genetic.entities.PatternAdvanced;
import forex.genetic.entities.PatternAdvancedSpecific;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author USER
 */
public class IndividuoEstrategiaManager {

    private final transient IndicadorController indicadorController = ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.Individuo);

    public double calculateRiskLevel(IndividuoEstrategia individuo, Fortaleza fortaleza, int index) {
        double riskLevel = 1.0;
        if (fortaleza == null) {
            fortaleza = individuo.getFortaleza();
        }
        if (fortaleza.getType().equals(Constants.FortalezaType.Stable)) {
            double risk1;
            double risk2;
            double risk3;
            double percentLevel = individuo.configuredRiskLevel / Constants.MAX_RISK_LEVEL;
            double percentFortalezaNumber = (fortaleza.getWonOperationsNumber() == 0) ? 0.0D
                    : fortaleza.getLostOperationsNumber() / (double) (fortaleza.getWonOperationsNumber());
            double percentFortalezaPips = (fortaleza.getWonPips() == 0) ? 0.0D
                    : Math.abs(fortaleza.getLostPips() / (double) (fortaleza.getWonPips()));
            //double percentFortaleza = Math.max(percentFortalezaNumber, percentFortalezaPips);
            double percentFortaleza = (percentFortalezaNumber + percentFortalezaPips) / 2;

            risk1 = (percentFortaleza > percentLevel) ? (1.0) / (1000.0) : (10.0);
            risk2 = ((fortaleza.getOperationsNumber() / (double) (index + 1))
                    < PropertiesManager.getMinOperNumByPeriod()) ? (2.0) / (1000.0) : (10.0);
            risk3 = (!processObligatory(individuo)) ? (3.0) / (100.0) : (10.0);

            if ((risk1 > 1.0) && (risk2 > 1.0) && (risk3 > 1.0)) {
                riskLevel = 10.0;
                //LogUtil.logTime("Risk level > 1.0 " + this.id, 1);
            } else if ((risk1 <= 1.0) && (risk2 <= 1.0) && (risk3 <= 1.0)) {
                riskLevel = (1.0) / (1000.0);
            } else {
                if (risk1 <= 1.0) {
                    if (risk2 <= 1.0) {
                        riskLevel = (5.0) / (1000.0);
                    } else if (risk3 <= 1.0) {
                        riskLevel = (6.0) / (1000.0);
                    } else {
                        riskLevel = (7.0) / (1000.0);
                    }
                } else if (risk2 <= 1.0) {
                    if (risk3 <= 1.0) {
                        riskLevel = (8.0) / (1000.0);
                    } else {
                        riskLevel = (9.0) / (1000.0);
                    }
                } else if (risk3 <= 1.0) {
                    riskLevel = (10.0) / (1000.0);
                }
            }
        } else if (fortaleza.getType().equals(Constants.FortalezaType.Pattern)) {
            if (fortaleza.getOperationsNumber() == 0) {
                riskLevel = (0.2);
            } else if (individuo.activeOperation) {
                riskLevel = (0.8);
            }
        }
        return riskLevel;
    }

    public boolean processObligatory(IndividuoEstrategia individuoEstrategia) {
        boolean process = true;
        for (int i = 0; process && (i < indicadorController.getIndicatorNumber()); i++) {
            Indicator openIndicator = null;
            if (individuoEstrategia.getOpenIndicators().size() > i) {
                openIndicator = individuoEstrategia.getOpenIndicators().get(i);
            }
            Indicator closeIndicator = null;
            if (individuoEstrategia.getCloseIndicators().size() > i) {
                closeIndicator = individuoEstrategia.getCloseIndicators().get(i);
            }
            IndicadorManager indicatorManager = indicadorController.getManagerInstance(i);
            if (indicatorManager.isObligatory() && ((openIndicator == null) || (closeIndicator == null))) {
                process = false;
            }
        }
        return process;
    }

    /**
     *
     * @param fortalezas
     * @return
     */
    protected boolean isContinuo(List<Fortaleza> fortalezas) {
        boolean continuo = true;
        int presentNumberPoblacion = PropertiesManager.getPresentNumberPoblacion();
        for (int i = fortalezas.size() - 1; (continuo && i >= presentNumberPoblacion); i--) {
            if (Math.abs((fortalezas.get(i).getCalculatedValue()
                    - fortalezas.get(i - presentNumberPoblacion).getCalculatedValue())) > 0.000000001D) {
                continuo = false;
            }
        }
        return continuo;
    }

    /**
     *
     * @param individuoReadData
     */
    public void corregir(IndividuoReadData individuoReadData) {
        this.setFortaleza(null);
        this.setListaFortaleza(null);
        this.setProcessedUntil(0);
        this.setProcessedFrom(0);
        this.setOptimizedOpenIndicators(null);
        this.setOptimizedCloseIndicators(null);
        this.setOpenPoint(null);
        this.setPrevOpenPoint(null);
        this.setOpenOperationIndex(0);
        this.setOpenPoblacionIndex(1);
        this.setActiveOperation(false);
        this.setCloseOperationIndex(0);
        this.setClosePoblacionIndex(-1);
        this.setOrdenes(new ArrayList<>());
        this.setCurrentOrder(null);
        this.setIndividuoReadData(individuoReadData);
        if (this.getTakeProfit() < PropertiesManager.getMinTP()) {
            this.setTakeProfit(PropertiesManager.getMinTP());
        } else if (this.getTakeProfit() > PropertiesManager.getMaxTP()) {
            this.setTakeProfit(PropertiesManager.getMaxTP());
        }
        if (this.getStopLoss() < PropertiesManager.getMinSL()) {
            this.setStopLoss(PropertiesManager.getMinSL());
        } else if (this.getStopLoss() > PropertiesManager.getMaxSL()) {
            this.setStopLoss(PropertiesManager.getMaxSL());
        }
        if (this.getLot() < PropertiesManager.getMinLot()) {
            this.setLot(PropertiesManager.getMinLot());
        } else if ((this.getLot() > PropertiesManager.getMaxLot())) {
            this.setLot(PropertiesManager.getMaxLot());
        }
        corregirIndicadores(this.openIndicators);
        corregirIndicadores(this.closeIndicators);
    }

    /**
     *
     * @param indicadores
     */
    protected void corregirIndicadores(List<? extends Indicator> indicadores) {
        for (int i = 0; i < indicadores.size(); i++) {
            if (indicadores.get(i) != null) {
                IntervalIndicator indicator = (IntervalIndicator) indicadores.get(i);
                if ((indicator.getInterval() == null)
                        || (indicator.getInterval().getLowInterval() == null)
                        || (indicator.getInterval().getHighInterval() == null)
                        || (Double.isNaN(indicator.getInterval().getLowInterval()))
                        || (Double.isNaN(indicator.getInterval().getHighInterval()))) {
                    indicadores.set(i, null);
                }
            }
        }
    }

    /**
     *
     */
    public void calculateCurrentPatternValue() {
        double calcValue = 0.0D;
        double risk = 0.0D;
        double won = 0.0D;
        double lost = 0.0D;
        double totalValue = 0.0D;
        double wonPattern = 0.0D;
        int countWon = 0;
        int countLost = 0;
        int size;
        if (this.currentPatterns != null) {
            size = this.currentPatterns.size();
            for (PatternAdvancedSpecific patternAdvancedSpecific : this.currentPatterns) {
                PatternAdvanced pattern = patternAdvancedSpecific.getPatternAdvanced();
                List<Order> patternList = pattern.getPattern();
                int index = patternAdvancedSpecific.getIndex();
                if (index < patternList.size()) {
                    Order order = patternList.get(index);
                    if (pattern.getValue() > 1.0D) {
                        wonPattern += (Double.compare(order.getPips(), 0.0D) > 0) ? pattern.getValue() : 0.0D;
                    }
                    calcValue += ((Double.compare(order.getPips(), 0.0D)) * pattern.getValue());
                    totalValue += pattern.getValue();
                    won += (Double.compare(order.getPips(), 0.0D) > 0) ? pattern.getValue() : 0.0D;
                    lost += (Double.compare(order.getPips(), 0.0D) < 0) ? pattern.getValue() : 0.0D;
                    countWon += (Double.compare(order.getPips(), 0.0D) > 0) ? 1 : 0;
                    countLost += (Double.compare(order.getPips(), 0.0D) < 0) ? 1 : 0;
                }
            }
            if (calcValue == 0.0D) {
                //risk = 0.001D;
            } else if ((calcValue > 0) && (this.activeOperation)) {
                calcValue /= 5000.0D;
                //risk = 0.002D;
            } else {
                double minByPeriod = PropertiesManager.getMinOperNumByPeriod();
                double countByPeriod = (this.ordenes.size() / (double) this.processedUntil);
                double promCalcValueByOrders = (totalValue / (double) this.ordenes.size());
                if ((calcValue > 0) && (promCalcValueByOrders < (PropertiesManager.getRiskLevel() / 70.0D))) {
                    calcValue /= 4000.0D;
                    //risk = 0.003D;
                } else if ((calcValue > 0) && (countByPeriod < minByPeriod)) {
                    calcValue /= 3000.0D;
                    //risk = 0.004D;
                } else {
                    if ((won + lost) > 0) {
                        double riskTemp = ((won) / (won + lost));
                        //risk = ((won) / (won + lost)) * 1.0;
                        risk = (riskTemp * 0.95 + ((countWon / (double) size)) * 0.025 + (promCalcValueByOrders) * 0.025);
                        if (riskTemp < (PropertiesManager.getRiskLevel() / 10.0D)) {
                            calcValue = ((calcValue / 1000.0D) * risk);
                            //risk *= 0.005D;
                        } else {
                            LogUtil.logTime(" RISK > RISK LEVEL Individuo=" + this.id + ";Ordenes=" + this.ordenes.size() + ";(risk=" + risk + ")", 1);
                        }
                    }
                }
            }
        }
        this.fortaleza.setValue(calcValue * risk);
        //this.fortaleza.setValue(risk);
        this.fortaleza.setRiskLevel(risk);
    }

    public boolean equalsReal(Object obj) {
        if (obj instanceof IndividuoEstrategia) {
            IndividuoEstrategia objIndividuo = (IndividuoEstrategia) obj;
            boolean value = ((Collections.frequency(this.openIndicators, null) != this.openIndicators.size()
                    && this.openIndicators.equals(objIndividuo.openIndicators))
                    //&& (this.closeIndicators.equals(objIndividuo.closeIndicators)
                    //&& Collections.frequency(this.closeIndicators, null) != this.closeIndicators.size())
                    && ((Math.abs((objIndividuo.takeProfit - this.takeProfit) / (double) this.takeProfit) < 0.02)
                    && (Math.abs((objIndividuo.stopLoss - this.stopLoss) / (double) this.stopLoss) < 0.02)));
            if (!value) {
                Fortaleza f;
                Fortaleza objF;
                int size = this.listaFortaleza.size();
                int presentNumberPoblacion = PropertiesManager.getPresentNumberPoblacion();
                boolean temp = (presentNumberPoblacion > 1);
                for (int i = size - 1; (temp) && (i >= (size - presentNumberPoblacion) + 1) && (i >= 0); i--) {
                    f = this.listaFortaleza.get(i);
                    objF = objIndividuo.listaFortaleza.get(i);
                    temp = (f == null) && (objF == null);
                    if (!temp) {
                        temp = ((f != null) && (objF != null)) && (f.equals(objF));
                    }
                }
                value = temp;
            }
            return value;
        } else {
            return false;
        }
    }

    public String toFileString(DateInterval dateInterval) {
        final DateFormat format = new SimpleDateFormat(DATE_PATTERN);
        StringBuilder buffer = new StringBuilder();
        buffer.append("ProcessedFrom&Until=");
        buffer.append((this.processedFrom));
        buffer.append("-");
        buffer.append((this.processedUntil));
        buffer.append("/");
        buffer.append(PropertiesManager.getPropertyInt(Constants.END_POBLACION));
        buffer.append(",");
        buffer.append("EstrategiaId=");
        buffer.append((this.id));
        buffer.append(",");

        if ((dateInterval == null) || (dateInterval.getLowInterval() == null) || (dateInterval.getHighInterval() == null)) {
            dateInterval = new DateInterval();
            dateInterval.setLowInterval(new Date());
            dateInterval.setHighInterval(new Date());
        }
        buffer.append("VigenciaLower=");
        buffer.append(format.format(dateInterval.getLowInterval()));
        buffer.append(",");
        //long vigDias = PropertiesManager.getPropertyLong(Constants.VIGENCIA);
        //long vigMillis = (vigDias * 24 * 60 * 60 * 1000);
        //Date vigencia = new Date(dateInterval.getHighInterval().getTime() + vigMillis);
        buffer.append("VigenciaHigher=");
        buffer.append((format.format(dateInterval.getHighInterval())));
        buffer.append(",");

        buffer.append("Active=" + this.isActive() + ",");
        buffer.append("Pair=" + PropertiesManager.getPair() + ",");
        buffer.append("Operation=" + PropertiesManager.getOperationType() + ",");

        buffer.append("TakeProfit=" + this.takeProfit + ",");
        buffer.append("StopLoss=" + this.stopLoss + ",");
        buffer.append("Lote=" + this.lot + ",");
        /*        buffer.append("MaxConsecutiveLostOperationsNumber=" + this.fortaleza.getMaxConsecutiveLostOperationsNumber() + ",");
         buffer.append("MaxConsecutiveWonOperationsNumber=" + this.fortaleza.getMaxConsecutiveWonOperationsNumber() + ",");
         buffer.append("MinConsecutiveLostOperationsNumber=" + this.fortaleza.getMinConsecutiveLostOperationsNumber() + ",");
         buffer.append("MinConsecutiveWonOperationsNumber=" + this.fortaleza.getMinConsecutiveWonOperationsNumber() + ",");
         buffer.append("AverageConsecutiveLostOperationsNumber=" + Math.round(this.fortaleza.getAverageConsecutiveLostOperationsNumber()) + ",");
         buffer.append("AverageConsecutiveWonOperationsNumber=" + Math.round(this.fortaleza.getAverageConsecutiveWonOperationsNumber()) + ",");
         * */
        for (Indicator indicator : this.openIndicators) {
            if (indicator != null) {
                buffer.append(indicator.toFileString("open"));
            } else {
                buffer.append("null");
            }
            buffer.append(",");
        }
        for (Indicator indicator : this.closeIndicators) {
            if (indicator != null) {
                buffer.append(indicator.toFileString("close"));
            } else {
                buffer.append("null");
            }
            buffer.append(",");
        }
        return buffer.toString();
    }

    public IndividuoEstrategia cloneIndividuoEstrategia() {
        IndividuoEstrategia cloned;
        try {
            cloned = (IndividuoEstrategia) super.clone();
        } catch (CloneNotSupportedException ex) {
            cloned = new IndividuoEstrategia();
        }
        cloned.id = this.id;
        cloned.fileId = this.fileId;
        cloned.processedUntil = this.processedUntil;
        cloned.processedFrom = this.processedFrom;
        cloned.generacion = this.generacion;
        cloned.parent1 = null;
        cloned.parent2 = null;
        cloned.idParent1 = this.idParent1;
        cloned.idParent2 = this.idParent2;
        cloned.individuoType = this.individuoType;
        cloned.takeProfit = this.takeProfit;
        cloned.stopLoss = this.stopLoss;
        cloned.lot = this.lot;
        cloned.initialBalance = this.initialBalance;
        cloned.openIndicators = this.openIndicators;
        cloned.closeIndicators = this.closeIndicators;
        cloned.optimizedOpenIndicators = this.optimizedOpenIndicators;
        cloned.optimizedCloseIndicators = this.optimizedCloseIndicators;
        cloned.fortaleza = this.fortaleza.clone();
        cloned.listaFortaleza = this.listaFortaleza;
        cloned.openOperationValue = this.openOperationValue;
        cloned.openSpread = this.openSpread;
        cloned.openPoblacionIndex = this.openPoblacionIndex;
        cloned.openOperationIndex = this.openOperationIndex;
        cloned.prevOpenPoint = this.prevOpenPoint;
        cloned.openPoint = this.openPoint;
        cloned.activeOperation = this.activeOperation;
        cloned.closePoblacionIndex = this.closePoblacionIndex;
        cloned.closeOperationIndex = this.closeOperationIndex;
        cloned.creationDate = this.creationDate;
        cloned.individuoReadData = this.individuoReadData;
        cloned.currentOrder = this.currentOrder;
        cloned.ordenes = new ArrayList(this.ordenes);
        return cloned;
    }

}
