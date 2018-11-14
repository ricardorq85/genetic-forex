package forex.genetic.util.jdbc;

public abstract class DataClient<T> {

	public DataClient(T client) {
		this.client = client;
	}

	private T client;

	public T getClient() {
		return client;
	}

	public void setClient(T client) {
		this.client = client;
	}

}
