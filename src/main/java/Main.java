import com.ssau.construction.controller.ConstructorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // чтобы создать JavaFX приложения, достаточно реализовать метод start(Stage)
    @Override
    public void start(Stage primaryStage) throws Exception{
        // Stage - это контейнер, ассоциированный с окном
        // Если вы загляните в файл sample.fxml, то у видете в нем XML объявление элемента GridPane, т.е. табличного контейнера
        // Этот контейнер мы будем считать корневым, т.е. все элементы нашего приложения будут содержаться в нем
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("constructor.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Моделирование работы платной парковки"); // задаем заголовок окна
        ((ConstructorController)loader.getController()).setStage(primaryStage);
        // создаем сцену с заданными шириной и высотой и содержащую наш корневым контейнером, и связываем ее с окном
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();
        primaryStage.setMaxHeight(primaryStage.getHeight());
        primaryStage.setMaxWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setMinWidth(primaryStage.getWidth());
        // запускаем окно
    }

    // метод main в JavaFX приложениях не является обязательным
    public static void main(String[] args) {
        launch(args);
    }
}
