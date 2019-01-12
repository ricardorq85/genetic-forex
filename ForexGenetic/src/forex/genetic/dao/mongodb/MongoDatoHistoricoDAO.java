package forex.genetic.dao.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.client.MongoDatabase;

import forex.genetic.entities.DateInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.RangoOperacionIndividuo;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MongoDatoHistoricoDAO extends MongoDefaultDatoHistoricoDAO {

	private final int minYear = 2008;

	public MongoDatoHistoricoDAO(MongoDatabase db) {
		this(db, true);
	}

	public MongoDatoHistoricoDAO(MongoDatabase db, boolean configure) {
		this(db, "datoHistorico2008", configure);
	}

	public MongoDatoHistoricoDAO(MongoDatabase db, String name, boolean configure) {
		super(db, name, configure);
		if (configure) {
			this.configureCollectionInitial();
		}
	}

	public void configureCollectionInitial() {
		int year = 2008;
		int currYear = DateUtil.obtenerAnyo(new Date());
		while (year <= currYear) {
			LogUtil.logTime(new StringBuilder("Configurando collection: ").append(getCollectionName()).toString(), 3);
			setCollection(year, false);
			configureCollection();
			year++;
		}
	}

	private void setCollection(int anyo) {
		setCollection(anyo, false);
	}

	protected void setCollection(int anyo, boolean configure) {
		StringBuilder sb = new StringBuilder("datoHistorico").append(anyo);
		setCollection(sb.toString(), configure);
	}

	@Override
	public List<Point> consultarPuntosInfinity() {
		List<Point> p = new ArrayList<>();
		int year = 2008;
		int currYear = DateUtil.obtenerAnyo(new Date());
		while (year <= currYear) {
			setCollection(year, false);
			p.addAll(super.consultarPuntosInfinity());
			year++;
		}
		return p;
	}

	@Override
	public long delete(Point obj, Date fechaReferencia) {
		setCollection(DateUtil.obtenerAnyo(obj.getDate()));
		return super.delete(obj, fechaReferencia);
	}

	@Override
	public void clean() {
		int year = 2008;
		int currYear = DateUtil.obtenerAnyo(new Date());
		while (year <= currYear) {
			setCollection(year++, false);
			super.clean();
		}
	}

	@Override
	public void consultarRangoOperacionIndicador(RangoOperacionIndividuo rangoOperacion) {
		int initialYear = DateUtil.obtenerAnyo(rangoOperacion.getFechaFiltro());
		int endYear = DateUtil.obtenerAnyo(rangoOperacion.getFechaFiltro2());
		int year = initialYear;
		// TODO ricardorq85 soporte multi años;
//		while (year <= endYear) {
		if (true) {
			setCollection(year);

//			RangoOperacionIndividuo newRangoOperacion = new RangoOperacionIndividuo();
//			List<RangoOperacionIndividuoIndicador> list = new ArrayList<>();
//			list.addAll(rangoOperacion.getIndicadores());
//			newRangoOperacion.setIndicadores(list);
//			newRangoOperacion.setFilterList(rangoOperacion.getFilterList());
//			newRangoOperacion.setFechaFiltro(rangoOperacion.getFechaFiltro());
//			newRangoOperacion.setFechaFiltro2(rangoOperacion.getFechaFiltro2());

			super.consultarRangoOperacionIndicador(rangoOperacion);

//			r.setTakeProfit((r.getTakeProfit() + newRangoOperacion.getTakeProfit()) / 2);
//			r.setStopLoss((r.getStopLoss() + newRangoOperacion.getStopLoss()) / 2);
//			r.setCantidad(r.getCantidad() + newRangoOperacion.getCantidad());
//			for (RangoOperacionIndividuoIndicador rangoOperacionIndividuoIndicador : list) {			}

			year++;
		}
	}

	@Override
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
			count += super.consultarCantidadPuntos(interval);
			year++;
		}
		return count;
	}

	@Override
	public Point consultarXFecha(Date fecha) throws GeneticDAOException {
		int year = DateUtil.obtenerAnyo(fecha);
		setCollection(year);
		Point p = super.consultarXFecha(fecha);
		return p;
	}

	@Override
	public Point consultarRetroceso(Order order) throws GeneticDAOException {
		Point p = null;
		int initialYear = DateUtil.obtenerAnyo(order.getOpenDate());
		int endYear = DateUtil.obtenerAnyo(order.getCloseDate());
		setCollection(initialYear);
		Point p1 = super.consultarRetroceso(order);
		p = p1;
		if (initialYear != endYear) {
			setCollection(endYear);
			Point p2 = super.consultarRetroceso(order);
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

	@Override
	public Date getFechaHistoricaMinima() {
		setCollection(minYear);
		Date fecha = super.getFechaHistoricaMinima();
		return fecha;
	}

	@Override
	public Date getFechaHistoricaMaxima() {
		int year = DateUtil.obtenerAnyo(new Date());
		Date date = null;
		while ((year >= minYear) && (date == null)) {
			setCollection(year);
			date = super.getFechaHistoricaMaxima();
			year--;
		}
		return date;
	}

	@Override
	public <H extends Order> Point consultarPuntoCierreByTakeOrStop(H order, DateInterval rango)
			throws GeneticDAOException {
		int initialYear = DateUtil.obtenerAnyo(rango.getLowInterval());
		int endYear = DateUtil.obtenerAnyo(rango.getHighInterval());
		int year = initialYear;
		Point p = null;
		if ((year <= endYear) && (p == null)) {
			setCollection(year);
			p = super.consultarPuntoCierreByTakeOrStop(order, rango);
			year++;
		}
		return p;
	}

	@Override
	public List<Point> consultarPuntosCierre(IndividuoEstrategia individuo, DateInterval rango) {
		int initialYear = DateUtil.obtenerAnyo(rango.getLowInterval());
		int endYear = DateUtil.obtenerAnyo(rango.getHighInterval());
		int year = initialYear;
		List<Point> p = new ArrayList<>();
		if ((year <= endYear)) {
			setCollection(year);
			p.addAll(super.consultarPuntosCierre(individuo, rango));
			year++;
		}
		return p;
	}

	@Override
	public List<Point> consultarProximosPuntosApertura(IndividuoEstrategia individuo, DateInterval rango) {
		int initialYear = DateUtil.obtenerAnyo(rango.getLowInterval());
		int endYear = DateUtil.obtenerAnyo(rango.getHighInterval());
		int year = initialYear;
		List<Point> puntosApertura = new ArrayList<>();
		while ((year <= endYear)) {
			setCollection(year);
			puntosApertura.addAll(super.consultarProximosPuntosApertura(individuo, rango));
			year++;
		}
		return puntosApertura;
	}

	@Override
	public Point consultarPuntoAnterior(Date fecha) throws GeneticDAOException {
		int initialYear = DateUtil.obtenerAnyo(fecha);
		setCollection(initialYear);
		Point p1 = super.consultarPuntoAnterior(fecha);
		Point p = p1;
		if ((initialYear > minYear) && (p1 == null)) {
			setCollection(--initialYear);
			Point p2 = super.consultarPuntoAnterior(fecha);
			p = p2;
		}
		return p;
	}

	@Override
	public List<Point> consultarHistoricoOrderByPrecio(Date fechaBase1, Date fechaBase2) throws GeneticDAOException {
		int initialYear = DateUtil.obtenerAnyo(fechaBase1);
		int endYear = DateUtil.obtenerAnyo(fechaBase2);
		setCollection(initialYear);
		List<Point> puntos = super.consultarHistoricoOrderByPrecio(fechaBase1, fechaBase2);
		if ((initialYear != endYear)) {
			setCollection(endYear);
			puntos.addAll(super.consultarHistoricoOrderByPrecio(fechaBase1, fechaBase2));
		}
		return puntos;
	}

	@Override
	public double contarCumplimientoIndicador(IntervalIndicatorManager<?> indManager, IntervalIndicator ii,
			DateInterval di) throws GeneticDAOException {
		int initialYear = DateUtil.obtenerAnyo(di.getLowInterval());
		int endYear = DateUtil.obtenerAnyo(di.getHighInterval());
		setCollection(initialYear);
		double puntos = super.contarCumplimientoIndicador(indManager, ii, di);
		if ((initialYear != endYear)) {
			setCollection(endYear);
			puntos += super.contarCumplimientoIndicador(indManager, ii, di);
		}
		return puntos;
	}

}
