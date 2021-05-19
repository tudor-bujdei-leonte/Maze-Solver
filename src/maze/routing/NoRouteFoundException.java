package maze.routing;

/** Exception that describes a maze that cannot be solved. Will 
 * be thrown if a RouteFinder rules out all paths from the 
 * entrance.
 * @see maze.routing.RouteFinder
*/
public class NoRouteFoundException extends RuntimeException {

    /** Exception constructor; calls the constructor of 
     * superclass RuntimeException
     */
    NoRouteFoundException(){
        super("Maze is unsolvable.");
    }
}
