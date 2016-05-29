/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import forex.genetic.manager.indicator.IndicatorManager;
import java.util.Vector;
import forex.genetic.manager.PropertiesManager;
import java.util.Collections;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.manager.IndividuoManager;
import forex.genetic.util.Constants;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static forex.genetic.util.Constants.*;
import forex.genetic.util.LogUtil;
import java.util.Iterator;

/**
 *
 * @author ricardorq85
 */
public class IndividuoEstrategia implements Comparable<IndividuoEstrategia>, Serializable, Cloneable {

    public static final long serialVersionUID = 201101251800L;
    private static final DateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    private String id = "0";
    private String fileId = null;
    private int processedUntil = 0;
    private int processedFrom = 0;
    private int generacion = -1;
    private IndividuoEstrategia parent1 = null;
    private IndividuoEstrategia parent2 = null;
    private String idParent1 = null;
    private String idParent2 = null;
    private IndividuoType individuoType = IndividuoType.INITIAL;
    private int takeProfit = 0;
    private int stopLoss = 0;
    private double lot = 0;
    private int initialBalance = 0;
    private List<? extends Indicator> openIndicators = null;
    private List<? extends Indicator> closeIndicators = null;
    private List<? extends Indicator> optimizedOpenIndicators = null;
    private List<? extends Indicator> optimizedCloseIndicators = null;
    private Fortaleza fortaleza = null;
    private List<Fortaleza> listaFortaleza = null;
    private Order currentOrder = null;
    private transient List<Order> ordenes = new ArrayList<Order>();
    private double openOperationValue = 0.0D;
    private double openSpread = 0.0D;
    private int openPoblacionIndex = 1;
    private int openOperationIndex = 0;
    private Point prevOpenPoint = null;
    private Point openPoint = null;
    private boolean activeOperation = false;
    private int closePoblacionIndex = -1;
    private int closeOperationIndex = 0;
    private Date creationDate = null;
    private IndividuoReadData individuoReadData = null;
    private transient List<PatternAdvanced> patterns = null;
    private transient List<PatternAdvancedSpecific> currentPatterns = null;
    private transient int lastOrderPatternIndex = 0;

    public IndividuoEstrategia() {
        this(0, null, null, IndividuoType.INITIAL);
    }

    public IndividuoEstrategia(IndividuoEstrategia parent1) {
        this(0, parent1, null, IndividuoType.INITIAL);
    }

    public IndividuoEstrategia(int generacion, IndividuoEstrategia parent1, IndividuoEstrategia parent2, IndividuoType individuoType) {
        setFileId(PropertiesManager.getFileId());
        setId(IndividuoManager.nextId());
        setGeneracion(generacion);
        setParent1(parent1);
        setParent2(parent2);
        setIndividuoType(individuoType);
        setCreationDate(new Date());
        this.optimizedCloseIndicators = new Vector<Indicator>(IndicatorManager.getIndicatorNumber());
        this.optimizedOpenIndicators = new Vector<Indicator>(IndicatorManager.getIndicatorNumber());
        this.individuoReadData = new IndividuoReadData();
        this.individuoReadData.setOperationType(PropertiesManager.getOperationType());
        this.individuoReadData.setPair(PropertiesManager.getPair());
    }

    public int getLastOrderPatternIndex() {
        return lastOrderPatternIndex;
    }

    public void setLastOrderPatternIndex(int lastOrderPatternIndex) {
        this.lastOrderPatternIndex = lastOrderPatternIndex;
    }

    public List<PatternAdvancedSpecific> getCurrentPatterns() {
        return currentPatterns;
    }

    public void setCurrentPatterns(List<PatternAdvancedSpecific> currentPatterns) {
        this.currentPatterns = currentPatterns;
    }

