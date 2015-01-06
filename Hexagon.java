/**
 *
 * @author jamiejakov
 */
package hexagon;
import java.util.*;
import javax.media.opengl.GL2;

public class Hexagon{
    public static final double INNER_HEXAGON_H = 0.08;
    public static final double STARTING_DISTANCE = 1.5;
    private final LinkedList<Wall> walls = new LinkedList<>();

    public Hexagon(int type, int[] locs, double[] levelColor, double speed){
        if (type == 1){ //0=small, 1=big
            createWalls(locs, type, levelColor, speed);
        }else{
            int[] smalllocs = { 1, 2, 3, 4, 5, 6 };
            createWalls(smalllocs, type, levelColor, speed);
        }
    }

    private void createWalls(int[] locs, int type, double[] levelColor, double speed){
        double[] pos = new double[2];
        double[] dir = new double[2];
        double[] limit = new double[2];
        double[] color = new double[3];
        double offset;
        int direct = 1;
        for (int i = 0; i<locs.length; i++){
            
            switch (locs[i]){
            case 1:
                pos[0] = 0.0;
                pos[1] = 1.0;
                dir[0] = 0.0;
                dir[1] = -2.0;
                
                break;
            case 2:
                pos[0] = Math.sqrt(3.0) / 2;
                pos[1] = 0.5;
                dir[0] = -1.0;
                dir[1] = -1.0;
                break;
            case 3:
                pos[0] = Math.sqrt(3.0) / 2;
                pos[1] = -0.5;
                dir[0] = -1.0;
                dir[1] = 1.0;
                break;
            case 4:
                pos[0] = 0.0;
                pos[1] = -1.0;
                dir[0] = 0.0;
                dir[1] = 2.0;
                break;
            case 5:
                pos[0] = Math.sqrt(3.0) / -2;
                pos[1] = -0.5;
                dir[0] = 1.0;
                dir[1] = 1.0;
                break;
            case 6:
                pos[0] = Math.sqrt(3.0) / -2;
                pos[1] = 0.5;
                dir[0] = 1.0;
                dir[1] = -1.0;
                break;
            default:
                break;
            }
            if (type==1){
                offset = STARTING_DISTANCE;
            }else{
                offset = INNER_HEXAGON_H;
                direct = 0;
                speed = 0;
            }
            if (locs[i]!=0){
                pos[0] = pos[0] * offset;
                pos[1] = pos[1] * offset;
                dir[0] = dir[0] * direct;
                dir[1] = dir[1] * direct;
                color[0] = levelColor[0];
                color[1] = levelColor[1];
                color[2] = levelColor[2];
                limit[0] = pos[0] * INNER_HEXAGON_H / STARTING_DISTANCE;
                limit[1] = pos[1] * INNER_HEXAGON_H / STARTING_DISTANCE;

                walls.add(new Wall(locs[i],pos,dir,color,type,speed, limit));
            }
        }
        
        
        
    }
    public LinkedList<Wall> getWalls(){
        return walls;
    }
    public boolean updatePos(double playerSize){
        for(Wall wall : walls){
            if (wall.updatePos(playerSize)){
               return true;
            }
        }
        return false;
    }
    public void draw(GL2 gl){
        for(Wall wall : walls){ 
            wall.draw(gl);
        }
    }
    public Wall getWallAt(int loc){
        for (Wall wall : walls){
            if (wall.getLoc() == loc){
                return wall;
            }
        }
        return null;
    }

}