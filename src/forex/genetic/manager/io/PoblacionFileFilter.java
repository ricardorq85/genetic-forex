/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import forex.genetic.entities.Learning;
import forex.genetic.entities.RelacionPair;
import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class PoblacionFileFilter implements FileFilter {

    private List<File> loadedFiles = null;
    private int processCounter = 0;
    private Learning learning = null;
    private int maxFilePerRead = 0;
    private int currentCountRead = 0;
    private long dateTime = 0L;

    /**
     *
     * @param loadedFiles
     * @param processCounter
     * @param learning
     * @param maxFilePerRead
     * @param dateTime
     */
    public PoblacionFileFilter(List<File> loadedFiles, int processCounter, Learning learning,
            int maxFilePerRead, long dateTime) {
        this.loadedFiles = loadedFiles;
        this.processCounter = processCounter;
        this.learning = learning;
        this.maxFilePerRead = maxFilePerRead;
        this.currentCountRead = 0;
        this.dateTime = dateTime;
    }

    @Override
    public boolean accept(File file) {
        boolean accept = false;
        if (currentCountRead < maxFilePerRead) {
            String name = file.getName();
            if ((name.contains(".gfx")) && (!loadedFiles.contains(file))) {
                List<RelacionPair> relacionPairList = learning.getRelacionMonedas();
                int processIndex = processCounter;
                if (relacionPairList.size() > (processIndex)) {
                    RelacionPair relacionPair = relacionPairList.get(processIndex);
                    if ((file.lastModified() > this.dateTime) && (name.contains(relacionPair.getPair()))
                            && (name.contains(relacionPair.getOperationType().name()))) {
                        accept = true;
                        currentCountRead++;
                    }
                } else {
                    accept = true;
                    currentCountRead++;
                }
            }
        }
        return accept;
    }
}
