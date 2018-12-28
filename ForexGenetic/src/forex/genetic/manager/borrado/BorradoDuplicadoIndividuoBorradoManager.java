package forex.genetic.manager.borrado;

import java.sql.Connection;

import forex.genetic.dao.oracle.OracleIndividuoBorradoDAO;

public class BorradoDuplicadoIndividuoBorradoManager extends BorradoDuplicadoIndividuoManager {

	public BorradoDuplicadoIndividuoBorradoManager(Connection conn) {
		super(conn, new OracleIndividuoBorradoDAO(conn), "DUPLICADO_BORRADO");
	}

}
