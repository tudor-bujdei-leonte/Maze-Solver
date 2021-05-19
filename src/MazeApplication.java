import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContentDisplay;
import javafx.scene.text.TextAlignment;
import javafx.scene.input.KeyCode;

import maze.visualisation.VisualMaze;
import maze.*;
import maze.routing.*;

/** The body of the maze solver application */
public class MazeApplication extends Application {

    /** The Stage of the application */
    private Stage stage = null;

    /** The main Scene of the application */
    private Scene main = null;

    /** The secondary Scene of the application. Used for inputs */
    private Scene input = null; 

    /** The attribute that specifies the type of the input */
    private RequestType inputType = null;

    /** The VisualMaze object to be displayed */
    private VisualMaze vMaze = null;

    /** The VBox that contains the output of VisualMaze */
    private VBox tileBox = null;

    /** The attribute that specifies the message to be displayed
     * on the main scene
     */
    private Label mainMessage = null;

    /** The attribute that specifies the message to be displayed
     * on the secondary scene
     */
    private Label inputMessage = null;

    /** The CSS style to be applied to all buttons */
    private static String buttonStyle = 
        "-fx-font-size: 2em; " + 
        "-fx-background-color: #b8bc3c; " + 
        "-fx-border-color: #c4bd52";

    /** The start point of a JavaFX application */
    @Override
    public void start(Stage stage){
        // set the stage
        this.stage = stage;

        // set the scenes
        mainScene();
        inputScene();

        // set the stage attributes
        stage.setTitle("Maze solver");
        stage.setWidth(VisualMaze.DISPLAY_WIDTH);
        stage.setHeight(VisualMaze.DISPLAY_HEIGHT + 200);
        stage.setScene(main);

        // display the app
        stage.show();
    }

    /** The initialiser for the main scene of the app */
    private void mainScene(){
        // create maze load and route load/save buttons
        Button loadMazeButton = new Button("Load Maze");
        loadMazeButton.setStyle(buttonStyle);
        loadMazeButton.setMinHeight(50);
        loadMazeButton.setMinWidth(170);
        Button loadRouteButton = new Button("Load Route");
        loadRouteButton.setStyle(buttonStyle);
        loadRouteButton.setMinHeight(50);
        loadRouteButton.setMinWidth(170);
        Button saveRouteButton = new Button("Save Route");
        saveRouteButton.setStyle(buttonStyle);
        saveRouteButton.setMinHeight(50);
        saveRouteButton.setMinWidth(170);

        // organise them in a HBox
        HBox routeBox = new HBox();
        routeBox.setAlignment(Pos.CENTER);
        routeBox.setSpacing(10);
        routeBox.setPadding(new Insets(10.0));
        routeBox.getChildren().addAll(
            loadMazeButton, loadRouteButton, saveRouteButton
        );

        // create maze display and display it in a VBox
        vMaze = null;
        tileBox = new VBox();
        tileBox.setAlignment(Pos.CENTER);
        tileBox.setBackground(new Background(
            new BackgroundFill(
                Color.rgb(176,188,60),
                CornerRadii.EMPTY,
                Insets.EMPTY
            )
        ));

        // which further goes into a HBox
        // this creates two layers of background: the interior
        // of the maze and the exterior to the left and right
        HBox tileBackground = new HBox();
        tileBackground.setAlignment(Pos.CENTER);
        tileBackground.setMinHeight(VisualMaze.DISPLAY_HEIGHT);
        tileBackground.setMinWidth(VisualMaze.DISPLAY_WIDTH);
        tileBackground.getChildren().add(tileBox);

        // create step button and label for messages
        Button stepButton = new Button("Next Step");
        stepButton.setMinHeight(50);
        stepButton.setMinWidth(80);
        stepButton.setStyle(buttonStyle);
        mainMessage = new Label();
        mainMessage.setFont(new Font("Arial", 30));
        mainMessage.setTextFill(Color.web("#c4bd52"));
        mainMessage.setContentDisplay(ContentDisplay.CENTER);

        // and organise them on a borderpane
        BorderPane stepBox = new BorderPane();
        stepBox.setPadding(new Insets(10.0));
        stepBox.setRight(stepButton);
        stepBox.setLeft(mainMessage);

        // load all containers on a root container
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(
            new BackgroundFill(
                Color.rgb(80,76,76),
                CornerRadii.EMPTY,
                Insets.EMPTY
            )
        ));
        root.getChildren().addAll(routeBox, tileBackground, stepBox);
        
        // set the main scene
        main = new Scene(root);

