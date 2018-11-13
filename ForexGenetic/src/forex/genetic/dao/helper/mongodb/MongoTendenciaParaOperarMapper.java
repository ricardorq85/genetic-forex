package forex.genetic.dao.helper.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;

public class MongoTendenciaParaOperarMapper extends MongoMapper<TendenciaParaOperar> {

	@Override
	public Map<String, Object> toPrimaryKeyMap(TendenciaParaOperar obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();

		objectMap.put("tipoExportacion", obj.getTipoExportacion());
		objectMap.put("periodo", obj.getPeriod());
		objectMap.put("tipoOperacion", obj.getTipoOperacion().name());
		objectMap.put("fechaBase", obj.getFechaBase());
		return objectMap;
	}

	@Override
	public Map<String, Object> toMap(TendenciaParaOperar obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();

		objectMap.put("tipoExportacion", obj.getTipoExportacion());
		objectMap.put("periodo", obj.getPeriod());
		objectMap.put("tipoTendencia", obj.getTipoTendencia());
		objectMap.put("tipoOperacion", obj.getTipoOperacion().name());
		objectMap.put("fechaBase", obj.getFechaBase());
		objectMap.put("fechaTendencia", obj.getFechaTendencia());
		objectMap.put("vigenciaLower", obj.getVigenciaLower());
		objectMap.put("vigenciaHigher", obj.getVigenciaHigher());
		objectMap.put("precioCalculado", obj.getPrecioCalculado());
		objectMap.put("stopApertura", obj.getStopApertura());
		objectMap.put("limitApertura", obj.getLimitApertura());
		objectMap.put("takeProfit", obj.getTp());
		objectMap.put("stopLoss", obj.getSl());
		objectMap.put("lote", obj.getLote());
		objectMap.put("loteCalculado", obj.getLoteCalculado());
		objectMap.put("regresion", regresionToMap(obj.getRegresion()));
		objectMap.put("regresionFiltrada", regresionToMap(obj.getRegresionFiltrada()));
		objectMap.put("fecha", new Date());
		objectMap.put("idEjecucion", obj.getIdEjecucion());
		objectMap.put("activa", obj.getActiva());

		return objectMap;
	}

	public Map<String, Object> regresionToMap(Regresion obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		if (obj != null) {
			objectMap.put("tiempoTendencia", obj.getTiempoTendencia());
			objectMap.put("r2", obj.getR2());
			objectMap.put("pendiente", obj.getPendiente());
			objectMap.put("desviacion", obj.getDesviacion());
			objectMap.put("minPrecio", obj.getMinPrecio());
			objectMap.put("maxPrecio", obj.getMaxPrecio());
			objectMap.put("cantidad", obj.getCantidad());
		}
		return objectMap;
	}

	@Override
	public Map<String, Object> toMapForDelete(TendenciaParaOperar obj, Date fechaReferencia) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("tipoExportacion", obj.getTipoExportacion());

		Map<String, Object> mapFechaBase = new HashMap<String, Object>();
		Date initDate = DateUtil.truncHourDate(obj.getFechaBase());
		Date endDate = DateUtil.adicionarMinutos(initDate, 60);
		mapFechaBase.put("$gte", initDate);
		mapFechaBase.put("$lt", endDate);
		objectMap.put("fechaBase", mapFechaBase);

		Map<String, Object> mapFechaReferencia = new HashMap<String, Object>();
		mapFechaReferencia.put("$lt", fechaReferencia);
		objectMap.put("fecha", mapFechaReferencia);

		Map<String, Object> mapIdEjecucion = new HashMap<String, Object>();
		mapIdEjecucion.put("$ne", obj.getIdEjecucion());
		objectMap.put("idEjecucion", mapIdEjecucion);

		return objectMap;
	}

	@Override
	public TendenciaParaOperarMaxMin helpOne(Document one) {
		TendenciaParaOperarMaxMin tpo = new TendenciaParaOperarMaxMin();
		tpo.setIdEjecucion(one.getString("idEjecucion"));
		tpo.setPeriodo(one.getString("periodo"));
		tpo.setTipoTendencia(one.getString("tipoTendencia"));
		tpo.setTipoOperacion(Constants.getOperationType(one.getString("tipoOperacion")));
		tpo.setFechaBase(one.getDate("fechaBase"));
		tpo.setFechaTendencia(one.getDate("fechaTendencia"));
		tpo.setVigenciaLower(one.getDate("vigenciaLower"));
		tpo.setVigenciaHigher(one.getDate("vigenciaHigher"));
		tpo.setPrecioCalculado(one.getDouble("precioCalculado"));
		tpo.setStopApertura(one.getDouble("stopApertura"));
		tpo.setLimitApertura(one.getDouble("limitApertura"));
		tpo.setTp(one.getDouble("takeProfit"));
		tpo.setSl(one.getDouble("stopLoss"));
		if (one.containsKey("lote")) {
			tpo.setLote(one.getDouble("lote"));
		}
		tpo.setLoteCalculado(one.getDouble("loteCalculado"));
		tpo.setFecha(one.getDate("fecha"));

		tpo.setRegresion(documentToRegresion((Document) one.get("regresion")));
		tpo.setRegresionFiltrada(documentToRegresion((Document) one.get("regresionFiltrada")));

		return tpo;
	}

	public Regresion documentToRegresion(Document one) {
		Regresion regresion = new Regresion();
		regresion.setTiempoTendencia(one.getDouble("tiempoTendencia"));
		regresion.setR2(one.getDouble("r2"));
		regresion.setPendiente(one.getDouble("pendiente"));
		regresion.setDesviacion(one.getDouble("desviacion"));
		regresion.setMinPrecio(one.getDouble("minPrecio"));
		regresion.setMaxPrecio(one.getDouble("maxPrecio"));
		regresion.setCantidad(one.getInteger("cantidad"));

		return regresion;
	}

}
