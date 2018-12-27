package com.ssau.construction.controller;

import com.ssau.construction.car.Car;
import com.ssau.construction.car.Cargo;
import com.ssau.construction.car.Passenger;
import com.ssau.construction.domain.GasStation;
import com.ssau.construction.domain.template.Template;
import com.ssau.construction.util.*;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class ConstructorController {

    public static final int DISTANCE_BETWEEN = 50;

    private static double petrolAmountTotal = 1000;
    private static double moneyAmount = 0;

    public static double getMoneyAmount() {
        return moneyAmount;
    }

    public static void setMoneyAmount(double moneyAmount) {
        ConstructorController.moneyAmount = moneyAmount;
    }

    public static double getPetrolAmountTotal() {
        return petrolAmountTotal;
    }

    public static void setPetrolAmountTotal(double petrolAmountTotal) {
        ConstructorController.petrolAmountTotal = petrolAmountTotal;
    }

    //Общие параметры
    @FXML
    TabPane tabPane;
    private Stage stage;
    private int size;
    private File initialFile;
    private File initialDirectory = new File("..");
    @FXML
    private Menu fileMenu;

    @FXML
    public void showModelingMenuItems() {
        fileMenu.getItems().get(0).setVisible(false);
        fileMenu.getItems().get(1).setVisible(false);
        fileMenu.getItems().get(2).setVisible(false);
        fileMenu.getItems().get(3).setVisible(true);
    }

    @FXML
    public void showConstructorMenuItems() {
        fileMenu.getItems().get(0).setVisible(true);
        fileMenu.getItems().get(1).setVisible(true);
        fileMenu.getItems().get(2).setVisible(true);
        fileMenu.getItems().get(3).setVisible(false);
    }

    @FXML
    public void onMenuSave() {
        try (FileOutputStream out = new FileOutputStream(initialFile.getPath())) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(constructorGasStation);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Ошибка при сохранении топологии в файл " + initialFile.getPath() + ". Убедитесь, что файл существует и доступен для записи!");
            alert.showAndWait();
        }
    }

    @FXML
    public void checkFileMenu() {
        if (initialFile != null && initialFile.exists() && initialFile.isFile() && initialFile.canWrite())
            fileMenu.getItems().get(0).setDisable(false);
        else fileMenu.getItems().get(0).setDisable(true);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        selectedEffect = new InnerShadow();
        selectedEffect.setColor(Color.web("#d88913a6"));
        selectedEffect.setChoke(0.0);
        selectedEffect.setBlurType(BlurType.GAUSSIAN);
        selectedEffect.setWidth(255.0);
        selectedEffect.setHeight(255.0);

        //Инициализация вкладки конструирования
        template = Template.Null;
        size = 50;
        Canvas canvas = new Canvas(819, 587);
        graphicsContextConstructor = canvas.getGraphicsContext2D();
        constructorGasStation = new GasStation(4, 4, 4, 4, graphicsContextConstructor, size);

        canvas.setOnMouseClicked(event -> {
                    if (template != Template.Null) {
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        if (constructorGasStation.isInGasStation(x, y)) {
                            constructorGasStation.createGasStationFunctionalBlock(x, y, template);
                            constructorGasStation.drawGasStationFunctionalBlock(x, y);
                        }
                        if (constructorGasStation.isInServiceArea(x, y)) {
                            constructorGasStation.createServiceAreaFunctionalBlock(x, y, template);
                            constructorGasStation.drawServiceAreaFunctionalBlock(x, y);
                        }
                    }
                }
        );
        constructor.getChildren().add(canvas);
        constructorGasStation.drawBackground();
        constructorGasStation.drawMarkup();
        constructorGasStation.drawHighway();
        constructorGasStation.drawFunctionalBlocks();
        //Инициализация вкладки моделирования
        canvas = new Canvas(900, 580);
        graphicsContextModeling = canvas.getGraphicsContext2D();
        modeling.getChildren().add(canvas);
    }

    //Параметры для constructor
    private Template template;
    private GasStation constructorGasStation;
    private InnerShadow selectedEffect;
    @FXML
    private Pane constructor;
    private GraphicsContext graphicsContextConstructor;

    @FXML
    private ImageView imageViewRoad;
    @FXML
    private ImageView imageViewGasolinePump;
    @FXML
    private ImageView imageViewGasolineTank;
    @FXML
    private ImageView imageViewEntry;
    @FXML
    private ImageView imageViewDeparture;
    @FXML
    private ImageView imageViewCashBox;
    @FXML
    private ImageView imageViewInfoTable;
    @FXML
    private ImageView imageViewLawn;

    private void clearConstructorContext() {
        graphicsContextConstructor.clearRect(0, 0, graphicsContextConstructor.getCanvas().getWidth(), graphicsContextConstructor.getCanvas().getHeight());
    }

    //    Параметры для modeling
    private GasStation modelingGasStation;
    private double probability = 1;
    private double passengerPercent = 0.8;
    private IntervalGetter intervalGetter = new DeterminateIntervalGetter(1);
    private IntervalGetter reverseIntervalGetter = new DeterminateIntervalGetter(1);
    private IntervalGetter intervalGetterParking = new DeterminateIntervalGetter(1);

    @FXML
    private Pane modeling;
    @FXML
    private ImageView load_button;
    @FXML
    private ImageView run_button;
    @FXML
    private ImageView pause_button;
    @FXML
    private ImageView stop_button;
    @FXML
    private ImageView go_button;
    @FXML
    private ImageView table_button;
    @FXML
    private Canvas modelTimeCanvas;


    private GraphicsContext graphicsContextModeling;
