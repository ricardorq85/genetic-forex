package forex.genetic.dao.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.helper.mongodb.MongoDatoHistoricoMapper;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.RangoOperacionIndividuo;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.entities.mongo.MongoOrder;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MongoDatoHistoricoDAO extends MongoGeneticDAO<Point> implements IDatoHistoricoDAO {

	private final int minYear = 2008;

	public MongoDatoHistoricoDAO() {
		this(true);
	}

	public MongoDatoHistoricoDAO(boolean configure) {
		this("datoHistorico2008", configure);
	}

	public MongoDatoHistoricoDAO(String name, boolean configure) {
		super(name, configure);
		int year = 2009;
		int currYear = DateUtil.obtenerAnyo(new Date());
		while (year <= currYear) {
			setCollection(year++, true);
		}
	}

	private void setCollection(int anyo) {
		setCollection(anyo, false);
	}

	protected void setCollection(int anyo, boolean configure) {
		StringBuilder sb = new StringBuilder("datoHistorico").append(anyo);
		setCollection(sb.toString(), configure);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("moneda", "periodo", "fechaHistorico"), indexOptions);
		this.collection.createIndex(Indexes.ascending("fechaHistorico"), indexOptions);

		this.configureIndexIndicators();
	}

	private void configureIndexIndicators() {
		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
			IntervalIndicatorManager<IntervalIndicator> indManager = ((IntervalIndicatorManager) indicadorController
					.getManagerInstance(i));
			IntervalIndicator indicator = indManager.getIndicatorInstance();
			StringBuilder indexNamePrefix = new StringBuilder("indicadores.").append(indicator.getName());
			String[] nombresCalculados = indManager.getNombresCalculados();
			for (int j = 0; j < nombresCalculados.length; j++) {
				String indexName = new StringBuilder(indexNamePrefix).append(".").append(nombresCalculados[j])
						.toString();
				// LogUtil.logTime("Index: " + indexName, 1);
				this.collection.createIndex(Indexes.ascending(indexName));
			}
		}
	}

	@Override
	public void consultarRangoOperacionIndicador(RangoOperacionIndividuo r) {
		int initialYear = DateUtil.obtenerAnyo(r.getFechaFiltro());
		int endYear = DateUtil.obtenerAnyo(r.getFechaFiltro2());
		int year = endYear; 
		// TODO ricardorq85 soporte multi años: year = initialYear;
		while (year <= endYear) {
			setCollection(year);
			consultarRangoOperacionIndicadorIntern(r);
			year++;
		}
	}

	private void consultarRangoOperacionIndicadorIntern(RangoOperacionIndividuo r) {
		int cantidad = 10;
		List<BsonField> accumulators = new ArrayList<>();
		accumulators.add(Accumulators.min("minLow", "$low"));
		accumulators.add(Accumulators.max("maxHigh", "$high"));
		accumulators.add(Accumulators.sum("registros", 1));

		List<Bson> filters = new ArrayList<>();
		filters.add(Filters.gte("fechaHistorico", r.getFechaFiltro()));
		filters.add(Filters.lte("fechaHistorico", r.getFechaFiltro2()));

		r.getFilterList().stream().forEach((one) -> {
//			filters.add(Filters.exists(one));

			String strNombre = one.toString().replaceAll("\\.", "");
			accumulators.add(Accumulators.sum("sum" + strNombre, "$" + one));
			accumulators.add(Accumulators.min("min" + strNombre, "$" + one));
			accumulators.add(Accumulators.max("max" + strNombre, "$" + one));
		});
	
//		LogUtil.logTime(
	//			Filters.and(filters).toBsonDocument(Document.class, MongoClient.getDefaultCodecRegistry()).toJson(), 1);


		MongoCursor<Document> cursor = this.collection.aggregate(Arrays.asList(Aggregates.match(Filters.and(filters)),
				Aggregates.unwind("$indicadores"),
				Aggregates.group(null, accumulators), Aggregates.sample(cantidad))).iterator();

		((MongoDatoHistoricoMapper) getMapper()).helpRangoOperacionIndividuo(cursor, r);
	}

	public void insertMany(List<Point> datos) {
		int initialIndex = 0;
		for (int i = 1; i < datos.size(); i++) {
			int prev = DateUtil.obtenerAnyo(datos.get(i - 1).getDate());
			int curr = DateUtil.obtenerAnyo(datos.get(i).getDate());
			if ((curr != prev) || (i == datos.size() - 1)) {
				List<Point> tempDatos = datos;
				if ((initialIndex > 0) || (i < datos.size() - 1)) {
					tempDatos = datos.subList(initialIndex, i);
				}
				setCollection(prev);
				super.insertMany(tempDatos);
				initialIndex = i;
			}
		}
	}


	@Override
	public long consultarCantidadPuntos(DateInterval interval) throws GeneticDAOException {
		int initialYear = DateUtil.obtenerAnyo(interval.getLowInterval());
		int endYear = DateUtil.obtenerAnyo(interval.getHighInterval());
		int year = initialYear;
		long count = 0;
		while (year <= endYear) {
			setCollection(year);
			count += consultarCantidadPuntosIntern(interval);
			year++;
		}
		return count;
	}

	private long consultarCantidadPuntosIntern(DateInterval interval) throws GeneticDAOException {
		long count = this.collection
				.countDocuments(Filters.and(Filters.gte("fechaHistorico", interval.getLowInterval()),
						Filters.lte("fechaHistorico", interval.getHighInterval())));
		return count;
	}

	@Override
	public Point consultarXFecha(Date fecha) throws GeneticDAOException {
		int year = DateUtil.obtenerAnyo(fecha);
		setCollection(year);
		Point p = consultarHistoricoIntern(fecha);
		return p;
	}

	public Point consultarHistoricoIntern(Date fecha) throws GeneticDAOException {
		Document doc = this.collection.find(Filters.eq("fechaHistorico", fecha)).limit(1).first();
		Point p = getMapper().helpOne(doc);
		return p;
	}

	@Override
	public Point consultarRetroceso(Order order) throws GeneticDAOException {
		Point p = null;
		int initialYear = DateUtil.obtenerAnyo(order.getOpenDate());
		int endYear = DateUtil.obtenerAnyo(order.getCloseDate());
		setCollection(initialYear);
		Point p1 = consultarRetrocesoIntern(order);
		p = p1;
		if (initialYear != endYear) {
			setCollection(endYear);
			Point p2 = consultarRetrocesoIntern(order);
			if (esVentaConPipsPositivos(order) || esCompraConPipsNegativos(order)) {
				if ((p1 != null) && ((p2 == null) || (p1.getHigh() > p2.getHigh()))) {
					p = p1;
				} else {
					p = p2;
				}
			} else {
				if ((p1 != null) && ((p2 == null) || (p1.getLow() < p2.getLow()))) {
					p = p1;
				} else {
					p = p2;
				}
			}
		}
		return p;
	}

	private Point consultarRetrocesoIntern(Order order) throws GeneticDAOException {
		List<Bson> filtros = new ArrayList<>();
		Bson sort = null;
		filtros.add(Filters.gt("fechaHistorico", order.getOpenDate()));
		filtros.add(Filters.lte("fechaHistorico", order.getCloseDate()));
		if (esVentaConPipsPositivos(order) || esCompraConPipsNegativos(order)) {
			filtros.add(Filters.gt("high", order.getOpenOperationValue()));
			sort = Sorts.descending("high");
		} else {
			filtros.add(Filters.lt("low", order.getOpenOperationValue()));
			sort = Sorts.ascending("low");
		}

		Bson bsonFiltrosCompletos = Filters.and(filtros);
		Document doc = this.collection.find(bsonFiltrosCompletos).sort(sort).limit(1).first();

		Point p = getMapper().helpOne(doc);
		return p;
	}

	private boolean esCompraConPipsNegativos(Order order) {
		boolean isBuy = (order.getTipo().equals(Constants.OperationType.BUY));
		return (isBuy) && (order.getPips() < 0);
	}

	private boolean esVentaConPipsPositivos(Order order) {
		boolean isBuy = (order.getTipo().equals(Constants.OperationType.BUY));
		return ((!isBuy) && (order.getPips() > 0));
	}

	public Date getFechaHistoricaMinima() {
		setCollection(minYear);
		Date fecha = null;
		Document doc = this.collection.find().sort(Sorts.ascending("fechaHistorico")).first();
		if (doc != null) {
			fecha = doc.getDate("fechaHistorico");
		}
		return fecha;
	}

	@Override
	public Date getFechaHistoricaMaxima() {
		int year = DateUtil.obtenerAnyo(new Date());
		Date date = null;
		while ((year >= minYear) && (date == null)) {
			setCollection(year);
			date = getFechaHistoricaMaximaIntern();
			year--;
		}
		return date;
	}

	public Date getFechaHistoricaMaximaIntern() {
		Date fecha = null;
//		Document doc = this.collection
//				.aggregate(Arrays.asList(Aggregates.group(null, Accumulators.max("maxDate", "$fechaHistorico"))))
//				.first();
		Document doc = this.collection.find().sort(Sorts.descending("fechaHistorico")).first();
		if (doc != null) {
			fecha = doc.getDate("fechaHistorico");
		}
		return fecha;
	}

	@Override
	public <H extends Order> Point consultarPuntoCierreByTakeOrStop(H order, DateInterval rango)
			throws GeneticDAOException {
		int initialYear = DateUtil.obtenerAnyo(rango.getLowInterval());
		int endYear = DateUtil.obtenerAnyo(rango.getHighInterval());
		setCollection(initialYear);
		Point p1 = consultarPuntoCierreByTakeOrStopIntern(order, rango);
		Point p = p1;
		if ((initialYear != endYear) && (p1 == null)) {
			setCollection(endYear);
			Point p2 = consultarPuntoCierreByTakeOrStopIntern(order, rango);
			p = p2;
		}
		return p;
	}

	private <H extends Order> Point consultarPuntoCierreByTakeOrStopIntern(H order, DateInterval rango)
			throws GeneticDAOException {
		List<Bson> filtros = new ArrayList<>();
		filtros.add(Filters.gt("fechaHistorico", rango.getLowInterval()));
		filtros.add(Filters.lte("fechaHistorico", rango.getHighInterval()));

		List<Bson> filtrosTakeStop = new ArrayList<>();
		boolean isBuy = (order.getTipo().equals(Constants.OperationType.BUY));
		MongoOrder mongoOrder = (MongoOrder) order;
		if (isBuy) {
			filtrosTakeStop.add(Filters.gte("high", mongoOrder.getClosePriceByTakeProfit()));
			filtrosTakeStop.add(Filters.lte("low", mongoOrder.getClosePriceByStopLoss()));
		} else {
			filtrosTakeStop.add(Filters.lte("low", mongoOrder.getClosePriceByTakeProfit()));
			filtrosTakeStop.add(Filters.gte("high", mongoOrder.getClosePriceByStopLoss()));
		}
		filtros.add(Filters.or(filtrosTakeStop));

		Bson bsonFiltrosCompletos = Filters.and(filtros);

		Document doc = this.collection.find(bsonFiltrosCompletos).sort(Sorts.orderBy(Sorts.ascending("fechaHistorico")))
				.limit(1).first();

		Point p = getMapper().helpOne(doc);
		return p;
	}

	@Override
	public List<Point> consultarPuntosCierre(IndividuoEstrategia individuo, DateInterval rango) {
		int initialYear = DateUtil.obtenerAnyo(rango.getLowInterval());
		int endYear = DateUtil.obtenerAnyo(rango.getHighInterval());
		setCollection(initialYear);
		List<Point> p = consultarPuntoCierreIntern(individuo, rango);
		if ((initialYear != endYear)) {
			setCollection(endYear);
			p.addAll(consultarPuntoCierreIntern(individuo, rango));
		}
		return p;
	}

	public List<Point> consultarPuntoCierreIntern(IndividuoEstrategia individuo, DateInterval rango) {
		List<Bson> filtros = new ArrayList<>();
		filtros.add(Filters.gt("fechaHistorico", rango.getLowInterval()));
		filtros.add(Filters.lte("fechaHistorico", rango.getHighInterval()));

		adicionarFiltroIndicadores(individuo.getCloseIndicators(), filtros);

		Bson bsonFiltrosCompletos = Filters.and(filtros);
		MongoCursor<Document> cursor = this.collection.find(bsonFiltrosCompletos)
				.sort(Sorts.orderBy(Sorts.ascending("fechaHistorico"))).iterator();

		List<Point> p = getMapper().helpList(cursor);
		return p;
	}

	@Override
	public List<Point> consultarProximosPuntosApertura(IndividuoEstrategia individuo, DateInterval rango) {
		int initialYear = DateUtil.obtenerAnyo(rango.getLowInterval());
		int endYear = DateUtil.obtenerAnyo(rango.getHighInterval());
		setCollection(initialYear);
		List<Point> puntosApertura = consultarProximosPuntosAperturaIntern(individuo, rango);
		if ((initialYear != endYear)) {
			setCollection(endYear);
			puntosApertura.addAll(consultarProximosPuntosAperturaIntern(individuo, rango));
		}
		return puntosApertura;
	}

	private List<Point> consultarProximosPuntosAperturaIntern(IndividuoEstrategia individuo, DateInterval rango) {
		List<Bson> filtros = new ArrayList<>();
		filtros.add(Filters.gt("fechaHistorico", rango.getLowInterval()));
		filtros.add(Filters.lte("fechaHistorico", rango.getHighInterval()));

		adicionarFiltroIndicadores(individuo.getOpenIndicators(), filtros);

		Bson bsonFiltrosCompletos = Filters.and(filtros);
		MongoCursor<Document> cursor = this.collection.find(bsonFiltrosCompletos)
				.sort(Sorts.orderBy(Sorts.ascending("fechaHistorico"))).iterator();

		List<Point> p = getMapper().helpList(cursor);
		return p;
	}

	private void adicionarFiltroIndicadores(List<? extends Indicator> indicadores, List<Bson> filtros) {
		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		for (int i = 0; i < indicadores.size(); i++) {
			IndicadorManager<?> managerInstance = indicadorController.getManagerInstance(i);
			String[] nombreCalculado = managerInstance.getNombresCalculados();
			List<Bson> filtrosDatosCalculados = new ArrayList<>();
			List<Bson> filtrosIndicadorLow = new ArrayList<>();
			List<Bson> filtrosIndicadorHigh = new ArrayList<>();
			List<Bson> filtrosIndicadorLowReves = new ArrayList<>();
			List<Bson> filtrosIndicadorHighReves = new ArrayList<>();

			for (int j = 0; j < nombreCalculado.length; j++) {
				IntervalIndicator intervalIndicator = ((IntervalIndicator) indicadores.get(i));
				if ((intervalIndicator != null) && (intervalIndicator.getInterval() != null)
						&& (intervalIndicator.getInterval().getLowInterval() != null)
						&& (intervalIndicator.getInterval().getHighInterval() != null)) {
					StringBuilder nombreIndicador = new StringBuilder("indicadores").append(".")
							.append(intervalIndicator.getName()).append(".");
					StringBuilder nombreIndicadorCalculado = new StringBuilder(nombreIndicador)
							.append(nombreCalculado[j]);

					if (nombreCalculado[j].endsWith("low")) {
						filtrosIndicadorHigh.add(Filters.gte(nombreIndicadorCalculado.toString(),
								intervalIndicator.getInterval().getHighInterval()));
						filtrosIndicadorLow.add(Filters.gte(nombreIndicadorCalculado.toString(),
								intervalIndicator.getInterval().getLowInterval()));

						filtrosIndicadorLowReves.add(Filters.gte(nombreIndicadorCalculado.toString(),
								intervalIndicator.getInterval().getLowInterval()));
						filtrosIndicadorLowReves.add(Filters.lte(nombreIndicadorCalculado.toString(),
								intervalIndicator.getInterval().getHighInterval()));

					} else if (nombreCalculado[j].endsWith("high")) {
						filtrosIndicadorHigh.add(Filters.lte(nombreIndicadorCalculado.toString(),
								intervalIndicator.getInterval().getHighInterval()));
						filtrosIndicadorLow.add(Filters.lte(nombreIndicadorCalculado.toString(),
								intervalIndicator.getInterval().getLowInterval()));

						filtrosIndicadorHighReves.add(Filters.gte(nombreIndicadorCalculado.toString(),
								intervalIndicator.getInterval().getLowInterval()));
						filtrosIndicadorHighReves.add(Filters.lte(nombreIndicadorCalculado.toString(),
								intervalIndicator.getInterval().getHighInterval()));
					} else {
						Bson filtroLow = Filters.gte(nombreIndicadorCalculado.toString(),
								intervalIndicator.getInterval().getLowInterval());
						Bson filtroHigh = Filters.lte(nombreIndicadorCalculado.toString(),
								intervalIndicator.getInterval().getHighInterval());
						filtrosDatosCalculados.add(Filters.and(filtroLow, filtroHigh));
					}
				}
			}
			if (!filtrosDatosCalculados.isEmpty()) {
				filtros.addAll(filtrosDatosCalculados);
			} else if ((!filtrosIndicadorLow.isEmpty()) && (!filtrosIndicadorHigh.isEmpty())) {
				filtros.add(Filters.or(Filters.and(filtrosIndicadorLow), Filters.and(filtrosIndicadorHigh),
						Filters.and(filtrosIndicadorLowReves), Filters.and(filtrosIndicadorHighReves)));
			}
		}
	}

	@Override
	public Point consultarPuntoAnterior(Date fecha) throws GeneticDAOException {
		int initialYear = DateUtil.obtenerAnyo(fecha);
		setCollection(initialYear);
		Point p1 = consultarPuntoAnteriorIntern(fecha);
		Point p = p1;
		if ((initialYear > minYear) && (p1 == null)) {
			setCollection(--initialYear);
			Point p2 = consultarPuntoAnteriorIntern(fecha);
			p = p2;
		}
		return p;
	}

	public Point consultarPuntoAnteriorIntern(Date fecha) throws GeneticDAOException {
		Document doc = this.collection.find(Filters.lt("fechaHistorico", fecha))
				.sort(Sorts.orderBy(Sorts.descending("fechaHistorico"))).limit(1).first();

		Point p = getMapper().helpOne(doc);
		return p;
	}

	@Override
	public List<? extends Point> consultarHistoricoOrderByPrecio(Date fechaBase1, Date fechaBase2)
			throws GeneticDAOException {
		// final double FACTOR_NUMERO_RANDOM_TENDENCIAS = 0.3;
		// TODO Parametro de entrada, o calcular acá
		int numeroRegistros = 10;
		Bson filtros = Filters.and(Filters.gte("fechaHistorico", fechaBase1),
				Filters.lte("fechaHistorico", fechaBase2));
		Bson sorts = Sorts.orderBy(Sorts.descending("high"), Sorts.ascending("fechaHistorico"));
		MongoCursor<Document> cursor = this.collection.aggregate(
				Arrays.asList(Aggregates.match(filtros), Aggregates.sample(numeroRegistros), Aggregates.sort(sorts)))
				.iterator();
		return getMapper().helpList(cursor);
	}

	@Override
	public List<Date> consultarPuntosApertura(Date fechaMayorQue, String idIndividuo) throws GeneticDAOException {
		throw new UnsupportedOperationException(
				"MongoDatoHistoricoDAO.consultarPuntosApertura no soportado. Se debe usar consultarProximoPuntoApertura");
	}

	@Override
	public List<Date> consultarPuntosApertura(DateInterval rango, String idIndividuo) throws GeneticDAOException {
		throw new UnsupportedOperationException(
				"MongoDatoHistoricoDAO.consultarPuntosApertura no soportado. Se debe usar consultarProximoPuntoApertura");
	}

	@Override
	public double consultarPrecioPonderado(Date fecha) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public double consultarMaximaDiferencia(Date fecha, String formatoAgrupador) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public long consultarCantidadPuntos() throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public Date getFechaHistoricaMinima(Date fechaMayorQue) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public Date getFechaHistoricaMaxima(Date fecha) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Point> consultarHistorico(Date fechaBase1, Date fechaBase2) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Point> consultarPuntoByLow(Date fechaBase1, Date fechaBase2, double base) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Point> consultarPuntoByHigh(Date fechaBase1, Date fechaBase2, double base) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public DoubleInterval consultarMaximoMinimo(Date fecha1, Date fecha2) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<? extends Point> consultarHistorico(Date fechaBase) throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public double consultarPorcentajeCumplimientoIndicador(IntervalIndicatorManager<?> indManager, IntervalIndicator ii)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public double consultarPorcentajeCumplimientoIndicador(IntervalIndicatorManager<?> indManager, IntervalIndicator ii,
			DateInterval di) throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

}
