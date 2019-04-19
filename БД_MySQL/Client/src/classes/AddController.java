package classes;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The <b>auxiliary</b> class-Controller for adding a new person
 * @author Dashchynski Nikita
 * @version 1.0
 */
public class AddController {
    @FXML
    private TextField name;
    @FXML
    private TextField adress;
    @FXML
    private TextField city;

    private Stage dialogStage;
    private Person person;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPerson(Person person) {
        this.person = person;

        name.setText(person.getName());
        adress.setText(person.getAdress());
        city.setText(person.getCity());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            person.setname(name.getText());
            person.setAdress(adress.getText());
            person.setCity(city.getText());

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (name.getText() == null || name.getText().length() == 0) {
            errorMessage += "Name\n";
        }
        if (adress.getText() == null || adress.getText().length() == 0) {
            errorMessage += "Адрес\n";
        }
        if (city.getText() == null || city.getText().length() == 0) {
            errorMessage += "Город\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Показываем сообщение об ошибке
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Некорректные данные");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}