/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.Constants.FortalezaType;
import forex.genetic.util.NumberUtil;
import java.io.Serializable;

/**
 *
 * @author ricardorq85
 */
public class Fortaleza implements Comparable<Fortaleza>, Serializable, Cloneable {

    public static final long serialVersionUID = 201101251800L;
    public static final String currentVersion = "5.1.08";
    private String version = "0.0";
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
    private double averageConsecutiveWonPips = 0.0;
    private double averageConsecutiveLostPips = 0.0;
    private double averageConsecutiveWonOperationsNumber = 0;
    private double averageConsecutiveLostOperationsNumber = 0;
    private double currentConsecutiveWonPips = 0.0;
    private double currentConsecutiveLostPips = 0.0;
    private int numConsecutiveLost = 0;
    private int numConsecutiveWon = 0;
    private int currentConsecutiveWonOperationsNumber = 0;
    private int currentConsecutiveLostOperationsNumber = 0;
    private double value = 0.0;
    private double calculatedValue = 0.0;
    private int presentFortaleza = 0;
    private double presentFortalezaDouble = 0;
    private double diffValue = 0.0;
    private double riskLevel = 0.0;
    private FortalezaType type = null;
    private int presentNumberPoblacion = 0;

    public Fortaleza() {
        setVersion(currentVersion);
        setType(PropertiesManager.getFortalezaType());
        setPresentNumberPoblacion(PropertiesManager.getPropertyInt(Constants.PRESENT_NUMBER_POBLACION));
    }

    public double getAverageConsecutiveLostOperationsNumber() {
        return averageConsecutiveLostOperationsNumber;
    }

    public void setAverageConsecutiveLostOperationsNumber(double averageConsecutiveLostOperationsNumber) {
        this.averageConsecutiveLostOperationsNumber = averageConsecutiveLostOperationsNumber;
    }

    public double getAverageConsecutiveLostPips() {
        return averageConsecutiveLostPips;
    }

    public void setAverageConsecutiveLostPips(double averageConsecutiveLostPips) {
        this.averageConsecutiveLostPips = averageConsecutiveLostPips;
    }

    public double getAverageConsecutiveWonOperationsNumber() {
        return averageConsecutiveWonOperationsNumber;
    }

    public void setAverageConsecutiveWonOperationsNumber(double averageConsecutiveWonOperationsNumber) {
        this.averageConsecutiveWonOperationsNumber = averageConsecutiveWonOperationsNumber;
    }

    public double getAverageConsecutiveWonPips() {
        return averageConsecutiveWonPips;
    }

    public void setAverageConsecutiveWonPips(double averageConsecutiveWonPips) {
        this.averageConsecutiveWonPips = averageConsecutiveWonPips;
    }

    public int getNumConsecutiveWon() {
        return numConsecutiveWon;
    }

    public void setNumConsecutiveWon(int numConsecutiveWon) {
        this.numConsecutiveWon = numConsecutiveWon;
    }

    public int getNumConsecutiveLost() {
        return numConsecutiveLost;
    }

    public void setNumConsecutiveLost(int numConsecutiveLost) {
        this.numConsecutiveLost = numConsecutiveLost;
    }

    public void setAverageConsecutiveLostOperationsNumber(int averageConsecutiveLostOperationsNumber) {
        this.averageConsecutiveLostOperationsNumber = averageConsecutiveLostOperationsNumber;
    }

    public double getPresentFortalezaDouble() {
        return presentFortalezaDouble;
    }

    public void setPresentFortalezaDouble(double presentFortalezaDouble) {
        this.presentFortalezaDouble = presentFortalezaDouble;
    }

    public int getPresentFortaleza() {
        return presentFortaleza;
    }

    public void setPresentFortaleza(int presentFortaleza) {
        this.presentFortaleza = presentFortaleza;
    }

    public int getCurrentConsecutiveLostOperationsNumber() {
        return currentConsecutiveLostOperationsNumber;
    }

    public void setCurrentConsecutiveLostOperationsNumber(int currentConsecutiveLostOperationsNumber) {
        this.currentConsecutiveLostOperationsNumber = currentConsecutiveLostOperationsNumber;
    }

