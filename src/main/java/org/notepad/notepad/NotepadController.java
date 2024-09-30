package org.notepad.notepad;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Optional;
import java.util.ResourceBundle;


public class NotepadController implements Initializable {

    FileChooser fileChooser = new FileChooser();
    ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
    ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
    ButtonType okButton=new ButtonType("OK",ButtonBar.ButtonData.CANCEL_CLOSE);
    @FXML
    private MenuItem menu_new;

    @FXML
    private MenuItem menu_open;

    @FXML
    private TextArea text_area;

    @FXML
    private VBox vBox;

    private File currentFile;
    private boolean isModified = false;


    public void newFile(ActionEvent actionEvent) {
        if (!isModified) {
            text_area.setText("");
            isModified = false;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Do you want to save existing file?", yesButton, noButton);
            alert.setTitle("Save changes");
            alert.setHeaderText("");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/org/notepad/Images/Notepad.png"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                actionSave();
            } else {
                isModified = false;
                text_area.setText("");
            }
        }
    }


    public void actionOpen(ActionEvent actionEvent) {
        if (!isModified) {
            openFile();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Do you want to save existing file?", yesButton, noButton);
            alert.setTitle("Save Changes");
            alert.setHeaderText("");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/org/notepad/Images/Notepad.png"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                actionSave();
                openFile();
            } else {
                readFile();
            }
        }
    }

    public void openFile() {
        readFile();
    }

    public void readFile() {
        File file = fileChooser.showOpenDialog(new Stage().getOwner());
        if (file != null) {
            try {
                String content = Files.readString(Paths.get(file.getAbsolutePath()));
                text_area.setText(content.toString());
                isModified = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentFile = file;
            isModified = false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        text_area.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                isModified = true;
            }
        });
    }

    public void actionSave(ActionEvent actionEvent) {
        if (currentFile != null) {
            saveFile(currentFile);
        } else {
            actionSaveAs();

        }
    }

    public void actionSave() {
        if (currentFile != null) {
            saveFile(currentFile);
        } else {
            actionSaveAs();
        }
    }

    private void actionSaveAs() {
        File file = fileChooser.showSaveDialog(new Stage().getOwner());
        if (file != null) {
            saveFile(file);
            currentFile = file;
            isModified = false;
        }
    }

    public void actionSaveAs(ActionEvent actionEvent) {
        File file = fileChooser.showSaveDialog(new Stage().getOwner());
        if (file != null) {
            saveFile(file);
            currentFile = file;
        }
    }

    public void saveFile(File file) {
        try {
            Files.write(Paths.get(file.getAbsolutePath()), text_area.getText().getBytes());
            isModified = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void actionClose(ActionEvent actionEvent) {
        if (isModified) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Do you want to save existing file?", yesButton, noButton);
            alert.setTitle("Save Changes");
            alert.setHeaderText("");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/org/notepad/Images/Notepad.png"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                actionSave();
                close();
            } else {
                close();
            }
        } else {
            close();
        }
    }

    public void close() {
        text_area.clear();
        currentFile = null;
        isModified = false;
    }

    public void actionQuit(ActionEvent actionEvent) {
        if (isModified) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Do you want to save changes?", yesButton, noButton);
            alert.setTitle("Save Changes");
            alert.setHeaderText("");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/org/notepad/Images/Notepad.png"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                actionSave();
                quit();
            } else {
                quit();
            }
        }else{
            quit();
        }
    }


    public void quit() {
        Platform.exit();
    }

    public void actionUndo(ActionEvent actionEvent) {
        text_area.undo();
    }

    public void actionCut(ActionEvent actionEvent) {
        text_area.cut();
    }

    public void actionCopy(ActionEvent actionEvent) {
        text_area.copy();
    }

    public void actionPaste(ActionEvent actionEvent) {
        text_area.paste();
    }

    public void actionDelete(ActionEvent actionEvent) {
        text_area.clear();
    }

    public void actionSelectAll(ActionEvent actionEvent) {
        text_area.selectAll();
    }

    public void actionAbout(ActionEvent actionEvent) {
        Alert alert=new Alert(Alert.AlertType.NONE,"Welcome to Notepad, developed by Rishav Rakesh! We provide a simple yet powerful text editing experience. " +
                "Our goal is to enhance your productivity with intuitive features and a user-friendly interface. \nTHANK YOU",okButton);
        alert.setTitle("ThanYou");
        alert.setHeaderText("");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/org/notepad/Images/Notepad.png"));
        alert.showAndWait();
    }
}
