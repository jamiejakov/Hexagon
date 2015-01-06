/**
 *
 * @author jamiejakov
 */
package hexagon;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import java.applet.*;
import java.util.*;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

public class Game extends Applet implements GLEventListener, Runnable {
    private static final double version = 1.1;
    private int menu = 0; //different screens: 0-Welcome, 1-LevelSelect, 2-PlayTheGame, 3-GameOver, 4-Win
    private Level level = null;
    private Input input;
    private int dificulty = 1;
    private double score = 0.0;
    private final String[] difString = new String[] {" ","1: NORMAL", "2: HARD", "3: EXTREME"};
    private final double[] levelColor = new double[3];
    private TextBox escBox = null;
    private TextBox spaceBox = null;
    private TextBox timerBox = null;
    private Date updateTimer = null;
    
    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        final GLUT glut = new GLUT();
        gl.glClear (GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );   
        gl.glLoadIdentity();                  // Reset The View
        
        switch(menu){
            case 0: 
                showWelcomeScreen(gl, glut);
                break;
            case 1:
                showLevelSelectScreen(gl, glut);
                break;
            case 2: 
                showGameScreen(gl, glut);
                break;
            case 3: 
                showGameOverScreen(gl, glut);
                break;
            case 4:
                showGameWonScreen(gl, glut);
                break;
            default:
                break;   
        }
    }
    
    private void showWelcomeScreen(GL2 gl, GLUT glut){  // Menu = 0
        if (input.escRel()){
            System.exit(0);
        }else if (input.spaceRel()){
            input.setSpaceRel();
            menu++;
            escBox = new TextBox(menu);
            spaceBox.change(menu, levelColor);
        }else{
            spaceBox.draw(gl, glut);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glColor3d(1.0, 1.0, 1.0);
            gl.glRasterPos3f(-0.3f, 0.6f, 0.0f);
            String text = "Welcome to HEXAGON v" + version;
            glut.glutBitmapString(GLUT.BITMAP_9_BY_15, text);
            gl.glRasterPos3f(-0.5f, 0.1f, 0.0f);
            text = "Clear the level by surviving 60 SECONDS!";
            glut.glutBitmapString(GLUT.BITMAP_9_BY_15, text);
            gl.glRasterPos3f(-0.6f, 0.0f, 0f);
            text = "Use Left and Right keys to move, avoid the walls";
            glut.glutBitmapString(GLUT.BITMAP_9_BY_15, text);
        }
    }
    private void showLevelSelectScreen(GL2 gl, GLUT glut){  //Menu = 1 
        if (input.escRel()){
            input.setEscRel();
            menu = 0; //Welcome
            escBox = null;
            spaceBox.change(menu, levelColor);
        }else if (input.spaceRel()){
            input.setSpaceRel();
            level = new Level(dificulty, levelColor);
            menu++;
            escBox.change(menu, levelColor);
            escBox.draw(gl, glut);
            spaceBox = null;
            timerBox = new TextBox(level.getScore());
            updateTimer = new Date();
        }else if (input.LRel()){
            if (dificulty==1){
                dificulty = 3;
            }else{
                dificulty--;
            }
            input.setLRel();
            setLevelColor(dificulty);
            spaceBox.change(menu, levelColor);
            escBox.draw(gl, glut);
        }else if (input.RRel()){
            if (dificulty==3){
                dificulty = 1;
            }else{
                dificulty++;
            }
            input.setRRel();
            setLevelColor(dificulty);
            spaceBox.change(menu, levelColor);
            escBox.draw(gl, glut);
        }else{
            spaceBox.draw(gl, glut);
            escBox.draw(gl, glut);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glColor3d(levelColor[0], levelColor[1], levelColor[2]);
            switch (dificulty){
                case 1:
                    levelColor[0] = 0.9;
                    levelColor[1] = 0.0;
                    levelColor[2] = 0.0;
                    gl.glRasterPos3f(-0.10f, 0.4f, 0f);
                    break;
                case 2:
                    levelColor[0] = 0.0;
                    levelColor[1] = 0.0;
                    levelColor[2] = 0.9;
                    gl.glRasterPos3f(-0.08f, 0.4f, 0f);
                    break;
                case 3:
                    levelColor[0] = 0.0;
                    levelColor[1] = 0.9;
                    levelColor[2] = 0.0;
                    gl.glRasterPos3f(-0.11f, 0.4f, 0f);
                    break;
                default:
                    break;
            }
            String text = this.difString[dificulty];
            glut.glutBitmapString(GLUT.BITMAP_9_BY_15, text);
            gl.glRasterPos3f(-0.7f, -0.0f, 0f);
            text = "<                                                     >";
            glut.glutBitmapString(GLUT.BITMAP_9_BY_15, text);
        }

        gl.glColor3d(1.0, 1.0, 1.0);
        gl.glRasterPos3f(-0.16f, 0.7f, 0f);
        String text = "Select Level";
        glut.glutBitmapString(GLUT.BITMAP_9_BY_15, text);
    }
    
        
    private void showGameScreen(GL2 gl, GLUT glut) {     // Menu =  2
        if (input.escRel()){
            input.setEscRel();
            score = level.getScore();
            menu--; //levelselect
            escBox.change(menu, levelColor);
            escBox.draw(gl, glut);
            spaceBox = new TextBox(menu,levelColor);
        }else{
            if(input.spaceRel()){
                input.setSpaceRel();
            }
            score = level.getScore();
            level.draw(gl);
            
            Date stopTime = new Date();
            long timediff = (stopTime.getTime() - updateTimer.getTime());
            if (timediff>=15){
                level.updatePos(input);
                updateTimer = new Date();
            }
            
            escBox.draw(gl, glut);
            timerBox.updateTimer(score);
            timerBox.draw(gl, glut);
            if (level.isGameOver()){
                menu = 3;
                level = null;
                spaceBox = new TextBox(menu,levelColor);
            }else if (level.isGameWon()){
                menu = 4;
                level = null;
                spaceBox = new TextBox(menu,levelColor);
            }
        }
    }
    
    private void showGameOverScreen(GL2 gl, GLUT glut){    // Menu = 3
        if (input.escRel()){
            input.setEscRel();
            menu = 1; //levelselect
            escBox.change(menu, levelColor);
            spaceBox.change(menu, levelColor);
        }else if (input.spaceRel()){ 
            input.setSpaceRel();
            level = new Level(dificulty, levelColor);
            menu = 2;
            escBox.change(menu, levelColor);
            spaceBox = null;
            timerBox = new TextBox(level.getScore());
        }else{
            spaceBox.draw(gl, glut);
            gl.glClearColor(0.4f, 0.4f, 0.4f, 0.0f);
            gl.glColor3d(levelColor[0], levelColor[1], levelColor[2]);
            gl.glRasterPos3f(-0.11f, 0.1f, 0f);
            String text = "GAME OVER!";
            glut.glutBitmapString(GLUT.BITMAP_9_BY_15, text);
            text = "Last run - " + score/1000 + "secs on " + this.difString[dificulty];
            gl.glRasterPos3f(-0.37f, 0.0f, 0f);
            glut.glutBitmapString(GLUT.BITMAP_9_BY_15, text);
            gl.glRasterPos3f(-0.07f, -0.1f, 0f);
            text = "RETRY?";
            glut.glutBitmapString(GLUT.BITMAP_9_BY_15, text);
        }
        escBox.draw(gl, glut);
    }
    private void showGameWonScreen(GL2 gl, GLUT glut){  // Menu = 4
        if (input.escRel() || input.spacePress()){
            input.setEscRel();
            input.setSpaceRel();
            menu = 1; //levelselect
            escBox.change(menu, levelColor);
            spaceBox.change(menu, levelColor);
        }else {
            gl.glClearColor(0.0f, 0.5f, 0.0f, 0.0f);
            gl.glColor3d(1.0, 1.0, 1.0);
            gl.glRasterPos3f(-0.45f, 0f, 0f);
            String text = difString[dificulty] + " level clear! Congratulations";
            glut.glutBitmapString(GLUT.BITMAP_9_BY_15, text);
        }
        escBox.draw(gl, glut);
        spaceBox.draw(gl, glut);
    }

    private void setLevelColor(int dificulty){
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
            break;
        case 3:
            levelColor[0] = 0.0;
            levelColor[1] = 0.9;
            levelColor[2] = 0.0;
            break;
        default:
            break;
	}
    }
    
    
    @Override
    public void dispose(GLAutoDrawable gLAutoDrawable) {
      //method body
    }

    @Override
    public void init(GLAutoDrawable gLAutoDrawable) {
      // method body
        this.input = new Input(gLAutoDrawable);
        
        setLevelColor(dificulty);
        spaceBox = new TextBox( menu, levelColor);
    }
    @Override
    public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
      // method body
    }
    @Override
    public void init(){
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        // The canvas 
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        Game g = new Game();
        glcanvas.addGLEventListener(g);
        glcanvas.setSize(700, 700);
        //creating frame
        //final JFrame frame = new JFrame ("Hexagon Game");
        //adding canvas to frame
        this.add(glcanvas);
        this.setSize(700,700);
        //frame.getContentPane().add(glcanvas);
        //frame.setSize(frame.getContentPane().getPreferredSize());
        //frame.setVisible(true);
        
        final FPSAnimator animator = new FPSAnimator( glcanvas, 300, true );
        animator.start();
    }
    
    @Override
    public void run(){
        //display();
    }
}
