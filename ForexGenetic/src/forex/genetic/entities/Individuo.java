/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author ricardorq85
 */
public class Individuo extends IndividuoEstrategia {

    /**
     *
     */
    public static final long serialVersionUID = 0x2eea76fa70L;
    private Date fechaHistorico = null;
    private Date fechaApertura = null;

    /**
     *
     */
    public Individuo() {
    }

    /**
     *
     * @param id
     */
    public Individuo(String id) {
        super(id);
    }

    /**
     *
     * @return
     */
    public Date getFechaApertura() {
        return (fechaApertura != null ? new Date(fechaApertura.getTime()) : null);
    }

    /**
     *
     * @param fechaApertura
     */
    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura != null ? new Date(fechaApertura.getTime()) : null;
    }

    /**
     *
     * @return
     */
    public Date getFechaHistorico() {
        return (fechaHistorico != null ? new Date(fechaHistorico.getTime()) : null);
    }

    /**
     *
     * @param fechaHistorico
     */
    public void setFechaHistorico(Date fechaHistorico) {
        this.fechaHistorico = fechaHistorico != null ? new Date(fechaHistorico.getTime()) : null;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(" Id=").append(this.id);
        if (idParent1 != null) {
            buffer.append("; Padre 1=").append(idParent1);
        }
        if (idParent2 != null) {
            buffer.append("; Padre 2=").append(idParent2);
        }
        buffer.append("; CreationDate=").append(this.creationDate);
        buffer.append("; TakeProfit=").append(this.takeProfit);
        buffer.append("; Stoploss=").append(this.stopLoss);
        buffer.append("; Lot=").append(this.lot);
        buffer.append("; Initial Balance=").append(this.initialBalance);
        buffer.append("\n\t");
        buffer.append("; Open Indicadores=").append(this.openIndicators);
        buffer.append("\n\t");
        buffer.append("; Close Indicadores=").append(this.closeIndicators);
        buffer.append("\n\t");
        return buffer.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Individuo other = (Individuo) obj;
        if ((super.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.fechaHistorico);
        hash = 41 * hash + Objects.hashCode(this.fechaApertura);
        return hash;
    }

    /**
     *
     * @param individuoEstrategia
     */
    public void copy(IndividuoEstrategia individuoEstrategia) {
        this.setId(individuoEstrategia.getId());
        this.setOpenIndicators(individuoEstrategia.getOpenIndicators());
        this.setCloseIndicators(individuoEstrategia.getCloseIndicators());
        this.setCreationDate(individuoEstrategia.getCreationDate());
        this.setGeneracion(individuoEstrategia.getGeneracion());
        this.setParent1(individuoEstrategia.getParent1());
        this.setParent2(individuoEstrategia.getParent2());
        this.setIdParent1(individuoEstrategia.getIdParent1());
        this.setIdParent2(individuoEstrategia.getIdParent2());
        this.setIndividuoType(individuoEstrategia.getIndividuoType());
        this.setInitialBalance(individuoEstrategia.getInitialBalance());
        this.setLot(individuoEstrategia.getLot());
        this.setStopLoss(individuoEstrategia.getStopLoss());
        this.setTakeProfit(individuoEstrategia.getTakeProfit());
    }
}
