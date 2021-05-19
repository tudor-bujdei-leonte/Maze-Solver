package maze;

/** Exception that describes a maze with more than one entrance 
 * @see maze.InvalidMazeException
*/
public class MultipleEntranceException extends InvalidMazeException {

    /** Exception constructor; calls the constructor of 
     * superclass InvalidMazeException
     */
    MultipleEntranceException(){
        super(
            "Unable to create maze. " + 
            "Found multiple maze entrance points."
        );
    }
}
