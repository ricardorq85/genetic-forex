package forex.genetic.tendencia.manager.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.mongodb.MongoTendenciaDAO;
import forex.genetic.dao.mongodb.MongoTendenciaUltimosDatosDAO;
import forex.genetic.entities.Tendencia;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.tendencia.manager.ExportarTendenciaGrupalManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.DataClient;

public class MongoExportarTendenciaGrupalManager extends ExportarTendenciaGrupalManager {

	private MongoTendenciaDAO tendenciaProcesoFiltradaDAO;
	private MongoTendenciaDAO tendenciaProcesoCompletaDAO;
	private List<TendenciaParaOperar> tendenciasSinFiltrar, tendenciasFiltradas;
	
	//private static final int 
	//TRUNC(PARAM.FECHA_PROCESO, 'HH24'))/60)) MAYORQ_EXIGIDA

	public MongoExportarTendenciaGrupalManager(DataClient dc, Date fechaBase) {
		super(dc);
		if (DateUtil.cumpleFechaParaTendenciaUltimosDatos(fechaBase)) {
			// TODO rrojasq Hacer filtrada
			this.tendenciaProcesoFiltradaDAO = new MongoTendenciaUltimosDatosDAO();
			this.tendenciaProcesoCompletaDAO = new MongoTendenciaDAO();
		} else {
			// TODO rrojasq Hacer filtrada
			this.tendenciaProcesoFiltradaDAO = new MongoTendenciaUltimosDatosDAO();
			this.tendenciaProcesoCompletaDAO = new MongoTendenciaDAO();
		}
	}

	@Override
	public void procesar() throws GeneticBusinessException {
		this.procesarRegresionParaCalculoJava();
	}

	private void consultarTendenciasSinFiltrarIntern() {
		List<Tendencia> tendencias = this.tendenciaProcesoCompletaDAO.consultar(procesoTendencia);
		this.tendenciasSinFiltrar = tendenciasToTendenciasParaOperar(tendencias);
	}

	private void consultarTendenciasFiltradasIntern() {
		List<Tendencia> tendencias = this.tendenciaProcesoFiltradaDAO.consultar(procesoTendencia);
		this.tendenciasFiltradas = tendenciasToTendenciasParaOperar(tendencias);
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendencias() throws GeneticBusinessException {
		return consultarTendenciasSinFiltrar();
	}

	private List<TendenciaParaOperar> tendenciasToTendenciasParaOperar(List<Tendencia> tendencias) {
		List<TendenciaParaOperar> list = new ArrayList<TendenciaParaOperar>();
		tendencias.stream().forEach((tendencia) -> {
			TendenciaParaOperar tpo = new TendenciaParaOperar();
			tpo.setFechaBase(tendencia.getFechaBase());
			tpo.setFechaTendencia(tendencia.getFechaTendencia());
			tpo.setPrecioCalculado(tendencia.getPrecioCalculado());
			list.add(tpo);
		});
		return list;
	}

	@Override
	protected void procesarRegresion() throws GeneticBusinessException {
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendenciasSinFiltrar() throws GeneticBusinessException {
		return tendenciasSinFiltrar;
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendenciasFiltradas() throws GeneticBusinessException {
		return tendenciasFiltradas;
	}

}
