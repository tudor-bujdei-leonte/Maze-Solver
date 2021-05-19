package maze;

/** Exception that describes a maze with inconsistent borders 
 * @see maze.InvalidMazeException
*/
public class RaggedMazeException extends InvalidMazeException {

    /** Exception constructor; calls the constructor of 
     * superclass InvalidMazeException
     * @param path: the path to the relevant file
     */
    RaggedMazeException(){
        super(
            "Unable to create maze. " + 
            "Row lengths not consistent."
        );
    }
}
