/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexagon;
import javax.media.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
/**
 *
 * @author jamiejakov
 */
public class TextBox {
    private double x,y;
    private final double bgColor[] = new double[3];
    private final double txtColor[] = new double[3];
    private final double[] size = new double[2];
    private double width, height;
    private String text;
    private final int type; // 0=escape box, 1= space box, 2 = timer
    
    public TextBox(int menu){
        this.type = 0;
        makeEsc(menu);
    }
    public TextBox(int menu, double[] levelColor){
        this.type = 1;
        makeSpace(menu, levelColor);
    }
    public TextBox(double seconds){
        this.type = 2;
        makeTimer(seconds);
    }
    
    
    // ************* ESCAPE ******************
    
    private void makeEsc(int menu){
        switch(menu){
            case 0:
                break;
            case 1:
                makeEscBack();
                break;
            case 2:
                makeEscMenu(0); // grey color
                break;
            case 3:
            case 4:
                makeEscMenu(1); // black color
            default:
                break;
        }
        width = 0.14;
	height = 0.08;
	x = -1.0;
	y = 1.0;
        
    }
    
    private void makeEscBack(){
        bgColor[0] = 0.69;
	bgColor[1] = 0.69;
	bgColor[2] = 0.69;
	txtColor[0] = 0.0;
	txtColor[1] = 0.0;
	txtColor[2] = 0.0;
	text ="BACK";
    }
    private void makeEscMenu(int n){
        if (n==1){
            bgColor[0] = 0.0;
            bgColor[1] = 0.0;
            bgColor[2] = 0.0;
            txtColor[0] = 1.0;
            txtColor[1] = 1.0;
            txtColor[2] = 1.0;
        }else{
            bgColor[0] = 0.69;
            bgColor[1] = 0.69;
            bgColor[2] = 0.69;
            txtColor[0] = 0.0;
            txtColor[1] = 0.0;
            txtColor[2] = 0.0;
        }
	text ="MENU";
    }


    // ************* SPACE ******************
    
    private void makeSpace(int menu, double[] levelColor){
        switch(menu){
            case 0:
                makeSpaceStart();
                break;
            case 1:
                makeSpaceSelect(levelColor);
                break;
            case 2:
                break;
            case 3:
                makeSpaceYes(levelColor);
                break;
            case 4:
                makeSpaceBack();
            default:
                break;
        }
        height = 0.08;
        txtColor[0] = 1.0;
        txtColor[1] = 1.0;
        txtColor[2] = 1.0;
        x = -width / 2;
        y = -0.2;
    }
  
    private void makeSpaceStart(){
        bgColor[0] = 0.0;
        bgColor[1] = 0.0;
        bgColor[2] = 1.0;
        text = "SPACE TO START";
        width = 0.40;
    }
    private void makeSpaceSelect(double[] levelColor){
        bgColor[0] = levelColor[0];
        bgColor[1] = levelColor[1];
        bgColor[2] = levelColor[2];
        text = "SPACE TO SELECT";
        width = 0.43;
    }
    private void makeSpaceYes(double[] levelColor){
        bgColor[0] = levelColor[0];
        bgColor[1] = levelColor[1];
        bgColor[2] = levelColor[2];
        text = "YES";
        width = 0.125;
    }
    private void makeSpaceBack(){
        bgColor[0] = 0.0;
        bgColor[1] = 0.0;
        bgColor[2] = 0.0;
        text = "BACK TO LEVEL SELECT";
        width = 0.55;
    }
   
    
    // ************* TIMER ******************
    
    private void makeTimer(double seconds){
        bgColor[0] = 0.69;
	bgColor[1] = 0.69;
	bgColor[2] = 0.69;
	txtColor[0] = 0.0;
	txtColor[1] = 0.0;
	txtColor[2] = 0.0;
        double t = seconds/1000;
	text = "Time: " + t;
	width = 0.35;
	height = 0.08;
	x = 1.0 -  width;
	y = 1.0;
    }
    
    
    // ************* OTHER METHODS ******************
    
    public void change(int menu, double[] levelColor){
        switch(type){
            case 0: 
                makeEsc(menu);
                break;
            case 1:
                makeSpace(menu, levelColor);
                break;
            default:
                break;
        }
    }
    public void updateTimer(double seconds){
        text = "Time: " + seconds/1000;
    }
    
    public void draw(GL2 gl, GLUT glut){
        float textPosX = (float)(x + 0.02);
        float textPosY = (float)(y - 0.05);
        
        gl.glColor3d(bgColor[0], bgColor[1], bgColor[2]);
	gl.glBegin(GL2.GL_POLYGON);
	gl.glVertex2d(x, y);
	gl.glVertex2d(x + width, y);
	gl.glVertex2d(x + width, y - height);
	gl.glVertex2d(x, y - height);
	gl.glEnd();
        
        gl.glColor3d(txtColor[0], txtColor[1], txtColor[2]);
        gl.glRasterPos3f(textPosX, textPosY, 0);
        glut.glutBitmapString(GLUT.BITMAP_9_BY_15, text);
        
    }
    
}
