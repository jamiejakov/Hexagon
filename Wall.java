
/**
 *
 * @author jamiejakov
 */

package hexagon;
import javax.media.opengl.GL2;

public class Wall{
	
    private double x, y; // center of each wall;
    private final double limitX, limitY;
    private final double directX, directY;
    private final double[] color = new double[3];
    private double size = 0;    // a of a hexagon (side of a single equaliteral triangle)
    private double h = 0;       // the height of the equliteral triangle (distance between center and point 0,0);
    private double l = 0;       // the distance between the center of the side and the points on the X axis
    private double h2 = 0;      // the distance between the center of the side and the points on the Y axis
    private final int location;   // top=1, top-right=2, bottom-right=3, bottom=4, bottom-left=5, top-left=6
    private final int type;       // small or large (0 = small, 1 = large)
    private double speed = 0;   // speed of the walls closing in on the center
    private int speedInc = 0;   // how much the speed is increased when coming close to the center


    public Wall(int location, double pos[], double direct[], double color[], int type,double speed, double[] limit){
        this.location = location;
        this.x = pos[0];
        this.y = pos[1];
        this.directX = direct[0];
        this.directY = direct[1];
        this.color[0] = color[0];
        this.color[1] = color[1];
        this.color[2] = color[2];
        this.type = type;
        speedInc = 0;
        this.speed = speed;
        this.limitX = limit[0];
        this.limitY = limit[1];
        calculateParam();
        
    }

    private void calculateParam(){

        h = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        //if (type == 1){
            size = h*(2 * Math.sqrt(3.0) / 3);
        //}

        switch (location){
        case 1:
        case 4:
            l = size / 2;
            h2 = 0;
            break;
        case 2:
        case 3:
        case 5:
        case 6:
            l = size / 4;
            h2 = size*(Math.sqrt(3.0) / 4);
            break;
        default:
            break;
        }
    }

    private double convertToPositive(double d){
        double result = d;
        if (result <= 0){
            result *= -1;
        }
        return result;
    }

    public boolean updatePos(double playerSize){
        if (type==1){
            if (Math.sqrt(Math.pow(0.0 - x, 2.0) + Math.pow(0.0 - y, 2.0)) <= playerSize * 15 && speedInc != 1){
                speed *= 1.5;
                speedInc = 1;
            }
            x += directX * speed;
            y += directY * speed*(Math.sqrt(3.0) / 3);
            calculateParam();
            return (convertToPositive(x) <= convertToPositive(limitX) &&
                    convertToPositive(y) <= convertToPositive(limitY) &&
                    type != 0); // TRUE = Delete this wall; FALSE = Don't delete wall   
        }
        return false;   
        
    }




    private int angle(int loc,int startEnd){
        int result;
        switch (loc){
            case 2:
            case 5:
                result = -1;
                break;
            case 3:
            case 6:
                result = +1;
                break;
            default:
                result = 0;
        }
        
        return result*startEnd;
    }


    public void draw(GL2 gl){	
        final double[] startPos = new double[2];
        final double[] endPos = new double[2];
        startPos[0] = x - l;
        endPos[0] = x + l;
        startPos[1] = y + h2 * angle(location, -1);
        endPos[1] = y + h2 * angle(location, 1);
        
        
        gl.glColor3d(color[0], color[1], color[2]);
        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glVertex2d(startPos[0], startPos[1]);
        gl.glVertex2d(endPos[0], endPos[1]);
        gl.glEnd();
    }
    
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public int getLoc(){
        return location;
    }
    
}