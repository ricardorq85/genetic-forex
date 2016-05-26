/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author ricardorq85
 */
public class Fortaleza implements Comparable<Fortaleza> {

    private double pips = 0.0;
    private double profit = 0.0;
    private double wonPips = 0.0;
    private double lostPips = 0.0;
    private double pipsFactor = 0.0;
    private int operationsNumber = 0;
    private int wonOperationsNumber = 0;
    private int lostOperationsNumber = 0;
    private double maxConsecutiveWonPips = 0.0;
    private double maxConsecutiveLostPips = 0.0;
    private int maxConsecutiveWonOperationsNumber = 0;
    private int maxConsecutiveLostOperationsNumber = 0;
    private double value = 0.0;

    public int getLostOperationsNumber() {
        return lostOperationsNumber;
    }

    public void setLostOperationsNumber(int lostOperationsNumber) {
        this.lostOperationsNumber = lostOperationsNumber;
    }

    public int getMaxConsecutiveLostOperationsNumber() {
        return maxConsecutiveLostOperationsNumber;
    }

    public void setMaxConsecutiveLostOperationsNumber(int maxConsecutiveLostOperationsNumber) {
        this.maxConsecutiveLostOperationsNumber = maxConsecutiveLostOperationsNumber;
    }

    public double getMaxConsecutiveLostPips() {
        return maxConsecutiveLostPips;
    }

    public void setMaxConsecutiveLostPips(double maxConsecutiveLostPips) {
        this.maxConsecutiveLostPips = maxConsecutiveLostPips;
    }

    public int getMaxConsecutiveWonOperationsNumber() {
        return maxConsecutiveWonOperationsNumber;
    }

    public void setMaxConsecutiveWonOperationsNumber(int maxConsecutiveWonOperationsNumber) {
        this.maxConsecutiveWonOperationsNumber = maxConsecutiveWonOperationsNumber;
    }

    public double getMaxConsecutiveWonPips() {
        return maxConsecutiveWonPips;
    }

    public void setMaxConsecutiveWonPips(double maxConsecutiveWonPips) {
        this.maxConsecutiveWonPips = maxConsecutiveWonPips;
    }

    public int getWonOperationsNumber() {
        return wonOperationsNumber;
    }

    public void setWonOperationsNumber(int wonOperationsNumber) {
        this.wonOperationsNumber = wonOperationsNumber;
    }

    public int getOperationsNumber() {
        return operationsNumber;
    }

    public void setOperationsNumber(int operationsNumber) {
        this.operationsNumber = operationsNumber;
    }

    public double getPips() {
        return pips;
    }

    public void setPips(double pips) {
        this.pips = pips;
    }

    public double getPipsFactor() {
        return pipsFactor;
    }

    public double getLostPips() {
        return lostPips;
    }

    public void setLostPips(double lostPips) {
        this.lostPips = lostPips;
    }

    public double getWonPips() {
        return wonPips;
    }

    public void setWonPips(double wonPips) {
        this.wonPips = wonPips;
    }

    public void setPipsFactor(double pipsFactor) {
        this.pipsFactor = pipsFactor;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(" Fortaleza=" + value);
        buffer.append("; Profit=" + this.profit);
        buffer.append("\n\t\t");
        buffer.append("; Pips=" + this.pips);
        buffer.append("; Pips Ganados=" + this.wonPips);
        buffer.append("; Pips Perdidos=" + this.lostPips);
        buffer.append("\n\t\t");
        buffer.append("; Max. Pips Consecutivos Ganados=" + this.maxConsecutiveWonPips);
        buffer.append("; Max. Pips Consecutivos Perdidos=" + this.maxConsecutiveLostPips);
        buffer.append("\n\t\t");
        buffer.append("; Numero Operaciones=" + (this.operationsNumber));
        buffer.append("; Numero Operaciones Ganadoras=" + (this.wonOperationsNumber));
        buffer.append("; Numero Operaciones Perdedoras=" + (this.lostOperationsNumber));
        buffer.append("\n\t\t");
        buffer.append("; Max. Numero Consecutivo Operaciones Ganadoras=" + (this.maxConsecutiveWonOperationsNumber));
        buffer.append("; Max. Numero Consecutivo Operaciones Perdedoras=" + (this.maxConsecutiveLostOperationsNumber));
        buffer.append("; PipsFactor=" + (this.pipsFactor));

        return buffer.toString();
    }

    public int compareTo(Fortaleza o) {
        return ((Double.isNaN(value)) ? 1 : -Double.compare(value, o.getValue()));
    }
}
