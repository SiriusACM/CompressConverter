package com.sens.tools.compressconverter;

import com.sens.tools.compressconverter.process.CompressionJob;
import com.sens.tools.compressconverter.type.CompressionType;
import com.sens.tools.compressconverter.type.SourceType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static com.sens.tools.compressconverter.conf.ConfigurationFile.*;

public class CompressConverterApplication extends Application {

    public static boolean RUNNING = false;
    private Button directoryButton;
    private Label selectedDirectoryLabel, workInProgressLabel;
    private Stage stage;
    private Properties configProperties;
    private VBox compressionBox, sourceBox;
    private HBox buttonBox;

    @Override
    public void init() {
        initConfiguration();
        directoryButton = initRootDirectory();
        sourceBox = initSourceBox();
        compressionBox = initCompressionSelector();
        buttonBox = initButtonBox();
        workInProgressLabel = new Label("Pas de traitements en cours");
    }

    private HBox initButtonBox() {
        final Button startButton = new Button("Démarrer");
        final Button stopButton = new Button("Arrêter");
        final Button closeButton = new Button("Fermer");

        startButton.setOnAction(e -> {
            RUNNING = true;
            System.out.println("Processus démarré");
            workInProgressLabel.setText("Processus démarré");
            saveConfigProperties();
            new CompressionJob(workInProgressLabel, (Properties) configProperties.clone()).start();
            stopButton.setDisable(false);
            startButton.setDisable(true);
        });

        stopButton.setOnAction(e -> {
            RUNNING = false;
            // Action à effectuer pour arrêter le processus
            System.out.println("Processus en cours d'arret");
            workInProgressLabel.setText("Processus en cours d'arret");
            stopButton.setDisable(true);
            startButton.setDisable(false);
        });
        stopButton.setDisable(true);

        closeButton.setOnAction(e -> {
            workInProgressLabel.setText("Fermeture en cours");
            saveConfigProperties();
            getStage().close();
        });

        final HBox hbBox = new HBox(10, startButton, stopButton, closeButton);
        hbBox.setAlignment(Pos.CENTER);
        return hbBox;
    }

    private Button initRootDirectory() {
        final String directoryStart = configProperties.getProperty(DIRECTORY_SOURCE_PATH, DIRECTORY_SOURCE_DEFAULT);
        selectedDirectoryLabel = new Label("Répertoire de départ: " + directoryStart);
        selectedDirectoryLabel.setWrapText(true);
        selectedDirectoryLabel.setMaxWidth(300);
        selectedDirectoryLabel.setTextAlignment(TextAlignment.LEFT);
        final Button button = new Button("Sélectionner un répertoire");
        button.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choisir un répertoire");
            directoryChooser.setInitialDirectory(new File(directoryStart));
            File selectedDirectory = directoryChooser.showDialog(getStage());
            if (selectedDirectory != null) {
                configProperties.setProperty(DIRECTORY_SOURCE_PATH, selectedDirectory.getAbsolutePath());
                selectedDirectoryLabel.setText("Répertoire sélectionné: " + selectedDirectory.getPath());
            }
        });
        return button;
    }

    private VBox initSourceBox() {
        final SourceType sourceType = SourceType.valueOf(configProperties.getProperty(FILE_TYPE_SOURCE, String.valueOf(SourceType.ZIPFILE)));
        final ToggleGroup compressionToggleGroup = new ToggleGroup();
        final VBox compBox = new VBox(10, new Label("Options de compression"));
        compBox.setPadding(new Insets(10));
        compBox.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-padding: 10;");

        for (final SourceType type : SourceType.values()) {
            final RadioButton radioButton = new RadioButton(type.getLabel());
            radioButton.setToggleGroup(compressionToggleGroup);
            radioButton.setOnAction(e -> {
                configProperties.setProperty(FILE_TYPE_SOURCE, type.name());
            });
            radioButton.setSelected(type.equals(sourceType));
            compBox.getChildren().add(radioButton);
        }

        return compBox;
    }

    private VBox initCompressionSelector() {

        final CompressionType compressionType = CompressionType.valueOf(configProperties.getProperty(COMPRESSION_TYPE_SELECTED, String.valueOf(CompressionType.ZIPMIN)));
        final ToggleGroup compressionToggleGroup = new ToggleGroup();
        final VBox compBox = new VBox(10, new Label("Options de compression"));
        compBox.setPadding(new Insets(10));
        compBox.setStyle("-fx-border-color: black; -fx-border-radius: 10; -fx-padding: 10;");

        for (final CompressionType type : CompressionType.values()) {
            final RadioButton radioButton = new RadioButton(type.getLabel());
            radioButton.setToggleGroup(compressionToggleGroup);
            radioButton.setOnAction(e -> {
                configProperties.setProperty(COMPRESSION_TYPE_SELECTED, type.name());
                //saveConfigProperties();
            });
            radioButton.setSelected(type.equals(compressionType));
            compBox.getChildren().add(radioButton);
        }

        return compBox;
    }

    private void initConfiguration() {
        configProperties = new Properties();
        final File configFile = new File(CONFIG_FILE);
        try {
            if (configFile.exists()) {
                configProperties.load(new FileInputStream(configFile));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.getChildren().add(directoryButton);
        root.getChildren().add(selectedDirectoryLabel);
        root.getChildren().add(sourceBox);
        root.getChildren().add(compressionBox);
        root.getChildren().add(workInProgressLabel);
        root.getChildren().add(buttonBox);
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        saveConfigProperties();
        super.stop();
    }

    public Stage getStage() {
        if (stage == null) {
            stage = (Stage) directoryButton.getScene().getWindow();
        }
        return stage;
    }

    private void saveConfigProperties() {
        final File configFile = new File(CONFIG_FILE);
        try {
            configProperties.store(new FileOutputStream(configFile), null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
