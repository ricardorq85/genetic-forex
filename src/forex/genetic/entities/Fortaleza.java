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
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author ricardorq85
 */
public class Fortaleza implements Comparable<Fortaleza>, Serializable, Cloneable {

    public static final long serialVersionUID = 201101251800L;
    //public static final String currentVersion = "v6.1.1.07";  
    public static final Long currentVersionNumber = 71044L;
    //private String version = "0.0";
    private Long versionNumber = 0L;
    private double pips = 0.0;
    private double profit = 0.0;
    private double wonPips = 0.0;
    private double lostPips = 0.0;
    private int operationsNumber = 0;
    private int wonOperationsNumber = 0;
    private int lostOperationsNumber = 0;
    private double maxConsecutiveWonPips = 0.0;
    private double maxConsecutiveLostPips = 0.0;
    private int maxConsecutiveWonOperationsNumber = 0;
    private int maxConsecutiveLostOperationsNumber = 0;
    private int minConsecutiveWonOperationsNumber = 0;
    private int minConsecutiveLostOperationsNumber = 0;
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
    private Map<Integer, Integer> modaGanadoras = null;
    private Map<Integer, Integer> modaPerdedoras = null;

    public Fortaleza() {
        //setVersion(currentVersion);
        setVersionNumber(currentVersionNumber);
        setType(PropertiesManager.getFortalezaType());
        setPresentNumberPoblacion(PropertiesManager.getPresentNumberPoblacion());
        modaGanadoras = new TreeMap<Integer, Integer>();
        modaPerdedoras = new TreeMap<Integer, Integer>();
    }

    public Map<Integer, Integer> getModaGanadoras() {
        return modaGanadoras;
    }

    public void setModaGanadoras(Map<Integer, Integer> modaGanadoras) {
        this.modaGanadoras = modaGanadoras;
    }

    public Map<Integer, Integer> getModaPerdedoras() {
        return modaPerdedoras;
    }

    public void setModaPerdedoras(Map<Integer, Integer> modaPerdedoras) {
        this.modaPerdedoras = modaPerdedoras;
    }

    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getMinConsecutiveLostOperationsNumber() {
        return minConsecutiveLostOperationsNumber;
    }

    public void setMinConsecutiveLostOperationsNumber(int minConsecutiveLostOperationsNumber) {
        this.minConsecutiveLostOperationsNumber = minConsecutiveLostOperationsNumber;
    }

    public int getMinConsecutiveWonOperationsNumber() {
        return minConsecutiveWonOperationsNumber;
    }

    public void setMinConsecutiveWonOperationsNumber(int minConsecutiveWonOperationsNumber) {
        this.minConsecutiveWonOperationsNumber = minConsecutiveWonOperationsNumber;
    }

    public double getAverageConsecutiveLostPips() {
        return averageConsecutiveLostPips;
    }

