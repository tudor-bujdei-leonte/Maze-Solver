// UML EXTENSION

package maze;

/** Exception that describes a maze with unexpected characters. 
 * This is not in the original UML diagram but I find it cleaner
 * to have it as a separate exception. 
 * @see maze.InvalidMazeException
*/
public class BadMazeFormatException extends InvalidMazeException {

    /** Exception constructor; calls the constructor of 
     * superclass InvalidMazeException
     * @param path: the path to the relevant file
     */
    BadMazeFormatException(){
        super(
            "Unable to create maze. " + 
            "Invalid characters appear in the input."
        );
    }
}