    public List<PatternAdvanced> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<PatternAdvanced> patterns) {
        this.patterns = patterns;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public List<Order> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<Order> ordenes) {
        this.ordenes = ordenes;
    }

    public IndividuoReadData getIndividuoReadData() {
        return individuoReadData;
    }

    public void setIndividuoReadData(IndividuoReadData individuoReadData) {
        this.individuoReadData = individuoReadData;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public double getOpenSpread() {
        return openSpread;
    }

    public void setOpenSpread(double openSpread) {
        this.openSpread = openSpread;
    }

    public boolean isActiveOperation() {
        return activeOperation;
    }

    public void setActiveOperation(boolean activeOperation) {
        this.activeOperation = activeOperation;
    }

    public Point getPrevOpenPoint() {
        return prevOpenPoint;
    }

    public void setPrevOpenPoint(Point prevOpenPoint) {
        this.prevOpenPoint = prevOpenPoint;
    }

    public Point getOpenPoint() {
        return openPoint;
    }

    public void setOpenPoint(Point openPoint) {
        this.openPoint = openPoint;
    }

    public int getCloseOperationIndex() {
        return closeOperationIndex;
    }

    public void setCloseOperationIndex(int closeOperationIndex) {
        this.closeOperationIndex = closeOperationIndex;
    }

    public int getClosePoblacionIndex() {
        return closePoblacionIndex;
    }

    public void setClosePoblacionIndex(int closePoblacionIndex) {
        this.closePoblacionIndex = closePoblacionIndex;
    }

    public int getOpenOperationIndex() {
        return openOperationIndex;
    }

    public void setOpenOperationIndex(int openOperationIndex) {
        this.openOperationIndex = openOperationIndex;
    }

    public double getOpenOperationValue() {
        return openOperationValue;
    }

    public void setOpenOperationValue(double openOperationValue) {
        this.openOperationValue = openOperationValue;
    }

    public int getOpenPoblacionIndex() {
        return openPoblacionIndex;
    }

    public void setOpenPoblacionIndex(int openPoblacionIndex) {
        this.openPoblacionIndex = openPoblacionIndex;
    }

    public int getProcessedFrom() {
        return processedFrom;
    }

    public void setProcessedFrom(int processedFrom) {
        this.processedFrom = processedFrom;
    }

    public int getProcessedUntil() {
        return processedUntil;
    }

    public void setProcessedUntil(int processedUntil) {
        this.processedUntil = processedUntil;
    }

    public IndividuoType getIndividuoType() {
        return individuoType;
    }

    public void setIndividuoType(IndividuoType individuoType) {
        this.individuoType = individuoType;
    }

    public IndividuoEstrategia getParent1() {
        return parent1;
    }

    public void setParent1(IndividuoEstrategia parent1) {
        this.idParent1 = (parent1 == null) ? null : parent1.id;
    }

    public IndividuoEstrategia getParent2() {
        return parent2;
    }

    public void setParent2(IndividuoEstrategia parent2) {
        this.idParent2 = (parent2 == null) ? null : parent2.id;
    }

    public Fortaleza getFortaleza() {
        return fortaleza;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFortaleza(Fortaleza fortaleza) {
        this.fortaleza = fortaleza;
    }

    public List<? extends Indicator> getOpenIndicators() {
        return openIndicators;
    }

    public void setOpenIndicators(List<? extends Indicator> openIndicators) {
        this.openIndicators = openIndicators;
    }

    public List<? extends Indicator> getCloseIndicators() {
        return closeIndicators;
    }

    public void setCloseIndicators(List<? extends Indicator> closeIndicators) {
        this.closeIndicators = closeIndicators;
    }

    public List<? extends Indicator> getOptimizedCloseIndicators() {
        return optimizedCloseIndicators;
    }

    public void setOptimizedCloseIndicators(List<? extends Indicator> optimizedCloseIndicators) {
        this.optimizedCloseIndicators = optimizedCloseIndicators;
    }

    public List<? extends Indicator> getOptimizedOpenIndicators() {
        return optimizedOpenIndicators;
    }

    public void setOptimizedOpenIndicators(List<? extends Indicator> optimizedOpenIndicators) {
        this.optimizedOpenIndicators = optimizedOpenIndicators;
    }

    public int getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(int initialBalance) {
        this.initialBalance = initialBalance;
    }

    public int getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(int stopLoss) {
        this.stopLoss = stopLoss;
    }

    public int getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(int takeProfit) {
        this.takeProfit = takeProfit;
    }

    public double getLot() {
        return lot;
    }

    public void setLot(double lot) {
        this.lot = lot;
    }

    public int getGeneracion() {
        return generacion;
    }

    public void setGeneracion(int generacion) {
        this.generacion = generacion;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getIdParent1() {
        return idParent1;
    }

    public void setIdParent1(String idParent1) {
        this.idParent1 = idParent1;
    }

    public String getIdParent2() {
        return idParent2;
    }

    public void setIdParent2(String idParent2) {
        this.idParent2 = idParent2;
    }

    public List<Fortaleza> getListaFortaleza() {
        return listaFortaleza;
    }

    public void setListaFortaleza(List<Fortaleza> listaFortaleza) {
        if (listaFortaleza == null) {
            this.listaFortaleza = new ArrayList<Fortaleza>();
        } else {
            this.listaFortaleza = listaFortaleza;
        }
    }

    public boolean isActive() {
        if (fortaleza.getType().equals(Constants.FortalezaType.Pattern)) {
            return ((this.fortaleza.getValue() >= 800.0) && (!this.activeOperation));
        } else if (fortaleza.getType().equals(Constants.FortalezaType.PatternAdvanced)) {
            return ((this.fortaleza.getRiskLevel() >= (PropertiesManager.getRiskLevel() / 10.0D)) && (!this.activeOperation));
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

    public String toFileString(DateInterval dateInterval) {
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
        buffer.append("MaxConsecutiveLostOperationsNumber=" + this.fortaleza.getMaxConsecutiveLostOperationsNumber() + ",");
        buffer.append("MaxConsecutiveWonOperationsNumber=" + this.fortaleza.getMaxConsecutiveWonOperationsNumber() + ",");
        buffer.append("MinConsecutiveLostOperationsNumber=" + this.fortaleza.getMinConsecutiveLostOperationsNumber() + ",");
        buffer.append("MinConsecutiveWonOperationsNumber=" + this.fortaleza.getMinConsecutiveWonOperationsNumber() + ",");
        buffer.append("AverageConsecutiveLostOperationsNumber=" + Math.round(this.fortaleza.getAverageConsecutiveLostOperationsNumber()) + ",");
        buffer.append("AverageConsecutiveWonOperationsNumber=" + Math.round(this.fortaleza.getAverageConsecutiveWonOperationsNumber()) + ",");
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IndividuoEstrategia) {
            IndividuoEstrategia objIndividuo = (IndividuoEstrategia) obj;
            boolean value = false;
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

    public boolean equalsReal(Object obj) {
        if (obj instanceof IndividuoEstrategia) {
            IndividuoEstrategia objIndividuo = (IndividuoEstrategia) obj;
            boolean value = ((this.openIndicators.equals(objIndividuo.openIndicators)
                    && Collections.frequency(this.openIndicators, null) != this.openIndicators.size())
                    //&& (this.closeIndicators.equals(objIndividuo.closeIndicators)
                    //&& Collections.frequency(this.closeIndicators, null) != this.closeIndicators.size())
                    && ((Math.abs((objIndividuo.takeProfit - this.takeProfit) / new Double(this.takeProfit)) < 0.02)
                    && (Math.abs((objIndividuo.stopLoss - this.stopLoss) / new Double(this.stopLoss)) < 0.02)));
            if (!value) {
                Fortaleza f = null;
                Fortaleza objF = null;
                int size = this.listaFortaleza.size();
                int presentNumberPoblacion = PropertiesManager.getPresentNumberPoblacion();
                boolean temp = (presentNumberPoblacion > 1);
                for (int i = size - 1; (temp) && (i >= (size - presentNumberPoblacion) + 1) && (i >= 0); i--) {
                    f = this.listaFortaleza.get(i);
                    objF = objIndividuo.listaFortaleza.get(i);
                    temp = (f == null) && (objF == null);
                    if (!temp) {
                        temp = ((f != null) && (objF != null));
                        temp = temp && (f.equals(objF));
                    }
                }
                value = temp;
            }
            return value;
        } else {
            return false;
        }
    }

    @Override
    public IndividuoEstrategia clone() {
        IndividuoEstrategia cloned = new IndividuoEstrategia();
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

    public double calculateRiskLevel(Fortaleza fortaleza, int index) {
        double riskLevel = 1.0;
        if (fortaleza == null) {
            fortaleza = this.getFortaleza();
        }
        if (fortaleza.getType().equals(Constants.FortalezaType.Stable)) {
            double risk1 = 1.0;
            double risk2 = 1.0;
            double risk3 = 1.0;
            double percentLevel = PropertiesManager.getRiskLevel() / Constants.MAX_RISK_LEVEL;
            double percentFortalezaNumber = (fortaleza.getWonOperationsNumber() == 0) ? 0.0D
                    : fortaleza.getLostOperationsNumber() / (double) (fortaleza.getWonOperationsNumber());
            double percentFortalezaPips = (fortaleza.getWonPips() == 0) ? 0.0D
                    : Math.abs(fortaleza.getLostPips() / (double) (fortaleza.getWonPips()));
            //double percentFortaleza = Math.max(percentFortalezaNumber, percentFortalezaPips);
            double percentFortaleza = (percentFortalezaNumber + percentFortalezaPips) / 2;

            risk1 = (percentFortaleza > percentLevel) ? (1.0) / (1000.0) : (10.0);
            risk2 = ((fortaleza.getOperationsNumber() / (index + 1))
                    < PropertiesManager.getMinOperNumByPeriod()) ? (2.0) / (1000.0) : (10.0);
            risk3 = (!processObligatory(this)) ? (3.0) / (100.0) : (10.0);

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
            } else if (this.activeOperation) {
                riskLevel = (0.8);
            }
        }
        return riskLevel;
    }

    private boolean isContinuo(List<Fortaleza> fortalezas) {
        boolean continuo = true;
        int presentNumberPoblacion = PropertiesManager.getPresentNumberPoblacion();
        for (int i = fortalezas.size() - 1; (continuo && i >= presentNumberPoblacion); i--) {
            if (!(fortalezas.get(i).getCalculatedValue() == fortalezas.get(i - presentNumberPoblacion).getCalculatedValue())) {
                continuo = false;
            }
        }
        return continuo;
    }

    private boolean processObligatory(IndividuoEstrategia individuoEstrategia) {
        boolean process = true;
        for (int i = 0; process && (i < IndicatorManager.getIndicatorNumber()); i++) {
            Indicator openIndicator = null;
            if (individuoEstrategia.getOpenIndicators().size() > i) {
                openIndicator = individuoEstrategia.getOpenIndicators().get(i);
            }
            Indicator closeIndicator = null;
            if (individuoEstrategia.getCloseIndicators().size() > i) {
                closeIndicator = individuoEstrategia.getCloseIndicators().get(i);
            }
            IndicatorManager indicatorManager = IndicatorManager.getInstance(i);
            if (indicatorManager.isObligatory() && ((openIndicator == null) || (closeIndicator == null))) {
                process = false;
            }
        }
        return process;
    }

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
        this.setOrdenes(new ArrayList<Order>());
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
    }

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
                    int presentNumberPoblacion = PropertiesManager.getPresentNumberPoblacion();
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
        return (Integer.valueOf(compare).compareTo(Integer.valueOf(0)));
    }

    private int comparePattern(IndividuoEstrategia other) {
        int compare = 0;
        /*if ((this.currentPatterns != null) && (other.currentPatterns != null)) {
        LogUtil.logTime(this.id + " compareTo.currentPatterns.0 " + other.id, 1);
        if ((compare == 0) && (!this.currentPatterns.isEmpty()) && (other.currentPatterns.isEmpty())) {
        LogUtil.logTime(this.id + " compareTo.currentPatterns.1 " + other.id, 1);
        compare = 1;
        }
        if ((compare == 0) && (this.currentPatterns.isEmpty()) && (!other.currentPatterns.isEmpty())) {
        LogUtil.logTime(this.id + " compareTo.currentPatterns.-1 " + other.id, 1);
        compare = -1;
        }
        }
        if ((this.patterns != null) && (other.patterns != null)) {
        LogUtil.logTime(this.id + " compareTo.patterns.0 " + other.id, 1);
        if ((compare == 0) && (!this.patterns.isEmpty()) && (other.patterns.isEmpty())) {
        LogUtil.logTime(this.id + " compareTo.patterns.1 " + other.id, 1);
        compare = 1;
        }
        if ((compare == 0) && (this.patterns.isEmpty()) && (!other.patterns.isEmpty())) {
        LogUtil.logTime(this.id + " compareTo.patterns.-1 " + other.id, 1);
        compare = -1;
        }
        }
        if ((this.ordenes != null) && (other.ordenes != null)) {
        LogUtil.logTime(this.id + " compareTo.ordenes.0 " + other.id, 1);
        if ((compare == 0) && (!this.ordenes.isEmpty()) && (other.ordenes.isEmpty())) {
        LogUtil.logTime(this.id + " compareTo.ordenes.1 " + other.id, 1);
        compare = 1;
        }
        if ((compare == 0) && (this.ordenes.isEmpty()) && (!other.ordenes.isEmpty())) {
        LogUtil.logTime(this.id + " compareTo.ordenes.-1 " + other.id, 1);
        compare = -1;
        }
        }
        if ((this.fortaleza != null) && (other.fortaleza != null)) {
        LogUtil.logTime(this.id + " compareTo.fortaleza.0 " + other.id, 1);
        if ((compare == 0) && (this.fortaleza.getOperationsNumber() != 0.0D) && (other.fortaleza.getOperationsNumber() == 0.0D)) {
        LogUtil.logTime(this.id + " compareTo.fortaleza.1 " + other.id, 1);
        compare = 1;
        }
        if ((compare == 0) && (this.getFortaleza().getOperationsNumber() == 0.0D) && (other.getFortaleza().getOperationsNumber() != 0.0D)) {
        LogUtil.logTime(this.id + " compareTo.fortaleza.-1 " + other.id, 1);
        compare = -1;
        }
        }*/
        if (compare == 0) {
            //LogUtil.logTime(this.id + " compareTo.value.0 " + other.id, 1);
            if ((this.fortaleza != null) && (other.fortaleza != null)) {
                double thisValue = this.fortaleza.getValue();
                double otherValue = other.fortaleza.getValue();
                compare = (Double.compare(thisValue, otherValue));
                if (compare == 0) {
                    //      LogUtil.logTime(this.id + " compareTo.value.1 " + other.id, 1);
                    double thisRisk = this.fortaleza.getRiskLevel();
                    double otherRisk = other.fortaleza.getRiskLevel();
                    compare = (Double.compare(thisRisk, otherRisk));
                }
            }
            if (compare == 0) {
                //LogUtil.logTime(this.id + " compareTo.currentPatterns2.0 " + other.id, 1);
                if ((this.currentPatterns != null) && (other.currentPatterns != null)) {
                    compare = Integer.valueOf(this.currentPatterns.size()).compareTo(Integer.valueOf(other.currentPatterns.size()));
                }
            }
            if (compare == 0) {
                //       LogUtil.logTime(this.id + " compareTo.ordenes2.0 " + other.id, 1);
                if ((this.ordenes != null) && (other.ordenes != null)) {
                    compare = Integer.valueOf(this.ordenes.size()).compareTo(Integer.valueOf(other.ordenes.size()));
                }
            }
        }
        if (compare == 0) {
            //LogUtil.logTime(this.id + " compareTo.getOperationsNumber.0 " + other.id, 1);
            if ((this.fortaleza != null) && (other.fortaleza != null)) {
                compare = Integer.valueOf(this.fortaleza.getOperationsNumber()).compareTo(Integer.valueOf(other.fortaleza.getOperationsNumber()));
            }
        }
        //LogUtil.logTime(this.id + " comparePattern " + other.id + "=" + compare, 1);
        return compare;
    }

    public void calculateCurrentPatternValue() {
        double calcValue = 0.0D;
        double risk = 0.0D;
        double won = 0.0D;
        double lost = 0.0D;
        double totalValue = 0.0D;
        double wonPattern = 0.0D;
        int countWon = 0;
        int countLost = 0;
        int size = 1;
        if (this.currentPatterns != null) {
            size = this.currentPatterns.size();
            for (Iterator<PatternAdvancedSpecific> it = this.currentPatterns.iterator(); it.hasNext();) {
                PatternAdvancedSpecific patternAdvancedSpecific = it.next();
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
                risk = 0.001D;
            } else if ((this.activeOperation) || (calcValue == 0.0D)) {
                calcValue /= 2;
                risk = 0.002D;
            } else {
                double minByPeriod = PropertiesManager.getMinOperNumByPeriod();
                double countByPeriod = (this.ordenes.size() / (double) this.processedUntil);
                double promCalcValueByOrders = (calcValue / (double) this.ordenes.size());
                if (promCalcValueByOrders < (PropertiesManager.getRiskLevel() / 70.0D)) {
                    calcValue /= 2;
                    risk = 0.003D;
                } else if (countByPeriod < minByPeriod) {
                    calcValue /= 2;
                    risk = 0.004D;
                } else {
                    if ((won + lost) > 0) {
                        //risk = ((won) / (won + lost)) * 1.0;
                        risk = (((won) / (won + lost)) * 0.80 + ((countWon / size)) * 0.10 + (promCalcValueByOrders) * 0.10);
                        if (risk < (PropertiesManager.getRiskLevel() / 10.0D)) {
                            calcValue /= 2;
                            risk *= 0.005D;
                        }
                    }
                }
            }
        }
        this.fortaleza.setValue(calcValue * risk);
        this.fortaleza.setRiskLevel(risk);
    }
}
