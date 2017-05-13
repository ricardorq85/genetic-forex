package forex.genetic.mediator;

import static forex.genetic.util.LogUtil.logTime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.delegate.GeneticDelegateBD;
import forex.genetic.delegate.PoblacionDelegate;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.io.CopyFileVisitor;
import forex.genetic.util.DateUtil;
import forex.genetic.util.FileUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class PointToPointMediator extends GeneticMediator {

	private Connection connection;

	@Override
	public void init() throws ClassNotFoundException, SQLException {
		this.connection = JDBCUtil.getConnection();
	}

	@Override
	public void start() throws SQLException, IOException {
		this.exportarDatosHistoricos();
		this.importarDatosHistoricos();
		this.procesarIndividuos();
		this.procesarTendencias();
		this.exportarIndividuos();
		this.crearOrdenes();
	}

	private void exportarDatosHistoricos() throws SQLException, IOException {
		DatoHistoricoDAO datoHistoricoDAO = new DatoHistoricoDAO(connection);
		Date fechaMaxima = datoHistoricoDAO.getFechaHistoricaMaxima();
		String fileName = "c:\\Users\\USER\\AppData\\Roaming\\MetaQuotes\\Terminal\\Common\\Files\\export\\Export.properties";
		FileUtil.save(fileName,
				"FECHA_INICIO=" + DateUtil.getDateString("yyyy.MM.dd HH:mm", fechaMaxima) + ",FECHA_FIN=");
	}

	private void importarDatosHistoricos() throws IOException {
		List<Path> files = this.copiarArchivosARuta();
		this.ejecutarCarga(files);
	}

	private String getFileId(String fileName) {
		String fileId = null;
		String[] sp = fileName.split("-");
		fileId = sp[1];
		return fileId;
	}

	private void ejecutarCarga(List<Path> files) throws FileNotFoundException, IOException {
		for (Path file : files) {
			this.actualizarProperty(file);
			PoblacionDelegate delegate = new PoblacionDelegate();
			logTime("Init Insert Datos Historicos", 1);
			delegate.cargarDatosHistoricos();
			logTime("End Insert Datos Historicos", 1);
		}
	}

	private void actualizarProperty(Path file) {
		String fileId = getFileId(file.getFileName().toFile().getName());
		PropertiesManager.setFileId(fileId);
	}

	private List<Path> copiarArchivosARuta() throws IOException {
		String sourcePathName = "c:\\Users\\USER\\AppData\\Roaming\\MetaQuotes\\Terminal\\Common\\Files\\export\\exported";
		String processedPathName = "c:\\Users\\USER\\AppData\\Roaming\\MetaQuotes\\Terminal\\Common\\Files\\export\\processed";
		String targetPathName = System.getProperty("user.dir") + "\\files\\";
		Path sourcePath = FileSystems.getDefault().getPath(sourcePathName);
		CopyFileVisitor fileVisitor = new CopyFileVisitor(sourcePathName, targetPathName, processedPathName);
		Files.walkFileTree(sourcePath, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, fileVisitor);
		return fileVisitor.getCopiedFiles();
	}

	private void leerArchivosDeLaRuta() {
		// TODO Auto-generated method stub
	}

	private void procesarIndividuos() throws FileNotFoundException {
		GeneticDelegateBD delegate = new GeneticDelegateBD();
		delegate.process();
	}

	private void procesarTendencias() {

	}

	private void exportarIndividuos() {

	}

	private void crearOrdenes() {

	}

}
