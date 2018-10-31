package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.TendenciaParaOperarDAO;
import forex.genetic.entities.DatoAdicionalTPO;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Extremos;
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.ProcesoTendenciaFiltradaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.NumberUtil;

public class AgrupadorTendenciaManager {

	private List<ProcesoTendenciaFiltradaBuySell> listaTendencias;
	protected List<TendenciaParaOperarMaxMin> tendenciasResultado;
	private Connection conn;
	private TendenciaParaOperarDAO tendenciaParaOperarDAO;
	private DatoHistoricoDAO datoHistoricoDAO;

	private Date fechaBase, maxFechaProceso;
	private int numeroTendencias, cantidadTotalTendencias, numeroPendientesPositivas, numeroPendientesNegativas;
	private double precioPonderado;
	private double sumaR2, sumaPendiente, sumaProbabilidad, sumaPrimeraTendencia;
	protected DatoAdicionalTPO adicionalTPO;
	private double stepLote = 0.01D;
	private double maxLote = 0.1D;

	public AgrupadorTendenciaManager(Date fechaBase, Date maxFechaProceso, Connection conn) throws SQLException {
		super();
		this.conn = conn;
		this.tendenciaParaOperarDAO = new TendenciaParaOperarDAO(conn);
		this.listaTendencias = new ArrayList<>();
		this.tendenciasResultado = new ArrayList<>();
		this.setFechaBase(fechaBase);
		this.maxFechaProceso = maxFechaProceso;
		this.datoHistoricoDAO = new DatoHistoricoDAO(conn);
		this.precioPonderado = datoHistoricoDAO.consultarPrecioPonderado(fechaBase);
	}

	protected void createDatoAdicional(Extremos extremos) {
		this.adicionalTPO = new DatoAdicionalTPO();
		this.adicionalTPO.setFechaBase(fechaBase);
		this.adicionalTPO.setNumeroTendencias(numeroTendencias);
		this.adicionalTPO.setR2Promedio(sumaR2 / numeroTendencias);
		this.adicionalTPO.setPendientePromedio(sumaPendiente / numeroTendencias);
		this.adicionalTPO.setProbabilidadPromedio(sumaProbabilidad / numeroTendencias);
		this.adicionalTPO.setCantidadTotalTendencias(cantidadTotalTendencias);
		this.adicionalTPO.setNumeroPendientesPositivas(numeroPendientesPositivas);
		this.adicionalTPO.setNumeroPendientesNegativas(numeroPendientesNegativas);
		this.adicionalTPO
				.setDiferenciaPrecioSuperior(extremos.getExtremosFiltrados().getHighInterval() - this.precioPonderado);
		this.adicionalTPO
				.setDiferenciaPrecioInferior(this.precioPonderado - extremos.getExtremosFiltrados().getLowInterval());
		this.adicionalTPO
				.setMinPrimeraTendencia(this.precioPonderado - extremos.getExtremosPrimeraTendencia().getLowInterval());
		this.adicionalTPO.setMaxPrimeraTendencia(
				this.precioPonderado - extremos.getExtremosPrimeraTendencia().getHighInterval());
		this.adicionalTPO.setAvgPrimeraTendencia(this.precioPonderado - (sumaPrimeraTendencia / numeroTendencias));
		this.adicionalTPO.setExtremos(extremos);
	}

	public void add(ProcesoTendenciaFiltradaBuySell paraProcesar) {
		this.listaTendencias.add(paraProcesar);
		this.numeroTendencias++;
		this.sumaR2 += paraProcesar.getRegresion().getR2();
		double pendiente = paraProcesar.getRegresion().getPendiente();
		this.sumaPendiente += pendiente;
		this.sumaProbabilidad += paraProcesar.getRegresion().getProbabilidad();
		this.cantidadTotalTendencias += paraProcesar.getRegresion().getCantidadTotal();
		this.numeroPendientesPositivas += (pendiente > 0 ? 1 : 0);
		this.numeroPendientesNegativas += (pendiente < 0 ? 1 : 0);
		this.sumaPrimeraTendencia += paraProcesar.getRegresion().getPrimeraTendencia();
	}

	public void procesar() throws SQLException {
		Extremos extremos = encontrarExtremos();
		if (extremos != null) {
			createDatoAdicional(extremos);
			procesarExtremos(extremos);
			inactivarInvalidas();
		}
	}

