package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.TendenciaParaOperarDAO;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.ProcesoTendenciaFiltradaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.util.Constants.OperationType;

public class AgrupadorTendenciaManager {

	private List<ProcesoTendenciaFiltradaBuySell> listaTendencias;
	private List<TendenciaParaOperarMaxMin> tendenciasResultado;
	private Connection conn;
	private TendenciaParaOperarDAO tendenciaParaOperarDAO;
	private ParametroDAO parametroDAO;

	private Date fechaBase;
	private boolean deleteTPO;

	public AgrupadorTendenciaManager(Date fechaBase, Connection conn) throws SQLException {
		super();
		this.conn = conn;
		this.tendenciaParaOperarDAO = new TendenciaParaOperarDAO(conn);
		this.parametroDAO = new ParametroDAO(conn);
		this.listaTendencias = new ArrayList<>();
		this.tendenciasResultado = new ArrayList<>();
		this.setFechaBase(fechaBase);
		this.deleteTPO = this.parametroDAO.getBooleanValorParametro("DELETE_TENDENCIA_PARA_OPERAR");
	}

	public void add(ProcesoTendenciaFiltradaBuySell paraProcesar) {
		this.listaTendencias.add(paraProcesar);
	}

	public void procesar() throws SQLException {
		// double maximoMaximo = Double.NEGATIVE_INFINITY, minimoMinimo =
		// Double.POSITIVE_INFINITY;
		// double maximoMinimo = Double.POSITIVE_INFINITY, minimoMaximo =
		// Double.NEGATIVE_INFINITY;
		double maximoMaximoFiltrada = Double.NEGATIVE_INFINITY, minimoMinimoFiltrada = Double.POSITIVE_INFINITY;
		double maximoMinimoFiltrada = Double.POSITIVE_INFINITY, minimoMaximoFiltrada = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < listaTendencias.size(); i++) {
			ProcesoTendenciaFiltradaBuySell procesoIndex = listaTendencias.get(i);
			Regresion regresionFiltradaIndex = procesoIndex.getRegresionFiltrada();
			if ((regresionFiltradaIndex != null) && (regresionFiltradaIndex.isRegresionValidaMaximoMinimo())) {
				maximoMaximoFiltrada = Math.max(maximoMaximoFiltrada, regresionFiltradaIndex.getMaxPrecio());
				minimoMinimoFiltrada = Math.min(minimoMinimoFiltrada, regresionFiltradaIndex.getMinPrecio());
				maximoMinimoFiltrada = Math.min(maximoMinimoFiltrada, regresionFiltradaIndex.getMaxPrecio());
				minimoMaximoFiltrada = Math.max(minimoMaximoFiltrada, regresionFiltradaIndex.getMinPrecio());
			}
		}
		DoubleInterval extremos = new DoubleInterval((minimoMinimoFiltrada), (maximoMaximoFiltrada));
		DoubleInterval extremosIntermedios = new DoubleInterval((minimoMaximoFiltrada), (maximoMinimoFiltrada));
		procesarExtremos(extremos, extremosIntermedios);
	}

	public void procesarExtremos(DoubleInterval extremos, DoubleInterval extremosIntermedios) throws SQLException {
		int lastIndex = listaTendencias.size() - 1;
		TendenciaParaOperarMaxMin buy = null;
		TendenciaParaOperarMaxMin sell = null;
		for (int i = 1; i < lastIndex; i++) {
			boolean regresionValida = this.validarRegresion(i);
			if (regresionValida) {
				ProcesoTendenciaBuySell procesoIndex = listaTendencias.get(i);
				TendenciaParaOperarMaxMin resultadoParaOperar = crearTendenciaParaOperarMaxMin(extremos,
						extremosIntermedios, procesoIndex);
				if (resultadoParaOperar != null) {
					if (OperationType.BUY.equals(resultadoParaOperar.getTipoOperacion())) {
						if ((buy == null) || (resultadoParaOperar.getVigenciaHigher().after(buy.getVigenciaHigher()))) {
							buy = resultadoParaOperar;
						}
					} else {
						if ((sell == null)
								|| (resultadoParaOperar.getVigenciaHigher().after(sell.getVigenciaHigher()))) {
							sell = resultadoParaOperar;
						}
					}
				}
			}
		}
		processDelete();
		if (buy != null) {
			this.tendenciasResultado.add(buy);
		}
		if (sell != null) {
			this.tendenciasResultado.add(sell);
		}
	}

	protected void processDelete() throws SQLException {
		if (deleteTPO) {
			TendenciaParaOperarMaxMin tpo = new TendenciaParaOperarMaxMin();
			tpo.setFechaBase(fechaBase);
			tendenciaParaOperarDAO.deleteTendenciaParaProcesar(tpo);
			conn.commit();
		}
	}

	private boolean validarRegresion(int index) {
		// ProcesoTendenciaBuySell procesoLastIndex = listaTendencias.get(index
		// - 1);
		ProcesoTendenciaFiltradaBuySell procesoIndex = listaTendencias.get(index);
		ProcesoTendenciaFiltradaBuySell procesoNextIndex = listaTendencias.get(index + 1);

		// return (validarRegresion(procesoLastIndex.getRegresion()) &&
		// validarRegresion(procesoIndex.getRegresion())
		// && validarRegresion(procesoNextIndex.getRegresion()));
		return (procesoIndex.isRegresionValida() && procesoNextIndex.isRegresionValida()
				&& (procesoIndex.getRegresion().getPendiente()
						* procesoIndex.getRegresionFiltrada().getPendiente() > 0));
	}

	// private boolean validarRegresion(Regresion regresion) {
	// return ((regresion != null) && (regresion.isRegresionValida()));
	// }

	private TendenciaParaOperarMaxMin crearTendenciaParaOperarMaxMin(DoubleInterval extremos,
			DoubleInterval extremosIntermedios, ProcesoTendenciaBuySell procesoIndex) {
		TendenciaParaOperarMaxMin tendencia = null;
		Regresion regresionIndex = procesoIndex.getRegresion();
		double precio = 0.0D, sl = 0.0D, tp = 0.0D;
		if (OperationType.BUY.equals(procesoIndex.getTipoOperacion())) {
			precio = extremos.getLowInterval();
			sl = precio;
			tp = extremosIntermedios.getHighInterval();
			// tp = extremos.getHighInterval();
		} else if (OperationType.SELL.equals(procesoIndex.getTipoOperacion())) {
			precio = extremos.getHighInterval();
			sl = precio;
			tp = extremosIntermedios.getLowInterval();
			// tp = extremos.getLowInterval();
		}

		tendencia = new TendenciaParaOperarMaxMin();
		tendencia.setRegresion(regresionIndex);
		tendencia.setTipoOperacion(procesoIndex.getTipoOperacion());
		tendencia.setVigenciaLower(regresionIndex.getMinFechaTendencia());
		tendencia.setVigenciaHigher(regresionIndex.getMaxFechaTendencia());
		tendencia.setFechaBase(procesoIndex.getFechaBase());
		tendencia.setFechaTendencia(procesoIndex.getFechaBase());
		tendencia.setPeriodo(procesoIndex.getPeriodo());
		tendencia.setPrecioCalculado(precio);
		tendencia.setSl(sl);
		tendencia.setTp(tp);
		return tendencia;
	}

	public Date getFechaBase() {
		return fechaBase;
	}

	public void setFechaBase(Date fechaBase) {
		this.fechaBase = fechaBase;
	}

	private void saveTendenciaParaOperar(TendenciaParaOperar ten) throws SQLException {
		boolean exists = tendenciaParaOperarDAO.exists(ten);
		if (exists) {
			tendenciaParaOperarDAO.updateTendenciaParaProcesar(ten);
		} else {
			tendenciaParaOperarDAO.insertTendenciaParaOperar(ten);
		}
	}

	public void export() throws SQLException {
		List<TendenciaParaOperarMaxMin> tendencias = this.tendenciasResultado;
		if (tendencias != null) {
			tendencias.stream().forEach((ten) -> {
				// System.out.println("INDEX=" + (index)+ "," + ten.toString());
				System.out.println(ten.toString());
				try {
					this.saveTendenciaParaOperar(ten);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			conn.commit();
		}
	}

	public List<TendenciaParaOperarMaxMin> getTendenciasResultado() {
		return tendenciasResultado;
	}

	public void setTendenciasResultado(List<TendenciaParaOperarMaxMin> tendenciasResultado) {
		this.tendenciasResultado = tendenciasResultado;
	}

}
