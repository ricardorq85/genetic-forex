package forex.genetic.util.jdbc;

import java.sql.Connection;

public class OracleDataClient extends DataClient<Connection> {

	public OracleDataClient(Connection client) {
		super(client);
	}
}
