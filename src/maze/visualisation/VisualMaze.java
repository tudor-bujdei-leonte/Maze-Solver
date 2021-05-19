// UML EXTENSION

package maze.visualisation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import maze.*;
import maze.Maze.Coordinate;
import maze.Tile.Type;
import maze.routing.*;

/** A bridging class between RouteFinder and JavaFX 
 * @see maze.Maze
 * @see maze.routing.RouteFinder
*/
public class VisualMaze{

    /** A constant that defines the width of the application */
    public static int DISPLAY_WIDTH = 600;

    /** A constant that defines the height of the application */
    public static int DISPLAY_HEIGHT = 400;

    /** The Maze to be displayed */
    private Maze maze = null;

    /** The RouteFinder to be displayed */
    private RouteFinder rf = null;

    /** A matrix of images that describe the current state of
     * the RouteFinder
     */
    private List<List<ImageView>> tileImages = null;

    /** A group that holds all tileImages to be kept together */
    private Group tiles = null;

    /** The maze constructor for VisualMaze
     * 
     * @param m: the Maze object to be visualised
     * @see maze.Maze
     */
    public VisualMaze(Maze m){
        this.maze = m;
        this.rf = new RouteFinder(m);
        try { this.createTiles(); }
        catch (FileNotFoundException e){
            System.out.println("Fatal: Cannot retrieve images.");
        }
    }

    /** The RouteFinder constructor for VisualMaze
     * 
     * @param rf: the RouteFinder object to be visualised i.e.
     * a maze with progress
     */
    public VisualMaze(RouteFinder rf){
        this.rf = rf;
        this.maze = rf.getMaze();
        try { this.createTiles(); }
        catch (FileNotFoundException e){
            System.out.println("Fatal: Cannot retrieve images.");
        }
    }

    /** The method that refreshes the group and the tiles
     * 
     * @throws FileNotFoundException If it cannot access
     * resources/images/*.jpg
     */
    private void createTiles() throws FileNotFoundException{
        // the image height is a rounded division between
        // the display height and the row count
        int height = DISPLAY_HEIGHT / (this.getMaze().getTiles().size());
        height = (height+4)/5*5;

        // the image width is a rounded division between
        // the display width and the row length
        int width = DISPLAY_WIDTH / (this.getMaze().getTiles().get(0).size()+1);
        width = (width+4)/5*5;
        double x = 0;
        double y = 0;

        // reset the images and read the string representation
        // of the RouteFinder line by line
        this.tileImages = new ArrayList<List<ImageView>>();
        String[] lines = this.getRouteFinder().toString().split("\n");
        for(String line: lines){
            List<ImageView> tileRow = new ArrayList<ImageView>();
            for(char c: line.toCharArray()){
                // then for each char create an image corresponding
                // to the type
                tileRow.add(imageViewFromChar(c, x, y, width, height));
                x = (x + 1) % this.getMaze().getTiles().get(0).size();
            }
            this.tileImages.add(tileRow);
            y += 1;
        }

        // set the head image to be the avatar if on the path or
        // the open chest if at the end
        Coordinate c = this.getMaze().getTileLocation(this.rf.getHead());
        int xh = this.getMaze().getTiles().size() - c.getY() - 1;
        int yh = c.getX();
        char ch = this.rf.getHead().getType() == Type.EXIT? 'f' : 'e';

        ImageView headImage = imageViewFromChar(ch, yh, xh, width, height);
        tileImages.get(xh).set(yh, headImage);

        // and group the images up for easy display
        tiles = new Group();

        for(List<ImageView> imageRow: this.tileImages)
            for(ImageView imgv: imageRow){
                this.tiles.getChildren().add(imgv);
            }
        }

    /** Parses a JavaFX ImageView from the given character
     * @param c: the character representation of the tile
     * @param x: the distance from the left of the group
     * @param y: the distance from the top of the group
     * @param width: the width of the resulting image
     * @param height: the height of the resulting image
     * @return a new ImageView object with the specified parameters
     * @throws FileNotFoundException If the application cannot
     * access resources/images/*.jpg
     */
    private ImageView imageViewFromChar(char c, double x, double y, int width, int height)
        throws FileNotFoundException{
        String path = Tile.charToImage(c);
        FileInputStream fistream = new FileInputStream(path);
        Image img = new Image(new FileInputStream(path));
        ImageView imgv = new ImageView(img);

        imgv.setFitWidth(width);
        imgv.setFitHeight(height);
        imgv.setX((x * width));
        imgv.setY((y * height));

        return imgv;
    }

    /** Moves one step through the maze and updates the images.
     * If along the path an image cannot be resolved the
     * application meets a fatal error and it cannot proceed.
     * This can only happen during abnormal loading. 
     * @throws NoRouteFoundException If the maze is unsolvable
     */
    public void step() throws NoRouteFoundException{
        try{
            this.getRouteFinder().step();
            this.createTiles();
        } catch(FileNotFoundException e){
            System.out.println("Fatal: Cannot retrieve images.");
        }
    }

    /** The getter for the tile group */
    public Group getTiles(){
        return this.tiles;
    }

    /** The getter for the maze */
    private Maze getMaze(){
        return this.maze;
    }

    /** The getter for the routefinder */
    public RouteFinder getRouteFinder(){
        return this.rf;
    }
}