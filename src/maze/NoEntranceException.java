package maze;

/** Exception that describes a maze with no entrance 
 * @see maze.InvalidMazeException
*/
public class NoEntranceException extends InvalidMazeException {

    /** Exception constructor; calls the constructor of 
     * superclass InvalidMazeException
     * @param path: the path to the relevant file
     */
    NoEntranceException(){
        super(
            "Unable to create maze. " + 
            "Cannot find maze entrance point."
        );
    }
}
