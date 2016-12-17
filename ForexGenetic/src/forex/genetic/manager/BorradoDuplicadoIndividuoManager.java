/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.sql.SQLException;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class BorradoDuplicadoIndividuoManager extends BorradoManager {

	public BorradoDuplicadoIndividuoManager() throws ClassNotFoundException, SQLException {
		super.tipoProceso = "DUPLICADO_INDIVIDUO";
	}

	@Override
	public void borrarIndividuos() throws ClassNotFoundException, SQLException {
		borrarDuplicados();
	}

	@Override
	protected List<Individuo> consultarIndividuos(Individuo individuo) throws ClassNotFoundException, SQLException {
		return null;
	}

	@Override
	public void validarYBorrarIndividuo(Individuo individuo) throws ClassNotFoundException, SQLException {
		borrarDuplicados(individuo);
	}

	/**
	 *
	 * @param tipoProceso
	 * @throws ClassNotFoundException
	 */
	protected void borrarDuplicados() throws ClassNotFoundException, SQLException {
		try {
			int count = 0;
			List<Individuo> individuosRepetidos = individuoDAO.consultarIndividuosRepetidos();
			while ((individuosRepetidos != null) && (!individuosRepetidos.isEmpty())) {
				deleteRepetidos(individuosRepetidos);
				count += individuosRepetidos.size();
				individuosRepetidos = individuoDAO.consultarIndividuosRepetidos();
			}
			LogUtil.logTime("Individuos borrados: " + count, 1);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}
	}

	protected void deleteRepetidos(List<Individuo> individuosRepetidos) throws SQLException {
		if (individuosRepetidos.size() > 0) {
			LogUtil.logTime("Individuos repetidos consultados: " + individuosRepetidos.size(), 1);
		}
		super.smartDelete(individuosRepetidos);
		conn.commit();
	}

	protected void borrarDuplicados(Individuo individuo) throws ClassNotFoundException, SQLException {
		try {
			int count = 0;
			List<Individuo> individuosRepetidos = individuoDAO.consultarIndividuoHijoRepetido(individuo);
			deleteRepetidos(individuosRepetidos);
			count += individuosRepetidos.size();
			if (count > 0) {
				LogUtil.logTime("Individuos borrados: " + count, 1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}
	}

}
