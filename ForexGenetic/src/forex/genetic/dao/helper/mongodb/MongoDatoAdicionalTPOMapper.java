package forex.genetic.dao.helper.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import forex.genetic.entities.DatoAdicionalTPO;
import forex.genetic.entities.Extremos;

public class MongoDatoAdicionalTPOMapper extends MongoMapper<DatoAdicionalTPO> {

	@Override
	public Map<String, Object> toPrimaryKeyMap(DatoAdicionalTPO obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("fechaBase", obj.getFechaBase());
		return objectMap;
	}

	@Override
	public Map<String, Object> toMap(DatoAdicionalTPO obj) {
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

	private void setExtremosToMap(Map<String, Object> map, Extremos extremos) {
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

	@Override
	public Map<String, Object> toMapForDelete(DatoAdicionalTPO obj, Date fechaReferencia) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DatoAdicionalTPO helpOne(Document one) {
		// TODO Auto-generated method stub
		return null;
	}
}
