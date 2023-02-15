package com.sens.tools.compressconverter.process;

import com.sens.tools.compressconverter.type.SourceType;
import javafx.scene.control.Label;

import java.util.Properties;

import static com.sens.tools.compressconverter.conf.ConfigurationFile.DIRECTORY_SOURCE_PATH;
import static com.sens.tools.compressconverter.conf.ConfigurationFile.FILE_TYPE_SOURCE;

public class CompressionJob {

    private final Label workInProgressLabel;
    private final Properties configProperties;

    public CompressionJob(final Label workInProgressLabel, final Properties configProperties) {
        this.workInProgressLabel = workInProgressLabel;
        this.configProperties = configProperties;
    }

    public void start() {
        workInProgressLabel.setText("Processus démarré");
        new FileListProcess(workInProgressLabel,configProperties).start();
    }
}