//    private StatisticController statisticController;
//    private ModelingCarSettingsController modelingCarSettingsController;
//    private ModelingParkingSettingsController modelingParkingSettingsController;

    private void clearModelingContext() {
        graphicsContextModeling.clearRect(0, 0, graphicsContextModeling.getCanvas().getWidth(), graphicsContextModeling.getCanvas().getHeight());
    }

    private ModelingTimer modelingTimer;
    private boolean isStartedModeling = false;

    //Обработчики для вкладки конструирования
    @FXML
    public void onChooseCashBox() {
        template = Template.CashBox;
        imageViewCashBox.setEffect(selectedEffect);
        imageViewRoad.setEffect(null);
        imageViewEntry.setEffect(null);
        imageViewDeparture.setEffect(null);
        imageViewInfoTable.setEffect(null);
        imageViewGasolinePump.setEffect(null);
        imageViewGasolineTank.setEffect(null);
        imageViewLawn.setEffect(null);
    }

    @FXML
    public void onChooseInfoTable() {
        template = Template.InfoTable;
        imageViewCashBox.setEffect(null);
        imageViewRoad.setEffect(null);
        imageViewEntry.setEffect(null);
        imageViewDeparture.setEffect(null);
        imageViewInfoTable.setEffect(selectedEffect);
        imageViewGasolinePump.setEffect(null);
        imageViewGasolineTank.setEffect(null);
        imageViewLawn.setEffect(null);
    }

    @FXML
    public void onChooseDeparture() {
        template = Template.Departure;
        imageViewCashBox.setEffect(null);
        imageViewRoad.setEffect(null);
        imageViewEntry.setEffect(null);
        imageViewDeparture.setEffect(selectedEffect);
        imageViewInfoTable.setEffect(null);
        imageViewGasolinePump.setEffect(null);
        imageViewGasolineTank.setEffect(null);
        imageViewLawn.setEffect(null);
    }

    @FXML
    public void onChooseEntry() {
        template = Template.Entry;
        imageViewCashBox.setEffect(null);
        imageViewRoad.setEffect(null);
        imageViewEntry.setEffect(selectedEffect);
        imageViewDeparture.setEffect(null);
        imageViewInfoTable.setEffect(null);
        imageViewGasolinePump.setEffect(null);
        imageViewGasolineTank.setEffect(null);
        imageViewLawn.setEffect(null);
    }

    @FXML
    public void onChooseGasolinePump() {
        template = Template.GasolinePump;
        imageViewCashBox.setEffect(null);
        imageViewRoad.setEffect(null);
        imageViewEntry.setEffect(null);
        imageViewDeparture.setEffect(null);
        imageViewInfoTable.setEffect(null);
        imageViewGasolinePump.setEffect(selectedEffect);
        imageViewGasolineTank.setEffect(null);
        imageViewLawn.setEffect(null);
    }

    @FXML
    public void onChooseGasolineTank() {
        template = Template.GasolineTank;
        imageViewCashBox.setEffect(null);
        imageViewRoad.setEffect(null);
        imageViewEntry.setEffect(null);
        imageViewDeparture.setEffect(null);
        imageViewInfoTable.setEffect(null);
        imageViewGasolinePump.setEffect(null);
        imageViewGasolineTank.setEffect(selectedEffect);
        imageViewLawn.setEffect(null);
    }

    @FXML
    public void onChooseLawn() {
        template = Template.Lawn;
        imageViewCashBox.setEffect(null);
        imageViewRoad.setEffect(null);
        imageViewEntry.setEffect(null);
        imageViewDeparture.setEffect(null);
        imageViewInfoTable.setEffect(null);
        imageViewGasolinePump.setEffect(null);
        imageViewGasolineTank.setEffect(null);
        imageViewLawn.setEffect(selectedEffect);
    }

    @FXML
    public void onChooseRoad() {
        template = Template.Road;
        imageViewCashBox.setEffect(null);
        imageViewRoad.setEffect(selectedEffect);
        imageViewEntry.setEffect(null);
        imageViewDeparture.setEffect(null);
        imageViewInfoTable.setEffect(null);
        imageViewGasolinePump.setEffect(null);
        imageViewGasolineTank.setEffect(null);
        imageViewLawn.setEffect(null);
    }

    @FXML
    public void onAuthorsClick() {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/forms/about_us.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Об авторах");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            // Передаём адресата в контроллер.
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setResizable(false);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onProjectClick() {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/forms/about_project.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("О системе");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            // Передаём адресата в контроллер.
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setResizable(false);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onConstructorSettingsClick() {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/forms/constructor_settings.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Размер заправки и сервисной зоны");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            // Передаём адресата в контроллер.
            ConstructorSettingsController controller = loader.getController();
            controller.setInitialParkingWidth(constructorGasStation.getFunctionalBlockH());
            controller.setInitialParkingHeight(constructorGasStation.getFunctionalBlockV());
            controller.setInitialServiceHeight(constructorGasStation.getServiceBlockV());
            controller.setInitialServiceWidth(constructorGasStation.getServiceBlockH());
            controller.setDialogStage(dialogStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setResizable(false);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            if (controller.isSubmitClicked()) {
                clearConstructorContext();
                constructorGasStation = new GasStation(
                        controller.getGasStationWidth(),
                        controller.getGasStationHeight(),
                        controller.getServiceWidth(),
                        controller.getServiceHeight(),
                        graphicsContextConstructor,
                        size,
                        constructorGasStation);
                constructorGasStation.drawBackground();
                constructorGasStation.drawMarkup();
                constructorGasStation.drawHighway();
                constructorGasStation.drawFunctionalBlocks();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onConstructorLoad() {
        FileChooser fileChooser = new FileChooser();
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }
        fileChooser.setTitle("Загрузка топологии:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл топологии АЗС", "*.top"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (FileInputStream in = new FileInputStream(file.getPath())) {
                ObjectInputStream objectInputStream = new ObjectInputStream(in);
                constructorGasStation = (GasStation) objectInputStream.readObject();
                constructorGasStation.setGraphicsContext(graphicsContextConstructor);
                for (int i = 0; i < constructorGasStation.getFunctionalBlockH(); i++) {
                    for (int j = 0; j < constructorGasStation.getFunctionalBlockV(); j++) {
                        if (constructorGasStation.getGasStationFunctionalBlock(i, j) != null) {
                            constructorGasStation.getGasStationFunctionalBlock(i, j).setGraphicsContext(graphicsContextConstructor);
                        }
                    }
                }
                for (int i = 0; i < constructorGasStation.getServiceBlockH(); i++) {
                    for (int j = 0; j < constructorGasStation.getServiceBlockV(); j++) {
                        if (constructorGasStation.getServiceAreaFunctionalBlock(i, j) != null) {
                            constructorGasStation.getServiceAreaFunctionalBlock(i, j).setGraphicsContext(graphicsContextConstructor);
                        }
                    }
                }
                clearConstructorContext();
                constructorGasStation.drawBackground();
                constructorGasStation.drawHighway();
                constructorGasStation.drawMarkup();
                constructorGasStation.drawFunctionalBlocks();

                initialFile = file;
                initialDirectory = new File(file.getParent());
            } catch (InvalidClassException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Загружаемая версия топологии некорректна!");
                alert.showAndWait();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Отсутствует класс топологии!");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Загружаемый файл не содержит топологии АЗС!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void onSave() {
        FileChooser fileChooser = new FileChooser();
        if (initialDirectory.exists()) {
            fileChooser.setInitialDirectory(initialDirectory);
            if (initialFile != null && initialFile.exists()) {
                fileChooser.setInitialFileName(initialFile.getName());
            }
        }
        fileChooser.setTitle("Сохранение топологии:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл топологии заправки", "*.top"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (FileOutputStream out = new FileOutputStream(file.getPath())) {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                objectOutputStream.writeObject(constructorGasStation);
                initialFile = file;
                initialDirectory = new File(file.getParent());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getErrorMessage(GasStation gasStation) {
        ArrayList<VerificatorError> errorList = Verificator.checkAll(gasStation);
        if (errorList.size() == 0)
            return null;
        else {
            StringBuilder message = new StringBuilder();
            if (errorList.contains(VerificatorError.hasNoCashBox)) {
                message.append("\n - На АЗС должна быть касса.");
            }
            if (errorList.contains(VerificatorError.hasNoInfoTable)) {
                message.append("\n - На АЗС должно быть информационное табло.");
            }
            if (errorList.contains(VerificatorError.IncorrectGasStationEntryDeparturePlacement)) {
                message.append("\n - Въезд и выезд на АЗС должны прилегать к шоссе. Выезд должен находиться правее въезда.");
            }
            if (errorList.contains(VerificatorError.IncorrectServiceAreaEntryDeparturePlacement)) {
                message.append("\n - Въезд и выезд в сервисную зону должны прилегать к шоссе. Выезд должен находиться правее въезда.");
            }
            if (errorList.contains(VerificatorError.UnrelatedGasStationGraph)) {
                message.append("\n - На АЗС требуется наличие пути от въезда до всех дорог и бензоколонок.");
            }
            if (errorList.contains(VerificatorError.UnrelatedServiceAreaGraph)) {
                message.append("\n - В сервисной зоне требуется наличие пути от въезда до всех дорог и баков с бензином.");
            }
            return message.toString();
        }
    }

    @FXML
    public void onCheck() {
        String message = getErrorMessage(constructorGasStation);
        if (message == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Топология соответствует правилам организации АЗС!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Топология не соответствует правилам организации АЗС: " + message);
            alert.showAndWait();
        }
    }

    @FXML
    public void onGoToModeling() {
        String message = getErrorMessage(constructorGasStation);
        if (message == null) {
            modelingGasStation = new GasStation(constructorGasStation.getFunctionalBlockH(),
                    constructorGasStation.getFunctionalBlockV(),
                    constructorGasStation.getServiceBlockH(),
                    constructorGasStation.getServiceBlockV(),
                    graphicsContextModeling,
                    size,
                    constructorGasStation);
            clearModelingContext();
            modelingGasStation.drawBackground();
            modelingGasStation.drawFunctionalBlocksInModeling();
            modelingGasStation.drawHighwayInModeling();
            run_button.setDisable(false);
            run_button.setImage(new Image("pictures/play_icon.png"));
            pause_button.setDisable(true);
            pause_button.setImage(new Image("pictures/pause_icon_disabled.png"));
            stop_button.setDisable(true);
            stop_button.setImage(new Image("pictures/stop_icon_disabled.png"));
            SingleSelectionModel<Tab> singleSelectionModel = tabPane.getSelectionModel();
            singleSelectionModel.select(1);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Топология не соответствует правилам организации АЗС: " + message);
            alert.showAndWait();
        }
    }

    //Обработчики для вкладки моделирования


//    @FXML
//    public void onModelingLoad() {
//        FileChooser fileChooser = new FileChooser();
//        if (initialDirectory.exists()){
//            fileChooser.setInitialDirectory(initialDirectory);
//        }
//        fileChooser.setTitle("Загрузка топологии:");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл топологии парковки", "*.top"));
//        File file = fileChooser.showOpenDialog(stage);
//        Parking loadingParking;
//        if (file != null) {
//            try (FileInputStream in = new FileInputStream(file.getPath())) {
//                ObjectInputStream objectInputStream = new ObjectInputStream(in);
//                Parking parking = (Parking) objectInputStream.readObject();
//                loadingParking = new Parking(parking.getFunctionalBlockH(), parking.getFunctionalBlockV(), graphicsContextModeling, size, parking);
//                for (int i = 0; i <  loadingParking.getFunctionalBlockH(); i++) {
//                    for (int j = 0; j <  loadingParking.getFunctionalBlockV(); j++) {
//                        if ( loadingParking.getFunctionalBlock(i, j) != null) {
//                            loadingParking.getFunctionalBlock(i, j).setGraphicsContext(graphicsContextModeling);
//                        }
//                    }
//                }
//                String message = getErrorMessage(loadingParking);
//                if (message == null) {
//                    modelingParking = loadingParking;
//                    clearModelingContext();
//                    modelingParking.drawBackground();
//                    modelingParking.drawHighwayInModeling();
//                    modelingParking.drawFunctionalBlocksInModeling();
//                    run_button.setDisable(false);
//                    run_button.setImage(new Image("play_icon.png"));
//                    pause_button.setDisable(true);
//                    pause_button.setImage(new Image("pause_icon_disabled.png"));
//                    stop_button.setDisable(true);
//                    stop_button.setImage(new Image("stop_icon_disabled.png"));
//
//                    initialDirectory = new File(file.getParent());
//                } else {
//                    Alert alert = new Alert(Alert.AlertType.WARNING);
//                    alert.setTitle("");
//                    alert.setHeaderText("");
//                    alert.setContentText("Топология сконструирована неверно, попробуйте изменить топологию в конструировании.");
//                    alert.showAndWait();
//                }
//            } catch (InvalidClassException e) {
//                e.printStackTrace();
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("");
//                alert.setHeaderText("");
//                alert.setContentText("Загружаемая версия топологии некорректна!");
//                alert.showAndWait();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("");
//                alert.setHeaderText("");
//                alert.setContentText("Отсутствует класс топологии!");
//                alert.showAndWait();
//            } catch (IOException e) {
//                e.printStackTrace();
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("");
//                alert.setHeaderText("");
//                alert.setContentText("Загружаемый файл не содержит топологии парковки!");
//                alert.showAndWait();
//            }
//        }
//    }

    //Процесс моделирования движения автомобилей
    private class ModelingTimer extends AnimationTimer {
        private long lastHighwayReverse = 0;
        private long lastHighway = 0;
        private long time;
        private long timeReverse;
        private boolean isStarted = true;

        private ArrayList<Car> cars = new ArrayList<>();
        private ArrayList<PathTransition> transitions = new ArrayList<>();
        private GasStationGraph gasStationGraph = new GasStationGraph(modelingGasStation);
        private ServiceAreaGraph serviceAreaGraph = new ServiceAreaGraph(modelingGasStation);

        private Random random = new Random();
        private Time modelTime = new Time();
        private long lastTimeTick;
        private long timeTick;

        {
            gasStationGraph.fillFreeGasolinePumps();
            serviceAreaGraph.fillFreeGasolineTanks();

            modelingGasStation.drawInfoTableInModeling(gasStationGraph.getFreeGasolinePumps().size());

            modelingGasStation.drawGasolinePumpNumbers();
            modelingGasStation.drawGasolineTankNumbers();
        }

        @Override
        public void handle(long now) {
            if (isStarted) {
                lastHighwayReverse = now - timeReverse;
                lastHighway = now - time;
                lastTimeTick = now - timeTick;
                isStarted = false;
            }
            timeReverse = now - lastHighwayReverse;
            time = now - lastHighway;
            timeTick = now - lastTimeTick;

            graphicsContextModeling.setFill(Paint.valueOf("#ffcc66"));
            graphicsContextModeling.fillRect(0, 0, 200, 75);
            graphicsContextModeling.setFill(Paint.valueOf("#000000"));
            graphicsContextModeling.fillText("Топливо (л): " + getPetrolAmountTotal(), 25, 25);
            graphicsContextModeling.fillText("Деньги (у.е.): " + getMoneyAmount(), 25, 50);

            if (now - lastTimeTick > 1_000_000_000L) {
                modelTime.inc();
                lastTimeTick = now;
                modelTimeCanvas.getGraphicsContext2D().clearRect(0, 0, 100, 50);
                modelTimeCanvas.getGraphicsContext2D().setFill(Color.BLACK);
                modelTimeCanvas.getGraphicsContext2D().setFont(Font.font("Arial", size / 2));
                modelTimeCanvas.getGraphicsContext2D().fillText(modelTime.toString(), 15, 35);
            }
            if (now - lastHighwayReverse > reverseIntervalGetter.getInterval() * 1_000_000_000L) {
                Car car;
                if (random.nextDouble() < passengerPercent)
                    car = new Passenger(graphicsContextModeling.getCanvas().getWidth() + 50, modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size + 75);
                else
                    car = new Cargo(graphicsContextModeling.getCanvas().getWidth() + 50, modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size + 75);
                cars.add(car);
                modeling.getChildren().add(car);

                Path path = new Path();
                path.getElements().add(new MoveTo(car.getX(),
                        car.getY()));
                path.getElements().add(new LineTo(-50, modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size + 75));

                PathTransition pathTransition = new PathTransition();
                pathTransition.setDuration(Duration.millis(8000));
                pathTransition.setNode(car);
                pathTransition.setPath(path);
                pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                pathTransition.setInterpolator(Interpolator.LINEAR);
                pathTransition.setOnFinished((event -> {
                    PathTransition pathTransition1 = (PathTransition) event.getSource();
                    Car car_car = (Car) pathTransition1.getNode();
                    cars.remove(car_car);
                    modeling.getChildren().remove(car_car);
                    transitions.remove(pathTransition1);
                }));
                pathTransition.play();
                transitions.add(pathTransition);
                reverseIntervalGetter.generateNext();
                lastHighwayReverse = now;
            }
            if (now - lastHighway > intervalGetter.getInterval() * 1_000_000_000L) {
                Car car;
                if (random.nextDouble() < passengerPercent)
                    car = new Passenger(-50, modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size + 25);
                else
                    car = new Cargo(-50, modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size + 25);
                cars.add(car);
                modeling.getChildren().add(car);

                Path path = new Path();
                path.getElements().add(new MoveTo(car.getX(),
                        car.getY()));

                PathTransition pathTransition = new PathTransition();
                transitions.add(pathTransition);


                if (car instanceof Passenger) {
                    //Путь от начала шоссе до въезда
                    path.getElements().add(new LineTo(gasStationGraph.getEntry().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() - 25,
                            modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size + 25));

                    pathTransition.setOnFinished(event -> {
                        PathTransition pathTransitionToEntry = (PathTransition) event.getSource();
                        Car car2 = (Passenger) pathTransitionToEntry.getNode();
                        PathTransition pathNextTransition = new PathTransition();
                        transitions.remove(pathTransitionToEntry);
                        transitions.add(pathNextTransition);
                        Path pathNext = new Path();
                        pathNext.getElements().add(new MoveTo(gasStationGraph.getEntry().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() - 25,
                                modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size + 25));

                        if (random.nextDouble() < probability && ((Passenger) car2).getPetrolAmount() < 25
                                && getPetrolAmountTotal() > (50 - ((Passenger) car2).getPetrolAmount()) && gasStationGraph.hasFreeGasolinePumps()) {

                            //Поворот на парковку

                            CubicCurveTo cubicTo = new CubicCurveTo();
                            cubicTo.setControlX1(gasStationGraph.getEntry().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN());
                            cubicTo.setControlY1(modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size + 25);
                            cubicTo.setControlX2(gasStationGraph.getEntry().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25);
                            cubicTo.setControlY2(modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size + 15);
                            cubicTo.setX(gasStationGraph.getEntry().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25);
                            cubicTo.setY(modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size - 25);
                            pathNext.getElements().add(cubicTo);
                            /*pathNext.getElements().add(new LineTo(gasStationGraph.getEntry().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25,
                                    modelingParking.getVERTICAL_MARGIN() + (modelingParking.getFunctionalBlockV()-1) * size + 25));*/


                            // Путь от въезда до парковочного места

                            car2.setGasolinePumpNode(gasStationGraph.getFreeGasolinePump());
                            modelingGasStation.drawInfoTableInModeling(gasStationGraph.getFreeGasolinePumps().size());
                            for (Node step : car2.getPathToGasStationEntry()
                                    ) {
                                pathNext.getElements().add(new LineTo(step.getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25,
                                        step.getJ() * size + modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + 25));
                            }

                            //Анимация по приезде на парковочное место
                            pathNextTransition.setOnFinished((event1) -> {
                                PathTransition pathTransitionToParkingPlace = (PathTransition) event1.getSource();
                                PathTransition pathTransition1 = new PathTransition();
                                transitions.remove(pathTransitionToParkingPlace);
                                transitions.add(pathTransition1);
                                Car car_car = (Car) pathTransitionToParkingPlace.getNode();
                                Path emptyPath = new Path();
                                emptyPath.getElements().add(new MoveTo(car_car.getGasolinePumpNode().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25,
                                        car_car.getGasolinePumpNode().getJ() * size + modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + 25));
                                emptyPath.getElements().add(new LineTo(car_car.getGasolinePumpNode().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25,
                                        car_car.getGasolinePumpNode().getJ() * size + modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + 25));
                                pathTransition1.setNode(car_car);
                                pathTransition1.setPath(emptyPath);
                                pathTransition1.setInterpolator(Interpolator.LINEAR);
                                pathTransition1.setDuration(Duration.millis(0.1));

                                double parkingTime = intervalGetterParking.getInterval();
                                pathTransition1.setDelay(Duration.millis(parkingTime * 1000));
                                car_car.setArrivalTime(modelTime.toString());

                                intervalGetterParking.generateNext();
                                pathTransition1.setOnFinished(event2 -> {
                                    PathTransition delay = (PathTransition) event2.getSource();
                                    PathTransition pathTransitionFromParkingPlace = new PathTransition();
                                    transitions.remove(delay);
                                    transitions.add(pathTransitionFromParkingPlace);
                                    Car car_car_car = (Car) delay.getNode();
                                    car_car_car.setDepartureTime(modelTime.toString());
                                    Path pathToDeparture = new Path();

                                    pathToDeparture.getElements().add(new MoveTo(car_car_car.getGasolinePumpNode().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25,
                                            car_car_car.getGasolinePumpNode().getJ() * size + modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + 25));

                                    setPetrolAmountTotal(petrolAmountTotal - (50 - ((Passenger) car2).getPetrolAmount()));
                                    setMoneyAmount(getMoneyAmount() + 1);
                                    graphicsContextModeling.setFill(Paint.valueOf("#ffcc66"));
                                    graphicsContextModeling.fillRect(0, 0, 200, 75);
                                    graphicsContextModeling.setFill(Paint.valueOf("#000000"));
                                    graphicsContextModeling.fillText("Топливо (л): " + getPetrolAmountTotal(), 25, 25);
                                    graphicsContextModeling.fillText("Деньги (у.е.): " + getMoneyAmount(), 25, 50);

                                    ((Passenger) car2).setPetrolAmount(50);

                                    //Путь от парковочного места до выезда

                                    for (Node step : car_car_car.getPathToGasStationDeparture()
                                            ) {
                                        pathToDeparture.getElements().add(new LineTo(step.getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25,
                                                step.getJ() * size + modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + 25));
                                    }

                                    pathTransitionFromParkingPlace.setOnFinished(event3 -> {
                                        gasStationGraph.freeParkingPlace(car_car_car.getGasolinePumpNode());
                                        modelingGasStation.drawInfoTableInModeling(gasStationGraph.getFreeGasolinePumps().size());
                                        PathTransition turn = (PathTransition) event3.getSource();
                                        Car car1 = (Car) turn.getNode();
                                        PathTransition pathTransitionToEnd = new PathTransition();
                                        transitions.remove(turn);
                                        transitions.add(pathTransitionToEnd);
                                        Path pathToEnd = new Path();
//        TODO                            statisticController.addRecord(new Record(((ParkingPlace) modelingParking.getParking()[car1.getParkingPlace().getI()][car1.getParkingPlace().getJ()]).getNumber(), car1.getType(), car1.getRate() * parkingTime / 60, car1.getArrivalTime(), car1.getDepartureTime()));
                                        //Поворот на шоссе

                                        pathToEnd.getElements().add(new MoveTo(modelingGasStation.getGasStationDepartureI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25, modelingGasStation.getGasStationDepartureJ() * size + modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + 25));
                                        /*CubicCurveTo cubicTo1 = new CubicCurveTo();
                                        cubicTo1.setControlX1(gasStationGraph.getDeparture().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25);
                                        cubicTo1.setControlY1(gasStationGraph.getDeparture().getJ() * size + modelingParking.getVERTICAL_MARGIN() + 65);
                                        cubicTo1.setControlX2(gasStationGraph.getDeparture().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 35);
                                        cubicTo1.setControlY2(gasStationGraph.getDeparture().getJ() * size + modelingParking.getVERTICAL_MARGIN() + 75);
                                        cubicTo1.setX(gasStationGraph.getDeparture().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 75);
                                        cubicTo1.setY(gasStationGraph.getDeparture().getJ() * size + modelingParking.getVERTICAL_MARGIN() + 75);*/
                                        pathToEnd.getElements().add(new LineTo(modelingGasStation.getGasStationDepartureI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25, (modelingGasStation.getGasStationDepartureJ() + 1) * size + modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + 25));

                                        //Путь до конца

                                        pathToEnd.getElements().add(new LineTo(modeling.getWidth() + 50, modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size + 25));

                                        pathTransitionToEnd.setDuration(Duration.millis((modelingGasStation.getHORIZONTAL_MARGIN() / size * 500) + (modelingGasStation.getFunctionalBlockH() - gasStationGraph.getDeparture().getI()) * 500 + (modelingGasStation.getServiceBlockH()) * 500 + 500));
                                        pathTransitionToEnd.setPath(pathToEnd);
                                        pathTransitionToEnd.setNode(car1);
                                        pathTransitionToEnd.setPath(pathToEnd);
                                        pathTransitionToEnd.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                                        pathTransitionToEnd.setInterpolator(Interpolator.LINEAR);
                                        pathTransitionToEnd.setOnFinished(event4 -> {
                                            PathTransition pathTransition2 = (PathTransition) event4.getSource();
                                            transitions.remove(pathTransition2);
                                            Car car_car_car_car = (Car) pathTransition2.getNode();
                                            cars.remove(car_car_car_car);
                                            modeling.getChildren().remove(car_car_car_car);
                                        });
                                        pathTransitionToEnd.play();
                                    });

                                    pathTransitionFromParkingPlace.setDuration(Duration.millis(car_car_car.getPathToGasStationDeparture().size() * 500));
                                    pathTransitionFromParkingPlace.setPath(pathToDeparture);
                                    pathTransitionFromParkingPlace.setNode(car_car_car);
                                    pathTransitionFromParkingPlace.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                                    pathTransitionFromParkingPlace.setInterpolator(Interpolator.LINEAR);
                                    pathTransitionFromParkingPlace.play();
                                });

                                pathTransition1.play();
                            });
                            pathNextTransition.setDuration(Duration.millis(car2.getPathToGasStationEntry().size() * 500 + 500));
                        } else {
                            pathNext.getElements().add(new LineTo(modeling.getWidth() + 50, modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size + 25));
                            pathNextTransition.setOnFinished((event5 -> {
                                PathTransition pathTransition1 = (PathTransition) event5.getSource();
                                transitions.remove(pathTransition1);
                                Car car_car = (Car) pathTransition1.getNode();
                                cars.remove(car_car);
                                modeling.getChildren().remove(car_car);
                            }));
                            pathNextTransition.setDuration(Duration.millis(modelingGasStation.getHORIZONTAL_MARGIN() / size * 500 + (modelingGasStation.getFunctionalBlockH() + 1 - modelingGasStation.getGasStationEntryI()) * 500));
                        }
                        pathNextTransition.setPath(pathNext);
                        pathNextTransition.setNode(car2);
                        pathNextTransition.setPath(pathNext);
                        pathNextTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        pathNextTransition.setInterpolator(Interpolator.LINEAR);
                        pathNextTransition.play();
                    });


                    pathTransition.setDuration(Duration.millis(gasStationGraph.getEntry().getI() * 500
                            + (modelingGasStation.getHORIZONTAL_MARGIN() / size * 500)));
                    pathTransition.setNode(car);
                    pathTransition.setPath(path);
                    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                    pathTransition.setInterpolator(Interpolator.LINEAR);
                    pathTransition.play();
                    intervalGetter.generateNext();
                    lastHighway = now;
                }
                if (car instanceof Cargo) {
                    double FUNK_plus_BETWEEN = modelingGasStation.getFunctionalBlockH() * size + DISTANCE_BETWEEN;

                    //Путь от начала шоссе до въезда
                    path.getElements().add(new LineTo(FUNK_plus_BETWEEN + serviceAreaGraph.getEntry().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() - 25,
                            modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + modelingGasStation.getServiceBlockV() * size + 25));

                    pathTransition.setOnFinished(event -> {
                        PathTransition pathTransitionToEntry = (PathTransition) event.getSource();
                        Car car2 = (Car) pathTransitionToEntry.getNode();
                        PathTransition pathNextTransition = new PathTransition();
                        transitions.remove(pathTransitionToEntry);
                        transitions.add(pathNextTransition);
                        Path pathNext = new Path();
                        pathNext.getElements().add(new MoveTo(FUNK_plus_BETWEEN + serviceAreaGraph.getEntry().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() - 25,
                                modelingGasStation.getGAS_STATION_VERTICAL_MARGIN_TOP() + modelingGasStation.getFunctionalBlockV() * size + 25));

                        if (random.nextDouble() < probability && serviceAreaGraph.hasFreeGasolineTanks()
                                && getPetrolAmountTotal() < 500) {


                            //Поворот на парковку

                            CubicCurveTo cubicTo = new CubicCurveTo();
                            cubicTo.setControlX1(FUNK_plus_BETWEEN + serviceAreaGraph.getEntry().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN());
                            cubicTo.setControlY1(modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + modelingGasStation.getServiceBlockV() * size + 25);
                            cubicTo.setControlX2(FUNK_plus_BETWEEN + serviceAreaGraph.getEntry().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25);
                            cubicTo.setControlY2(modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + modelingGasStation.getServiceBlockV() * size + 15);
                            cubicTo.setX(FUNK_plus_BETWEEN + serviceAreaGraph.getEntry().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25);
                            cubicTo.setY(modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + modelingGasStation.getServiceBlockV() * size - 25);
                            pathNext.getElements().add(cubicTo);
                            /*pathNext.getElements().add(new LineTo(gasStationGraph.getEntry().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25,
                                    modelingParking.getVERTICAL_MARGIN() + (modelingParking.getFunctionalBlockV()-1) * size + 25));*/


                            // Путь от въезда до парковочного места

                            car2.setGasolineTankNode(serviceAreaGraph.getFreeGasolineTank());
                            for (Node step : car2.getPathToServiceAreaEntry()
                                    ) {
                                pathNext.getElements().add(new LineTo(FUNK_plus_BETWEEN + step.getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25,
                                        step.getJ() * size + modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + 25));
                            }

                            //Анимация по приезде на парковочное место
                            pathNextTransition.setOnFinished((event1) -> {
                                PathTransition pathTransitionToParkingPlace = (PathTransition) event1.getSource();
                                PathTransition pathTransition1 = new PathTransition();
                                transitions.remove(pathTransitionToParkingPlace);
                                transitions.add(pathTransition1);
                                Car car_car = (Car) pathTransitionToParkingPlace.getNode();
                                Path emptyPath = new Path();
                                emptyPath.getElements().add(new MoveTo(FUNK_plus_BETWEEN + car_car.getGasolineTankNode().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25,
                                        car_car.getGasolineTankNode().getJ() * size + modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + 25));
                                emptyPath.getElements().add(new LineTo(FUNK_plus_BETWEEN + car_car.getGasolineTankNode().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25,
                                        car_car.getGasolineTankNode().getJ() * size + modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + 25));
                                pathTransition1.setNode(car_car);
                                pathTransition1.setPath(emptyPath);
                                pathTransition1.setInterpolator(Interpolator.LINEAR);
                                pathTransition1.setDuration(Duration.millis(0.1));

                                double parkingTime = intervalGetterParking.getInterval();
                                pathTransition1.setDelay(Duration.millis(parkingTime * 1000));
                                car_car.setArrivalTime(modelTime.toString());

                                intervalGetterParking.generateNext();
                                pathTransition1.setOnFinished(event2 -> {
                                    PathTransition delay = (PathTransition) event2.getSource();
                                    PathTransition pathTransitionFromParkingPlace = new PathTransition();
                                    transitions.remove(delay);
                                    transitions.add(pathTransitionFromParkingPlace);
                                    Car car_car_car = (Car) delay.getNode();
                                    car_car_car.setDepartureTime(modelTime.toString());
                                    Path pathToDeparture = new Path();

                                    pathToDeparture.getElements().add(new MoveTo(FUNK_plus_BETWEEN + car_car_car.getGasolineTankNode().getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25,
                                            car_car_car.getGasolineTankNode().getJ() * size + modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + 25));

                                    setPetrolAmountTotal(getPetrolAmountTotal() + 100);
                                    graphicsContextModeling.setFill(Paint.valueOf("#ffcc66"));
                                    graphicsContextModeling.fillRect(0, 0, 200, 75);
                                    graphicsContextModeling.setFill(Paint.valueOf("#000000"));
                                    graphicsContextModeling.fillText("Топливо (л): " + getPetrolAmountTotal(), 25, 25);
                                    graphicsContextModeling.fillText("Деньги (у.е.): " + getMoneyAmount(), 25, 50);

                                    //Путь от парковочного места до выезда

                                    for (Node step : car_car_car.getPathToServiceAreaDeparture()
                                            ) {
                                        pathToDeparture.getElements().add(new LineTo(FUNK_plus_BETWEEN + step.getI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25,
                                                step.getJ() * size + modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + 25));
                                    }

                                    pathTransitionFromParkingPlace.setOnFinished(event3 -> {
                                        serviceAreaGraph.freeParkingPlace(car_car_car.getGasolineTankNode());
                                        PathTransition turn = (PathTransition) event3.getSource();
                                        Car car1 = (Car) turn.getNode();
                                        PathTransition pathTransitionToEnd = new PathTransition();
                                        transitions.remove(turn);
                                        transitions.add(pathTransitionToEnd);
                                        Path pathToEnd = new Path();
//        TODO                            statisticController.addRecord(new Record(((ParkingPlace) modelingParking.getParking()[car1.getParkingPlace().getI()][car1.getParkingPlace().getJ()]).getNumber(), car1.getType(), car1.getRate() * parkingTime / 60, car1.getArrivalTime(), car1.getDepartureTime()));
                                        //Поворот на шоссе

                                        pathToEnd.getElements().add(new MoveTo(FUNK_plus_BETWEEN + modelingGasStation.getServiceAreaDepartureI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25, modelingGasStation.getServiceAreaDepartureJ() * size + modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + 25));
                                        /*CubicCurveTo cubicTo1 = new CubicCurveTo();
                                        cubicTo1.setControlX1(gasStationGraph.getDeparture().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25);
                                        cubicTo1.setControlY1(gasStationGraph.getDeparture().getJ() * size + modelingParking.getVERTICAL_MARGIN() + 65);
                                        cubicTo1.setControlX2(gasStationGraph.getDeparture().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 35);
                                        cubicTo1.setControlY2(gasStationGraph.getDeparture().getJ() * size + modelingParking.getVERTICAL_MARGIN() + 75);
                                        cubicTo1.setX(gasStationGraph.getDeparture().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 75);
                                        cubicTo1.setY(gasStationGraph.getDeparture().getJ() * size + modelingParking.getVERTICAL_MARGIN() + 75);*/
                                        pathToEnd.getElements().add(new LineTo(FUNK_plus_BETWEEN + modelingGasStation.getServiceAreaDepartureI() * size + modelingGasStation.getHORIZONTAL_MARGIN() + 25, (modelingGasStation.getGasStationDepartureJ() + 1) * size + modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + 25));

                                        //Путь до конца

                                        pathToEnd.getElements().add(new LineTo(FUNK_plus_BETWEEN + modeling.getWidth() + 50, modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + modelingGasStation.getServiceBlockV() * size + 25));

                                        pathTransitionToEnd.setDuration(Duration.millis((modelingGasStation.getHORIZONTAL_MARGIN() / size * 500) + (modelingGasStation.getServiceBlockH() - serviceAreaGraph.getDeparture().getI()) * 500 + 500));
                                        pathTransitionToEnd.setPath(pathToEnd);
                                        pathTransitionToEnd.setNode(car1);
                                        pathTransitionToEnd.setPath(pathToEnd);
                                        pathTransitionToEnd.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                                        pathTransitionToEnd.setInterpolator(Interpolator.LINEAR);
                                        pathTransitionToEnd.setOnFinished(event4 -> {
                                            PathTransition pathTransition2 = (PathTransition) event4.getSource();
                                            transitions.remove(pathTransition2);
                                            Car car_car_car_car = (Car) pathTransition2.getNode();
                                            cars.remove(car_car_car_car);
                                            modeling.getChildren().remove(car_car_car_car);
                                        });
                                        pathTransitionToEnd.play();
                                    });

                                    pathTransitionFromParkingPlace.setDuration(Duration.millis(car_car_car.getPathToServiceAreaDeparture().size() * 500));
                                    pathTransitionFromParkingPlace.setPath(pathToDeparture);
                                    pathTransitionFromParkingPlace.setNode(car_car_car);
                                    pathTransitionFromParkingPlace.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                                    pathTransitionFromParkingPlace.setInterpolator(Interpolator.LINEAR);
                                    pathTransitionFromParkingPlace.play();
                                });

                                pathTransition1.play();
                            });
                            pathNextTransition.setDuration(Duration.millis(car2.getPathToServiceAreaEntry().size() * 500 + 500));
                        } else {
                            pathNext.getElements().add(new LineTo(modeling.getWidth() + 50, modelingGasStation.getSERVICE_AREA_VERTICAL_MARGIN_TOP() + modelingGasStation.getServiceBlockV() * size + 25));
                            pathNextTransition.setOnFinished((event5 -> {
                                PathTransition pathTransition1 = (PathTransition) event5.getSource();
                                transitions.remove(pathTransition1);
                                Car car_car = (Car) pathTransition1.getNode();
                                cars.remove(car_car);
                                modeling.getChildren().remove(car_car);
                            }));
                            pathNextTransition.setDuration(Duration.millis(modelingGasStation.getHORIZONTAL_MARGIN() / size * 500 + (modelingGasStation.getServiceBlockH() + 1 - modelingGasStation.getServiceAreaEntryI()) * 500));
                        }
                        pathNextTransition.setPath(pathNext);
                        pathNextTransition.setNode(car2);
                        pathNextTransition.setPath(pathNext);
                        pathNextTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        pathNextTransition.setInterpolator(Interpolator.LINEAR);
                        pathNextTransition.play();
                    });


                    pathTransition.setDuration(Duration.millis(serviceAreaGraph.getEntry().getI() * 500
                            + (modelingGasStation.getHORIZONTAL_MARGIN() / size * 500)));
                    pathTransition.setNode(car);
                    pathTransition.setPath(path);
                    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                    pathTransition.setInterpolator(Interpolator.LINEAR);
                    pathTransition.play();
                    intervalGetter.generateNext();
                    lastHighway = now;
                }
            }
        }

        //        public void pauseAnimation(){
//            for (PathTransition pathTransition:
//                    transitions) {
//                pathTransition.pause();
//            }
//            this.stop();
//        }
//
        public void resumeAnimation() {
            for (PathTransition pathTransition :
                    transitions) {
                pathTransition.play();
            }
            isStarted = true;
            this.start();
        }
//
//        public void stopAnimation(){
//            for (PathTransition pathTransition:
//                    transitions) {
//                pathTransition.stop();
//            }
//            transitions.clear();
//            for (Car car : cars
//                    ) {
//                modeling.getChildren().remove(car);
//            }
//            modelTimeCanvas.getGraphicsContext2D().clearRect(0,0,100,50);
//            cars.clear();
//            this.stop();
//        }
    }

    //
    @FXML
    public void onStartModeling() {
        if (modelingTimer == null || !isStartedModeling) {
            modelingTimer = this.new ModelingTimer();
            modelingTimer.start();
            isStartedModeling = true;
            load_button.setDisable(true);
            load_button.setImage(new Image("load_icon2_disabled.png"));
            go_button.setDisable(true);
            go_button.setImage(new Image("goto_icon2_disabled.png"));
            stop_button.setDisable(false);
            stop_button.setImage(new Image("stop_icon.png"));
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("statistic.fxml"));
//            AnchorPane page = null;
//            try {
//                page = (AnchorPane) loader.load();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            // Создаём диалоговое окно Stage.
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle("Статистика");
//            Scene scene = new Scene(page);
//            dialogStage.setScene(scene);
//            statisticController = loader.getController();
//            statisticController.setStage(dialogStage);
//            dialogStage.initOwner(stage);
//            dialogStage.setX(stage.getX() + stage.getWidth());
//            dialogStage.setY(stage.getY());
//            dialogStage.setHeight(stage.getHeight());
//
//            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
//            dialogStage.show();
//            dialogStage.setMaxHeight(dialogStage.getHeight());
//            dialogStage.setMaxWidth(dialogStage.getWidth());
//            dialogStage.setMinHeight(dialogStage.getHeight());
//            dialogStage.setMinWidth(dialogStage.getWidth());
        } else modelingTimer.resumeAnimation();
        pause_button.setDisable(false);
        pause_button.setImage(new Image("pause_icon.png"));
        run_button.setDisable(true);
        run_button.setImage(new Image("play_icon_disabled.png"));
        table_button.setDisable(false);
        table_button.setImage(new Image("table_icon.png"));
    }
//
//    @FXML
//    public void onPauseModeling() {
//        modelingTimer.pauseAnimation();
//        run_button.setDisable(false);
//        run_button.setImage(new Image("play_icon.png"));
//        pause_button.setDisable(true);
//        pause_button.setImage(new Image("pause_icon_disabled.png"));
//
//    }
//
//    @FXML
//    public void onStopModeling(){
//        modelingTimer.stopAnimation();
//        isStartedModeling = false;
//        modelingParking.drawFunctionalBlocksInModeling();
//        load_button.setDisable(false);
//        load_button.setImage(new Image("load_icon2.png"));
//        go_button.setDisable(false);
//        go_button.setImage(new Image("goto_icon2.png"));
//        run_button.setDisable(false);
//        run_button.setImage(new Image("play_icon.png"));
//        statisticController.close();
//        pause_button.setDisable(true);
//        pause_button.setImage(new Image("pause_icon_disabled.png"));
//        stop_button.setDisable(true);
//        stop_button.setImage(new Image("stop_icon_disabled.png"));
//        table_button.setDisable(true);
//        table_button.setImage(new Image("table_icon_disabled.png"));
//    }
//
//    @FXML
//    public void onModelingCarSettingsClick() {
//        try {
//            if (modelingCarSettingsController==null) {
//                // Загружаем fxml-файл и создаём новую сцену
//                // для всплывающего диалогового окна.
//                FXMLLoader loader = new FXMLLoader();
//                loader.setLocation(getClass().getResource("modeling_car_settings.fxml"));
//                AnchorPane page = (AnchorPane) loader.load();
//                // Создаём диалоговое окно Stage.
//                Stage dialogStage = new Stage();
//                dialogStage.setTitle("Параметры потока автомобилей");
//                Scene scene = new Scene(page);
//                dialogStage.setScene(scene);
//                // Передаём адресата в контроллер.
//                ModelingCarSettingsController controller = loader.getController();
//                //controller.setInitialParkingWidth(constructorGasStation.getFunctionalBlockH());
//                //controller.setInitialParkingHeight(constructorGasStation.getFunctionalBlockV());
//                controller.setDialogStage(dialogStage);
//                dialogStage.initModality(Modality.WINDOW_MODAL);
//                dialogStage.initOwner(stage);
//                // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
//
//                dialogStage.setResizable(false);
//                modelingCarSettingsController = controller;
//            }
//            modelingCarSettingsController.showAndWait();
//            if (modelingCarSettingsController.isSubmitClicked()) {
//                probability = modelingCarSettingsController.getSelectedCarEnterProbability();
//                intervalGetter = modelingCarSettingsController.getIntervalGetter();
//                try {
//                    reverseIntervalGetter = (IntervalGetter) intervalGetter.clone();
//                } catch (CloneNotSupportedException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    public void onModelingParkingSettingsClick() {
//        try {
//            if (modelingParkingSettingsController == null) {
//                // Загружаем fxml-файл и создаём новую сцену
//                // для всплывающего диалогового окна.
//                FXMLLoader loader = new FXMLLoader();
//
//                loader.setLocation(getClass().getResource("modeling_parking_settings.fxml"));
//                AnchorPane page = (AnchorPane) loader.load();
//                // Создаём диалоговое окно Stage.
//                Stage dialogStage = new Stage();
//                dialogStage.setTitle("Параметры парковки");
//                Scene scene = new Scene(page);
//                dialogStage.setScene(scene);
//                // Передаём адресата в контроллер.
//                ModelingParkingSettingsController controller = loader.getController();
//                //controller.setInitialParkingWidth(constructorGasStation.getFunctionalBlockH());
//                //controller.setInitialParkingHeight(constructorGasStation.getFunctionalBlockV());
//                controller.setDialogStage(dialogStage);
//                dialogStage.initModality(Modality.WINDOW_MODAL);
//                dialogStage.initOwner(stage);
//                dialogStage.setResizable(false);
//                // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
//                modelingParkingSettingsController=controller;
//            }
//            modelingParkingSettingsController.showAndWait();
//            if (modelingParkingSettingsController.isSubmitClicked()) {
//                Cargo.setRate(modelingParkingSettingsController.getCargoRate());
//                Passenger.setRate(modelingParkingSettingsController.getPassengerRate());
//                passengerPercent = modelingParkingSettingsController.getPassengerPercent();
//                intervalGetterParking = modelingParkingSettingsController.getIntervalGetter();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    public void onTableButtonClick(){
//        statisticController.show();
//    }
}
