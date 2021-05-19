package maze;

/** Exception that describes a maze with no exit 
 * @see maze.InvalidMazeException
*/
public class NoExitException extends InvalidMazeException {

    /** Exception constructor; calls the constructor of 
     * superclass InvalidMazeException
     * @param path: the path to the relevant file
     */
    NoExitException(){
        super(
            "Unable to create maze. " + 
            "Cannot find maze exit point."
        );
    }
}
