package com.QYun.SuperSpineViewer.Controller;

import com.QYun.SuperSpineViewer.Loader;
import com.QYun.SuperSpineViewer.Main;
import com.QYun.SuperSpineViewer.RecordFX;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Exporter extends Main implements Initializable {
    @FXML
    private Label L_Version;
    @FXML
    private Label L_Skel;
    @FXML
    private Label L_Atlas;
    @FXML
    private Label L_FPS;
    @FXML
    private JFXTextField T_Path;
    @FXML
    private JFXProgressBar P_Export;

    @FXML
    void B_Export() {
        if (outPath != null && spine.getAnimate() != null) {
            spine.setIsPlay(false);
            spine.setIsLoop(false);
            spine.setPercent(2);
            spine.setSpeed(quality);
            spine.setIsPlay(true);
            System.out.println("请求：开始录制");
            recordFX.Start(spine.getProjectName() + "_" + spine.getAnimate());
        }
    }

    @FXML
    void B_Open() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Skeleton");
        File last = new File(Pref.get("lastOpen", System.getProperty("user.home")));
        if (!last.canRead())
            last = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(last);

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Skeleton File", "*.json", "*.skel", "*.txt", "*.bytes")
        );

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            openPath = file.getAbsolutePath();
            new Loader().init();
            if (isLoad) System.out.println("请求重载");
            else System.out.println("请求初始化");
            Pref.put("lastOpen", file.getParent());
        }
    }

    @FXML
    void B_Path() {
        Platform.runLater(() -> {
            DirectoryChooser chooser = new DirectoryChooser();
            File last = new File(outPath);
            if (!last.canRead())
                last = new File(System.getProperty("user.home"));
            chooser.setInitialDirectory(last.getParentFile());
            chooser.setTitle("Save Location");
            outPath = chooser.showDialog(new Stage()).getAbsolutePath() + File.separator;
            T_Path.setText(outPath);
            Pref.put("lastSave", outPath);
        });
    }

    @FXML
    void RB_S() {
        quality = 0.5f;
    }

    @FXML
    void RB_E() {
        quality = 0.25f;
    }

    @FXML
    void RB_F() {
        quality = 1f;
    }

    @FXML
    void RB_N() {
        perform = 6;
    }

    @FXML
    void RB_H() {
        perform = 18;
    }

    @FXML
    void RB_L() {
        perform = 3;
    }

    @FXML
    void RB_MOV() {
        sequence = 0;
    }
    @FXML
    void RB_MP4() {
        sequence = 1;
    }

    @FXML
    void RB_Sequence() {
        sequence = 9;
    }

    @FXML
    void RB_Json() {
    }

    @FXML
    void PreA() {preA = !preA;}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FPS = L_FPS;
        Skel = L_Skel;
        Atlas = L_Atlas;
        progressBar = P_Export;
        T_Path.setText(outPath);
        recordFX = new RecordFX();
        spine.spineVersionProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> L_Version.setText("Version : " + newValue)));

        System.out.println("SuperSpineViewer已启动");
        if (openPath != null) {
            Platform.runLater(() -> {
                new Loader().init();
                System.out.println("从命令行加载");
            });
        }
    }
}
