package forex.genetic.entities.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCursor;

import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;

public class MongoTendenciaParaOperarHelper {

	public static Map<String, Object> toPrimaryKeyMap(TendenciaParaOperar obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();

		objectMap.put("tipoExportacion", obj.getTipoExportacion());
		objectMap.put("periodo", obj.getPeriod());
		objectMap.put("tipoOperacion", obj.getTipoOperacion().name());
		objectMap.put("fechaBase", obj.getFechaBase());
		return objectMap;
	}

	public static Map<String, Object> toMap(TendenciaParaOperar obj) {
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
		if (obj.getRegresion() != null) {
			objectMap.put("tiempoTendencia", obj.getRegresion().getTiempoTendencia());
			objectMap.put("r2", obj.getRegresion().getR2());
			objectMap.put("pendiente", obj.getRegresion().getPendiente());
			objectMap.put("desviacion", obj.getRegresion().getDesviacion());
			objectMap.put("r2Filtrada", obj.getRegresionFiltrada().getR2());
			objectMap.put("minPrecio", obj.getRegresion().getMinPrecio());
			objectMap.put("maxPrecio", obj.getRegresion().getMaxPrecio());
			objectMap.put("cantidad", obj.getRegresion().getCantidad());
		}
		if (obj.getRegresionFiltrada() != null) {
			objectMap.put("pendienteFiltrada", obj.getRegresionFiltrada().getPendiente());
			objectMap.put("desviacionFiltrada", obj.getRegresionFiltrada().getDesviacion());
			objectMap.put("cantidadFiltrada", obj.getRegresionFiltrada().getCantidad());
		}
		objectMap.put("fecha", new Date());
		objectMap.put("idEjecucion", obj.getIdEjecucion());
		objectMap.put("activa", obj.getActiva());

		return objectMap;
	}

	public static Map<String, Object> toMapForDelete(TendenciaParaOperar obj, Date fechaReferencia) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
//		WHERE TIPO_EXPORTACION=? AND TRUNC(FECHA_BASE,'HH24')=TRUNC(?,'HH24')" + " AND ID_EJECUCION<>?"
//				+ " AND FECHA<?";
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

	public static List<TendenciaParaOperarMaxMin> helpList(MongoCursor<Document> cursor) {
		List<TendenciaParaOperarMaxMin> list = new ArrayList<>();
		try {
			while (cursor.hasNext()) {
				list.add(helpOne(cursor.next()));
			}
		} finally {
			cursor.close();
		}
		return list;
	}

	public static TendenciaParaOperarMaxMin helpOne(Document one) {
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

		Regresion regresion = new Regresion();
		regresion.setTiempoTendencia(one.getDouble("tiempoTendencia"));
		regresion.setR2(one.getDouble("r2"));
		regresion.setPendiente(one.getDouble("pendiente"));
		regresion.setDesviacion(one.getDouble("desviacion"));
		regresion.setMinPrecio(one.getDouble("minPrecio"));
		regresion.setMaxPrecio(one.getDouble("maxPrecio"));
		regresion.setCantidad(one.getInteger("cantidad"));
		tpo.setRegresion(regresion);
		return tpo;
	}
}
