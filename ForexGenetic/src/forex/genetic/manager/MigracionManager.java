/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.entities.Point;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class MigracionManager {

	private Connection conn;
	private DatoHistoricoDAO datoHistoricoDAO;
	private MongoDatoHistoricoDAO mongoDatoHistoricoDAO;

	/**
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 *
	 */
	public MigracionManager() throws ClassNotFoundException, SQLException {
		conn = JDBCUtil.getConnection();
		datoHistoricoDAO = new DatoHistoricoDAO(conn);
		mongoDatoHistoricoDAO = new MongoDatoHistoricoDAO();
		mongoDatoHistoricoDAO.cleanDatosHistoricos();
	}

	public void migrate() throws SQLException {
		Date fechaMinima = datoHistoricoDAO.getFechaHistoricaMinima();
		Date fechaMinimaDestino = mongoDatoHistoricoDAO.getFechaHistoricaMinima();
		Date fechaMaxima = datoHistoricoDAO.getFechaHistoricaMaxima();
		Date fechaInicialConsulta = DateUtil.obtenerFechaMinima(fechaMinima, fechaMinimaDestino);
		/*Date fechaInicialConsulta;
		try {
			fechaInicialConsulta = DateUtil.obtenerFecha("2008/05/06 08:45");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fechaInicialConsulta = DateUtil.obtenerFechaMinima(fechaMinima, fechaMinimaDestino);
fo		}*/
		
		while (!fechaInicialConsulta.after(fechaMaxima)) {
			Date fechaFinalConsulta = DateUtil.adicionarDias(fechaInicialConsulta, 10);
			LogUtil.logTime("Exportando..." + 
					DateUtil.getDateString(fechaInicialConsulta) + "-" + DateUtil.getDateString(fechaFinalConsulta), 1);
			List<Point> datosConsultados = datoHistoricoDAO.consultarHistorico(fechaInicialConsulta,
					fechaFinalConsulta);
			datosConsultados.stream().forEach((dato) -> {
				mongoDatoHistoricoDAO.insertOrUpdateDatoHistorico(dato);
			});

			fechaInicialConsulta = DateUtil
					.adicionarMinutos(datosConsultados.get(datosConsultados.size() - 1).getDate(), 1);
		}

	}
}