/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.DateInterval;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Interval;
import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.entities.Tendencia;

/**
 *
 * @author ricardorq85
 */
public class TendenciaHelper {

    /**
     *
     * @param resultado
     * @return
     * @throws SQLException
     */
    public static List<Tendencia> createTendencia(ResultSet resultado) throws SQLException {
        List<Tendencia> list = new ArrayList<Tendencia>();
        while (resultado.next()) {
            Tendencia obj = new Tendencia();

            Timestamp ts = resultado.getTimestamp("FECHA_BASE");
            if (ts != null) {
                obj.setFechaBase(new Date(ts.getTime()));
            }

            obj.setPrecioBase(resultado.getDouble("PRECIO_BASE"));

            Individuo ind = new Individuo();
            ind.setId(resultado.getString("ID_INDIVIDUO"));
            obj.setIndividuo(ind);

            ts = resultado.getTimestamp("FECHA_TENDENCIA");
            if (ts != null) {
                obj.setFechaTendencia(new Date(ts.getTime()));
            }

            obj.setPipsActuales(resultado.getDouble("PIPS_ACTUALES"));
            obj.setDuracion(resultado.getLong("DURACION"));
            obj.setPips(resultado.getDouble("PIPS"));
            obj.setDuracionActual(resultado.getLong("DURACION_ACTUAL"));
            obj.setPrecioCalculado(resultado.getDouble("PRECIO_CALCULADO"));

            ts = resultado.getTimestamp("FECHA_APERTURA");
            if (ts != null) {
                obj.setFechaApertura(new Date(ts.getTime()));
            }
            obj.setPrecioApertura(resultado.getDouble("OPEN_PRICE"));
            obj.setTipoTendencia(resultado.getString("TIPO_TENDENCIA"));
            obj.setProbabilidadPositivos(resultado.getDouble("PROBABILIDAD_POSITIVOS"));
            obj.setProbabilidadNegativos(resultado.getDouble("PROBABILIDAD_NEGATIVOS"));
            obj.setProbabilidad(resultado.getDouble("PROBABILIDAD"));
            ts = resultado.getTimestamp("FECHA");
            if (ts != null) {
                obj.setFecha(new Date(ts.getTime()));
            }

            list.add(obj);
        }
        return list;
    }

    /**
     *
     * @param resultado
     * @return
     * @throws SQLException
     */
    public static ProcesoTendencia createProcesoTendencia(ResultSet resultado) throws SQLException {
        ProcesoTendencia obj = null;
        if (resultado.next()) {
            obj = mapProcesoTendencia(resultado);
        }
        return obj;
    }

    /**
     *
     * @param resultado
     * @return
     * @throws SQLException
     */
    public static List<ProcesoTendencia> createProcesoTendenciaDetail(ResultSet resultado) throws SQLException {
        List<ProcesoTendencia> obj = new ArrayList<ProcesoTendencia>();
        while (resultado.next()) {
            obj.add(mapProcesoTendencia(resultado));
        }
        return obj;
    }

    private static ProcesoTendencia mapProcesoTendencia(ResultSet resultado) throws SQLException {
        ProcesoTendencia obj = new ProcesoTendencia();
        Interval<Date> intervaloFecha = new DateInterval();
        Timestamp ts = resultado.getTimestamp("MIN_FECHA");
        if (ts != null) {
            intervaloFecha.setLowInterval(new Date(ts.getTime()));
        }
        ts = resultado.getTimestamp("MAX_FECHA");
        if (ts != null) {
            intervaloFecha.setHighInterval(new Date(ts.getTime()));
        }
        Interval<Double> intervaloPrecio = new DoubleInterval("");
        if (resultado.getObject("MIN_PRECIO") != null) {
            intervaloPrecio.setLowInterval(resultado.getDouble("MIN_PRECIO"));
        }
        if (resultado.getObject("MAX_PRECIO") != null) {
            intervaloPrecio.setHighInterval(resultado.getDouble("MAX_PRECIO"));
        }

        obj.setCantidad(resultado.getInt("CANTIDAD"));
        obj.setValorMasProbable(resultado.getDouble("VALOR_MAS_PROBABLE"));
        obj.setPipsMasProbable(resultado.getDouble("PIPS_MAS_PROBABLE"));
        obj.setPrecioBasePromedio(resultado.getDouble("PRECIO_BASE_PROM"));
        obj.setProbabilidad(resultado.getDouble("PROBABILIDAD"));
        obj.setIntervaloFecha(intervaloFecha);
        obj.setIntervaloPrecio(intervaloPrecio);

        if ((intervaloFecha.getLowInterval() == null) || (intervaloFecha.getHighInterval() == null)
                || (intervaloPrecio.getLowInterval() == null) || (intervaloPrecio.getHighInterval() == null)) {
            obj = null;
        }
        return obj;
    }

	public static List<Date> createFechasTendencia(ResultSet resultado) throws SQLException {
		List<Date> fechas = new ArrayList<>();
		while (resultado.next()) {
			if (resultado.getObject(1) != null) {
				fechas.add(new Date(resultado.getTimestamp(1).getTime()));
			}
		}
		return fechas;
	}
}
