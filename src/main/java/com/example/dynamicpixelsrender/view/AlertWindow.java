package com.example.dynamicpixelsrender.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertWindow {
    public static String INFORMATION = "i";
    public static String WARNING = "w";
    public static String ERROR = "e";


    public static void showInfo(String header, String text, String status) {
        Alert alert = new Alert(null);
        switch (status) {
            case "i":
                alert.setAlertType(AlertType.INFORMATION);
                alert.setTitle("Information");
                break;

            case "w":
                alert.setAlertType(AlertType.WARNING);
                alert.setTitle("Warning");
                break;

            case "e":
                alert.setAlertType(AlertType.ERROR);
                alert.setTitle("Error");
                break;

            default:
                alert.setAlertType(AlertType.ERROR);
                alert.setTitle("System error");
                break;
        }
        alert.setHeaderText(header);
        alert.setContentText(text);

        alert.showAndWait();
    }

}
