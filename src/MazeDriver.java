import java.io.IOException;

import maze.*;
import maze.routing.*;

// This is not implemented. It is mentioned that it is not marked.

public class MazeDriver {
    public static void main(String args[]) {
        try{
            Maze maze = Maze.fromTxt(
                "../resources/mazes/maze2.txt"
            );

            System.out.println(maze.toString());

            // System.out.println((Tile.fromChar('~') == null));
            // System.out.println(maze.toString());
            // Maze.Coordinate c = maze.new Coordinate(5, 0);
            // System.out.println(maze.getTileAtLocation(c).toString());
            // System.out.println(maze.getAdjacentTile(maze.getTileAtLocation(c), Maze.Direction.EAST).toString());
            // System.out.println(maze.getEntrance().toString());
            // System.out.println(maze.getExit().toString());
            // System.out.println(maze.getTileLocation(maze.getEntrance()).toString());
            // System.out.println(maze.getTileLocation(maze.getExit()).toString());
            
            RouteFinder rf = new RouteFinder(maze);
            for(int i = 0; i < 50; i++)
                rf.step();
            System.out.println(rf.toString());

            // rf.save("rf.obj");
            // rf = RouteFinder.load("rf.obj");
            // System.out.println(rf.toString());

            // RouteFinder rf = RouteFinder.load("./resources/routes/invalid/empty.route");
            // System.out.println(rf.toString());

        } catch (InvalidMazeException e){
            System.out.println(e.getMessage());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
