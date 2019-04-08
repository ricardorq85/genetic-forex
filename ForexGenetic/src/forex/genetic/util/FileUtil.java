package forex.genetic.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

	private FileUtil(){}
	
	public static void save(String fileName, String text) throws IOException {
		if (text != null) {
			FileOutputStream writer = new FileOutputStream(fileName);
			writer.write(text.getBytes());
			writer.close();
		}
	}
	
	public static boolean createFolder(String name) {
		File dir = new File(name);
		return dir.mkdir();
	}
}
