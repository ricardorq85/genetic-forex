package forex.genetic.entities.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.IntervalIndicator;

public class MongoDatoHistoricoHelper {

	public static Map<String, Object> toPrimaryKeyMap(Point obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();

		objectMap.put("moneda", obj.getMoneda());
		objectMap.put("periodo", obj.getPeriodo());
		objectMap.put("fechaHistorico", obj.getDate());
		return objectMap;
	}

	public static Map<String, Object> toMap(Point datoHistorico) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("moneda", datoHistorico.getMoneda());
		objectMap.put("periodo", datoHistorico.getPeriodo());
		objectMap.put("monedaComparacion", datoHistorico.getMonedaComparacion());
		objectMap.put("fechaHistorico", datoHistorico.getDate());
		objectMap.put("open", datoHistorico.getOpen());
		objectMap.put("low", datoHistorico.getLow());
		objectMap.put("high", datoHistorico.getHigh());
		objectMap.put("close", datoHistorico.getClose());
		objectMap.put("volume", datoHistorico.getVolume());
		objectMap.put("spread", datoHistorico.getSpread());
		objectMap.put("closeCompare", datoHistorico.getCloseCompare());
		objectMap.put("fechaRegistro", new Date());

		List<Map<String, Object>> indicadores = new ArrayList<Map<String, Object>>();

		List<IntervalIndicator> indicadoresBase = ((List<IntervalIndicator>) datoHistorico.getIndicators());
		indicadoresBase.stream().forEach((ind) -> {
			indicadores.add(ind.toMap());
		});

		objectMap.put("indicadores", indicadores);

		return objectMap;
	}
	/*
	 * public static Map<String, Object> regresionToMap(Regresion obj) { Map<String,
	 * Object> objectMap = new HashMap<String, Object>(); if (obj != null) {
	 * objectMap.put("tiempoTendencia", obj.getTiempoTendencia());
	 * objectMap.put("r2", obj.getR2()); objectMap.put("pendiente",
	 * obj.getPendiente()); objectMap.put("desviacion", obj.getDesviacion());
	 * objectMap.put("minPrecio", obj.getMinPrecio()); objectMap.put("maxPrecio",
	 * obj.getMaxPrecio()); objectMap.put("cantidad", obj.getCantidad()); } return
	 * objectMap; }
	 * 
	 * public static Map<String, Object> toMapForDelete(TendenciaParaOperar obj,
	 * Date fechaReferencia) { Map<String, Object> objectMap = new HashMap<String,
	 * Object>(); // WHERE TIPO_EXPORTACION=? AND
	 * TRUNC(FECHA_BASE,'HH24')=TRUNC(?,'HH24')" + " AND ID_EJECUCION<>?" // +
	 * " AND FECHA<?"; objectMap.put("tipoExportacion", obj.getTipoExportacion());
	 * 
	 * Map<String, Object> mapFechaBase = new HashMap<String, Object>(); Date
	 * initDate = DateUtil.truncHourDate(obj.getFechaBase()); Date endDate =
	 * DateUtil.adicionarMinutos(initDate, 60); mapFechaBase.put("$gte", initDate);
	 * mapFechaBase.put("$lt", endDate); objectMap.put("fechaBase", mapFechaBase);
	 * 
	 * Map<String, Object> mapFechaReferencia = new HashMap<String, Object>();
	 * mapFechaReferencia.put("$lt", fechaReferencia); objectMap.put("fecha",
	 * mapFechaReferencia);
	 * 
	 * Map<String, Object> mapIdEjecucion = new HashMap<String, Object>();
	 * mapIdEjecucion.put("$ne", obj.getIdEjecucion()); objectMap.put("idEjecucion",
	 * mapIdEjecucion);
	 * 
	 * return objectMap; }
	 * 
	 * public static List<TendenciaParaOperarMaxMin> helpList(MongoCursor<Document>
	 * cursor) { List<TendenciaParaOperarMaxMin> list = new ArrayList<>(); try {
	 * while (cursor.hasNext()) { list.add(helpOne(cursor.next())); } } finally {
	 * cursor.close(); } return list; }
	 * 
	 * public static TendenciaParaOperarMaxMin helpOne(Document one) {
	 * TendenciaParaOperarMaxMin tpo = new TendenciaParaOperarMaxMin();
	 * tpo.setIdEjecucion(one.getString("idEjecucion"));
	 * tpo.setPeriodo(one.getString("periodo"));
	 * tpo.setTipoTendencia(one.getString("tipoTendencia"));
	 * tpo.setTipoOperacion(Constants.getOperationType(one.getString("tipoOperacion"
	 * ))); tpo.setFechaBase(one.getDate("fechaBase"));
	 * tpo.setFechaTendencia(one.getDate("fechaTendencia"));
	 * tpo.setVigenciaLower(one.getDate("vigenciaLower"));
	 * tpo.setVigenciaHigher(one.getDate("vigenciaHigher"));
	 * tpo.setPrecioCalculado(one.getDouble("precioCalculado"));
	 * tpo.setStopApertura(one.getDouble("stopApertura"));
	 * tpo.setLimitApertura(one.getDouble("limitApertura"));
	 * tpo.setTp(one.getDouble("takeProfit")); tpo.setSl(one.getDouble("stopLoss"));
	 * if (one.containsKey("lote")) { tpo.setLote(one.getDouble("lote")); }
	 * tpo.setLoteCalculado(one.getDouble("loteCalculado"));
	 * tpo.setFecha(one.getDate("fecha"));
	 * 
	 * tpo.setRegresion(documentToRegresion((Document) one.get("regresion")));
	 * tpo.setRegresionFiltrada(documentToRegresion((Document)
	 * one.get("regresionFiltrada")));
	 * 
	 * return tpo; }
	 * 
	 * public static Regresion documentToRegresion(Document one) { Regresion
	 * regresion = new Regresion();
	 * regresion.setTiempoTendencia(one.getDouble("tiempoTendencia"));
	 * regresion.setR2(one.getDouble("r2"));
	 * regresion.setPendiente(one.getDouble("pendiente"));
	 * regresion.setDesviacion(one.getDouble("desviacion"));
	 * regresion.setMinPrecio(one.getDouble("minPrecio"));
	 * regresion.setMaxPrecio(one.getDouble("maxPrecio"));
	 * regresion.setCantidad(one.getInteger("cantidad"));
	 * 
	 * return regresion; }
	 */
}