    public double getCurrentConsecutiveLostPips() {
        return currentConsecutiveLostPips;
    }

    public void setCurrentConsecutiveLostPips(double currentConsecutiveLostPips) {
        this.currentConsecutiveLostPips = currentConsecutiveLostPips;
    }

    public int getCurrentConsecutiveWonOperationsNumber() {
        return currentConsecutiveWonOperationsNumber;
    }

    public void setCurrentConsecutiveWonOperationsNumber(int currentConsecutiveWonOperationsNumber) {
        this.currentConsecutiveWonOperationsNumber = currentConsecutiveWonOperationsNumber;
    }

    public double getCurrentConsecutiveWonPips() {
        return currentConsecutiveWonPips;
    }

    public void setCurrentConsecutiveWonPips(double currentConsecutiveWonPips) {
        this.currentConsecutiveWonPips = currentConsecutiveWonPips;
    }

    public int getPresentNumberPoblacion() {
        return presentNumberPoblacion;
    }

    public void setPresentNumberPoblacion(int presentNumberPoblacion) {
        this.presentNumberPoblacion = presentNumberPoblacion;
    }

    public FortalezaType getType() {
        return type;
    }

    public void setType(FortalezaType type) {
        this.type = type;
    }

    public double getCalculatedValue() {
        return calculatedValue;
    }

