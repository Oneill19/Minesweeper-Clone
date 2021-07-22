package mines;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;

public class MinesFX extends Application {
    //create images and sounds instances
    private final Image uncheck = new Image(getClass().getResourceAsStream("unchecked.png"), 48, 48, false, false);
    private final Image checked = new Image(getClass().getResourceAsStream("checked.png"), 48, 48, false, false);
    private final Image mine = new Image(getClass().getResourceAsStream("mine.png"), 48, 48, false, false);
    private final Image flag = new Image(getClass().getResourceAsStream("flag.png"), 48, 48, false, false);
    private final Image one = new Image(getClass().getResourceAsStream("1.png"), 48, 48, false, false);
    private final Image two = new Image(getClass().getResourceAsStream("2.png"), 48, 48, false, false);
    private final Image three = new Image(getClass().getResourceAsStream("3.png"), 48, 48, false, false);
    private final Image four = new Image(getClass().getResourceAsStream("4.png"), 48, 48, false, false);
    private final Image five = new Image(getClass().getResourceAsStream("5.png"), 48, 48, false, false);
    private final Image six = new Image(getClass().getResourceAsStream("6.png"), 48, 48, false, false);
    private final Image seven = new Image(getClass().getResourceAsStream("7.png"), 48, 48, false, false);
    private final Image eight = new Image(getClass().getResourceAsStream("8.png"), 48, 48, false, false);
    private final Image win = new Image(getClass().getResourceAsStream("win.png"), 96, 96, false, false);
    private final Image lose = new Image(getClass().getResourceAsStream("lose.png"), 96, 96, false, false);
    private final Media winSound = new Media(getClass().getResource("winSound.mp3").toString());
    private final Media loseSound = new Media(getClass().getResource("loseSound.mp3").toString());

    private int width = 10;
    private int height = 10;
    private int mines = 10;
    private Mines m;
    private Stage stage;
    private HBox root;
    private GridPane board;
    private MinesController controller;

    //main
    public static void main(String[] args) {
        launch(args);
    }

    //set a scene for the window
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        createRoot();   //create the root box
        createLogic();  //create the logic of the game
        createBoard();  //create the board of the game
        createResetButton();    //create the reset button
        Scene scene = new Scene(root);  //create a new scene
        stage.setTitle("MinesFX");  //set the title of the stage
        stage.setScene(scene);  //set the scene
        stage.show();   //show the  scene
    }

    //create the root box
    private void createRoot() {
        try {
            FXMLLoader loader = new FXMLLoader();   //create a FXMLLoader object
            loader.setLocation(getClass().getResource("minesFX.fxml")); //load the fxml file
            root = loader.load();   //load the fxml file to the vertical box
            controller = loader.getController();    //load the controller
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        root.setPrefSize(795, 620); //set the size of the root
        //set the value on the text fields
        controller.getHeightInput().setText(String.valueOf(height));
        controller.getWidthInput().setText(String.valueOf(width));
        controller.getMinesInput().setText((String.valueOf(mines)));
    }

    //create the logic of the game with Mines class
    private void createLogic() {
        m = new Mines(height, width, mines);
    }

    //create the board of the game
    private void createBoard() {
        //handler for the buttons
        class ButtonPresser implements EventHandler<MouseEvent> {
            private final int x;
            private final int y;

            //constructor
            private ButtonPresser(int x, int y) {
                this.x = x;
                this.y = y;
            }

            //handler for the buttons
            @Override
            public void handle(MouseEvent event) {
                //if it is a primary click
                if (event.getButton() == MouseButton.PRIMARY) {
                    //press the button
                    if (!m.open(x, y)) {
                        m.setShowAll(true); //if the button is a bomb show the board
                        createPopMSG("You lost!", lose, loseSound); //create a pop up message
                    }
                    //if the game ended
                    else if (m.isDone()) {
                        m.setShowAll(true); //show the board
                        createPopMSG("You win!", win, winSound);    //create a pop up message
                    }
                    //if it is a secondary click
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    m.toggleFlag(x, y); //toggle flag
                }
                //update the board
                board.getChildren().clear();
                createBoard();
            }
        }
        board = new GridPane(); //create a new GridPane
        board.setAlignment(Pos.CENTER); //set the alignment of the board
        //create the buttons for the board
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Button b = new Button();    //create a new button
                b.setPrefSize(60, 60); //set the size of the button
                setIcon(b, i, j);   //set the icon of the button
                b.setOnMouseClicked(new ButtonPresser(i, j));   //set the handler for the button
                board.add(b, j, i); //add the button to the board
            }
        }
        BackgroundFill bgFill = new BackgroundFill(Color.LIGHTSLATEGREY, new CornerRadii(0), null); //create a background for the GridPane
        Background bg = new Background(bgFill);
        board.setBackground(bg);
        root.getChildren().add(board);  //add the board to the root
    }

    //create a pop up message
    private void createPopMSG(String title, Image image, Media sound) {
        Stage popUp = new Stage();  //create a new stage
        VBox vbox = new VBox(); //create a new vertical box
        Label label1 = new Label(); //create a new label
        Label label2 = new Label(); //create a new label
        MediaPlayer player = new MediaPlayer(sound);    //create a music player
        vbox.setPrefWidth(250); //set the width of the vertical box
        popUp.setTitle(title);  //set the title of the pop up stage
        label1.setText(title);  //set the text of the first label
        label2.setGraphic(new ImageView(image));    //set the image for the second label
        vbox.getChildren().addAll(label1, label2);  //add the labels to the vertical box
        vbox.setAlignment(Pos.CENTER);  //set the alignment of the vertical box
        Scene scene = new Scene(vbox);  //create a new scene with vertical box
        popUp.setScene(scene);  //set the scene
        player.play();  //play the music
        popUp.show();   //show the stage
    }

    //set icon of the button by the get function of the logics of the game
    private void setIcon(Button b, int x, int y) {
        switch (m.get(x, y)) {
            case ".":
                b.setGraphic(new ImageView(uncheck));
                break;
            case "X":
                b.setGraphic(new ImageView(mine));
                break;
            case " ":
                b.setGraphic(new ImageView(checked));
                break;
            case "F":
                b.setGraphic(new ImageView(flag));
                break;
            case "1":
                b.setGraphic(new ImageView(one));
                break;
            case "2":
                b.setGraphic(new ImageView(two));
                break;
            case "3":
                b.setGraphic(new ImageView(three));
                break;
            case "4":
                b.setGraphic(new ImageView(four));
                break;
            case "5":
                b.setGraphic(new ImageView(five));
                break;
            case "6":
                b.setGraphic(new ImageView(six));
                break;
            case "7":
                b.setGraphic(new ImageView(seven));
                break;
            case "8":
                b.setGraphic(new ImageView(eight));
                break;
        }
    }

    //create the reset button
    private void createResetButton() {
        Button resetButton = controller.getResetButton();   //get the button from the controller
        //handler for the reset button
        class ResetButtonPresser implements EventHandler<MouseEvent> {
            @Override
            public void handle(MouseEvent event) {
                height = Integer.parseInt(controller.getHeightInput().getText());   //set the new height
                width = Integer.parseInt(controller.getWidthInput().getText()); //set the new width
                mines = Integer.parseInt(controller.getMinesInput().getText()); //set the new mines number
                mines = Math.min(mines, height * width);
                start(stage);   //reset the stage
            }
        }
        resetButton.setOnMouseClicked(new ResetButtonPresser());    //set the handler for the reset button
    }
}
