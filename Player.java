
/**
 *
 * @author jamiejakov
 */

package hexagon;
import javax.media.opengl.GL2;

public class Player{
    public static final double INNER_HEXAGON_H = 0.08;
    private static final double SIZE = 0.013;
    private static final double PLAYER_OFFSET = 0.023;
    private static final double[] COLOR = new double[] {0.99, 0.39, 0.0 };

    private final double[] pos;
    private int location = 1; // towards which wall is the player facing.
    private double[] realPos = new double[2];
    private double degree;  // angle of player in regards to rotation on Z axis
	
    private double speed;


    public Player(double speed){
        this.speed = speed;
        pos = new double[] {0 , INNER_HEXAGON_H + SIZE + PLAYER_OFFSET};
        realPos[0] = pos[0];
        realPos[1] = pos[1];
        degree = 0;
    }

    public void draw(GL2 gl, double screenRotate){
        float f = (float)(screenRotate + degree);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
	gl.glLoadIdentity();
	gl.glPushMatrix();
	gl.glRotatef(f, 0.0f, 0.0f, 1.0f);
	
        gl.glColor3d(COLOR[0], COLOR[1], COLOR[2]);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2d(pos[0] + SIZE, pos[1] - SIZE);
        gl.glVertex2d(pos[0] - SIZE, pos[1] - SIZE);
        gl.glVertex2d(pos[0], pos[1] + SIZE);
        gl.glEnd();
        
        gl.glPopMatrix();
    }
    public boolean checkIfTouchingWall(Wall wall){
        return (Math.hypot(realPos[0] - wall.getX(), 
                           realPos[1] - wall.getY()) <= (SIZE * 1.9));

    }
    public void changeRealPos(){
        double deg = degree;
	double x = 0, y = 0;
	double h = INNER_HEXAGON_H + SIZE + PLAYER_OFFSET;
	if ((deg >= -30 && deg <= 30) || deg <= -330 || deg >= 330){               // 1
            x = 0;
            y = h;
            location = 1;
	}
	else if ((deg > 270 && deg < 330) || (deg > -90 && deg < -30)){            // 2
            x = h*Math.sqrt(3) / 2;
            y = h / 2;
            location  = 2;
	}
	else if ((deg > 210 && deg < 270) || (deg > -150 && deg < -90)){           // 3
            x = h*Math.sqrt(3) / 2;
            y = -h / 2;
            location = 3;
	}
	else if ((deg >= 150 && deg <= 210) || (deg >= -210 && deg <= -150)){     // 4
            x = 0;
            y = -h;
            location = 4;
	}
	else if ((deg > 90 && deg < 150) || (deg > -270 && deg < -210)){           // 5
            x = -h*Math.sqrt(3) / 2;
            y = -h / 2;
            location = 5;
	}
	else if ((deg > 30 && deg < 90) || (deg > -330 && deg < -270)){            // 6
            x = -h*Math.sqrt(3) / 2;
            y = h / 2;
            location = 6;
	}
	realPos[0] = x;
	realPos[1] = y;
    }
    
    private double fixDegrees(double deg){
	double result = deg;
	if (result >= 360){
		result = deg - 360.0;
	}
	else if (deg <= -360){
		result = deg + 360;
	}
	return result;
    }
       
    
    public double getSize(){
        return SIZE;
    }
    public double getDegree(){
        return degree;
    }
    public double getSpeed(){
        return speed;
    }
    public int getLoc(){
        return location;
    }
    public void setDegree(double degree){
        this.degree = fixDegrees(degree);
        changeRealPos();
    }
    public void setSpeed(double speed){
        this.speed = speed;
    }

}