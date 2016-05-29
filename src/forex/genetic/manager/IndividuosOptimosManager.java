/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.EstrategiaDAO;
import forex.genetic.dao.IndividuoDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class IndividuosOptimosManager {

    private Connection conn = null;

    public void obtenerIndividuosOptimos() throws ClassNotFoundException, SQLException, ParseException {
        conn = JDBCUtil.getConnection();
        ParametroDAO parametroDAO = new ParametroDAO(conn);
        IndividuoDAO individuoDAO = new IndividuoDAO(conn);
        DatoHistoricoDAO datoHistoricoDAO = new DatoHistoricoDAO(conn);
        EstrategiaDAO estrategiaDAO = new EstrategiaDAO(conn);
        Date fechaMaxHistorico = datoHistoricoDAO.getFechaHistoricaMaxima();
        Date fechaPeriodo = parametroDAO.getDateValorParametro("FECHA_INDIVIDUO_OPTIMO");
        int periodo = parametroDAO.getIntValorParametro("PERIODO_OPTIMOS");

        while (fechaPeriodo.compareTo(fechaMaxHistorico) < 0) {
            Date fechaSiguiente = DateUtil.adicionarMinutos(fechaPeriodo, periodo);
            LogUtil.logTime(fechaPeriodo.toString(), 1);
            List<IndividuoOptimo> individuos = individuoDAO.consultarIndividuosOptimos();
            DateInterval dateInterval = new DateInterval();
            dateInterval.setLowInterval(fechaPeriodo);
            dateInterval.setHighInterval(fechaSiguiente);
            for (int i = 0; i < individuos.size(); i++) {
                IndividuoOptimo individuoOptimo = individuos.get(i);
                individuoOptimo.setFechaVigencia(fechaPeriodo);
                individuoOptimo.setMinutosFuturo(periodo);
                individuoOptimo.setNombreEstrategia(PropertiesManager.getNombreEstrategia());
                individuoDAO.consultarDetalleIndividuo(individuoOptimo);
                estrategiaDAO.insertIndividuoEstrategia(individuoOptimo);
                String strIndividuo = individuoOptimo.toFileString(dateInterval);
                System.out.println(strIndividuo);
            }
            fechaPeriodo = fechaSiguiente;
            parametroDAO.updateDateValorParametro("FECHA_INDIVIDUO_OPTIMO", fechaPeriodo);
            conn.commit();
        }
    }
}
