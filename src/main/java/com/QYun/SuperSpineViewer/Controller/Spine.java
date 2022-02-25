package com.QYun.SuperSpineViewer.Controller;

import com.QYun.Spine.Universal;
import com.QYun.SuperSpineViewer.Loader;
import com.QYun.SuperSpineViewer.Main;
import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static javafx.animation.Interpolator.EASE_BOTH;

public class Spine extends Main implements Initializable {
    @FXML
    private BorderPane Viewer;

    @FXML
    private StackPane spinePane;

    @FXML
    private StackPane loadPane;

    @FXML
    private JFXSpinner purple;

    @FXML
    private JFXSpinner blue;

    @FXML
    private JFXSpinner cyan;

    @FXML
    private JFXSpinner green;

    @FXML
    private JFXSpinner yellow;

    @FXML
    private JFXSpinner orange;

    @FXML
    private JFXSpinner red;

    @FXML
    ListView<Text> list1;

    //拖拽文件加载模型
    @FXML
    void dragov(DragEvent event) {
        if (event.getGestureSource() != spinePane
                && event.getDragboard().hasFiles()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }
    @FXML
    void draged(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.getFiles() != null) {
            openPath = String.valueOf(db.getFiles().get(0));
            new Loader().init();
            if (isLoad) System.out.println("请求重载");
            else System.out.println("请求初始化");
        }
        event.consume();
    }
    //拖拽到列表中点击加载模型
    JFXComboBox<String> C_Animate = new JFXComboBox<>() {{
        setItems(spine.getAnimatesList());
        setOnAction(event -> spine.setAnimate(getValue()));
    }};
    ObservableList<File> list;
    @FXML
    void dragovL(DragEvent event) {
        if (event.getGestureSource() != spinePane
                && event.getDragboard().hasFiles()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }
    @FXML
    void dragedL(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            List<File> files = db.getFiles();
            List<Text> textList = files.stream().map(File::getAbsolutePath)
                    .map(Text::new)
                    .peek(text -> text.setOnMouseClicked(clickEvent -> {
                        Object source = clickEvent.getSource();
                        Text clickSource = null;
                        if (source instanceof Text) {
                            clickSource = (Text) source;
                            System.out.println(clickSource.getText());
                        }
                        openPath = clickSource.getText();
                        new Loader().init();
                        if (isLoad) System.out.println("请求重载");
                        else System.out.println("请求初始化");


                        //默认播放Idle动画，这里需要改成记录上一次选择的动画名称，因为不是所有模型都有同样的动画名
                        C_Animate.setValue("Idle");
                        spine.setIsPlay(false);
                        try {
                            Thread.sleep(250);
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        spine.setIsPlay(true);
                    }))
                    .collect(Collectors.toList());
            ObservableList<Text> fileObservableList = FXCollections.observableArrayList(textList);
            list1.setItems(fileObservableList);
            event.consume();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AtomicReference<String> headerColor = new AtomicReference<>(getColor((short) ((Math.random() * 12) % 22)));

        ImageView spineLogo = new ImageView() {{
            setImage(new Image("/UI/SpineLogo.png", 138, 0, true, true, false));
        }};

        Label project = new Label("等待加载...") {{
            setStyle("-fx-text-fill: #f1f1f2;");
            getStyleClass().add("normal-label");
        }};

        spine.projectNameProperty().addListener(
                (observable, oldValue, newValue) -> Platform.runLater(() -> project.setText(newValue)));

        StackPane header = new StackPane() {{
            setStyle("-fx-background-radius: 0 5 0 0; -fx-min-height: 138; -fx-background-color: " + headerColor);
            getChildren().addAll(spineLogo, project);
            setAlignment(spineLogo, Pos.CENTER);
            setAlignment(Pos.BOTTOM_LEFT);
        }};


        JFXSlider T_Scale = new JFXSlider() {{
            setSnapToTicks(true);
            setShowTickLabels(true);
            setMin(0.25);
            setMax(2.0);
            setMajorTickUnit(0.25);
            setBlockIncrement(0.1);
            setValue(1.0);

            setValueFactory(slider ->
                    Bindings.createStringBinding(() -> ((int) (getValue() * 10) / 10f) + "x", slider.valueProperty())
            );
            valueProperty().addListener(
                    (observable, oldValue, newValue) ->{
//                        T_Scale1.setText(String.valueOf((getValue())));
                        spine.setScale((float) getValue());
                    });
        }};
        JFXTextField T_Scale1 = new JFXTextField()
        {{
            setText(String.valueOf(T_Scale.getValue()));

            setOnKeyReleased(keyEvent -> {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                    if (getText().matches("^[1-9]\\d*$|^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$"))
                        if(getText()!="0"){
                    T_Scale.setValue(Float.parseFloat(getText()));
                    spine.setScale(Float.parseFloat(getText()));
                }});
            setOnScroll(ScrollEvent -> {
                double Scale_temp=Double.parseDouble(getText())-(ScrollEvent.getTextDeltaY()/30);
                setText(String.valueOf(String.format("%.2f",Scale_temp)));
                T_Scale.setValue(Double.parseDouble(String.format("%.2f",Scale_temp)));
                spine.setScale(Float.parseFloat(String.valueOf(T_Scale.getValue())));
            });

        }};


        JFXTextField T_Width = new JFXTextField() {{
            setEditable(false);
        }};

        JFXTextField T_Height = new JFXTextField() {{
            setEditable(false);
        }};

        JFXSlider T_X = new JFXSlider() {{
            setMin(-200);
            setMax(500);
            setMajorTickUnit(100);
            setBlockIncrement(10);
            setValue(0);
            setShowTickLabels(true);

            setValueFactory(slider ->
                    Bindings.createStringBinding(() -> ((int) (getValue()))+" ", slider.valueProperty())
            );
            valueProperty().addListener(
                    (observable, oldValue, newValue) -> spine.setX((float) getValue()));

        }};

        JFXTextField T_X1 = new JFXTextField() {{
//            setPromptText("0");
            setText(String.valueOf(T_X.getValue()));
//            setTextFormatter(new TextFormatter<String>(change -> {
//                if (change.getText().matches("[0-9]*|\\.|-"))
//                    return change;
//                return null;
//            }));

            setOnKeyReleased(keyEvent -> {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                    if (getText().matches("-?[0-9]\\d*|-?([1-9]\\d*.\\d*|0\\.\\d*[1-9]\\d*)"))
                        T_X.setValue(Float.parseFloat(getText()));
                        spine.setX(Float.parseFloat(getText()));
            });
            setOnScroll(ScrollEvent -> {
                float T_X_temp=Math.round(Float.parseFloat(getText())-ScrollEvent.getTextDeltaY());
                T_X.setValue(T_X_temp);
                setText(String.valueOf(T_X_temp));
                spine.setX(T_X_temp);
            });
        }};

        JFXSlider T_Y = new JFXSlider() {{
            setMin(-200);
            setMax(500);
            setMajorTickUnit(100);
            setBlockIncrement(1);
            setValue(0);
            setShowTickLabels(true);

            setValueFactory(slider ->
                    Bindings.createStringBinding(() -> ((int) (getValue()))+" ", slider.valueProperty())
            );
            valueProperty().addListener(
                    (observable, oldValue, newValue) -> spine.setY((float) getValue()));
        }};

        JFXTextField T_Y1 = new JFXTextField() {{
//            setPromptText("0.0");
              setText(String.valueOf(T_Y.getValue()));
//            setTextFormatter(new TextFormatter<String>(change -> {
//                if (change.getText().matches("[0-9]*|\\.|-"))
//                    return change;
//                return null;
//            }));

            setOnKeyReleased(keyEvent -> {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                    if (getText().matches("-?[0-9]\\d*|-?([1-9]\\d*.\\d*|0\\.\\d*[1-9]\\d*)"))
                        T_Y.setValue(Float.parseFloat(getText()));
                        spine.setY(Float.parseFloat(getText()));
            });
            setOnScroll(ScrollEvent -> {
                float T_Y_temp=Math.round(Float.parseFloat(getText())-ScrollEvent.getTextDeltaY());
                T_Y.setValue(T_Y_temp);
                setText(String.valueOf(T_Y_temp));
                spine.setY(T_Y_temp);
            });
        }};

        JFXSlider S_Speed = new JFXSlider() {{
//            setSnapToTicks(true);
//            setShowTickLabels(true);
            setMin(0.25);
            setMax(2.0);
            setMajorTickUnit(0.25);
            setBlockIncrement(0.25);
            setValue(1);
            setShowTickLabels(true);

            setValueFactory(slider ->
                    Bindings.createStringBinding(() -> ((int) (getValue() * 100) / 100f) + "x", slider.valueProperty())
            );
            valueProperty().addListener(
                    (observable, oldValue, newValue) -> spine.setSpeed((Float.parseFloat(String.valueOf(newValue)))));
        }};

        JFXComboBox<String> C_Skins = new JFXComboBox<>() {{
            setItems(spine.getSkinsList());
            setOnAction(event -> spine.setSkin(getValue()));
        }};

//        JFXComboBox<String> C_Animate = new JFXComboBox<>() {{
//            setItems(spine.getAnimatesList());
//            setOnAction(event -> spine.setAnimate(getValue()));
//        }};

        FontIcon playIcon = new FontIcon() {{
            setIconSize(20);
            setIconLiteral("fas-play");
            setIconColor(Paint.valueOf("WHITE"));
        }};

        FontIcon pauseIcon = new FontIcon() {{
            setIconSize(20);
            setIconLiteral("fas-pause");
            setIconColor(Paint.valueOf("WHITE"));
        }};

        JFXButton playButton = new JFXButton("") {{
            setStyle("-fx-background-radius: 40;-fx-background-color: " + getColor((short) ((Math.random() * 20) % 22)));
            setRipplerFill(Color.valueOf(headerColor.get()));
            setButtonType(ButtonType.RAISED);
            setPrefSize(56, 56);
            setScaleX(0);
            setScaleY(0);
            setGraphic(playIcon);

            translateYProperty().bind(Bindings.createDoubleBinding(() ->
                    header.getBoundsInParent().getHeight() - getHeight() / 2, header.boundsInParentProperty(), heightProperty()));

            setOnAction(event -> {
                if (isLoad) {
                    if (spine.isIsPlay()) {
                        spine.setIsPlay(false);
                        setGraphic(playIcon);
                    } else {
                        spine.setIsPlay(true);
                        setGraphic(pauseIcon);
                        headerColor.set(getColor((short) ((Math.random() * 12) % 22)));
                        header.setStyle("-fx-background-radius: 0 5 0 0; -fx-min-height: 138; -fx-background-color: " + headerColor);
                        setStyle("-fx-background-radius: 40;-fx-background-color: " + getColor((short) ((Math.random() * 20) % 22)));
                        setRipplerFill(Color.valueOf(headerColor.get()));
                    }
                }
            });
        }};

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(240),
                new KeyValue(playButton.scaleXProperty(),
                        1,
                        EASE_BOTH),
                new KeyValue(playButton.scaleYProperty(),
                        1,
                        EASE_BOTH)));
        animation.setDelay(Duration.millis((2000 * Math.random()) + 1000));
        animation.play();
        JFXDepthManager.setDepth(spinePane, 1);

        spinePane.getChildren().addAll(new VBox() {{
            getChildren().addAll(header, new StackPane() {{
                setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: rgb(255,255,255,0.87);");
                setMargin(playButton, new Insets(0, 26, 0, 0));
                setAlignment(playButton, Pos.TOP_RIGHT);

                getChildren().add(new ScrollPane(new VBox(20) {{
                    setPadding(new Insets(14, 16, 20, 16));
                    getChildren().addAll(
                            new HBox(
                            new Label("缩放比例") {{getStyleClass().add("normal-label");}},
                                    new HBox(T_Scale1){{setMaxWidth(100);setStyle("-fx-padding: 0 0 0 18;");}}
                            ),
                            new VBox(T_Scale){{setMaxWidth(300);}},


                            new HBox(
                            new Label("坐标X 　") {{getStyleClass().add("normal-label");}},
                                    new HBox(T_X1){{setMaxWidth(100);setStyle("-fx-padding: 0 0 0 18;");}}
                            ),
                            new VBox(T_X){{setMaxWidth(300);}},


                            new HBox(
                            new Label("坐标Y 　") {{getStyleClass().add("normal-label");}},
                                    new HBox(T_Y1){{setMaxWidth(100);setStyle("-fx-padding: 0 0 0 18;");}}
                            ),
                            new VBox(T_Y){{setMaxWidth(300);}},

                            new HBox(
                            new Label("模型宽度") {{getStyleClass().add("normal-label");}},
                                    new HBox(T_Width){{setMaxWidth(250);setStyle("-fx-padding: 0 0 0 18;");}}
                            ),
                            new HBox(
                            new Label("模型高度") {{getStyleClass().add("normal-label");}},
                                    new HBox(T_Height){{setMaxWidth(250);setStyle("-fx-padding: 0 0 0 18;");}}
                            ),
                            new Label("播放速度") {{getStyleClass().add("normal-label");}}, S_Speed,

                            new FlowPane(
                                    new Label("循环") {{
                                        getStyleClass().add("normal-label");
                                    }},
                                    new JFXToggleButton() {{
                                        setSelected(true);
                                        spine.setIsLoop(isSelected());
                                        setOnAction(event1 -> spine.setIsLoop(isSelected()));
                                    }},
                                    new JFXButton("重新读取模型") {{
                                        setStyle("-fx-text-fill:#5264AE;-fx-font-size:14px;");
                                        setButtonType(ButtonType.FLAT);

                                        setOnAction(event -> {
                                            Universal.Range = -1;
                                            new Loader().init();
                                        });
                                    }},
                                    new JFXButton("重置参数") {{
                                        setStyle("-fx-text-fill:#5264AE;-fx-font-size:14px;");
                                        setButtonType(ButtonType.FLAT);

                                        setOnAction(event -> {
                                            spine.setScale(1);
                                            spine.setX(0);
                                            spine.setY(0);
                                            spine.setIsPlay(false);

//                                            T_Scale.clear();
//                                            T_X.clear();
//                                            T_Y.clear();
                                            T_Scale.setValue(1);
                                            T_X.setValue(0);
                                            T_Y.setValue(0);
                                            C_Skins.setValue(null);
                                            C_Animate.setValue(null);
                                            S_Speed.setValue(1);
                                            System.gc();
                                        });
                                    }}
                            )
                            {{
                                setMaxWidth(300);
                                setStyle("-fx-padding: 0 0 0 18;");
                            }},

                            new VBox(
                                    new HBox(new Label("皮肤") {{getStyleClass().add("normal-label");}},
                                            new HBox(new Label("动画") {{getStyleClass().add("normal-label");}}){{setStyle("-fx-padding: 0 0 0 80;");}}),
                                    new HBox(C_Skins,
                                            new HBox(C_Animate){{setStyle("-fx-padding: 0 0 0 55;");}})
                            )
                    );
                }}) {{
                    setHbarPolicy(ScrollBarPolicy.NEVER);
                }});
            }});
            setVgrow(header, Priority.NEVER);
        }}, playButton);

        spineRender = new ImageView() {{
            setScaleY(-1);
            fitWidthProperty().addListener((observable, oldValue, newValue) -> {
                T_Width.setPromptText(String.valueOf(newValue.intValue()));
                width = newValue.intValue();
                Pref.putDouble("stageWidth", newValue.doubleValue() + 368);
            });

            fitHeightProperty().addListener((observable, oldValue, newValue) -> {
                T_Height.setPromptText(String.valueOf(newValue.intValue()));
                height = newValue.intValue();
                Pref.putDouble("stageHeight", newValue.doubleValue() + 103);
            });
        }};

        spine.isPlayProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                if (newValue) Platform.runLater(() -> playButton.setGraphic(pauseIcon));
                else Platform.runLater(() -> playButton.setGraphic(playIcon));
            }
        });
    }

    public boolean isLoaded() {
        try {
            setProgressAnimate(purple);
            Thread.sleep(100);
            setProgressAnimate(blue);
            Thread.sleep(100);
            setProgressAnimate(cyan);
            Thread.sleep(100);
            setProgressAnimate(green);
            Thread.sleep(100);
            setProgressAnimate(yellow);
            Thread.sleep(100);
            setProgressAnimate(orange);
            Thread.sleep(100);
            setProgressAnimate(red);
            Thread.sleep(1000);
            new Timeline(
                    new KeyFrame(
                            Duration.seconds(1),
                            new KeyValue(loadPane.opacityProperty(), 0)
                    )
            ).play();
            Thread.sleep(800);
            Platform.runLater(() -> {
                loadPane.getChildren().removeAll(purple, blue, cyan, green, yellow, orange, red);
                Viewer.getChildren().remove(loadPane);
                Viewer.setCenter(spineRender);
                spineRender.fitHeightProperty().bind(spineRender.getScene().heightProperty().add(-103));
                spineRender.fitWidthProperty().bind(spineRender.getScene().widthProperty().add(-368));
                Viewer = null;
                loadPane = null;
                purple = null;
                blue = null;
                cyan = null;
                green = null;
                yellow = null;
                orange = null;
                red = null;
            });
            return isLoad = true;
        } catch (InterruptedException ignored) {
            return false;
        }
    }

    private void setProgressAnimate(JFXSpinner spinner) {
        new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        new KeyValue(spinner.progressProperty(), 1)
                )
        ).play();
    }

    private String getColor(short i) {
        return switch (i) {
            case 0 -> "#455A64";
            case 1 -> "#616161";
            case 2 -> "#512DA8";
            case 3 -> "#5D4037";
            case 4 -> "#9C27B0";
            case 5 -> "#7B1FA2";
            case 6 -> "#673AB7";
            case 7 -> "#7C4DFF";
            case 8 -> "#3F51B5";
            case 9 -> "#536DFE";
            case 10 -> "#2196F3";
            case 11 -> "#448AFF";
            case 12 -> "#0288D1";
            case 13 -> "#00BCD4";
            case 14 -> "#009688";
            case 15 -> "#4CAF50";
            case 16 -> "#689F38";
            case 17 -> "#607D8B";
            case 18 -> "#FFC107";
            case 19 -> "#FF9800";
            case 20 -> "#FF5722";
            case 21 -> "#795548";
            case 22 -> "#9E9E9E";
            default -> "#FFFFFF";
        };
    }
}
