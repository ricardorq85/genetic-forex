package forex.genetic.dao.helper.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import forex.genetic.entities.DatoAdicionalTPO;
import forex.genetic.entities.Extremos;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.util.DateUtil;

public class MongoDatoAdicionalTPOHelper {

	public static Map<String, Object> toPrimaryKeyMap(DatoAdicionalTPO obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("fechaBase", obj.getFechaBase());
		return objectMap;
	}

	public static Map<String, Object> toMap(DatoAdicionalTPO obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("fechaBase", obj.getFechaBase());
		objectMap.put("fecha", new Date());
		objectMap.put("r2Promedio", obj.getR2Promedio());
		objectMap.put("pendientePromedio", obj.getPendientePromedio());
		objectMap.put("probabilidadPromedio", obj.getProbabilidadPromedio());
		objectMap.put("numeroTendencia", obj.getNumeroTendencias());
		objectMap.put("cantidadTotalTendencias", obj.getCantidadTotalTendencias());
		objectMap.put("numeroPendientesPositivas", obj.getNumeroPendientesPositivas());
		objectMap.put("diferenciaPrecioSuperior", obj.getDiferenciaPrecioSuperior());
		objectMap.put("diferenciaPrecioInferior", obj.getDiferenciaPrecioInferior());
		objectMap.put("minPrimeraTendencia", obj.getMinPrimeraTendencia());
		objectMap.put("maxPrimeraTendencia", obj.getMaxPrimeraTendencia());
		objectMap.put("avgPrimeraTendencia", obj.getAvgPrimeraTendencia());
		
		Extremos extremos = obj.getExtremos();
		setExtremosToMap(objectMap, extremos);

		objectMap.put("factorDatos", obj.getFactorDatos());

		return objectMap;
	}

	private static void setExtremosToMap(Map<String, Object> map, Extremos extremos) {
		if ((extremos != null) && (extremos.getExtremosExtremo() != null)) {
			map.put("minExtremoExtremo", extremos.getExtremosExtremo().getLowInterval());
			map.put("maxExtremoExtremo", extremos.getExtremosExtremo().getHighInterval());
		}
		if ((extremos != null) && (extremos.getExtremosFiltrados() != null)) {
			map.put("minExtremoFiltrado", extremos.getExtremosFiltrados().getLowInterval());
			map.put("maxExtremoFiltrado", extremos.getExtremosFiltrados().getHighInterval());
		}
		if ((extremos != null) && (extremos.getExtremosIntermedios() != null)) {
			map.put("minExtremoIntermedio", extremos.getExtremosIntermedios().getLowInterval());
			map.put("maxExtremoIntermedio", extremos.getExtremosIntermedios().getHighInterval());
		}
		if ((extremos != null) && (extremos.getExtremosSinFiltrar() != null)) {
			map.put("minExtremoSinFiltrar", extremos.getExtremosSinFiltrar().getLowInterval());
			map.put("maxExtremoSinFiltrar", extremos.getExtremosSinFiltrar().getHighInterval());
		}
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
}
