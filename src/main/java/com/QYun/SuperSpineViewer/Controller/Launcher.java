package com.QYun.SuperSpineViewer.Controller;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

import static com.QYun.SuperSpineViewer.Main.Pref;

public class Launcher extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
        Label icon = new Label() {{
            setGraphic(new FontIcon() {{
                setIconSize(18);
                setIconLiteral("fas-draw-polygon");
                setIconColor(Paint.valueOf("WHITE"));
            }});
        }};

        primaryStage.setScene(new Scene(new JFXDecorator(primaryStage, FXMLLoader.load(Launcher.this.getClass().getResource("/UI/Primary.fxml"))) {{
            setCustomMaximize(true);
            setGraphic(icon);
        }}, Screen.getScreens().get(0).getBounds().getWidth() / 2.5, Screen.getScreens().get(0).getBounds().getHeight() / 1.35) {{
            getStylesheets().addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                    JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                    Launcher.class.getResource("/UI/Main.css").toExternalForm());
        }});

        primaryStage.getIcons().add(new Image("UI/SuperSpineViewer.png"));
        primaryStage.setWidth(Pref.getDouble("stageWidth", 1280));
        primaryStage.setHeight(Pref.getDouble("stageHeight", 800));
        primaryStage.setTitle("QYun SoarTeam v1.2.6-暴风修改版1.2");
        primaryStage.show();
        icon.requestFocus();
    }
}
