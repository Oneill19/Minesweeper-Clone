package mines;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MinesController {
    @FXML
    private Button resetButton; //reset button

    @FXML
    private TextField widthInput;   //text field for the width input

    @FXML
    private TextField heightInput;  //text field for the height input

    @FXML
    private TextField minesInput;   //text field for the mines input

    public TextField getWidthInput() {
        return widthInput;
    }   //return the width input

    public TextField getHeightInput() {
        return heightInput;
    }   //return the height input

    public TextField getMinesInput() {
        return minesInput;
    }   //return the mines input

    public Button getResetButton() {
        return resetButton;
    }   //return the reset button
}
