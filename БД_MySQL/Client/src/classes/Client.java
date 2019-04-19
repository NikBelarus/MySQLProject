package classes;

import interfaces.WorkersInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * The main class of the <b>Client</b> programm
 * @author Dashchynski Nikita
 * @version 1.0
 * @see Application
 */
public class Client extends Application {

    private static WorkersInterface workers;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group group = new Group();
        Scene scene = new Scene(group);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent content = fxmlLoader.load();
        MainController workers = fxmlLoader.getController();

        BorderPane root = new BorderPane();
        root.setCenter(content);
        group.getChildren().add(root);

        primaryStage.setTitle("MainController");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        this.primaryStage = primaryStage;
        workers.setMainApp(this);
    }

    boolean showPersonEditDialog(Person person) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Client.class.getResource("add.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Новая запись");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AddController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            dialogStage.setResizable(false);
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * The enter point into the programm
     * @param args comes from command line
     */
    public static void main(String[] args) {
        try {
            String objectName = "Workers";
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
            //Registry reg = LocateRegistry.getRegistry("10.150.3.228", 1099);
            workers = (WorkersInterface) reg.lookup(objectName);

        } catch (RemoteException e) {
            System.out.println("нет доступа к БД");
            System.exit(0);
        } catch (NotBoundException e) {
            System.out.println("объект не найден");
            System.exit(0);
        }
        launch(args);
    }

    static boolean add(Person peopleHelp){
        try {
            workers.add(peopleHelp);
            return true;
        } catch (RemoteException e) {
            System.out.println("нет доступа к БД");
            return false;
        }
    }

    static boolean delete(String name, String adress, String city){
        try {
            workers.delete(name, adress, city);
            return true;
        } catch (RemoteException e) {
            System.out.println("нет доступа к БД");
            return false;
        }
    }

    static String countPeople(){
        try {
            String temp = workers.countPeople();
            return temp;
        } catch (RemoteException e) {
            System.out.println("нет доступа к БД");
            return null;
        }
    }

    static ArrayList<Person> show(){
        try {
            ArrayList<Person> peopleHelps = workers.show();
            return peopleHelps;
        } catch (RemoteException e) {
            System.out.println("нет доступа к БД");
            return null;
        }
    }
}