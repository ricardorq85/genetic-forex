/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class FileLineReader {

    /**
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<String[]> readFileAsString(String filePath)
            throws java.io.IOException {
        List<String[]> list = new ArrayList<String[]>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filePath), Charset.defaultCharset()))) {
            String lineRead = null;
            while ((lineRead = reader.readLine()) != null) {
                list.add(lineRead.split(","));
            }
        }
        return list;
    }
}
