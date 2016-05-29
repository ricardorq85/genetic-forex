/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.IntegerInterval;
import forex.genetic.entities.Pattern;
import java.util.Map;

/**
 *
 * @author ricardorq85
 */
public class PatternManager {

    private Pattern patternFortalezaValue = null;
    private Pattern patternNumberOperation = null;
    private static final int FORTALEZA_VALUE_VARIATION = 10;
    private static final int MAX_FORTALEZA_VALUE = 1000;

    public PatternManager() {
        patternFortalezaValue = new Pattern();
        patternNumberOperation = new Pattern();
    }

    public void addPatternNumberOperation(double value, double pips) {
        this.addPattern(patternNumberOperation, value, pips);
    }

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

    public Map<IntegerInterval, Integer> getPatternWinnerFortalezaValue() {
        return patternFortalezaValue.getWinner();
    }

    public Map<IntegerInterval, Integer> getPatternLoserFortalezaValue() {
        return patternFortalezaValue.getLoser();
    }

    public Map<IntegerInterval, Integer> getPatternZeroFortalezaValue() {
        return patternFortalezaValue.getZero();
    }

    public Map<IntegerInterval, Integer> getPatternWinnerNumberOperation() {
        return patternNumberOperation.getWinner();
    }

    public Map<IntegerInterval, Integer> getPatternLoserNumberOperation() {
        return patternNumberOperation.getLoser();
    }

    public Map<IntegerInterval, Integer> getPatternZeroNumberOperation() {
        return patternNumberOperation.getZero();
    }
}
