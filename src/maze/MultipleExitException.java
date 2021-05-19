package maze;

/** Exception that describes a maze with more than one exit 
 * @see maze.InvalidMazeException
*/
public class MultipleExitException extends InvalidMazeException {

    /** Exception constructor; calls the constructor of 
     * superclass InvalidMazeException
     * @param path: the path to the relevant file
     */
    MultipleExitException(){
        super(
            "Unable to create maze. " + 
            "Found multiple maze exit points."
        );
    }
}