        // set the button events
        stepButton.setOnAction(e->{nextStep();});
        loadMazeButton.setOnAction(e->{requestInput(RequestType.MAZE);});
        loadRouteButton.setOnAction(e->{requestInput(RequestType.LROUTE);});
        saveRouteButton.setOnAction(e->{requestInput(RequestType.SROUTE);});
    }

    /** The initialiser for the secondary scene of the app */
    private void inputScene(){
        // create the label for messages
        inputMessage = new Label("Input path below:");
        inputMessage.setFont(new Font("Arial", 30));
        inputMessage.setTextFill(Color.web("#c4bd52"));
        inputMessage.setTextAlignment(TextAlignment.CENTER);
        inputMessage.setContentDisplay(ContentDisplay.CENTER);
        inputMessage.setWrapText(true);
        inputMessage.setMaxWidth(VisualMaze.DISPLAY_WIDTH * 3/4);

        // and display it on a HBox
        HBox errorBox = new HBox();
        errorBox.setAlignment(Pos.CENTER);
        errorBox.setPadding(new Insets(10.0));
        errorBox.getChildren().add(inputMessage);

        // create a textfield for inputs
        TextArea textField = new TextArea();
        textField.setFont(new Font("Arial", 25));
        textField.setMaxHeight(200);
        textField.setMaxWidth(500);
        textField.setWrapText(true);

        // and display it on a HBOX
        HBox textBox = new HBox();
        textBox.setAlignment(Pos.CENTER);
        textBox.getChildren().add(textField);

        // create the confirm and return buttons
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle(buttonStyle);
        cancelButton.setMinHeight(30);
        cancelButton.setMinWidth(150);
        Button OKButton = new Button("OK");
        OKButton.setStyle(buttonStyle);
        OKButton.setMinHeight(30);
        OKButton.setMinWidth(150);

        // and organise them on a HBox
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(10.0));
        buttonBox.getChildren().addAll(cancelButton, OKButton);

        // display all containers on a root
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(
            new BackgroundFill(
                Color.rgb(80,76,76),
                CornerRadii.EMPTY,
                Insets.EMPTY
            )
        ));
        root.getChildren().addAll(errorBox, textBox, buttonBox);

        // initialise the scene
        input = new Scene(root);

        // add the button events
        // and an additional event for submitting on pressing
        // enter while the text field is focused
        cancelButton.setOnAction(e->{cancelInput(textField);});
        OKButton.setOnAction(e->{submitInput(textField.getText().strip());});
        textField.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER)
                submitInput(textField.getText().strip());
        });
    }

    /** Creates an input request by setting the input type and
     * switching to the input scene
     * @param type: the type of input request
     */
    private void requestInput(RequestType type){
        inputType = type;
        inputMessage.setText("Input path below");
        stage.setScene(input);
    }

    /** Loads a maze to the application
     * @param path: the path to the maze file
     */
    private void loadMaze(String path){
        try{
            Maze m = Maze.fromTxt(path);
            VisualMaze newVMaze = new VisualMaze(m);
            if(tileBox.getChildren().size() > 0)
                tileBox.getChildren().remove(vMaze.getTiles());
            vMaze = newVMaze;
            tileBox.getChildren().add(vMaze.getTiles());

            mainMessage.setText("Successfully loaded!");
            stage.setScene(main);
        } catch (IOException e){
            inputMessage.setText("Unable to read file.");
        } catch(InvalidMazeException e){
            inputMessage.setText(e.getMessage());
        }
    }

    /** Saves the RouteFinder to a serialised object
     * @param path: the path to the savefile
     */
    private void saveRoute(String path){
        if(vMaze == null)
            inputMessage.setText("You must load a maze first!");
        else try{
            vMaze.getRouteFinder().save(path);

            mainMessage.setText("Successfully saved!");
            stage.setScene(main);
        } catch (IOException e){
            inputMessage.setText("Unable to read file.");
        }
    }

    /** Loads a RouteFinder to the application
     * @param path: the path to the RouteFinder serialised object
     */
    private void loadRoute(String path){
        try{
            RouteFinder rf = RouteFinder.load(path);
            VisualMaze newVMaze = new VisualMaze(rf);
            if(tileBox.getChildren().size() > 0)
                tileBox.getChildren().remove(vMaze.getTiles());
            vMaze = newVMaze;
            tileBox.getChildren().add(vMaze.getTiles());

            mainMessage.setText("Successfully loaded!");
            stage.setScene(main);
        } catch (IOException e){
            inputMessage.setText("Unable to read file.");
        } catch(ClassNotFoundException e){
            inputMessage.setText("Cannot find specified class.");
        }
    }

    /** Proceeds one step into the maze, then reloads the display
     */
    private void nextStep(){
        if(vMaze == null)
            mainMessage.setText("You must load a maze first!");
        else try{
            vMaze.step();
            tileBox.getChildren().clear();
            tileBox.getChildren().add(vMaze.getTiles());

            mainMessage.setText("You took one step.");
            if(vMaze.getRouteFinder().isFinished())
                mainMessage.setText("You reached the end!");
        } catch (NoRouteFoundException e){
            mainMessage.setText(e.getMessage());
        }
    }

    /** Saves the path input for further parsing */
    private void submitInput(String txt){
        switch(inputType){
            case MAZE:
                loadMaze(txt);
                break;
            case LROUTE:
                loadRoute(txt);
                break;
            case SROUTE:
                saveRoute(txt);
                break;
        }
    }

    /** Returns to the main scene without submitting a path */
    private void cancelInput(TextArea textField){
        mainMessage.setText("Canceled operation.");
        stage.setScene(main);
    }

    /** The inner enum that specifies possible user request types.
     * MAZE loads a Maze from text, LROUTE loads a RouteFinder
     * from an object file, SROUTE saves a RouteFinder to an object
     * file.
     */
    private enum RequestType{
        MAZE,
        LROUTE,
        SROUTE;
    }

    public static void main(String args[]){
        launch(args);
    }
}
