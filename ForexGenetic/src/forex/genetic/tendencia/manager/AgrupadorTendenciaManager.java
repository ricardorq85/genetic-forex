package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.TendenciaParaOperarDAO;
import forex.genetic.entities.DatoAdicionalTPO;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Extremos;
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
	private DatoHistoricoDAO datoHistoricoDAO;
	private ParametroDAO parametroDAO;

	private Date fechaBase;
	private int cantidadDatosAdicionales;
	private double precioPonderado;
	private double sumaR2, sumaPendiente, sumaProbabilidad, sumaPrecio;
	private DatoAdicionalTPO adicionalTPO;
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
		this.datoHistoricoDAO = new DatoHistoricoDAO(conn);
		this.precioPonderado = datoHistoricoDAO.consultarPrecioPonderado(fechaBase);
	}

	public void add(ProcesoTendenciaFiltradaBuySell paraProcesar) {
		this.listaTendencias.add(paraProcesar);
		this.cantidadDatosAdicionales++;
		this.sumaR2 += paraProcesar.getRegresion().getR2();
		this.sumaPendiente += paraProcesar.getRegresion().getPendiente();
		this.sumaProbabilidad += paraProcesar.getRegresion().getProbabilidad();
		if ((paraProcesar.getTendencias() != null) && (paraProcesar.getTendencias().size() > 0)) {
			this.sumaPrecio += paraProcesar.getTendencias().get(0).getPrecioCalculado();
		}
	}

	public void procesar() throws SQLException {
		createDatoAdicional();
		Extremos extremos = encontrarExtremos();
		procesarExtremos(extremos);
	}

	protected void createDatoAdicional() {
		this.adicionalTPO = new DatoAdicionalTPO();
		this.adicionalTPO.setFechaBase(fechaBase);
		this.adicionalTPO.setR2Promedio(sumaR2 / cantidadDatosAdicionales);
		this.adicionalTPO.setPendientePromedio(sumaPendiente / cantidadDatosAdicionales);
		this.adicionalTPO.setProbabilidadPromedio(sumaProbabilidad / cantidadDatosAdicionales);
		this.adicionalTPO.setDiferenciaPrecioPromedio((sumaPrecio / cantidadDatosAdicionales) - precioPonderado);
	}

	protected Extremos encontrarExtremos() {
		double valorMaximoDeLosMaximosExtremo = Double.NEGATIVE_INFINITY,
				valorMinimoDeLosMinimosExtremo = Double.POSITIVE_INFINITY;
		double valorMaximoDeLosMaximosSinFiltrar = Double.NEGATIVE_INFINITY,
				valorMinimoDeLosMinimosSinFiltrar = Double.POSITIVE_INFINITY;
		double valorMaximoDeLosMaximosFiltrada = Double.NEGATIVE_INFINITY,
				valorMinimoDeLosMinimosFiltrada = Double.POSITIVE_INFINITY;
		double valorMinimoDeLosMaximosFiltrada = Double.POSITIVE_INFINITY,
				valorMaximoDeLosMinimosFiltrada = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < listaTendencias.size(); i++) {
			ProcesoTendenciaFiltradaBuySell procesoIndex = listaTendencias.get(i);
			Regresion regresionFiltradaIndex = procesoIndex.getRegresionFiltrada();
			Regresion regresionIndex = procesoIndex.getRegresion();
			if (regresionFiltradaIndex != null) {
				if (regresionFiltradaIndex.isRegresionValidaMaximoMinimo()) {
					valorMaximoDeLosMaximosFiltrada = Math.max(valorMaximoDeLosMaximosFiltrada,
							regresionFiltradaIndex.getMaxPrecio());
					valorMinimoDeLosMinimosFiltrada = Math.min(valorMinimoDeLosMinimosFiltrada,
							regresionFiltradaIndex.getMinPrecio());
					valorMinimoDeLosMaximosFiltrada = Math.min(valorMinimoDeLosMaximosFiltrada,
							regresionFiltradaIndex.getMaxPrecio());
					valorMaximoDeLosMinimosFiltrada = Math.max(valorMaximoDeLosMinimosFiltrada,
							regresionFiltradaIndex.getMinPrecio());
				}
			}
			if (regresionIndex != null) {
				if (regresionIndex.isRegresionValidaMaximoMinimo()) {
					valorMaximoDeLosMaximosExtremo = Math.max(valorMaximoDeLosMaximosExtremo,
							regresionIndex.getMaxPrecioExtremo());
					valorMinimoDeLosMinimosExtremo = Math.min(valorMinimoDeLosMinimosExtremo,
							regresionIndex.getMinPrecioExtremo());
					valorMaximoDeLosMaximosSinFiltrar = Math.max(valorMaximoDeLosMaximosSinFiltrar,
							regresionIndex.getMaxPrecio());
					valorMinimoDeLosMinimosSinFiltrar = Math.min(valorMinimoDeLosMinimosSinFiltrar,
							regresionIndex.getMinPrecio());
				}
			}
		}
		DoubleInterval extremosFiltrada = new DoubleInterval((valorMinimoDeLosMinimosFiltrada),
				(valorMaximoDeLosMaximosFiltrada));
		DoubleInterval extremosIntermedios = new DoubleInterval((valorMaximoDeLosMinimosFiltrada),
				(valorMinimoDeLosMaximosFiltrada));
		DoubleInterval extremosExtremo = new DoubleInterval((valorMinimoDeLosMinimosExtremo),
				(valorMaximoDeLosMaximosExtremo));
		DoubleInterval extremosSinFiltrar = new DoubleInterval((valorMinimoDeLosMinimosSinFiltrar),
				(valorMaximoDeLosMaximosSinFiltrar));
		Extremos extremos = new Extremos(extremosFiltrada, extremosIntermedios, extremosExtremo, extremosSinFiltrar);
		return extremos;
	}

	public void procesarExtremos(Extremos extremos) throws SQLException {
		int lastIndex = listaTendencias.size() - 1;
		TendenciaParaOperarMaxMin buy = null;
		TendenciaParaOperarMaxMin sell = null;
		Regresion maximaRegresionValidaBuy = null;
		Regresion maximaRegresionValidaSell = null;
		for (int i = 1; i < lastIndex; i++) {
			boolean regresionValida = this.validarRegresion(i);
			if (regresionValida) {
				ProcesoTendenciaBuySell procesoIndex = listaTendencias.get(i);
				TendenciaParaOperarMaxMin resultadoParaOperar = crearTendenciaParaOperarMaxMin(extremos, procesoIndex);
				if (resultadoParaOperar != null) {
					if (OperationType.BUY.equals(resultadoParaOperar.getTipoOperacion())) {
						if ((buy == null) || (resultadoParaOperar.getVigenciaHigher().after(buy.getVigenciaHigher()))) {
							buy = resultadoParaOperar;
							maximaRegresionValidaBuy = procesoIndex.getRegresion();
						}
					} else {
						if ((sell == null)
								|| (resultadoParaOperar.getVigenciaHigher().after(sell.getVigenciaHigher()))) {
							sell = resultadoParaOperar;
							maximaRegresionValidaSell = procesoIndex.getRegresion();
						}
					}
				}
			}
		}
		processDelete();
		extremos.setMaximaRegresionFiltradaBuy(maximaRegresionValidaBuy);
		extremos.setMaximaRegresionFiltradaSell(maximaRegresionValidaSell);
		TendenciaParaOperarMaxMin[] tendenciaExtremos = crearTendenciaParaOperarMaxMinExtremo(extremos,
				listaTendencias.get(lastIndex));
		TendenciaParaOperarMaxMin[] tendenciaExtremosSinFiltrar = crearTendenciaParaOperarMaxMinSinFiltrar(extremos,
				listaTendencias.get(lastIndex));

		// if (tendenciaExtremos != null) {
		// this.tendenciasResultado.addAll(Arrays.asList(tendenciaExtremos));
		// }
		if (buy != null) {
			this.tendenciasResultado.add(tendenciaExtremos[0]);
			this.tendenciasResultado.add(tendenciaExtremosSinFiltrar[0]);
			this.tendenciasResultado.add(buy);
		}
		if (sell != null) {
			this.tendenciasResultado.add(tendenciaExtremos[1]);
			this.tendenciasResultado.add(tendenciaExtremosSinFiltrar[1]);
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
		ProcesoTendenciaFiltradaBuySell procesoIndex = listaTendencias.get(index);
		boolean valida = false;
		for (int i = index + 1; !valida && i < listaTendencias.size(); i++) {
			ProcesoTendenciaFiltradaBuySell procesoNextIndex = listaTendencias.get(index + 1);
			valida = (procesoNextIndex.isValidacionRegresionValida()
					&& (procesoNextIndex.getRegresionFiltrada().getPendiente()
							* procesoIndex.getRegresionFiltrada().getPendiente() > 0));
		}
		valida = (valida) && (procesoIndex.isRegresionValida() && (procesoIndex.getRegresion().getPendiente()
				* procesoIndex.getRegresionFiltrada().getPendiente() > 0));
		return valida;
	}

	private TendenciaParaOperarMaxMin crearTendenciaParaOperarMaxMin(Extremos extremos,
			ProcesoTendenciaBuySell procesoIndex) {
		double precio = 0.0D, sl = 0.0D, tp = 0.0D;
		if (OperationType.BUY.equals(procesoIndex.getTipoOperacion())) {
			precio = extremos.getExtremosFiltrados().getLowInterval();
			sl = precio;
			tp = extremos.getExtremosIntermedios().getHighInterval();
			// tp = extremos.getHighInterval();
		} else if (OperationType.SELL.equals(procesoIndex.getTipoOperacion())) {
			precio = extremos.getExtremosFiltrados().getHighInterval();
			sl = precio;
			tp = extremos.getExtremosIntermedios().getLowInterval();
			// tp = extremos.getLowInterval();
		}

		TendenciaParaOperarMaxMin tendencia = crearTendenciaParaOperar(procesoIndex, procesoIndex.getTipoOperacion(),
				precio, tp, sl, procesoIndex.getPeriodo());
		return tendencia;
	}

	private TendenciaParaOperarMaxMin[] crearTendenciaParaOperarMaxMinExtremo(Extremos extremos,
			ProcesoTendenciaBuySell procesoIndex) {
		if ((extremos.getMaximaRegresionFiltradaBuy() == null) && (extremos.getMaximaRegresionFiltradaSell() == null)) {
			return null;
		}
		TendenciaParaOperarMaxMin tendenciaBuy = new TendenciaParaOperarMaxMin();
		TendenciaParaOperarMaxMin tendenciaSell = new TendenciaParaOperarMaxMin();
		double precioBuy = 0.0D, precioSell = 0.0D, slBuy = 0.0D, slSell = 0.0D, tpBuy = 0.0D, tpSell = 0.0D;
		precioBuy = extremos.getExtremosExtremo().getLowInterval();
		slBuy = precioBuy;
		tpBuy = extremos.getExtremos().getLowInterval();

		precioSell = extremos.getExtremosExtremo().getHighInterval();
		slSell = precioSell;
		tpSell = extremos.getExtremos().getHighInterval();

		tendenciaBuy = crearTendenciaParaOperar(procesoIndex, OperationType.BUY, precioBuy, tpBuy, slBuy, "EXTREMO",
				extremos.getMaximaRegresionFiltradaBuy());
		tendenciaSell = crearTendenciaParaOperar(procesoIndex, OperationType.SELL, precioSell, tpSell, slSell,
				"EXTREMO", extremos.getMaximaRegresionFiltradaSell());
		return new TendenciaParaOperarMaxMin[] { tendenciaBuy, tendenciaSell };
	}

	private TendenciaParaOperarMaxMin[] crearTendenciaParaOperarMaxMinSinFiltrar(Extremos extremos,
			ProcesoTendenciaBuySell procesoIndex) {
		if ((extremos.getMaximaRegresionFiltradaBuy() == null) && (extremos.getMaximaRegresionFiltradaSell() == null)) {
			return null;
		}
		TendenciaParaOperarMaxMin tendenciaBuy = new TendenciaParaOperarMaxMin();
		TendenciaParaOperarMaxMin tendenciaSell = new TendenciaParaOperarMaxMin();
		double precioBuy = 0.0D, precioSell = 0.0D, slBuy = 0.0D, slSell = 0.0D, tpBuy = 0.0D, tpSell = 0.0D;
		precioBuy = extremos.getExtremos().getLowInterval();
		slBuy = precioBuy;
		tpBuy = extremos.getExtremosFiltrados().getLowInterval();

		precioSell = extremos.getExtremos().getHighInterval();
		slSell = precioSell;
		tpSell = extremos.getExtremosFiltrados().getHighInterval();

		tendenciaBuy = crearTendenciaParaOperar(procesoIndex, OperationType.BUY, precioBuy, tpBuy, slBuy,
				"EXTREMO_SINFILTRAR", extremos.getMaximaRegresionFiltradaBuy());
		tendenciaSell = crearTendenciaParaOperar(procesoIndex, OperationType.SELL, precioSell, tpSell, slSell,
				"EXTREMO_SINFILTRAR", extremos.getMaximaRegresionFiltradaSell());
		return new TendenciaParaOperarMaxMin[] { tendenciaBuy, tendenciaSell };
	}

	private TendenciaParaOperarMaxMin crearTendenciaParaOperar(ProcesoTendenciaBuySell procesoIndex,
			OperationType tipoOperacion, double precio, double tp, double sl, String periodo) {
		return this.crearTendenciaParaOperar(procesoIndex, tipoOperacion, precio, tp, sl, periodo, null);
	}

	private TendenciaParaOperarMaxMin crearTendenciaParaOperar(ProcesoTendenciaBuySell procesoIndex,
			OperationType tipoOperacion, double precio, double tp, double sl, String periodo, Regresion regresion) {
		TendenciaParaOperarMaxMin tpo = new TendenciaParaOperarMaxMin();
		Regresion regresionIndex = regresion;
		if (regresion == null) {
			regresionIndex = procesoIndex.getRegresion();
		}
		tpo.setRegresion(regresionIndex);
		tpo.setTipoOperacion(tipoOperacion);
		tpo.setVigenciaLower(regresionIndex.getMinFechaTendencia());
		tpo.setVigenciaHigher(regresionIndex.getMaxFechaTendencia());
		tpo.setFechaBase(procesoIndex.getFechaBase());
		tpo.setFechaTendencia(procesoIndex.getFechaBase());
		tpo.setPeriodo(periodo);
		tpo.setPrecioCalculado(precio);
		tpo.setSl(sl);
		tpo.setTp(tp);
		return tpo;
	}

	private void saveTendenciaParaOperar(TendenciaParaOperar ten) throws SQLException {
		boolean exists = tendenciaParaOperarDAO.exists(ten);
		if (exists) {
			tendenciaParaOperarDAO.updateTendenciaParaProcesar(ten);
		} else {
			tendenciaParaOperarDAO.insertTendenciaParaOperar(ten);
		}
	}

	public void saveDatosAdicionalesTPO(DatoAdicionalTPO datoAdicional) throws SQLException {
		boolean exists = tendenciaParaOperarDAO.existsDatoAdicional(datoAdicional);
		if (exists) {
			tendenciaParaOperarDAO.updateDatoAdicionalTPO(datoAdicional);
		} else {
			tendenciaParaOperarDAO.insertDatosAdicionalesTPO(datoAdicional);
		}
	}

	public void export() throws SQLException {
		this.saveDatosAdicionalesTPO(this.adicionalTPO);
		conn.commit();
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

	public Date getFechaBase() {
		return fechaBase;
	}

	public void setFechaBase(Date fechaBase) {
		this.fechaBase = fechaBase;
	}

}
