package forex.genetic.factory;

public class GeneticFactory {

	protected static String drivers[] = new String[] { "oracle", "mongodb" };
//	protected static String drivers[] = new String[] { "mongodb" };

	public static void registerDriver(String oneDriver) {
		drivers = new String[] { oneDriver };
	}

}
