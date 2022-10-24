package com.example.dynamicpixelsrender;
import com.example.dynamicpixelsrender.model.LogicNote;
import com.example.dynamicpixelsrender.model.RenderLogic;
import com.example.dynamicpixelsrender.view.PCCustomDialogController;
import com.example.dynamicpixelsrender.view.RootController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{

    private Stage primaryStage;
    private BorderPane root;

    private RenderLogic renderLogic;

    LogicNote imageLogicNote = new LogicNote();
    LogicNote videoLogicNote = new LogicNote();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        renderLogic = new RenderLogic(true);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Random pixel render");
        this.primaryStage.getIcons().add(new Image("file:resources/images/icon_32.png"));

        System.out.println("Start");
        long start = System.currentTimeMillis();

        initRootLayout();

        long end = System.currentTimeMillis() - start;
        System.out.println("Time: "+ end/1000f +"s");
        System.out.println("Done");
    }

    private void initRootLayout(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Root.fxml"));
            root = (BorderPane) loader.load();

            Scene screen = new Scene(root);
            primaryStage.setScene(screen);

            RootController rootController = loader.getController();
            rootController.setMain(this);
            rootController.setImageData(renderLogic.getImageWidth(), renderLogic.getImageHeight(), renderLogic.getImageColor(), renderLogic.getImagePixelWidth(), renderLogic.getImagePixelHeight(), renderLogic.isImageAlpha());
            rootController.connectGUIElements();

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent event) {
                    System.out.println("EXIT");
                    imageLogicNote.setCancel(true);
                    videoLogicNote.setCancel(true);
                }
            });

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LogicNote getImageLogicNote() {
        return imageLogicNote;
    }

    public LogicNote getVideoLogicNote() {
        return videoLogicNote;
    }

    public void setImageLogicNote(LogicNote imageLogicNote) {
        this.imageLogicNote = imageLogicNote;
    }


    public RenderLogic getRenderLogic() {
        return renderLogic;
    }

    public void renderImage(int width, int height, int color, int p_width, int p_height, boolean alpha) {
        renderLogic.setImageWidth(width);
        renderLogic.setImageHeight(height);
        renderLogic.setImageColor(color);
        renderLogic.setImagePixelWidth(p_width);
        renderLogic.setImagePixelHeight(p_height);
        renderLogic.setImageAlpha(alpha);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                renderLogic.createImage(imageLogicNote);
                System.gc();
            }
        });
        thread.start();
    }

    public void showPCCustomDialog(boolean isImage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/PCCustomDialog.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();

            Stage dialogStage = new Stage();
            if(isImage) {
                dialogStage.setTitle("Image custom pixel color");
            }
            dialogStage.initModality(Modality.NONE);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(borderPane);
            dialogStage.setScene(scene);

            PCCustomDialogController pccdController = loader.getController();

            if(isImage) {
                pccdController.setTableList(renderLogic.getImageCustomPC());
            }


            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


