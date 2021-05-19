package maze;

import java.io.Serializable;

/** The class to describe a Tile object */
public class Tile implements Serializable {
    /** The type of the tile 
     * @see maze.Tile.Type
    */
    private Type type = null;

    /** The constructor initialises the type. Private and
     * exclusively used in Tile.fromChar
     * @see maze.Tile.Type
     * @see maze.Tile.fromChar
     */
    private Tile(Type typeIn){
        this.type = typeIn;
    }

    /** Creates a new Tile object from a character
     * @param c: the character that describes the type
     * @return A new Tile object with the specified type
     * @throws BadMazeFormatException If the character does not 
     * match any predefined type
     * @see maze.Tile.Type
     */
    protected static Tile fromChar(char c) throws BadMazeFormatException{
        switch(c){
            case 'e':
                return new Tile(Type.ENTRANCE);
            case 'x':
                return new Tile(Type.EXIT);
            case '#':
                return new Tile(Type.WALL);
            case '.':
                return new Tile(Type.CORRIDOR);
            default:
                throw new BadMazeFormatException();
        }
    }

    /** Getter for the Tile Type */
    public Type getType(){
        return this.type;
    }

    /** Method that clears whether the tile is navigable.
     * Modified by the UML extension to make tiles act as one-way
     * walls if they have already been visited.
     * @return False if the tile is a wall or has been visited,
     * true otherwise
     * @see maze.Tile.Type
     */
    public boolean isNavigable(){
        return this.getType() != Type.WALL && 
            this.visited == false;
    }
    
    /** Parses the tile to a type-specific String
     * @return The string representation of the tile's type
     * @see maze.Tile.Type
     */
    public String toString(){
        String s = null;

        switch(this.type){
            case ENTRANCE:
                s = "e";
                break;
            case EXIT:
                s = "x";
                break;
            case WALL:
                s = "#";
                break;
            case CORRIDOR:
                s = ".";
                break;
        }

        return s;
    }

    /** The inner enum that specifies possible tile types. 
     * Entrance and exit are unique. All are navigable with 
     * the exception of wall. 
     */
    public enum Type{
        ENTRANCE,
        EXIT,
        CORRIDOR,
        WALL;
    } 

    // UML EXTENSIONS
    
    /** The attribute that specifies whether a tile has been 
     * visited in a RouteFinder 
     */
    private boolean visited = false;

    /** The attribute that specifies whether a RouteFinder has
     * decided that the tile is not on the right path to the
     * exit
     */
    private boolean wrongPath = false;

    /** When a RouteFinder visit()s a tile, it will mark the 
     * tile as visited and retrieve it for further processing
     * @return The visited tile
     */
    public Tile visit(){
        this.visited = true;
        return this;
    }

    /** When a RouteFinder leave()s a tile, it will mark it as 
     * wrong because it did not find a path to the exit by
     * going through it.
     */
    public void leave(){
        this.wrongPath = true;
    }

    /** The getter for Tile.visited attribute. */
    public boolean isVisited(){
        return this.visited;
    }

    /** The getter for Tile.wrongPath attribute */
    public boolean isWrongPath(){
        return this.wrongPath;
    }

    /** Parses a tile char to its corresponding type-specific 
     * image path from the resources folder.
     * @param c: the symbol of the the tile to be parsed
     * @return The string path to the resource that provides
     * a matching image. May not be well formatted for OS other
     * than Windows but I am unable to test for this.
     */
    public static String charToImage(char c){
        String path = Tile.class.getResource("Tile.class").toString();
        path = path.substring(5, path.length() - 19);
        path += "resources/images/";
                
        switch(c){
            case '#':
                path += "wall.jpg";
                break;
            case 'e':
                path += "head.jpg";
                break;
            case 'x':
                path += "exit.jpg";
                break;
            case '.':
                path += "corridor.jpg";
                break;
            case '-':
                path += "wrongpath.jpg";
                break;
            case '*':
                path += "path.jpg";
                break;
            case 'f':
                path += "final.jpg";
                break;
            default:
                path += "non_tile_placeholder.jpg";
                break;
        }

        return path;
    }

}
