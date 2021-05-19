package maze;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/** Class that describes a Maze object */
public class Maze implements Serializable{

    /** Specifies the unique entrance point of the maze */
    private Tile entrance = null;

    /** Specifies the unique exit point of the maze */
    private Tile exit = null;

    /** A 2D List that holds the maze Tiles */
    private List<List<Tile>> tiles = 
        new ArrayList<List<Tile>>();

    /** The constructor is only used for private instantiation */
    private Maze(){ }

    /** Loads a new Maze from the given file. Uses a 
     * java.io.BufferedReader with java.io.FileReader to read 
     * the file and constructs the maze top to bottom, left to 
     * right: line becomes row, character becomes tile.
     * @param path: the path to the relevant file
     * @return Returns the new Maze object
     * @throws maze.InvalidMazeException Specifies wrong input
     * format, e.g. multiple exits or wrong characters
     * @throws IOException If the file is unreadable
     * @see maze.InvalidMazeException
     * @see maze.Tile
     */
    public static Maze fromTxt (String path) 
        throws InvalidMazeException, IOException{
        // initialise the BufferedReader
        BufferedReader breader = new BufferedReader(
            new FileReader(path)
        );
        // if successful, create a new maze
        Maze newMaze = new Maze();

        // and read the file line by line
        String line = breader.readLine();
        while(line != null){
            // create a new row to be populated with each char
            List<Tile> tileRow = new ArrayList<Tile>();
            char[] chars = line.toCharArray();

            for(char c: chars){
                // parse the char to a Tile, then add to row
                Tile newTile = Tile.fromChar(c);
                tileRow.add(newTile);
            }

            // if row length is inconsistent, throw exception
            if(newMaze.tiles.size() > 0 && newMaze.tiles.get(0).size() != tileRow.size())
                throw new RaggedMazeException();

            // if all checks passed, add the row to the matrix
            newMaze.tiles.add(tileRow);

            // move to the next row
            line = breader.readLine();
        }
        breader.close();

        // set entrance and exit points
        // this could have easily been implemented when parsing
        // but the public tests unexpectedly impose some weird
        // constrants on setEntrance and setExit
        for(List<Tile> row: newMaze.getTiles())
            for(Tile t: row){
                if(t.getType() == Tile.Type.ENTRANCE)
                    newMaze.setEntrance(t);
                if(t.getType() == Tile.Type.EXIT)
                    newMaze.setExit(t);
            }

        // if there's no entrance or exit, throw exception
        if(newMaze.getEntrance() == null)
            throw new NoEntranceException();
        if(newMaze.getExit() == null)
            throw new NoExitException();

        return newMaze;
    }

    /** Finds the nearest tile to the given one in the direction
     * specified
     * @param t: the initial tile
     * @param d: the direction in which to look
     * @return The location of the closest tile in direction d
     * @see maze.Tile
     * @see maze.Maze.Direction 
     * @see maze.Maze.Coordinate
     */
    public Tile getAdjacentTile(Tile t, Direction d){
        int dx = 0;
        int dy = 0;

        if(d == Direction.NORTH)
            dy = 1;
        else if(d == Direction.SOUTH)
            dy = -1;
        else if(d == Direction.EAST)
            dx = 1;
        else if(d == Direction.WEST)
            dx = -1;

        Coordinate loc = this.getTileLocation(t);
        loc = new Coordinate(loc.getX()+dx, loc.getY()+dy);

        return this.getTileAtLocation(loc);
    }

    /** Getter for the entrance tile of the maze */
    public Tile getEntrance(){
        return this.entrance;
    }

    /** Getter for the exit tile of the maze */
    public Tile getExit(){
        return this.exit;
    }

    /** Getter for a tile at specified coordinates
     * @param c: the relevant pair of coordinates
     * @return The tile at location (c.x, x.y), or a wall if 
     * the coordinate is out of bounds
     * @see maze.Maze.Coordinate
     * @see maze.Tile
     */
    public Tile getTileAtLocation(Coordinate c){
        if(c.getX() < 0 || c.getY() < 0 || 
            c.getY() >= this.getTiles().size() || 
            c.getX() >= this.getTiles().get(0).size())
            return null;

        // the cartesian-like structure of the maze implies
        // that a tile is not located at tiles[x][y], but rather
        // at tiles[rowCount - y - 1][x]
        return this.getTiles().get(
            this.getTiles().size()-c.getY()-1).get(
                c.getX());
    }