    public void setAverageConsecutiveLostPips(double averageConsecutiveLostPips) {
        this.averageConsecutiveLostPips = averageConsecutiveLostPips;
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

    public double getAverageConsecutiveLostOperationsNumber() {
        return averageConsecutiveLostOperationsNumber;
    }

    public void setAverageConsecutiveLostOperationsNumber(double averageConsecutiveLostOperationsNumber) {
        this.averageConsecutiveLostOperationsNumber = averageConsecutiveLostOperationsNumber;
    }

    public double getAverageConsecutiveWonOperationsNumber() {
        return averageConsecutiveWonOperationsNumber;
    }

    public void setAverageConsecutiveWonOperationsNumber(double averageConsecutiveWonOperationsNumber) {
        this.averageConsecutiveWonOperationsNumber = averageConsecutiveWonOperationsNumber;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.versionNumber != null ? this.versionNumber.hashCode() : 0);
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
        buffer.append(" Fortaleza Value=" + value + " CalculatedValue=" + calculatedValue + " Diff=" + diffValue);
        buffer.append("; Profit=" + this.profit);
        buffer.append("; PresentFortaleza=" + this.presentFortaleza);
        buffer.append("; PresentFortalezaDouble=" + this.presentFortalezaDouble);
        buffer.append("\n\t\t");
        buffer.append("; RiskLevel=" + this.riskLevel);
        buffer.append("; Version=" + this.versionNumber);
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
        buffer.append("; Min. Numero Consecutivo Operaciones Ganadoras=" + (this.minConsecutiveWonOperationsNumber));
        buffer.append("; Min. Numero Consecutivo Operaciones Perdedoras=" + (this.minConsecutiveLostOperationsNumber));
        buffer.append("\n\t\t");
        buffer.append("; Promedio de operaciones consecutivas ganadas=" + this.averageConsecutiveWonOperationsNumber);
        buffer.append("; Promedio de pips consecutivos ganados=" + this.averageConsecutiveWonPips);
        buffer.append("; Numero de veces que realiza operaciones ganando=" + this.numConsecutiveWon);
        buffer.append("\n\t\t");
        buffer.append("; Promedio de operaciones consecutivas perdidas=" + this.averageConsecutiveLostOperationsNumber);
        buffer.append("; Promedio de pips consecutivos perdidos=" + this.averageConsecutiveLostPips);
        buffer.append("; Numero de veces que realiza operaciones perdiendo=" + this.numConsecutiveLost);
        buffer.append("\n\t\t");
        buffer.append("; Numero actual de operaciones ganadas=" + this.currentConsecutiveWonOperationsNumber);
        buffer.append("; Numero actual de operaciones perdidas=" + this.currentConsecutiveLostOperationsNumber);
        buffer.append("\n\t\t");
        buffer.append("; Moda ganadora=" + this.getModa(modaGanadoras));
        buffer.append("; Moda perdedora=" + this.getModa(modaPerdedoras));

        return buffer.toString();
    }

    @Override
    public Fortaleza clone() {
        Fortaleza f = new Fortaleza();
        f.setDiffValue(diffValue);
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
        f.setMinConsecutiveLostOperationsNumber(minConsecutiveLostOperationsNumber);
        f.setMinConsecutiveWonOperationsNumber(minConsecutiveWonOperationsNumber);
        f.setOperationsNumber(operationsNumber);
        f.setPips(pips);
        f.setProfit(profit);
        f.setRiskLevel(riskLevel);
        f.setValue(value);
        f.setCalculatedValue(calculatedValue);
        f.setVersionNumber(versionNumber);
        f.setWonOperationsNumber(wonOperationsNumber);
        f.setWonPips(wonPips);
        f.setAverageConsecutiveLostOperationsNumber(averageConsecutiveLostOperationsNumber);
        f.setAverageConsecutiveLostPips(averageConsecutiveLostPips);
        f.setAverageConsecutiveWonOperationsNumber(averageConsecutiveWonOperationsNumber);
        f.setAverageConsecutiveWonPips(averageConsecutiveWonPips);
        f.setNumConsecutiveLost(numConsecutiveLost);
        f.setNumConsecutiveWon(numConsecutiveWon);
        f.setModaGanadoras(new TreeMap<Integer, Integer>(modaGanadoras));
        f.setModaPerdedoras(new TreeMap<Integer, Integer>(modaPerdedoras));
        return f;
    }

    public Integer getModaPerdedora() {
        return this.getModa(modaPerdedoras);
    }

    public Integer getModaGanadora() {
        return this.getModa(modaGanadoras);
    }

    public Integer getModa(Map<Integer, Integer> mapModa) {
        Integer moda = -1;
        if (!mapModa.isEmpty()) {
            Integer max = Collections.max(mapModa.values());
            Set modaSet = mapModa.keySet();
            for (Iterator<Integer> it = modaSet.iterator(); it.hasNext() && moda < 0;) {
                Integer value = it.next();
                if (mapModa.get(value) == max) {
                    moda = value;
                }
            }
        }
        return moda;
    }

