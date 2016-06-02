/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import forex.genetic.entities.indicator.Indicator;
import forex.genetic.util.Constants;
import forex.genetic.util.Constants.IndividuoType;
import forex.genetic.util.io.PropertiesManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class IndividuoEstrategia implements Comparable<IndividuoEstrategia>, Serializable, Cloneable {

    public static final long serialVersionUID = 201101251800L;
    protected static final String DATE_PATTERN = "yyyy.MM.dd HH:mm";
    protected String id = "0";
    protected String fileId = null;
    protected int processedUntil = 0;
    protected int processedFrom = 0;
    protected int generacion = -1;
    protected IndividuoEstrategia parent1 = null;
    protected IndividuoEstrategia parent2 = null;
    protected String idParent1 = null;
    protected String idParent2 = null;
    protected IndividuoType individuoType = IndividuoType.INITIAL;
    protected int takeProfit = 0;
    protected int stopLoss = 0;
    protected double lot = 0;
    protected int initialBalance = 0;
    protected List<? extends Indicator> openIndicators = null;
    protected List<? extends Indicator> closeIndicators = null;
    protected List<? extends Indicator> optimizedOpenIndicators = null;
    protected List<? extends Indicator> optimizedCloseIndicators = null;
    protected Fortaleza fortaleza = null;
    protected List<Fortaleza> listaFortaleza = null;
    protected Order currentOrder = null;
    protected transient List<Order> ordenes;
    protected double openOperationValue = 0.0D;
    protected double openSpread = 0.0D;
    protected int openPoblacionIndex = 1;
    protected int openOperationIndex = 0;
    protected Point prevOpenPoint = null;
    protected Point openPoint = null;
    protected boolean activeOperation = false;
    protected int closePoblacionIndex = -1;
    protected int closeOperationIndex = 0;
    protected Date creationDate = null;
    protected IndividuoReadData individuoReadData = null;
    protected transient List<PatternAdvanced> patterns = null;
    protected transient List<PatternAdvancedSpecific> currentPatterns = null;
    protected transient int lastOrderPatternIndex = 0;
    private final int indicatorNumber;
    private int configuredRiskLevel;
    private int configuredPresentNumberPoblacion;

//    public IndividuoEstrategia() {
    //      this(0, null, null, IndividuoType.INITIAL);
    //}
    //public IndividuoEstrategia(IndividuoEstrategia parent1) {
    //  this(0, parent1, null, IndividuoType.INITIAL);
    //}
    /**
     *
     * @param id
     */
//    public IndividuoEstrategia(String id) {
    //       this(0, null, null, null, id);
    //  }
    /**
     *
     * @param generacion
     * @param parent1
     * @param parent2
     * @param individuoType
     */
//    public IndividuoEstrategia(int generacion, IndividuoEstrategia parent1,
    //           IndividuoEstrategia parent2, IndividuoType individuoType) {
    //       this(generacion, parent1, parent2, individuoType, IndividuoManager.nextId());
    //  }
    /**
     *
     * @param generacion
     * @param parent1
     * @param parent2
     * @param individuoType
     * @param id
     */
    public IndividuoEstrategia(int generacion, IndividuoEstrategia parent1,
            IndividuoEstrategia parent2, IndividuoType individuoType,
            String id, int indicatorNumber, String fileId,
            Constants.OperationType operationType, String pair, int configuredRiskLevel,
            int configuredPresentNumberPoblacion) {
        this.fileId = fileId;
        this.id = id;
        this.generacion = generacion;
        this.parent1 = parent1;
        this.parent2 = parent2;
        this.individuoType = individuoType;
        this.creationDate = new Date();
        this.indicatorNumber = indicatorNumber;
        this.optimizedCloseIndicators = Collections.synchronizedList(new ArrayList(indicatorNumber));
        this.optimizedOpenIndicators = Collections.synchronizedList(new ArrayList(indicatorNumber));
        this.individuoReadData = new IndividuoReadData();
        this.individuoReadData.setOperationType(operationType);
        this.individuoReadData.setPair(pair);
        this.ordenes = new ArrayList<>();
        this.configuredRiskLevel = configuredRiskLevel;
        this.configuredPresentNumberPoblacion = configuredPresentNumberPoblacion;
    }

    /**
     *
     * @return
     */
    public int getLastOrderPatternIndex() {
        return lastOrderPatternIndex;
    }

    /**
     *
     * @param lastOrderPatternIndex
     */
    public void setLastOrderPatternIndex(int lastOrderPatternIndex) {
        this.lastOrderPatternIndex = lastOrderPatternIndex;
    }

    /**
     *
     * @return
     */
    public List<PatternAdvancedSpecific> getCurrentPatterns() {
        return currentPatterns;
    }

    /**
     *
     * @param currentPatterns
     */
    public void setCurrentPatterns(List<PatternAdvancedSpecific> currentPatterns) {
        this.currentPatterns = currentPatterns;
    }

    /**
     *
     * @return
     */
    public List<PatternAdvanced> getPatterns() {
        return patterns;
    }

    /**
     *
     * @param patterns
     */
    public void setPatterns(List<PatternAdvanced> patterns) {
        this.patterns = patterns;
    }

    /**
     *
     * @return
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     *
     * @param currentOrder
     */
    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    /**
     *
     * @return
     */
    public List<Order> getOrdenes() {
        return ordenes;
    }

    /**
     *
     * @param ordenes
     */
    public void setOrdenes(List<Order> ordenes) {
        this.ordenes = ordenes;
    }

    /**
     *
     * @return
     */
    public IndividuoReadData getIndividuoReadData() {
        return individuoReadData;
    }

    /**
     *
     * @param individuoReadData
     */
    public void setIndividuoReadData(IndividuoReadData individuoReadData) {
        this.individuoReadData = individuoReadData;
    }

    /**
     *
     * @return
     */
    public Date getCreationDate() {
        return (this.creationDate = creationDate != null ? new Date(creationDate.getTime()) : null);
    }

    /**
     *
     * @param creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate != null ? new Date(creationDate.getTime()) : null;
    }

    /**
     *
     * @return
     */
    public double getOpenSpread() {
        return openSpread;
    }

    /**
     *
     * @param openSpread
     */
    public void setOpenSpread(double openSpread) {
        this.openSpread = openSpread;
    }

    /**
     *
     * @return
     */
    public boolean isActiveOperation() {
        return activeOperation;
    }

    /**
     *
     * @param activeOperation
     */
    public void setActiveOperation(boolean activeOperation) {
        this.activeOperation = activeOperation;
    }

    /**
     *
     * @return
     */
    public Point getPrevOpenPoint() {
        return prevOpenPoint;
    }

    /**
     *
     * @param prevOpenPoint
     */
    public void setPrevOpenPoint(Point prevOpenPoint) {
        this.prevOpenPoint = prevOpenPoint;
    }

    /**
     *
     * @return
     */
    public Point getOpenPoint() {
        return openPoint;
    }

    /**
     *
     * @param openPoint
     */
    public void setOpenPoint(Point openPoint) {
        this.openPoint = openPoint;
    }

    /**
     *
     * @return
     */
    public int getCloseOperationIndex() {
        return closeOperationIndex;
    }

    /**
     *
     * @param closeOperationIndex
     */
    public void setCloseOperationIndex(int closeOperationIndex) {
        this.closeOperationIndex = closeOperationIndex;
    }

    /**
     *
     * @return
     */
    public int getClosePoblacionIndex() {
        return closePoblacionIndex;
    }

    /**
     *
     * @param closePoblacionIndex
     */
    public void setClosePoblacionIndex(int closePoblacionIndex) {
        this.closePoblacionIndex = closePoblacionIndex;
    }

    /**
     *
     * @return
     */
    public int getOpenOperationIndex() {
        return openOperationIndex;
    }

    /**
     *
     * @param openOperationIndex
     */
    public void setOpenOperationIndex(int openOperationIndex) {
        this.openOperationIndex = openOperationIndex;
    }

    /**
     *
     * @return
     */
    public double getOpenOperationValue() {
        return openOperationValue;
    }

    /**
     *
     * @param openOperationValue
     */
    public void setOpenOperationValue(double openOperationValue) {
        this.openOperationValue = openOperationValue;
    }

    /**
     *
     * @return
     */
    public int getOpenPoblacionIndex() {
        return openPoblacionIndex;
    }

    /**
     *
     * @param openPoblacionIndex
     */
    public void setOpenPoblacionIndex(int openPoblacionIndex) {
        this.openPoblacionIndex = openPoblacionIndex;
    }

    /**
     *
     * @return
     */
    public int getProcessedFrom() {
        return processedFrom;
    }

    /**
     *
     * @param processedFrom
     */
    public void setProcessedFrom(int processedFrom) {
        this.processedFrom = processedFrom;
    }

    /**
     *
     * @return
     */
    public int getProcessedUntil() {
        return processedUntil;
    }

    /**
     *
     * @param processedUntil
     */
    public void setProcessedUntil(int processedUntil) {
        this.processedUntil = processedUntil;
    }

    /**
     *
     * @return
     */
    public IndividuoType getIndividuoType() {
        return individuoType;
    }

    /**
     *
     * @param individuoType
     */
    public void setIndividuoType(IndividuoType individuoType) {
        this.individuoType = individuoType;
    }

    /**
     *
     * @return
     */
    public IndividuoEstrategia getParent1() {
        return parent1;
    }

    /**
     *
     * @param parent1
     */
    public void setParent1(IndividuoEstrategia parent1) {
        this.idParent1 = (parent1 == null) ? null : parent1.id;
    }

    /**
     *
     * @return
     */
    public IndividuoEstrategia getParent2() {
        return parent2;
    }

    /**
     *
     * @param parent2
     */
    public void setParent2(IndividuoEstrategia parent2) {
        this.idParent2 = (parent2 == null) ? null : parent2.id;
    }

    /**
     *
     * @return
     */
    public Fortaleza getFortaleza() {
        return fortaleza;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @param fortaleza
     */
    public void setFortaleza(Fortaleza fortaleza) {
        this.fortaleza = fortaleza;
    }

    /**
     *
     * @return
     */
    public List<? extends Indicator> getOpenIndicators() {
        return openIndicators;
    }

    /**
     *
     * @param openIndicators
     */
    public void setOpenIndicators(List<? extends Indicator> openIndicators) {
        this.openIndicators = openIndicators;
    }

    /**
     *
     * @return
     */
    public List<? extends Indicator> getCloseIndicators() {
        return closeIndicators;
    }

    /**
     *
     * @param closeIndicators
     */
    public void setCloseIndicators(List<? extends Indicator> closeIndicators) {
        this.closeIndicators = closeIndicators;
    }

    /**
     *
     * @return
     */
    public List<? extends Indicator> getOptimizedCloseIndicators() {
        return optimizedCloseIndicators;
    }

    /**
     *
     * @param optimizedCloseIndicators
     */
    public void setOptimizedCloseIndicators(List<? extends Indicator> optimizedCloseIndicators) {
        this.optimizedCloseIndicators = optimizedCloseIndicators;
    }

    /**
     *
     * @return
     */
    public List<? extends Indicator> getOptimizedOpenIndicators() {
        return optimizedOpenIndicators;
    }

    /**
     *
     * @param optimizedOpenIndicators
     */
    public void setOptimizedOpenIndicators(List<? extends Indicator> optimizedOpenIndicators) {
        this.optimizedOpenIndicators = optimizedOpenIndicators;
    }

    /**
     *
     * @return
     */
    public int getInitialBalance() {
        return initialBalance;
    }

    /**
     *
     * @param initialBalance
     */
    public void setInitialBalance(int initialBalance) {
        this.initialBalance = initialBalance;
    }

    /**
     *
     * @return
     */
    public int getStopLoss() {
        return stopLoss;
    }

    /**
     *
     * @param stopLoss
     */
    public void setStopLoss(int stopLoss) {
        this.stopLoss = stopLoss;
    }

    /**
     *
     * @return
     */
    public int getTakeProfit() {
        return takeProfit;
    }

    /**
     *
     * @param takeProfit
     */
    public void setTakeProfit(int takeProfit) {
        this.takeProfit = takeProfit;
    }

    /**
     *
     * @return
     */
    public double getLot() {
        return lot;
    }

    /**
     *
     * @param lot
     */
    public void setLot(double lot) {
        this.lot = lot;
    }

    /**
     *
     * @return
     */
    public int getGeneracion() {
        return generacion;
    }

    /**
     *
     * @param generacion
     */
    public void setGeneracion(int generacion) {
        this.generacion = generacion;
    }

    /**
     *
     * @return
     */
    public String getFileId() {
        return fileId;
    }

    /**
     *
     * @param fileId
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /**
     *
     * @return
     */
    public String getIdParent1() {
        return idParent1;
    }

    /**
     *
     * @param idParent1
     */
    public void setIdParent1(String idParent1) {
        this.idParent1 = idParent1;
    }

    /**
     *
     * @return
     */
    public String getIdParent2() {
        return idParent2;
    }

    /**
     *
     * @param idParent2
     */
    public void setIdParent2(String idParent2) {
        this.idParent2 = idParent2;
    }

    /**
     *
     * @return
     */
    public List<Fortaleza> getListaFortaleza() {
        return listaFortaleza;
    }

    /**
     *
     * @param listaFortaleza
     */
    public void setListaFortaleza(List<Fortaleza> listaFortaleza) {
        if (listaFortaleza == null) {
            this.listaFortaleza = new ArrayList<>();
        } else {
            this.listaFortaleza = listaFortaleza;
        }
    }

    /**
     *
     * @return
     */
    public boolean isActive() {
        if (fortaleza.getType().equals(Constants.FortalezaType.Pattern)) {
            return ((this.fortaleza.getValue() >= 800.0) && (!this.activeOperation));
        } else if (fortaleza.getType().equals(Constants.FortalezaType.PatternAdvanced)) {
            return ((this.fortaleza.getRiskLevel() >= (this.configuredRiskLevel / 10.0D)) && (!this.activeOperation));
        } else {
            return (this.fortaleza.getValue() > 1.0);
        }
    }

    @Override
    public int hashCode() {
        return (this.getId().hashCode());
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(" Id=" + (this.id));
        buffer.append("; Generacion=" + (this.generacion));
        buffer.append("; ProcessedFrom=" + (this.processedFrom));
        buffer.append("; ProcessedUntil=" + (this.processedUntil));
        buffer.append("; IndividuoType=" + (this.individuoType));
        buffer.append("; ActiveOperation=" + (this.activeOperation) + ";");
        buffer.append("\n\t");
        buffer.append(((this.fortaleza == null) ? 0.0 : this.fortaleza.toString()));
        buffer.append("\n\t");
        if (idParent1 != null) {
            buffer.append("; Padre 1=" + idParent1);
        }
        if (idParent2 != null) {
            buffer.append("; Padre 2=" + idParent2);
        }
        buffer.append("; CreationDate=" + this.creationDate);
        buffer.append("; TakeProfit=" + this.takeProfit);
        buffer.append("; Stoploss=" + this.stopLoss);
        buffer.append("; Lot=" + this.lot);
        buffer.append("; Initial Balance=" + this.initialBalance);
        buffer.append("\n\t");
        buffer.append("; Open Indicadores=" + (this.openIndicators));
        buffer.append("\n\t");
        buffer.append("; Close Indicadores=" + (this.closeIndicators));
//        buffer.append("\n\t");
//        buffer.append("; Orden actual=" + (this.currentOrder));
        buffer.append("\n\t");
        buffer.append("; Ordenes=" + (this.ordenes));
        buffer.append("\n\t");
        buffer.append("; Patrones=" + ((this.patterns == null) ? "null" : this.patterns.size()));
        buffer.append("; Patrones actuales=" + ((this.currentPatterns == null) ? "null" : this.currentPatterns.size()) + "-" + ((this.currentPatterns == null) ? "" : this.currentPatterns));

        return buffer.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IndividuoEstrategia) {
            IndividuoEstrategia objIndividuo = (IndividuoEstrategia) obj;
            boolean value;
            value = (this.getId().equals(objIndividuo.getId()));
            if (!value) {
                value = ((((this.openIndicators == null) && (objIndividuo.openIndicators == null))
                        || ((this.openIndicators != null) && (objIndividuo.openIndicators != null) && (this.openIndicators.equals(objIndividuo.openIndicators))))
                        && (((this.closeIndicators == null) && (objIndividuo.closeIndicators == null))
                        || ((this.closeIndicators != null) && (objIndividuo.closeIndicators != null) && (this.closeIndicators.equals(objIndividuo.closeIndicators))))
                        && (this.takeProfit == objIndividuo.takeProfit)
                        && (this.stopLoss == objIndividuo.stopLoss));
            }
            return value;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(IndividuoEstrategia other) {
        int compare = 0;
        if (!this.equals(other)) {
            if ((this.fortaleza == null) && (other.fortaleza == null)) {
                compare = 0;
            } else if (this.fortaleza == null) {
                compare = -1;
            } else if (other.fortaleza == null) {
                compare = 1;
            } else {
                if (fortaleza.getType().equals(Constants.FortalezaType.PatternAdvanced)) {
                    compare = this.comparePattern(other);
                } else {
                    Fortaleza f1 = this.fortaleza;
                    Fortaleza fo1 = other.fortaleza;
                    int presentNumberPoblacion = this.configuredPresentNumberPoblacion;
                    int size = this.listaFortaleza.size();
                    compare = f1.compareTo(fo1);
                    compare *= (presentNumberPoblacion + 1);
                    //if ((compare == 0) && ((f1.getValue() != 0.0) || (fo1.getValue() != 0.0))) {
                    if ((f1.getValue() != 0.0) && (fo1.getValue() != 0.0)
                            && (f1.getValue() != Double.NEGATIVE_INFINITY) && (fo1.getValue() != Double.NEGATIVE_INFINITY)) {
                        for (int i = size - 1; (i > (size - presentNumberPoblacion) + 1) && (i > 0); i--) {
                            Fortaleza f2 = this.listaFortaleza.get(i - 1);
                            Fortaleza f = f2.calculateDifference(f1);
                            Fortaleza fo2 = other.listaFortaleza.get(i - 1);
                            Fortaleza fo = fo2.calculateDifference(fo1);
                            f1 = f2;
                            fo1 = fo2;
                            if ((f == null) && (fo == null)) {
                                compare += 0;
                            } else if (f == null) {
                                compare = 1;
                            } else if (fo == null) {
                                compare = -1;
                            } else {
                                f.setValue(f.calculate() * this.calculateRiskLevel(f, i - 1));
                                fo.setValue(fo.calculate() * this.calculateRiskLevel(fo, i - 1));
                                compare += f.compareTo(fo) * (presentNumberPoblacion - (size - i - 1));
                            }
                        }
                    }
                }
            }

            if (compare == 0) {
                if (this.creationDate == null) {
                    compare = -1;
                } else if (other.creationDate == null) {
                    compare = 1;
                } else {
                    compare = (this.creationDate).compareTo((other.creationDate));
                }
            }
        }
        return (Integer.valueOf(compare).compareTo(0));
    }

    protected int comparePattern(IndividuoEstrategia other) {
        int compare = 0;
        if (compare == 0) {
            //LogUtil.logTime(this.id + " compareTo.value.0 " + other.id, 1);
            if ((this.fortaleza != null) && (other.fortaleza != null)) {
                double thisRisk = this.fortaleza.getRiskLevel();
                double otherRisk = other.fortaleza.getRiskLevel();
                double thisValue = this.fortaleza.getValue();
                double otherValue = other.fortaleza.getValue();
                compare = (Double.compare(thisValue, otherValue));
                if (compare == 0) {
                    //      LogUtil.logTime(this.id + " compareTo.value.1 " + other.id, 1);
                    compare = (Double.compare(thisRisk, otherRisk));
                }
            }
            if (compare == 0) {
                //LogUtil.logTime(this.id + " compareTo.currentPatterns2.0 " + other.id, 1);
                if ((this.currentPatterns != null) && (other.currentPatterns != null)) {
                    compare = Integer.valueOf(this.currentPatterns.size()).compareTo(other.currentPatterns.size());
                }
            }
            if (compare == 0) {
                //       LogUtil.logTime(this.id + " compareTo.ordenes2.0 " + other.id, 1);
                if ((this.ordenes != null) && (other.ordenes != null)) {
                    compare = Integer.valueOf(this.ordenes.size()).compareTo(other.ordenes.size());
                }
            }
        }
        if (compare == 0) {
            //LogUtil.logTime(this.id + " compareTo.getOperationsNumber.0 " + other.id, 1);
            if ((this.fortaleza != null) && (other.fortaleza != null)) {
                compare = Integer.valueOf(this.fortaleza.getOperationsNumber()).compareTo(other.fortaleza.getOperationsNumber());
            }
        }
        //LogUtil.logTime(this.id + " comparePattern " + other.id + "=" + compare, 1);
        return compare;
    }

}
