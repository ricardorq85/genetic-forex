/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class FileLineReader {

    public static List<String[]> readFileAsString(String filePath)
            throws java.io.IOException {
        List<String[]> list = new ArrayList<String[]>();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        String lineRead = null;
        while ((lineRead = reader.readLine()) != null) {
            list.add(lineRead.split(","));
        }
        reader.close();
        return list;
    }
}
