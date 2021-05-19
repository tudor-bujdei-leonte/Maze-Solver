package maze.routing;

import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

import maze.Maze;
import maze.Tile;
import maze.Maze.Direction;

/** The class that describes the process of solving a maze.
 * It will look at every tile connected to the entrance and
 * stop when it lands on the exit. It goes as far as it can in
 * one maze.Maze.Direction and then switches to the next direction.
 * When no direction is left available it will backtrack to the 
 * last tile that can still go in another direction.
 * @see maze.Maze
 * @see maze.Tile
 */
public class RouteFinder implements Serializable {

    /** The maze.Maze that the RouteFinder solves */
    private Maze maze = null;

    /** The attribute that holds the path */
    private Stack<Tile> route = new Stack<Tile>();

    /** The attribute that specifies whether exit has been reached */
    private boolean finished = false;

    /** The constructor of RouteFinder. Initialises the stack with
     * the entrance of the maze.
     * @param m: The Maze object to work with
     * @see maze.Maze
     */
    public RouteFinder(Maze m){
        this.maze = m;
        this.route.add(this.maze.getEntrance());
    }

    /** The getter for the working maze */
    public Maze getMaze(){
        return this.maze;
    }

    /** The getter for the route. Turns the stack into a list */
    public List<Tile> getRoute(){
        return new ArrayList<Tile>(this.route);
    }

    /** The getter for the finished attribute */
    public boolean isFinished(){
        return finished;
    }

    /** Loads a RouteFinder from a serialised object
     * @param path: the filepath to the object
     * @return A new RouteFinder extracted from the file
     * @throws IOException If the file cannot be read
     * @throws ClassNotFoundException If the file is not a
     * RouteFinder serialised object
     */
    public static RouteFinder load(String path) throws IOException, ClassNotFoundException{
        ObjectInputStream oistream = new ObjectInputStream(
            new FileInputStream(path)
        );
        RouteFinder newRF = (RouteFinder)oistream.readObject();
        oistream.close();

        return newRF;
    }

    /** Saves the current RouteFinder in a serialised object format
     * @param path: the filepath at which to save the object
     * @throws IOException If the file cannot be read to
     */
    public void save(String path) throws IOException{
        if(route.isEmpty())
            throw new NoRouteFoundException();

        ObjectOutputStream oostream = new ObjectOutputStream(
            new FileOutputStream(path)
        );

        oostream.writeObject(this);

        oostream.flush();
        oostream.close();
    }

    /** Progresses one step through the maze i.e. either adds
     * one element to the stack or removes one.
     * @return True if the exit is found, false otherwise
     * @throws NoRouteFoundException If the maze is found to be
     * unsolvable. That happens if all tiles that are connected
     * to entrance have been visited and none of them was an
     * exit.
     */
    public boolean step() throws NoRouteFoundException{
        if(route.isEmpty())
            throw new NoRouteFoundException();
            
        Tile curTile = route.peek().visit();
        
        if(curTile.equals(this.getMaze().getExit())){
            this.finished = true;
            return true;
        }

        for(Direction d: Direction.values()){
            Tile t = this.getMaze().getAdjacentTile(curTile, d);
            if(t != null && t.isNavigable()){
                this.route.add(t);
                return false;
            }
        }

        route.pop().leave();
        if(route.isEmpty())
            throw new NoRouteFoundException();
        return false;
    }

    /** Parses the RouteFinder to String format */
    public String toString(){
        String txt = "";

        List<List<Tile>> tiles = this.getMaze().getTiles();

        for(List<Tile> row: tiles){
            for(Tile t: row)
                if(t.isWrongPath()) txt += "-";
                else if(t.isVisited()) txt += "*";
                else txt += t.toString();
            txt += "\n";
        }  

        return txt;
    }
    
    // UML EXTENSION

    /** The getter for the top of the stack. I'm calling it Head
     * because it's visually more like a queue (snake).
     */
    public Tile getHead(){
        return this.route.peek();
    }
}
