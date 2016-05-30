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

    public Integer getHorasAgrupacion() {
        return horasAgrupacion;
    }

    public void setHorasAgrupacion(Integer horasAgrupacion) {
        this.horasAgrupacion = horasAgrupacion;
    }

    public Integer getMinutosAgrupacion() {
        return minutosAgrupacion;
    }

    public void setMinutosAgrupacion(Integer minutosAgrupacion) {
        this.minutosAgrupacion = minutosAgrupacion;
    }

}
