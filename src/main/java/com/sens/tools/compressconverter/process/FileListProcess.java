package com.sens.tools.compressconverter.process;

import com.sens.tools.compressconverter.type.RecursiveType;
import com.sens.tools.compressconverter.type.SourceType;
import javafx.scene.control.Label;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.sens.tools.compressconverter.conf.ConfigurationFile.*;

public class FileListProcess {

    private final Label workInProgressLabel;
    private final List<File> listFiles = new ArrayList<>();
    private final SourceType sourceType;
    private final RecursiveType recursiveType;
    private final Properties configProperties;

    public FileListProcess(final Label workInProgressLabel, final Properties configProperties ) {
        this.workInProgressLabel = workInProgressLabel;
        this.configProperties = configProperties;
        this.sourceType = SourceType.valueOf(configProperties.getProperty(FILE_TYPE_SOURCE));
        this.recursiveType = RecursiveType.valueOf(configProperties.getProperty(RECURSIVE_TYPE, RecursiveType.NON_RECURSIVE.name()));
    }

    public void start() {
        final File directory = new File(configProperties.getProperty(DIRECTORY_SOURCE_PATH));
        directoryCrowler(directory);
    }

    private void directoryCrowler(final File directory) {
        if (directory.exists() && directory.isDirectory()) {
            final File[] files = directory.listFiles();
            if (files != null) {
                listFiles.addAll(Arrays.asList(files).stream().filter(file -> isValid(file)).toList());
            }
        }
    }

    private boolean isValid(final File file) {
        if (file.isDirectory()) {
            if (recursiveType.equals(RecursiveType.RECURSIVE)) {
                directoryCrowler(file);
            }
            return false;
        }
        return false;
    }
}
