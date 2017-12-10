package forex.genetic.manager.borrado;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.IndividuoBorradoDAO;

public class BorradoDuplicadoIndividuoBorradoManager extends BorradoDuplicadoIndividuoManager {

	public BorradoDuplicadoIndividuoBorradoManager(Connection conn) throws ClassNotFoundException, SQLException {
		super(conn, new IndividuoBorradoDAO(conn), "DUPLICADO_BORRADO");
	}

}
