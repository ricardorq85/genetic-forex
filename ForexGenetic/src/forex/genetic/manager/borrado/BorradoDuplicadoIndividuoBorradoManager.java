package forex.genetic.manager.borrado;

import java.sql.Connection;

import forex.genetic.dao.IndividuoBorradoDAO;

public class BorradoDuplicadoIndividuoBorradoManager extends BorradoDuplicadoIndividuoManager {

	public BorradoDuplicadoIndividuoBorradoManager(Connection conn) {
		super(conn, new IndividuoBorradoDAO(conn), "DUPLICADO_BORRADO");
	}

}
