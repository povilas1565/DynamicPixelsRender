package com.example.dynamicpixelsrender.model;


import com.example.dynamicpixelsrender.view.AlertWindow;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LogicNote {
    private boolean status = true;
    private Exception exception;
    private boolean cancel = false;

    private Label label;
    private Button buttonRender;
    private Button buttonCancel;

    public void setStatus(Exception exception) {
        this.status = false;
        this.exception = exception;
    }

    public void setLable(Label label) {
        this.label = label;
    }

    public void setButton(Button render, Button cancel) {
        this.buttonRender = render;
        this.buttonCancel = cancel;
    }

    public void runing(boolean run) {
        if (run) {
            buttonRender.setDisable(true);
            buttonCancel.setDisable(false);

            status = true;
        } else {
            buttonRender.setDisable(false);
            buttonCancel.setDisable(true);
        }
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void display(String text) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                label.setText(text);
            }
        });
    }

    public void displayAlert(String tipe) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                if (status) {
                    AlertWindow.showInfo("Success", tipe + "Done", AlertWindow.INFORMATION);

                } else {
                    AlertWindow.showInfo("Failure", tipe + "Error" + exception, AlertWindow.ERROR);
                }
            }
        });
    }
}








