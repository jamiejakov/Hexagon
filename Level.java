/**
 *
 * @author jamiejakov
 */
package hexagon;
import java.util.*;
import javax.media.opengl.GL2;


public class Level {
    public static final long WIN_CONDITION = 60000; // 60 seconds = 60,000 miliseconds
    public static final int SCREEN_ROTATION_LIMIT = 150;

    
    private boolean gameover = false;
    private int hexagonTimer = 0;   // when new hexagon wall should appear
    private float screenRotate = 0; //rotation degrees
    private double screenRotateDirection = 1; // 1 - rotate left, -1 rotate right    
    
    private double wallSpeed = 0.007;
    private double screenRotation = 0.6; // speed of rotation (this * direction) added to Rotate
    private int hexagonAppearTime = 50; // after how much time a new set of walls should appear
    
    private final double[] levelColor = new double[3]; //color of the level background
    private final Player player;
    private LinkedList<Hexagon> hexagons = new LinkedList<>();
    private final Hexagon smallHex;
    private final Date startTime;
    private Date rotationTimer = null;

    
    public Level(int dificulty, double[] levelColor){
        double playerSpeed = 6.2;
        this.levelColor[0]=levelColor[0];
        this.levelColor[1]=levelColor[1];
        this.levelColor[2]=levelColor[2];
	screenRotateDirection = (int)(Math.random() * (1-0))+1;
	if (screenRotateDirection == 0){
		screenRotateDirection = -1;
	}
        switch (dificulty){
        case 1:
            levelColor[0] = 0.9;
            levelColor[1] = 0.0;
            levelColor[2] = 0.0;
            break;
        case 2:
            levelColor[0] = 0.0;
            levelColor[1] = 0.0;
            levelColor[2] = 0.9;
            wallSpeed *= dificulty*0.6;
            playerSpeed *= dificulty*0.5;
            screenRotation *= dificulty*0.5;
            hexagonAppearTime = 40;
            break;
        case 3:
            levelColor[0] = 0.0;
            levelColor[1] = 0.9;
            levelColor[2] = 0.0;
            wallSpeed *= dificulty*0.6;
            playerSpeed *= dificulty*0.5;
            screenRotation *= dificulty*0.5;
            hexagonAppearTime = 30;
            break;
        default:
            break;
	}
        
        int[] smallLocs = new int[] { 1,2,3,4,5,6 }; // make small Hexagon
        this.smallHex = new Hexagon(0, smallLocs, levelColor, 0); 
       
        addNewHexagon(); // first big hexagon
        player = new Player(playerSpeed);
        startTime = new Date();
        rotationTimer = new Date();
    }   
    
    private boolean checkArray(int num, int[] array) {
        boolean found = false;
        for (int i = 0; !found && (i < array.length); i++) {
            found = (array[i] == num);
        }
        return found;
    }
    
    private void addNewHexagon(){
        int[] locs = new int[6];
        int number = (int)(Math.random() * (2-0)) + 1; //max=2; min=0(technically 1 but this works);
        int[] remove = new int[number];
        for (int i = 0; i<number; i++){
            remove[i] = (int)(Math.random() * (6-1)) + 1;// max=6; min=1;
        }
        int j =0;
        for (int i = 1; i<=6; i++){
            if(checkArray(i, remove)){
                locs[i-1] = 0;
                j++;
            }else{
                locs[i-1]=i;
            }
        }
        Hexagon hex = new Hexagon(1, locs, levelColor, wallSpeed);
        hexagons.add(hex);
    }
    
    
    public void updatePos(Input input){
        if (!hexagons.isEmpty()){
            for (Hexagon hexagon : hexagons) {
                if (hexagon.updatePos(player.getSize())){
                    hexagons.remove(hexagon);
                    return;
                }
            }    
            if (hexagonTimer >= hexagonAppearTime){
                addNewHexagon();
                hexagonTimer = 0;
            }
            if (input.leftPress()){
                player.setDegree(player.getDegree() + player.getSpeed());
            }
            if (input.rightPress()){
                player.setDegree(player.getDegree() - player.getSpeed());
            }
            hexagonTimer++;
            if (!hexagons.isEmpty()){
                for (Hexagon hexagon :hexagons){
                    Wall w = hexagon.getWallAt(player.getLoc());
                    if (w!=null){
                        gameover = player.checkIfTouchingWall(w);
                        return;
                    }  
                }
                 
            }
        }
        
    }
    
    
    
    public void draw(GL2 gl){
        if (isGameOver()){
            System.out.println("GAME OVER");
            //System.exit(0);
	}else if (isGameWon()){
            System.out.println("WIN");
	}else{
            gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            if (screenRotate >= SCREEN_ROTATION_LIMIT ||
                screenRotate <= -SCREEN_ROTATION_LIMIT){
                screenRotateDirection *= -1;
            }
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glPushMatrix();
            
            Date stopTime = new Date();
            long timediff = (stopTime.getTime() - rotationTimer.getTime());
            if (timediff>=15){
                screenRotate += screenRotation*screenRotateDirection;
                rotationTimer = new Date();
            }
            gl.glRotatef(screenRotate, 0.0f, 0.0f, 1.0f);

            if (smallHex!=null){
                smallHex.draw(gl);
            }
            if (!hexagons.isEmpty()){
                for (Hexagon hexagon : hexagons){
                    hexagon.draw(gl);
                }
            }
            player.draw(gl, screenRotate);
            gl.glPopMatrix();
        }

    }
    
    public boolean isGameOver(){
        return gameover;
    }
    public boolean isGameWon(){
        return getScore() >= WIN_CONDITION;
    }
    public double getScore(){
        Date stopTime = new Date();
        long timediff = (stopTime.getTime() - startTime.getTime());
        return timediff;
    }
}
