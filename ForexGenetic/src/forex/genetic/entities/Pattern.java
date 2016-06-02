/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author ricardorq85
 */
public class Pattern {

    private Map<IntegerInterval, Integer> winner = null;
    private Map<IntegerInterval, Integer> loser = null;
    private Map<IntegerInterval, Integer> zero = null;

    /**
     *
     */
    public Pattern() {
        winner = new TreeMap<IntegerInterval, Integer>();
        loser = new TreeMap<IntegerInterval, Integer>();
        zero = new TreeMap<IntegerInterval, Integer>();
    }

    /**
     *
     * @return
     */
    public Map<IntegerInterval, Integer> getLoser() {
        return loser;
    }

    /**
     *
     * @param loser
     */
    public void setLoser(Map<IntegerInterval, Integer> loser) {
        this.loser = loser;
    }

    /**
     *
     * @return
     */
    public Map<IntegerInterval, Integer> getWinner() {
        return winner;
    }

    /**
     *
     * @param winner
     */
    public void setWinner(Map<IntegerInterval, Integer> winner) {
        this.winner = winner;
    }

    /**
     *
     * @return
     */
    public Map<IntegerInterval, Integer> getZero() {
        return zero;
    }

    /**
     *
     * @param zero
     */
    public void setZero(Map<IntegerInterval, Integer> zero) {
        this.zero = zero;
    }

    @Override
    public String toString() {
        return "Pattern{" + "winner=" + winner + ", loser=" + loser + ", zero=" + zero + '}';
    }
}
