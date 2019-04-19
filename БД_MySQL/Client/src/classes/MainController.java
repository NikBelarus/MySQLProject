package classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;

import java.util.ArrayList;

/**
 * The class-Controller of the <b>main window</b>
 * @author Dashchynski Nikita
 * @version 1.0
 */
public class MainController {
    @FXML
    private TableView myTable;
    @FXML
    private TableColumn name;
    @FXML
    private TableColumn adress;
    @FXML
    private TableColumn city;

    private ObservableList<Person> usersData = FXCollections.observableArrayList();
    private Client main;

    @FXML
    public void initialize(){
        try {
            // устанавливаем тип и значение которое должно хранится в колонке
            name.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
            adress.setCellValueFactory(new PropertyValueFactory<Person, String>("adress"));
            city.setCellValueFactory(new PropertyValueFactory<Person, String>("city"));
            show();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem menuItem = new MenuItem("удалить");
            contextMenu.getItems().add(menuItem);

            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Person person = (Person) myTable.getSelectionModel().getSelectedItem();
                    try {
                        delete(person.getName(), person.getAdress(), person.getCity());
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ошибка");
                        alert.setTitle("Information");
                        alert.setHeaderText(null);
                        alert.show();
                    }
                }
            });

            myTable.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    contextMenu.show(myTable, event.getScreenX(), event.getScreenY());
                }
            });

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ошибка");
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.show();
        }
    }

    public void setMainApp(Client main) {
        this.main = main;
    }

    public void add(){
        Person tempPerson = new Person();
        boolean okClicked = main.showPersonEditDialog(tempPerson);
        if (okClicked) {
            usersData.add(tempPerson);
            boolean res = Client.add(tempPerson);
            show();
        }
    }

    public void delete(String name, String adress, String city){
        boolean res = Client.delete(name, adress, city);
        if(res)
            show();
    }

    public void show(){
        usersData.clear();
        ArrayList<Person> peopleHelps = Client.show();
        if(peopleHelps != null){
            usersData.addAll(peopleHelps);
            // заполняем таблицу данными
            myTable.setItems(usersData);
        }
    }

    public void countPeople(){
        String temp = Client.countPeople();
        if(temp != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, temp);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.show();
        }
    }
}