    public void setCalculatedValue(double calculatedValue) {
        this.calculatedValue = calculatedValue;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public double getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(double riskLevel) {
        this.riskLevel = riskLevel;
    }

    public double getDiffValue() {
        return diffValue;
    }

    public void setDiffValue(double diffValue) {
        this.diffValue = diffValue;
    }

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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Fortaleza other = (Fortaleza) obj;
        if ((this.version == null) ? (other.version != null) : !this.version.equals(other.version)) {
            return false;
        }
        if (Double.doubleToLongBits(this.pips) != Double.doubleToLongBits(other.pips)) {
            return false;
        }
        if (Double.doubleToLongBits(this.wonPips) != Double.doubleToLongBits(other.wonPips)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lostPips) != Double.doubleToLongBits(other.lostPips)) {
            return false;
        }
        if (this.operationsNumber != other.operationsNumber) {
            return false;
        }
        if (this.wonOperationsNumber != other.wonOperationsNumber) {
            return false;
        }
        if (this.lostOperationsNumber != other.lostOperationsNumber) {
            return false;
        }
        if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.version != null ? this.version.hashCode() : 0);
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.pips) ^ (Double.doubleToLongBits(this.pips) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.wonPips) ^ (Double.doubleToLongBits(this.wonPips) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.lostPips) ^ (Double.doubleToLongBits(this.lostPips) >>> 32));
        hash = 79 * hash + this.operationsNumber;
        hash = 79 * hash + this.wonOperationsNumber;
        hash = 79 * hash + this.lostOperationsNumber;
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(" Fortaleza Value=" + value + " CalculatedValue=" + calculatedValue + " Diff=" + diffValue + " Pipsfactor=" + pipsFactor);
        buffer.append("; Profit=" + this.profit);
        buffer.append("; PresentFortaleza=" + this.presentFortaleza);
        buffer.append("; PresentFortalezaDouble=" + this.presentFortalezaDouble);
        buffer.append("\n\t\t");
        buffer.append("; RiskLevel=" + this.riskLevel);
        buffer.append("; Version=" + this.version);
        buffer.append("; Type=" + this.type);
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
        buffer.append("\n\t\t");
        buffer.append("; Promedio de operaciones ganadas=" + this.averageConsecutiveWonOperationsNumber);
        buffer.append("; Promedio de pips ganados=" + this.averageConsecutiveWonPips);
        buffer.append("; Numero de operaciones que ganan consecutivamente=" + this.numConsecutiveWon);
        buffer.append("\n\t\t");
        buffer.append("; Promedio de operaciones perdidas=" + this.averageConsecutiveLostOperationsNumber);
        buffer.append("; Promedio de pips perdidos=" + this.averageConsecutiveLostPips);
        buffer.append("; Numero de operaciones que ganan consecutivamente=" + this.numConsecutiveLost);

        return buffer.toString();
    }

    public int compareTo(Fortaleza o) {
        int compare = (-Double.compare(this.value, o.getValue()));
        if (this.getType().equals(Constants.FortalezaType.Stable)) {
            if (compare == 0) {
                compare += (-Double.compare(this.getPips(), o.getPips())) * 15;
                compare += (-Double.compare(this.getOperationsNumber(), o.getOperationsNumber())) * 10;
                compare += (-Double.compare(this.getWonOperationsNumber() / NumberUtil.zeroToOne(this.getLostOperationsNumber()),
                        o.getWonOperationsNumber() / NumberUtil.zeroToOne(o.getLostOperationsNumber()))) * 10;
                compare += (-Double.compare(this.getMaxConsecutiveWonOperationsNumber() / NumberUtil.zeroToOne(this.getMaxConsecutiveLostOperationsNumber()),
                        o.getMaxConsecutiveWonOperationsNumber() / NumberUtil.zeroToOne(o.getMaxConsecutiveLostOperationsNumber()))) * 5;
                compare += (-Double.compare(this.getPresentFortaleza(), o.getPresentFortaleza())) * 5;
                compare += (-Double.compare(Math.abs(this.getMaxConsecutiveWonPips()) / NumberUtil.zeroToOne(this.getMaxConsecutiveLostPips()),
                        Math.abs(o.getMaxConsecutiveWonPips()) / NumberUtil.zeroToOne(o.getMaxConsecutiveLostPips()))) * 8;
            }
        } else if (this.getType().equals(Constants.FortalezaType.Embudo)) {
            if (compare == 0) {
                compare = (-Double.compare(this.getOperationsNumber(), o.getOperationsNumber()));
            }
        }
        return compare;
    }

    @Override
    public Fortaleza clone() throws CloneNotSupportedException {
        Fortaleza f = new Fortaleza();
        f.setDiffValue(diffValue);
        f.setPresentFortaleza(presentFortaleza);
        f.setCurrentConsecutiveLostOperationsNumber(currentConsecutiveLostOperationsNumber);
        f.setCurrentConsecutiveLostPips(currentConsecutiveLostPips);
        f.setCurrentConsecutiveWonOperationsNumber(currentConsecutiveWonOperationsNumber);
        f.setCurrentConsecutiveWonPips(currentConsecutiveWonPips);
        f.setLostOperationsNumber(lostOperationsNumber);
        f.setLostPips(lostPips);
        f.setMaxConsecutiveLostOperationsNumber(maxConsecutiveLostOperationsNumber);
        f.setMaxConsecutiveLostPips(maxConsecutiveLostPips);
        f.setMaxConsecutiveWonOperationsNumber(maxConsecutiveWonOperationsNumber);
        f.setMaxConsecutiveWonPips(maxConsecutiveWonPips);
        f.setOperationsNumber(operationsNumber);
        f.setPips(pips);
        f.setPipsFactor(pipsFactor);
        f.setProfit(profit);
        f.setRiskLevel(riskLevel);
        f.setValue(value);
        f.setCalculatedValue(calculatedValue);
        f.setVersion(version);
        f.setWonOperationsNumber(wonOperationsNumber);
        f.setWonPips(wonPips);
        f.setAverageConsecutiveLostOperationsNumber(averageConsecutiveLostOperationsNumber);
        f.setAverageConsecutiveLostOperationsNumber(maxConsecutiveLostOperationsNumber);
        f.setAverageConsecutiveLostPips(averageConsecutiveLostPips);
        f.setAverageConsecutiveWonOperationsNumber(averageConsecutiveWonOperationsNumber);
        f.setAverageConsecutiveWonPips(averageConsecutiveWonPips);
        f.setNumConsecutiveLost(numConsecutiveLost);
        f.setNumConsecutiveWon(numConsecutiveWon);
        return f;
    }
}
