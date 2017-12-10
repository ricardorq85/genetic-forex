/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.IntegerInterval;
import forex.genetic.entities.Order;
import forex.genetic.entities.Pattern;
import forex.genetic.entities.PatternAdvanced;
import forex.genetic.entities.PatternAdvancedSpecific;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Tendencia;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class PatternManager {

    private Pattern patternFortalezaValue = null;
    private Pattern patternNumberOperation = null;
    private Pattern patternMaxWonNumberOperation = null;
    private Pattern patternMaxLostNumberOperation = null;
    private Pattern patternMaxPromWonNumberOperation = null;
    private Pattern patternMaxPromLostNumberOperation = null;
    private Pattern patternModaWonNumberOperation = null;
    private Pattern patternModaLostNumberOperation = null;
    private static final int FORTALEZA_VALUE_VARIATION = 50;
    private static final int MAX_FORTALEZA_VALUE = 1000;
    private static final int MAX_VALUE_SIZE = 10;

    /**
     *
     */
    public PatternManager() {
    }

    /**
     *
     */
    public void initPatternManager() {
        patternFortalezaValue = new Pattern();
        patternNumberOperation = new Pattern();
        patternMaxWonNumberOperation = new Pattern();
        patternMaxLostNumberOperation = new Pattern();
        patternMaxPromWonNumberOperation = new Pattern();
        patternMaxPromLostNumberOperation = new Pattern();
        patternModaLostNumberOperation = new Pattern();
        patternModaWonNumberOperation = new Pattern();
    }

    /**
     *
     * @param poblacion
     */
    public void processPattern(Poblacion poblacion) {
        double tendence = 0.0D;
        LogUtil.logTime("processPattern. Individuos=" + poblacion.getIndividuos().size(), 2);
        List<IndividuoEstrategia> individuos = new ArrayList<IndividuoEstrategia>(poblacion.getIndividuos());
        int count = 0;
        for (Iterator<IndividuoEstrategia> it = individuos.iterator(); it.hasNext();) {
            IndividuoEstrategia individuoEstrategia = it.next();
            this.processPatterns(individuoEstrategia);
            if ((individuoEstrategia.getCurrentPatterns() != null) && (!individuoEstrategia.getCurrentPatterns().isEmpty())) {
                tendence += (individuoEstrategia.getFortaleza().getRiskLevel());
                count++;
            }
        }
        poblacion.setTendencia(new Tendencia(tendence / (double) count));
        LogUtil.logTime("End processPattern. Individuos=" + poblacion.getIndividuos().size(), 2);
    }

    /**
     *
     * @param individuo
     */
    public void processPatterns(IndividuoEstrategia individuo) {
        List<PatternAdvanced> patternAdvancedList = new ArrayList<PatternAdvanced>();
        List<PatternAdvanced> tempPatternAdvancedList = new ArrayList<PatternAdvanced>();
        if ((individuo.getPatterns() != null) && (!individuo.getPatterns().isEmpty())) {
            tempPatternAdvancedList = individuo.getPatterns();
            //patternAdvancedList.addAll(tempPatternAdvancedList);
        }
        int lastProcessedIndex = individuo.getLastOrderPatternIndex();
        int ordenesCompletasIndividuoSize = individuo.getOrdenes().size();
        List<Order> ordenesCompletasIndividuo = individuo.getOrdenes();
        for (int k = lastProcessedIndex + 1; k < ordenesCompletasIndividuoSize + 1; k++) {
            patternAdvancedList.clear();
            patternAdvancedList.addAll(tempPatternAdvancedList);
            List<Order> ordenesIndividuo = ordenesCompletasIndividuo.subList(0, k);
            individuo.setOrdenes(ordenesIndividuo);
            if (ordenesIndividuo != null) {
                int ordenesIndividuoSize = ordenesIndividuo.size();
                if ((ordenesIndividuoSize > lastProcessedIndex) && (ordenesIndividuoSize > 1)) {
                    LogUtil.logTime("processPattern. Individuo=" + individuo.getId(), 4);
                    //for (int i = Math.min(lastProcessedIndex, Math.max(ordenesIndividuoSize - MAX_VALUE_SIZE, 0)); i < ordenesIndividuoSize - 2; i++) {
                    for (int i = Math.max(-1, lastProcessedIndex - MAX_VALUE_SIZE) + 1; i < ordenesIndividuoSize - 2; i++) {
                        for (int j = Math.min(ordenesIndividuoSize, i + MAX_VALUE_SIZE); j > i + 2; j--) {
                            List<Order> tempPattern = new ArrayList(ordenesIndividuo.subList(i, Math.max(lastProcessedIndex, Math.min(ordenesIndividuoSize, j))));
                            PatternAdvanced tempPatternAdvanced = new PatternAdvanced(tempPattern);
                            if (tempPatternAdvanced.containsBreakOrder()) {
                                individuo.setLastOrderPatternIndex(ordenesIndividuoSize);
                                int index = patternAdvancedList.indexOf(tempPatternAdvanced);
                                if (index < 0) {
                                    patternAdvancedList.add(tempPatternAdvanced);
                                } else {
                                    tempPatternAdvanced = patternAdvancedList.get(index);
                                    double tempValue = tempPatternAdvanced.getValue() + 1.0D;
                                    tempPatternAdvanced.setValue(tempValue);
                                }
                            }
                        }
                    }
                    //reprocessPatterns(patternAdvancedList);
                    individuo.setPatterns(patternAdvancedList);
                    LogUtil.logTime("patternAdvancedList Individuo=" + individuo.getId() + " patternAdvancedList Size=" + patternAdvancedList.size(), 5);
                    List<PatternAdvancedSpecific> currentPatternAdvancedList = processCurrrentPatterns(individuo);
                    reprocessCurrentPatterns(currentPatternAdvancedList);
                    individuo.setCurrentPatterns(currentPatternAdvancedList);
                    LogUtil.logTime("currentPatternAdvancedList Individuo=" + individuo.getId() + " currentPatternAdvancedList Size=" + currentPatternAdvancedList.size(), 5);
                }
            }
            individuo.calculateCurrentPatternValue();
        }
    }

    private void reprocessPatterns(List<PatternAdvanced> patternAdvancedList) {
        for (Iterator<PatternAdvanced> it = patternAdvancedList.iterator(); it.hasNext();) {
            PatternAdvanced patternAdvanced = it.next();
            if (patternAdvanced.getValue() < 2) {
                it.remove();
            }
        }
    }

    private void reprocessCurrentPatterns(List<PatternAdvancedSpecific> currentPatternAdvancedList) {
        //Collections.sort(currentPatternAdvancedList);
        //Collections.reverse(currentPatternAdvancedList);
        List<PatternAdvancedSpecific> temp = new ArrayList<PatternAdvancedSpecific>(currentPatternAdvancedList);
        currentPatternAdvancedList.clear();
        for (int i = 0; i < temp.size(); i++) {
            PatternAdvancedSpecific patternAdvancedSpecific = temp.get(i);
            PatternAdvanced patternAdvanced = patternAdvancedSpecific.getPatternAdvanced();
            boolean contains = false;
            for (int j = i + 1; j < temp.size() && !contains; j++) {
                PatternAdvancedSpecific patternAdvancedSpecific2 = temp.get(j);
                PatternAdvanced patternAdvanced2 = patternAdvancedSpecific2.getPatternAdvanced();
                contains = (patternAdvanced.contains(patternAdvanced2));
            }
            for (int j = 0; j < currentPatternAdvancedList.size() && !contains; j++) {
                PatternAdvancedSpecific patternAdvancedSpecific2 = currentPatternAdvancedList.get(j);
                PatternAdvanced patternAdvanced2 = patternAdvancedSpecific2.getPatternAdvanced();
                contains = (patternAdvanced.contains(patternAdvanced2));
            }
            if (!contains) {
                currentPatternAdvancedList.add(patternAdvancedSpecific);
            }
        }

        /*
         * for (int i = 0; i < currentPatternAdvancedList.size(); i++) {
         * PatternAdvancedSpecific patternAdvancedSpecific =
         * currentPatternAdvancedList.get(i); PatternAdvanced patternAdvanced =
         * patternAdvancedSpecific.getPatternAdvanced(); for (int j = i + 1; j <
         * currentPatternAdvancedList.size(); j++) { PatternAdvancedSpecific
         * patternAdvancedSpecific2 = currentPatternAdvancedList.get(j);
         * PatternAdvanced patternAdvanced2 =
         * patternAdvancedSpecific2.getPatternAdvanced(); if
         * (patternAdvanced2.contains(patternAdvanced)) {
         * currentPatternAdvancedList.remove(patternAdvancedSpecific2); } } }
         */
    }

    private List<PatternAdvancedSpecific> processCurrrentPatterns(IndividuoEstrategia individuo) {
        List<PatternAdvancedSpecific> currentPatternAdvancedList = new ArrayList<>();
        List<PatternAdvanced> patternAdvancedList = individuo.getPatterns();
        if (!patternAdvancedList.isEmpty()) {
            List<Order> ordenesIndividuo = individuo.getOrdenes();
            if ((ordenesIndividuo != null) && (!(ordenesIndividuo.isEmpty()))) {
                int size = ordenesIndividuo.size();
                boolean foundAny = true;
                Order lastOrder = ordenesIndividuo.get(size - 1);
                boolean breakLastOrder = false;
                if (lastOrder != null) {
                    for (int j = ordenesIndividuo.size() - 2; j > 0 && foundAny; j--) {
                        Order breakOrder = ordenesIndividuo.get(j);
                        if (breakLastOrder || !lastOrder.comparePattern(breakOrder)) {
                            breakLastOrder = true;
                            List<Order> tempPattern = ordenesIndividuo.subList(j, size);
                            foundAny = false;
                            for (Iterator<PatternAdvanced> it = patternAdvancedList.iterator(); it.hasNext();) {
                                PatternAdvanced pattern = it.next();
                                //if (pattern.getValue() > 1.0D) {
                                if (pattern.containsCurrent(tempPattern)) {
                                    foundAny = true;
                                    PatternAdvancedSpecific patternAdvancedSpecific = new PatternAdvancedSpecific(pattern, tempPattern.size());
                                    if (!currentPatternAdvancedList.contains(patternAdvancedSpecific)) {
                                        currentPatternAdvancedList.add(patternAdvancedSpecific);
                                    }
                                }
                                //}
                            }
                        }
                    }
                }
            }
        }
        return currentPatternAdvancedList;
    }

    /**
     *
     */
    public void printModas() {
        LogUtil.logTime("patternFortalezaValue " + patternFortalezaValue.toString(), 1);
        LogUtil.logTime("patternNumberOperation " + patternNumberOperation.toString(), 1);
        LogUtil.logTime("patternMaxWonNumberOperation " + patternMaxWonNumberOperation.toString(), 1);
        LogUtil.logTime("patternMaxLostNumberOperation " + patternMaxLostNumberOperation.toString(), 1);
        LogUtil.logTime("patternMaxPromWonNumberOperation " + patternMaxPromWonNumberOperation.toString(), 1);
        LogUtil.logTime("patternMaxPromLostNumberOperation " + patternMaxPromLostNumberOperation.toString(), 1);
        LogUtil.logTime("patternModaWonNumberOperation " + patternModaWonNumberOperation.toString(), 1);
        LogUtil.logTime("patternModaLostNumberOperation " + patternModaLostNumberOperation.toString(), 1);
    }

    /**
     *
     * @param value
     * @param pips
     */
    public void addPatternModaLostNumberOperation(double value, double pips) {
        this.addPattern(patternModaLostNumberOperation, value, pips);
    }

    /**
     *
     * @param value
     * @param pips
     */
    public void addPatternModaWonNumberOperation(double value, double pips) {
        this.addPattern(patternModaWonNumberOperation, value, pips);
    }

    /**
     *
     * @param value
     * @param pips
     */
    public void addPatternMaxPromLostNumberOperation(double value, double pips) {
        this.addPattern(patternMaxPromLostNumberOperation, value, pips);
    }

    /**
     *
     * @param value
     * @param pips
     */
    public void addPatternMaxPromWonNumberOperation(double value, double pips) {
        this.addPattern(patternMaxPromWonNumberOperation, value, pips);
    }

    /**
     *
     * @param value
     * @param pips
     */
    public void addPatternMaxLostNumberOperation(double value, double pips) {
        this.addPattern(patternMaxLostNumberOperation, value, pips);
    }

    /**
     *
     * @param value
     * @param pips
     */
    public void addPatternMaxWonNumberOperation(double value, double pips) {
        this.addPattern(patternMaxWonNumberOperation, value, pips);
    }

    /**
     *
     * @param value
     * @param pips
     */
    public void addPatternNumberOperation(double value, double pips) {
        this.addPattern(patternNumberOperation, value, pips);
    }

    /**
     *
     * @param value
     * @param pips
     */
    public void addPatternFortalezaValue(double value, double pips) {
        this.addPattern(patternFortalezaValue, value, pips);
    }

    private void addPattern(Pattern pattern, double value, double pips) {
        if (pips > 0) {
            this.addPatternFortalezaValue(pattern.getWinner(), value);
        } else if (pips < 0) {
            this.addPatternFortalezaValue(pattern.getLoser(), value);
        } else {
            this.addPatternFortalezaValue(pattern.getZero(), value);
        }
    }

    /**
     *
     * @param patternValue
     * @param value
     */
    public void addPatternFortalezaValue(Map<IntegerInterval, Integer> patternValue, double value) {
        boolean found = false;
        for (int i = MAX_FORTALEZA_VALUE; (i > 0) && !found; i -= FORTALEZA_VALUE_VARIATION) {
            if ((value > (i - FORTALEZA_VALUE_VARIATION)) && (value <= i)) {
                this.add(patternValue, new IntegerInterval(i - FORTALEZA_VALUE_VARIATION, i), 1);
                found = true;
            }
        }
    }

    private void add(Map<IntegerInterval, Integer> patternValue, IntegerInterval key, Integer addValue) {
        Integer moda = patternValue.get(key);
        if (moda == null) {
            moda = addValue;
        } else {
            moda += addValue;
        }
        patternValue.put(key, moda);
    }

    /**
     *
     * @return
     */
    public Map<IntegerInterval, Integer> getPatternWinnerFortalezaValue() {
        return patternFortalezaValue.getWinner();
    }

    /**
     *
     * @return
     */
    public Map<IntegerInterval, Integer> getPatternLoserFortalezaValue() {
        return patternFortalezaValue.getLoser();
    }

    /**
     *
     * @return
     */
    public Map<IntegerInterval, Integer> getPatternZeroFortalezaValue() {
        return patternFortalezaValue.getZero();
    }

    /**
     *
     * @return
     */
    public Map<IntegerInterval, Integer> getPatternWinnerNumberOperation() {
        return patternNumberOperation.getWinner();
    }

    /**
     *
     * @return
     */
    public Map<IntegerInterval, Integer> getPatternLoserNumberOperation() {
        return patternNumberOperation.getLoser();
    }

    /**
     *
     * @return
     */
    public Map<IntegerInterval, Integer> getPatternZeroNumberOperation() {
        return patternNumberOperation.getZero();
    }
}