    public Fortaleza calculateDifference(Fortaleza f2) {
        Fortaleza f = new Fortaleza();
        f.setDiffValue(f2.diffValue - diffValue);
        f.setCurrentConsecutiveLostOperationsNumber(f2.currentConsecutiveLostOperationsNumber - currentConsecutiveLostOperationsNumber);
        f.setCurrentConsecutiveLostPips(f2.currentConsecutiveLostPips - currentConsecutiveLostPips);
        f.setCurrentConsecutiveWonOperationsNumber(f2.currentConsecutiveWonOperationsNumber - currentConsecutiveWonOperationsNumber);
        f.setCurrentConsecutiveWonPips(f2.currentConsecutiveWonPips - currentConsecutiveWonPips);
        f.setLostOperationsNumber(f2.lostOperationsNumber - lostOperationsNumber);
        f.setLostPips(f2.lostPips - lostPips);
        f.setMaxConsecutiveLostOperationsNumber(f2.maxConsecutiveLostOperationsNumber - maxConsecutiveLostOperationsNumber);
        f.setMaxConsecutiveLostPips(f2.maxConsecutiveLostPips - maxConsecutiveLostPips);
        f.setMaxConsecutiveWonOperationsNumber(f2.maxConsecutiveWonOperationsNumber - maxConsecutiveWonOperationsNumber);
        f.setMaxConsecutiveWonPips(f2.maxConsecutiveWonPips - maxConsecutiveWonPips);
        f.setMinConsecutiveLostOperationsNumber(f2.minConsecutiveLostOperationsNumber - minConsecutiveLostOperationsNumber);
        f.setMinConsecutiveWonOperationsNumber(f2.minConsecutiveWonOperationsNumber - minConsecutiveWonOperationsNumber);
        f.setOperationsNumber(f2.operationsNumber - operationsNumber);
        f.setPips(f2.pips - pips);
        f.setProfit(f2.profit - profit);
        f.setRiskLevel(f2.riskLevel - riskLevel);
        f.setValue(f2.value - value);
        f.setCalculatedValue(f2.calculatedValue - calculatedValue);
        f.setVersionNumber(versionNumber);
        f.setWonOperationsNumber(f2.wonOperationsNumber - wonOperationsNumber);
        f.setWonPips(f2.wonPips - wonPips);
        f.setAverageConsecutiveLostOperationsNumber(f2.averageConsecutiveLostOperationsNumber - averageConsecutiveLostOperationsNumber);
        f.setAverageConsecutiveLostPips(f2.averageConsecutiveLostPips - averageConsecutiveLostPips);
        f.setAverageConsecutiveWonOperationsNumber(f2.averageConsecutiveWonOperationsNumber - averageConsecutiveWonOperationsNumber);
        f.setAverageConsecutiveWonPips(f2.averageConsecutiveWonPips - averageConsecutiveWonPips);
        f.setNumConsecutiveLost(f2.numConsecutiveLost - numConsecutiveLost);
        f.setNumConsecutiveWon(f2.numConsecutiveWon - numConsecutiveWon);
        return f;
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
        if ((this.versionNumber == null) ? (other.versionNumber != null) : !this.versionNumber.equals(other.versionNumber)) {
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

    public int compareTo(Fortaleza o) {
        int compare = (Double.compare((this.operationsNumber == 0.0D) ? -100000.0D : this.value, (o.operationsNumber == 0.0D) ? -100000.0D : o.getValue())) * 50;
        if (this.getType().equals(Constants.FortalezaType.Stable)) {
            if (compare == 0) {
                compare += (Double.compare(this.getPips(), o.getPips())) * 30;
                compare += (Double.compare(this.getOperationsNumber(), o.getOperationsNumber())) * 10;
                compare += (Double.compare(this.getWonOperationsNumber() / NumberUtil.zeroToOne(this.getLostOperationsNumber()),
                        o.getWonOperationsNumber() / NumberUtil.zeroToOne(o.getLostOperationsNumber()))) * 5;
                compare += (Double.compare(this.getMaxConsecutiveWonOperationsNumber() / NumberUtil.zeroToOne(this.getMaxConsecutiveLostOperationsNumber()),
                        o.getMaxConsecutiveWonOperationsNumber() / NumberUtil.zeroToOne(o.getMaxConsecutiveLostOperationsNumber()))) * 2;
                compare += (Double.compare(Math.abs(this.getMaxConsecutiveWonPips() / NumberUtil.zeroToOne(this.getMaxConsecutiveLostPips())),
                        Math.abs(o.getMaxConsecutiveWonPips() / NumberUtil.zeroToOne(o.getMaxConsecutiveLostPips())))) * 3;
            }
        } else if (this.getType().equals(Constants.FortalezaType.Pips)) {
            if (compare == 0) {
                compare += (Double.compare(this.getPips(), o.getPips())) * 50;
            }
        } else if (this.getType().equals(Constants.FortalezaType.Embudo)) {
            if (compare == 0) {
                compare += (Double.compare(this.getOperationsNumber(), o.getOperationsNumber())) * 50;
            }
        } else if (this.getType().equals(Constants.FortalezaType.Pattern)) {
            if (compare == 0) {
                if ((this.value > 0.0D) && (this.operationsNumber > 0)) {
                    compare += (Double.compare((this.pips / this.wonOperationsNumber), (o.pips / o.wonOperationsNumber))) * 50;
                } else if ((this.value < 0.0D) && (this.operationsNumber > 0)) {
                    compare += (Double.compare((this.pips / this.lostOperationsNumber), (o.pips / o.lostOperationsNumber))) * 50;
                }
            }
        } else if (this.getType().equals(Constants.FortalezaType.PatternAdvanced)) {
            compare = 0;
        }
        if ((compare == 0) && (!this.getType().equals(Constants.FortalezaType.PatternAdvanced))) {
            compare += (Double.compare(this.getPips(), o.getPips())) * 50;
        }
        return compare;
    }

    public double calculate() {
        return this.calculate(0.0D);
    }

    public double calculate(double otherValue) {
        double fortalezaValue = 0.0;
        if (this.getVersionNumber() == null) {
            this.setType(PropertiesManager.getFortalezaType());
        }
        if (this.getType().equals(Constants.FortalezaType.Stable)) {
            fortalezaValue += (Double.compare(this.getPips(), 0.0D)) * 50.0D;
            fortalezaValue += (Double.compare((this.getWonOperationsNumber() - this.getLostOperationsNumber()), 0.0D)) * 30.0D;
            fortalezaValue += (Double.compare((this.getMaxConsecutiveWonOperationsNumber() - this.getMaxConsecutiveLostOperationsNumber()), 0.0D)) * 10.0D;
        } else if (this.getType().equals(Constants.FortalezaType.Pips)) {
            //fortalezaValue += (this.getPips() / 1000);
            fortalezaValue += (this.getPips());
        } else if (this.getType().equals(Constants.FortalezaType.Embudo)) {
            fortalezaValue += (this.getPips());
        } else if (this.getType().equals(Constants.FortalezaType.Pattern)) {
            if (this.currentConsecutiveWonOperationsNumber > 0) {
                Integer moda = getModa(modaGanadoras);
                if ((moda != null) && (this.currentConsecutiveWonOperationsNumber < moda)) {
                    if (this.currentConsecutiveWonOperationsNumber < this.averageConsecutiveWonOperationsNumber) {
                        fortalezaValue += otherValue * 0.2;
                        fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / moda)) * 0.25;
                        fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / averageConsecutiveWonOperationsNumber)) * 0.30;
                        fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / maxConsecutiveWonOperationsNumber)) * 0.10;
                        if (this.currentConsecutiveWonOperationsNumber < wonOperationsNumber) {
                            fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / (double) wonOperationsNumber)) * 0.15;
                        }
                    } else {
                        fortalezaValue += otherValue * 0.2;
                        fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / moda)) * 0.25;
                        fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / maxConsecutiveWonOperationsNumber)) * 0.10;
                        if (this.currentConsecutiveWonOperationsNumber < wonOperationsNumber) {
                            fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / (double) wonOperationsNumber)) * 0.15;
                        }
                    }
                    //fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / maxConsecutiveWonOperationsNumber)) * 0.15;
                } else if (this.currentConsecutiveWonOperationsNumber < this.averageConsecutiveWonOperationsNumber) {
                    fortalezaValue += otherValue * 0.2;
                    fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / averageConsecutiveWonOperationsNumber)) * 0.30;
                    fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / maxConsecutiveWonOperationsNumber)) * 0.10;
                    if (this.currentConsecutiveWonOperationsNumber < wonOperationsNumber) {
                        fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / (double) wonOperationsNumber)) * 0.15;
                    }
                } else if (this.currentConsecutiveWonOperationsNumber < this.maxConsecutiveWonOperationsNumber) {
                    fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / maxConsecutiveWonOperationsNumber)) * 0.10;
                    if (this.currentConsecutiveWonOperationsNumber < wonOperationsNumber) {
                        fortalezaValue += (100 - (currentConsecutiveWonOperationsNumber * 100 / (double) wonOperationsNumber)) * 0.15;
                    }
                } else {
                    fortalezaValue = -(35.0D);
                }
            } else if (this.currentConsecutiveLostOperationsNumber > 0) {
                Integer moda = getModa(modaPerdedoras);
                if ((moda != null) && (this.currentConsecutiveLostOperationsNumber < moda)) {
                    if (this.currentConsecutiveLostOperationsNumber < this.averageConsecutiveLostOperationsNumber) {
                        fortalezaValue -= otherValue * 0.2;
                        fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / moda)) * 0.25;
                        fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / averageConsecutiveLostOperationsNumber)) * 0.30;
                        fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / maxConsecutiveLostOperationsNumber)) * 0.10;
                        if (this.currentConsecutiveLostOperationsNumber < lostOperationsNumber) {
                            fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / (double) lostOperationsNumber)) * 0.15;
                        }
                    } else {
                        fortalezaValue -= otherValue * 0.2;
                        fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / moda)) * 0.25;
                        fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / maxConsecutiveLostOperationsNumber)) * 0.10;
                        if (this.currentConsecutiveLostOperationsNumber < lostOperationsNumber) {
                            fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / (double) lostOperationsNumber)) * 0.15;
                        }
                    }
                    //fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / maxConsecutiveLostOperationsNumber)) * 0.15;
                } else if (this.currentConsecutiveLostOperationsNumber < this.averageConsecutiveLostOperationsNumber) {
                    fortalezaValue -= otherValue * 0.2;
                    fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / averageConsecutiveLostOperationsNumber)) * 0.30;
                    fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / maxConsecutiveLostOperationsNumber)) * 0.10;
                    if (this.currentConsecutiveLostOperationsNumber < lostOperationsNumber) {
                        fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / (double) lostOperationsNumber)) * 0.15;
                    }
                } else if (this.currentConsecutiveLostOperationsNumber < this.maxConsecutiveLostOperationsNumber) {
                    fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / maxConsecutiveLostOperationsNumber)) * 0.10;
                    if (this.currentConsecutiveLostOperationsNumber < lostOperationsNumber) {
                        fortalezaValue -= (100 - (currentConsecutiveLostOperationsNumber * 100 / (double) lostOperationsNumber)) * 0.15;
                    }
                } else {
                    fortalezaValue = (35.0D);
                }
            } else {
                fortalezaValue = (15D);
            }
//            else {
//                if (this.currentConsecutiveWonOperationsNumber > 0) {
//                    fortalezaValue = 10.0D * (this.maxConsecutiveWonOperationsNumber - this.currentConsecutiveWonOperationsNumber);
//                } else if (this.currentConsecutiveLostOperationsNumber > 0) {
//                    fortalezaValue = -10.0D * (this.maxConsecutiveLostOperationsNumber - this.currentConsecutiveLostOperationsNumber);
//                } else {
//                    fortalezaValue = 20.0D;
//                }
//            }
        } else if (this.getType().equals(Constants.FortalezaType.PatternAdvanced)) {
            fortalezaValue = (0.0D);
        }
        return fortalezaValue;
    }
}