    /** Getter for the coordinates of a given tile
     * @param t: the tile for which to find coordinates
     * @return A Coordinate object with the relevant position or
     * null if the tile is not in the maze
     * @see maze.Maze.Coordinate
     * @see maze.Tile
     */
    public Coordinate getTileLocation(Tile t){
        int y = this.getTiles().size();
        int x = this.getTiles().get(0).size();

        for(int i = 0; i < y; i++)
            for(int j = 0; j < x; j++)
                if(this.getTiles().get(i).get(j).equals(t))
                    // the cartesian-like structure of the maze
                    // means tiles are not located at (x, y)
                    // but rather (rowCount - y - 1, x)
                    return new Coordinate(j, y-i-1);
        
        return null;
    }

    /** Getter for the tile matrix
     * @see maze.Tile
     */
    public List<List<Tile>> getTiles(){
        return this.tiles;
    }

    /** Setter for the maze entrance. The tests require it to 
     * throw IllegalArgumentException, which is odd, considering
     * that it's private and my code cannot pass an illegal
     * argument to it.
     * @param t: The tile to be set as entrance
     * @throws maze.MultipleEntranceException If there already is
     * an entrance
     * @throws IllegalArgumentException If the argument is not a
     * tile in the current maze
    */
    private void setEntrance(Tile t) 
        throws MultipleEntranceException, IllegalArgumentException{
        if(this.getEntrance() != null)
            throw new MultipleEntranceException();
        if(this.getTileLocation(t) == null)
            throw new IllegalArgumentException("Tile not found in maze.");
        this.entrance = t;
    }

    /** Setter for the maze exit. The tests require it to throw
     * IllegalArgumentException, which is odd, considering that
     * it's private and my code cannot pass an illegal argument
     * to it.
     * @param t: The tile to be set as exit
     * @throws maze.MultipleEntranceException If there already
     * is an entrance
     * @throws IllegalArgumentException If the argument is not a
     * tile in the current maze
     */
    private void setExit(Tile t) 
        throws MultipleExitException, IllegalArgumentException{
        if(this.getExit() != null)
            throw new MultipleExitException();
        if(this.getTileLocation(t) == null)
            throw new IllegalArgumentException("Tile not found in maze.");
        this.exit = t;
    }

    /** Method for parsing the maze to string for printing
     * @return A String object with endlines that describes the maze
     * @see maze.Tile
     */
    public String toString(){
        String txt = "";
        for(List<Tile> row: this.getTiles()){
            for(Tile t: row)
                txt += t.toString();
            txt += "\n";
        }

        return txt;
    }

    /** The inner class that describes a pair of coordinates(x, y).
     * Both are 0-indexed; x=column left to right; y=row bottom to 
     * top. That makes the maze similar to a cartesian plane and
     * sets calls to the array to (rowCount - y - 1, x) instead of
     * (x, y)
     */
    public class Coordinate{

        /** The pair of coordinates */
        private int x = 0;
        private int y = 0;

        /** The constructor sets the internal coordinates to those
         * given
         * @param xIn: the column index (0-indexed) (left to right)
         * @param yIn: the row index(0-indexed) (bottom to top)
         */
        public Coordinate(int xIn, int yIn){
            this.x = xIn;
            this.y = yIn;
        }

        /** The getter for the column index */
        public int getX(){
            return this.x;
        }

        /** The getter for the row index */
        public int getY(){
            return this.y;
        }

        /** Method to parse the pair of coordinates to a string
         * @return String formatted as(x, y) where x is 
         * Coordinate.x and y is Coordinate.y
         */
        public String toString(){
            return "(" + this.getX() + ", " + this.getY() + ")";
        }
    }

    /** The inner enum that describes a direction relative to 
     * the tile matrix. NORTH is up, SOUTH is down, EAST is 
     * right and WEST is left 
     */
    public enum Direction{
        NORTH,
        EAST,
        SOUTH,
        WEST;
    }
}