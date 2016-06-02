/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author USER
 */
public class IndividuoTendencia {

    private Integer minutosAgrupacion = null;

    private Integer horasAgrupacion;

    /**
     *
     * @return
     */
    public Integer getHorasAgrupacion() {
        return horasAgrupacion;
    }

    /**
     *
     * @param horasAgrupacion
     */
    public void setHorasAgrupacion(Integer horasAgrupacion) {
        this.horasAgrupacion = horasAgrupacion;
    }

    /**
     *
     * @return
     */
    public Integer getMinutosAgrupacion() {
        return minutosAgrupacion;
    }

    /**
     *
     * @param minutosAgrupacion
     */
    public void setMinutosAgrupacion(Integer minutosAgrupacion) {
        this.minutosAgrupacion = minutosAgrupacion;
    }

}
