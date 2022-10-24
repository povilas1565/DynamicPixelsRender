package com.example.dynamicpixelsrender.view;

import com.example.dynamicpixelsrender.Main;
import com.example.dynamicpixelsrender.model.RenderLogic;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RootController {
    private Main main;

    @FXML
    private TextField imgResTFWidth;

    @FXML
    private TextField imgResTFHeight;

    @FXML
    private Label imgStaL;

    @FXML
    private Button imgCancelB;

    @FXML
    private Button imgRenderB;

    @FXML
    private ToggleGroup imgPCGroup;
    @FXML
    private RadioButton imgRadioARGB;
    @FXML
    private RadioButton imgRadioBW;
    @FXML
    private RadioButton imgRadioCustom;

    @FXML
    private TextField imgPSWidth;

    @FXML
    private TextField imgPSHeight;

    @FXML
    private CheckBox imgPCAlphaCB;

    @FXML
    public void buttonRImage() {
        System.out.println("Render Image");

        int w = 0;
        int h = 0;
        int p_w = 0;
        int p_h = 0;
        boolean cpc_ok = true;

        String errorText = "";

        if(imgResTFWidth.getText().isEmpty() || imgResTFHeight.getText().isEmpty()) {
            errorText += "Empty \"Resolution\" form filled\n";
        }else{
            w = Integer.parseInt(imgResTFWidth.getText());
            h = Integer.parseInt(imgResTFHeight.getText());
            if(w == 0 || h == 0) {
                errorText += "Wrong \"Resolution\" form filled\n";
            }
        }

        if(imgPSWidth.getText().isEmpty() || imgPSHeight.getText().isEmpty()) {
            errorText += "Empty \"Pixel size\" form filled \n";
        }else {
            p_w = Integer.parseInt(imgPSWidth.getText());
            p_h = Integer.parseInt(imgPSHeight.getText());
            if(p_w == 0 || p_h == 0) {
                errorText += "Wrong \"Pixel size\" form filled \n";
            }
        }

        if(main.getRenderLogic().getImageCustomPC().size() == 0 && imgPCGroup.getSelectedToggle().equals(imgRadioCustom)) {
            cpc_ok = false;
            errorText += "Custom Pixel Color list empty\n";
        }

        if(w != 0 && h != 0 && p_w != 0 && p_h != 0 && cpc_ok) {
            if(imgPCGroup.getSelectedToggle().equals(imgRadioARGB)) {
                main.renderImage(w, h, RenderLogic.COLOR_RGB, p_w, p_h, imgPCAlphaCB.isSelected());
            }else if(imgPCGroup.getSelectedToggle().equals(imgRadioBW)) {
                main.renderImage(w, h, RenderLogic.COLOR_BW, p_w, p_h, imgPCAlphaCB.isSelected());
            }else if(imgPCGroup.getSelectedToggle().equals(imgRadioCustom)) {
                main.renderImage(w, h, RenderLogic.COLOR_CUSTOM, p_w, p_h, imgPCAlphaCB.isSelected());
            }
        }else {
            AlertWindow.showInfo("Try again", errorText.substring(0, errorText.length() - 1), AlertWindow.WARNING);
        }
    }

    @FXML
    private void initialize(){
        imgResTFWidth.textProperty().addListener(onlyIntFilter(imgResTFWidth));
        imgResTFHeight.textProperty().addListener(onlyIntFilter(imgResTFHeight));
        imgPSWidth.textProperty().addListener(onlyIntFilter(imgPSWidth));
        imgPSHeight.textProperty().addListener(onlyIntFilter(imgPSHeight));
    }

    @FXML
    private void imageCancel() {
        main.getImageLogicNote().setCancel(true);
    }

    @FXML
    private void imagePCCustom() {
        main.showPCCustomDialog(true);
    }

    private ChangeListener<String> onlyIntFilter(TextField field) {
        ChangeListener<String> changeListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.isEmpty()) {
                    try {
                        int i = Integer.parseInt(newValue);
                        if(i < 1 ) {
                            if(i < 0) {
                                field.textProperty().set(i*(-1)+"");
                            }else {
                                if(!field.getStyleClass().contains("error_field")) {
                                    field.getStyleClass().add("error_field");
                                }
                            }
                        }else {
                            if(field.getStyleClass().contains("error_field")) {
                                field.getStyleClass().remove("error_field");
                            }
                        }
                    } catch (Exception e) {
                        field.textProperty().set(oldValue);
                    }
                }else {
                    if(!field.getStyleClass().contains("error_field")) {
                        field.getStyleClass().add("error_field");
                    }
                }
            }
        };

        return changeListener;
    }

    public void setMain(Main main) {

        this.main = main;
    }

    public void setImageData(int width, int height, int color, int ps_width, int ps_height, boolean pc_alpha) {
        imgResTFWidth.setText(width+"");
        imgResTFHeight.setText(height+"");

        //Set RadioButton
        if(color == RenderLogic.COLOR_RGB) {
            imgPCGroup.selectToggle(imgRadioARGB);
        }else{
            imgPCGroup.selectToggle(imgRadioBW);
        }

        imgPSWidth.setText(ps_width+"");
        imgPSHeight.setText(ps_height+"");
        imgPCAlphaCB.setSelected(pc_alpha);

    }

    public void connectGUIElements() {
        main.getImageLogicNote().setLable(imgStaL);
        main.getImageLogicNote().setButton(imgRenderB, imgCancelB);
    }

    }