	private void inactivarInvalidas() {
		List<TendenciaParaOperarMaxMin> tendencias = this.tendenciasResultado;
		if (tendencias != null) {
			tendencias.stream().forEach((tpo) -> {
				try {
					if (!tpo.getRegresion().isRegresionValida()) {
						tpo.setActiva(0);
					} else {
						double pendiente = this.adicionalTPO.getPendientePromedio();
						if (OperationType.BUY.equals(tpo.getTipoOperacion())) {
							if (pendiente < 0) {
								// tpo.setActiva(0);
							}
						} else if (OperationType.SELL.equals(tpo.getTipoOperacion())) {
							if (pendiente > 0) {
								// tpo.setActiva(0);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
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
		double valorMinimoPrimeraTendenciaFiltrada = Double.POSITIVE_INFINITY,
				valorMaximoPrimeraTendenciaFiltrada = Double.NEGATIVE_INFINITY;

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

					valorMinimoPrimeraTendenciaFiltrada = Math.min(valorMinimoPrimeraTendenciaFiltrada,
							regresionIndex.getPrimeraTendencia());
					valorMaximoPrimeraTendenciaFiltrada = Math.max(valorMaximoPrimeraTendenciaFiltrada,
							regresionIndex.getPrimeraTendencia());
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
		DoubleInterval extremosPrimeraTendencia = new DoubleInterval((valorMinimoPrimeraTendenciaFiltrada),
				(valorMaximoPrimeraTendenciaFiltrada));
		Extremos extremos = null;
		if (!(extremosFiltrada.getLowInterval().isInfinite() && extremosFiltrada.getHighInterval().isInfinite())) {
			extremos = new Extremos(extremosFiltrada, extremosIntermedios, extremosExtremo, extremosSinFiltrar);
			extremos.setExtremosPrimeraTendencia(extremosPrimeraTendencia);
		}
		return extremos;
	}

	public void procesarExtremos(Extremos extremos) throws SQLException {
		int lastIndex = listaTendencias.size() - 1;
		TendenciaParaOperarMaxMin buy = null, ultimoBuy = null, buyMayorPendiente = null;
		TendenciaParaOperarMaxMin sell = null, ultimoSell = null, sellMayorPendiente = null;
		Regresion maximaRegresionValidaBuy = null;
		Regresion maximaRegresionValidaSell = null;
		for (int i = 1; i < lastIndex; i++) {
			boolean regresionValida = this.validarRegresion(i);
			ProcesoTendenciaBuySell procesoIndex = listaTendencias.get(i);
			if (procesoIndex.getTipoOperacion() != null) {
				TendenciaParaOperarMaxMin resultadoParaOperar = crearTendenciaParaOperarMaxMin(extremos, procesoIndex);
				Regresion regresionFiltrada = resultadoParaOperar.getRegresionFiltrada();
				Regresion regresion = resultadoParaOperar.getRegresion();
				// System.out.println(i);
				if (resultadoParaOperar != null) {
					if (OperationType.BUY.equals(resultadoParaOperar.getTipoOperacion())) {
						if ((regresionFiltrada != null) && (regresionFiltrada.isRegresionValidaMaximoMinimo())) {
							ultimoBuy = resultadoParaOperar;
							if ((buyMayorPendiente == null || (Math.abs(regresion.getPendiente()) > Math
									.abs(buyMayorPendiente.getRegresion().getPendiente())))) {
								buyMayorPendiente = resultadoParaOperar;
							}
						}
						if (regresionValida) {
							if ((buy == null)
									|| (resultadoParaOperar.getVigenciaHigher().after(buy.getVigenciaHigher()))) {
								buy = resultadoParaOperar;
								maximaRegresionValidaBuy = procesoIndex.getRegresion();
							}
						}
					} else {
						if ((regresionFiltrada != null) && (regresionFiltrada.isRegresionValidaMaximoMinimo())) {
							ultimoSell = resultadoParaOperar;
							if ((sellMayorPendiente == null || (Math.abs(regresion.getPendiente()) > Math
									.abs(sellMayorPendiente.getRegresion().getPendiente())))) {
								sellMayorPendiente = resultadoParaOperar;
							}
						}
						if (regresionValida) {
							if ((sell == null)
									|| (resultadoParaOperar.getVigenciaHigher().after(sell.getVigenciaHigher()))) {
								sell = resultadoParaOperar;
								maximaRegresionValidaSell = procesoIndex.getRegresion();
							}
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
		if ((ultimoBuy != null) && (ultimoBuy != buy)) {
			ultimoBuy.setActiva(0);
			ultimoBuy.setTipoTendencia("ULTIMA_TENDENCIA");
			this.tendenciasResultado.add(ultimoBuy);
		}
		if ((buyMayorPendiente != null) && (buyMayorPendiente != buy) && (buyMayorPendiente != ultimoBuy)) {
			buyMayorPendiente.setActiva(0);
			buyMayorPendiente.setTipoTendencia("MEJOR_PENDIENTE");
			this.tendenciasResultado.add(buyMayorPendiente);
		}
		if (sell != null) {
			this.tendenciasResultado.add(tendenciaExtremos[1]);
			this.tendenciasResultado.add(tendenciaExtremosSinFiltrar[1]);
			this.tendenciasResultado.add(sell);
		}
		if ((ultimoSell != null) && (ultimoSell != sell)) {
			ultimoSell.setActiva(0);
			ultimoSell.setTipoTendencia("ULTIMA_TENDENCIA");
			this.tendenciasResultado.add(ultimoSell);
		}
		if ((sellMayorPendiente != null) && (sellMayorPendiente != sell) && (sellMayorPendiente != ultimoSell)) {
			sellMayorPendiente.setActiva(0);
			sellMayorPendiente.setTipoTendencia("MEJOR_PENDIENTE");
			this.tendenciasResultado.add(sellMayorPendiente);
		}
	}

	protected void processDelete() throws SQLException {
		TendenciaParaOperarMaxMin tpo = new TendenciaParaOperarMaxMin();
		tpo.setFechaBase(fechaBase);
		int borrados = tendenciaParaOperarDAO.deleteTendenciaParaProcesar(tpo, maxFechaProceso);
		LogUtil.logTime("Borrando tendencias para operar: maxFechaProceso=" + DateUtil.getDateString(maxFechaProceso)
				+ "; borrados=" + borrados, 1);
		conn.commit();
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

	/*
	 * private double calcularPrecioParaOperar(Extremos extremos,
	 * ProcesoTendenciaBuySell procesoIndex) { double precio = 0.0D; if
	 * (OperationType.BUY.equals(procesoIndex.getTipoOperacion())) {
	 * //extremos.getExtremosFiltrados() precio =
	 * extremos.getExtremosFiltrados().getLowInterval(); } else if
	 * (OperationType.SELL.equals(procesoIndex.getTipoOperacion())) { precio =
	 * extremos.getExtremosFiltrados().getHighInterval(); } return precio; }
	 */

	private TendenciaParaOperarMaxMin crearTendenciaParaOperarMaxMin(Extremos extremos,
			ProcesoTendenciaBuySell procesoIndex) {
		double precio = 0.0D, sl = 0.0D, tp = 0.0D;
		double stopApertura = 0.0D, limitApertura = 0.0D;
		if (OperationType.BUY.equals(procesoIndex.getTipoOperacion())) {
			precio = extremos.getExtremosFiltrados().getLowInterval();
			stopApertura = extremos.getExtremosFiltrados().getHighInterval();
			limitApertura = extremos.getExtremosSinFiltrar().getLowInterval();
			// slPips = extremos.getExtremosFiltrados().getHighInterval() -
			// this.precioPonderado;
			sl = precio - Math.max(adicionalTPO.getDiferenciaPrecioSuperior(), (200.0D / 100000.0D));
			tp = extremos.getExtremosIntermedios().getHighInterval();
			// tp = extremos.getHighInterval();
		} else if (OperationType.SELL.equals(procesoIndex.getTipoOperacion())) {
			precio = extremos.getExtremosFiltrados().getHighInterval();
			stopApertura = extremos.getExtremosFiltrados().getLowInterval();
			limitApertura = extremos.getExtremosSinFiltrar().getHighInterval();
			// slPips = -(this.precioPonderado -
			// extremos.getExtremosFiltrados().getLowInterval());
			sl = precio + Math.max(adicionalTPO.getDiferenciaPrecioInferior(), (200.0D / 100000.0D));
			tp = extremos.getExtremosIntermedios().getLowInterval();
			// tp = extremos.getLowInterval();
		}

		TendenciaParaOperarMaxMin tendencia = crearTendenciaParaOperar(procesoIndex, "MEJOR_TENDENCIA",
				procesoIndex.getTipoOperacion(), precio, tp, sl, stopApertura, limitApertura,
				procesoIndex.getPeriodo());
		return tendencia;
	}

	private TendenciaParaOperarMaxMin[] crearTendenciaParaOperarMaxMinExtremo(Extremos extremos,
			ProcesoTendenciaBuySell procesoIndex) {
		if ((extremos.getMaximaRegresionFiltradaBuy() == null) && (extremos.getMaximaRegresionFiltradaSell() == null)) {
			return null;
		}
		TendenciaParaOperarMaxMin tendenciaBuy = new TendenciaParaOperarMaxMin();
		TendenciaParaOperarMaxMin tendenciaSell = new TendenciaParaOperarMaxMin();
		double precioBuy = 0.0D, precioSell = 0.0D;
		double slPipsBuy = 0.0D, slPipsSell = 0.0D;
		double tpBuy = 0.0D, tpSell = 0.0D;
		double stopAperturaBuy = 0.0D, stopAperturaSell = 0.0D;
		double limitAperturaBuy = 0.0D, limitAperturaSell = 0.0D;
		precioBuy = extremos.getExtremosExtremo().getLowInterval();
		stopAperturaBuy = extremos.getExtremosFiltrados().getHighInterval();
		limitAperturaBuy = extremos.getExtremosExtremo().getLowInterval();
		tpBuy = extremos.getExtremosSinFiltrar().getLowInterval();

		precioSell = extremos.getExtremosExtremo().getHighInterval();
		stopAperturaSell = extremos.getExtremosFiltrados().getLowInterval();
		limitAperturaSell = extremos.getExtremosExtremo().getHighInterval();
		slPipsSell = this.precioPonderado - extremos.getExtremosFiltrados().getLowInterval();
		tpSell = extremos.getExtremosSinFiltrar().getHighInterval();

		tendenciaBuy = crearTendenciaParaOperar(procesoIndex, "EXTREMO_EXTREMO", OperationType.BUY, precioBuy, tpBuy,
				0.0D, slPipsBuy, stopAperturaBuy, limitAperturaBuy, "EXTEXT", extremos.getMaximaRegresionFiltradaBuy());
		tendenciaSell = crearTendenciaParaOperar(procesoIndex, "EXTREMO_EXTREMO", OperationType.SELL, precioSell,
				tpSell, 0.0D, slPipsSell, stopAperturaSell, limitAperturaSell, "EXTEXT",
				extremos.getMaximaRegresionFiltradaSell());
		tendenciaBuy.setActiva(0);
		tendenciaSell.setActiva(0);
		return new TendenciaParaOperarMaxMin[] { tendenciaBuy, tendenciaSell };
	}

	private TendenciaParaOperarMaxMin[] crearTendenciaParaOperarMaxMinSinFiltrar(Extremos extremos,
			ProcesoTendenciaBuySell procesoIndex) {
		if ((extremos.getMaximaRegresionFiltradaBuy() == null) && (extremos.getMaximaRegresionFiltradaSell() == null)) {
			return null;
		}
		TendenciaParaOperarMaxMin tendenciaBuy = new TendenciaParaOperarMaxMin();
		TendenciaParaOperarMaxMin tendenciaSell = new TendenciaParaOperarMaxMin();
		double precioBuy = 0.0D, precioSell = 0.0D;
		double slBuy = 0.0D, slSell = 0.0D;
		double tpBuy = 0.0D, tpSell = 0.0D;
		double stopAperturaBuy = 0.0D, stopAperturaSell = 0.0D;
		double limitAperturaBuy = 0.0D, limitAperturaSell = 0.0D;
		precioBuy = extremos.getExtremosSinFiltrar().getLowInterval();
		stopAperturaBuy = extremos.getExtremosFiltrados().getHighInterval();
		limitAperturaBuy = extremos.getExtremosExtremo().getLowInterval();
		slBuy = extremos.getExtremosFiltrados().getHighInterval() - this.precioPonderado;
		tpBuy = extremos.getExtremosFiltrados().getLowInterval();

		precioSell = extremos.getExtremosSinFiltrar().getHighInterval();
		stopAperturaSell = extremos.getExtremosFiltrados().getLowInterval();
		limitAperturaSell = extremos.getExtremosExtremo().getHighInterval();
		slSell = this.precioPonderado - extremos.getExtremosFiltrados().getLowInterval();
		tpSell = extremos.getExtremosFiltrados().getHighInterval();

		tendenciaBuy = crearTendenciaParaOperar(procesoIndex, "EXTREMO_SIN_FILTRAR", OperationType.BUY, precioBuy,
				tpBuy, 0.0D, slBuy, stopAperturaBuy, limitAperturaBuy, "EXTSINF", extremos.getMaximaRegresionFiltradaBuy());
		tendenciaSell = crearTendenciaParaOperar(procesoIndex, "EXTREMO_SIN_FILTRAR", OperationType.SELL, precioSell,
				tpSell, 0.0D, slSell, stopAperturaSell, limitAperturaSell, "EXTSINF",
				extremos.getMaximaRegresionFiltradaSell());
		tendenciaBuy.setActiva(0);
		tendenciaSell.setActiva(0);
		return new TendenciaParaOperarMaxMin[] { tendenciaBuy, tendenciaSell };
	}

	private TendenciaParaOperarMaxMin crearTendenciaParaOperar(ProcesoTendenciaBuySell procesoIndex,
			String tipoTendencia, OperationType tipoOperacion, double precio, double tp, double sl, double stopApertura,
			double limitApertura, String periodo) {
		return this.crearTendenciaParaOperar(procesoIndex, tipoTendencia, tipoOperacion, precio, tp, sl, 0.0D, stopApertura,
				limitApertura, periodo, null);
	}

	private TendenciaParaOperarMaxMin crearTendenciaParaOperar(ProcesoTendenciaBuySell procesoIndex,
			String tipoTendencia, OperationType tipoOperacion, double precio, double tp, double sl, double slPips,
			double stopApertura, double limitApertura, String periodo, Regresion regresion) {
		TendenciaParaOperarMaxMin tpo = new TendenciaParaOperarMaxMin();
		Regresion regresionIndex = regresion;
		if (regresion == null) {
			regresionIndex = procesoIndex.getRegresion();
		}
		tpo.setRegresion(regresionIndex);
		tpo.setTipoTendencia(tipoTendencia);
		tpo.setRegresionFiltrada(procesoIndex.getRegresionFiltrada());
		tpo.setTipoOperacion(tipoOperacion);
		tpo.setVigenciaLower(regresionIndex.getMinFechaTendencia());
		tpo.setVigenciaHigher(regresionIndex.getMaxFechaTendencia());
		tpo.setFechaBase(procesoIndex.getFechaBase());
		tpo.setFechaTendencia(procesoIndex.getFechaBase());
		tpo.setPeriodo(periodo);
		tpo.setPrecioCalculado(precio);
		if (sl != 0.0D) {
			tpo.setSl(sl);
		}
		if (slPips != 0.0D) {
			tpo.setSlXPips(slPips);
		}
		tpo.setStopApertura(stopApertura);
		tpo.setLimitApertura(limitApertura);
		tpo.setTp(tp);
		double lote = this.calcularLote(tpo);
		tpo.setLoteCalculado(lote);
		return tpo;
	}

	protected double calcularLote(TendenciaParaOperarMaxMin tpo) {
		int lotScaleRounding = PropertiesManager.getLotScaleRounding();
		double lote = stepLote;
		double pendiente = this.adicionalTPO.getPendientePromedio();
		if (OperationType.BUY.equals(tpo.getTipoOperacion())) {
			if (pendiente > 0) {
				lote += stepLote;
				lote += Math.abs(pendiente);
			} else {
				lote -= stepLote;
				lote -= Math.abs(pendiente);
			}
			lote += stepLote * (this.adicionalTPO.getNumeroPendientesPositivas()
					- this.adicionalTPO.getNumeroPendientesNegativas()) / 2;
		} else if (OperationType.SELL.equals(tpo.getTipoOperacion())) {
			if (pendiente < 0) {
				lote += stepLote;
				lote += Math.abs(pendiente);
			} else {
				lote -= stepLote;
				lote -= Math.abs(pendiente);
			}
			lote += stepLote * (this.adicionalTPO.getNumeroPendientesNegativas()
					- this.adicionalTPO.getNumeroPendientesPositivas()) / 2;
		}
		lote = Math.max(stepLote, lote);
		lote = Math.min(maxLote, lote);
		lote = NumberUtil.round(lote, lotScaleRounding);
		return lote;
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
		if (datoAdicional != null) {
			boolean exists = tendenciaParaOperarDAO.existsDatoAdicional(datoAdicional);
			if (exists) {
				tendenciaParaOperarDAO.updateDatoAdicionalTPO(datoAdicional);
			} else {
				tendenciaParaOperarDAO.insertDatosAdicionalesTPO(datoAdicional);
			}
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
