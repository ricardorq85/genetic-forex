/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.IndividuoDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.util.DateUtil;
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
        Date fechaMaxHistorico = datoHistoricoDAO.getFechaHistoricaMaxima();
        Date fechaPeriodo = parametroDAO.getDateValorParametro("FECHA_INDIVIDUO_OPTIMO");

        while (fechaPeriodo.compareTo(fechaMaxHistorico) < 0) {
            Date fechaSiguiente = DateUtil.adicionarMes(fechaPeriodo);
            List<IndividuoOptimo> individuos = individuoDAO.consultarIndividuosOptimos();
            DateInterval dateInterval = new DateInterval();
            dateInterval.setLowInterval(fechaPeriodo);
            dateInterval.setHighInterval(fechaSiguiente);
            System.out.println(fechaPeriodo);
            for (int i = 0; i < individuos.size(); i++) {
                IndividuoOptimo individuoOptimo = individuos.get(i);
                individuoDAO.consultarDetalleIndividuo(individuoOptimo);
                String strIndividuo = individuoOptimo.toFileString(dateInterval);
                System.out.println(strIndividuo);
            }
            fechaPeriodo = fechaSiguiente;
            parametroDAO.updateDateValorParametro("FECHA_INDIVIDUO_OPTIMO", fechaPeriodo);
            conn.commit();
        }
    }
}
