/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import forex.genetic.entities.Learning;
import forex.genetic.util.io.PropertiesManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author ricardorq85
 */
public class SerializationLearningManager {

    /**
     *
     * @param file
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Learning readObject(File file)
            throws IOException, ClassNotFoundException {
        Learning l;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file))) {
            l = (Learning) reader.readObject();
        }
        return l;
    }

    /**
     *
     * @return
     */
    public Learning readLearning() {
        Learning learning = new Learning();
        File root = new File(PropertiesManager.getLearningPath());
        FilenameFilter nameFilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                boolean accept = false;
                if ((name.contains(".gfl"))
                        && (name.contains(PropertiesManager.getOperationType().name()))
                        && (name.contains(PropertiesManager.getPair()))) {
                    return true;
                }
                return accept;
            }
        };
        if ((root != null) && (root.exists())) {
            File[] files = root.listFiles(nameFilter);
            for (int i = 0; i < Math.min(1, files.length); i++) {
                try {
                    File file = files[i];
                    learning = this.readObject(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return learning;
    }

    /**
     *
     * @param learning
     * @throws IOException
     */
    public void writeObject(Learning learning)
            throws IOException {
        try (ObjectOutputStream writer = new ObjectOutputStream(
                new FileOutputStream(PropertiesManager.getLearningPath()
                        + PropertiesManager.getOperationType() + PropertiesManager.getPair() + ".gfl"))) {
            writer.writeObject(learning);
        }
    }
}
