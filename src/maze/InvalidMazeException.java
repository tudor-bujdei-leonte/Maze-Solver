package maze;

/** Superclass for Maze runtime exceptions 
 * @see maze.InvalidMazeException
*/
public class InvalidMazeException extends RuntimeException {

    /** Exception constructor; calls the constructor of 
     * superclass RuntimeException
     * @param errMessage: the exception message to be printed
     */
    InvalidMazeException(String errMessage){
        super(errMessage);
    }
}
