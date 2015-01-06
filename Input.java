/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexagon;

/**
 *
 * @author jamiejakov
 */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GLAutoDrawable;
import java.awt.Component;

public final class Input implements KeyListener, Runnable {

    private boolean keyLeft = false;;
    private boolean keyRight = false;;
    private boolean keyEsc = false;;
    private boolean keySpace = false;;
    private boolean keyLRel = false;
    private boolean keyRRel = false;
    private boolean keyERel = false;
    private boolean keySRel = false;

    public Input(GLAutoDrawable gLAutoDrawable) {
        ((Component) gLAutoDrawable).addKeyListener(this);
    }
    
    @Override
    public void run() {
        while (true) {

            if (keyLeft) {
                return;
            }
            if (keyRight) {
                return;
            }
            if (keyEsc) {
                return;
            }
            if (keySpace) {
                return;
            }

            try {
                    Thread.sleep(15);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_Q) {
            this.keyERel = false;
            this.keyEsc = true;
        }
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            this.keyLRel = false;
            this.keyLeft = true;
        }
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            this.keyRRel = false;
            this.keyRight = true;
        }
        if (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_F) {
            this.keySRel = false;
            this.keySpace = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            this.keyLRel = true;
            this.keyLeft = false;
        }
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            this.keyRRel = true;
            this.keyRight = false;
        }
        if (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_F) {
            this.keySRel = true;
            this.keySpace = false;
        }
        if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_Q) {
            this.keyERel = true;
            this.keyEsc = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }
    
    
    public boolean rightPress(){
        return keyRight;
    }
    public boolean leftPress(){
        return keyLeft;
    }
    public boolean spacePress(){
        return keySpace;
    }
    public boolean escPress(){
        return keyEsc;
    }
    
    public boolean escRel(){
        return keyERel;
    }
    public boolean spaceRel(){
        return keySRel;
    }
    public boolean RRel(){
        return keyRRel;
    }
    public boolean LRel(){
        return keyLRel;
    }
    
    public void setEscRel(){
        keyERel = false;
    }
    public void setSpaceRel(){
        keySRel = false;
    }
    public void setRRel(){
        keyRRel = false;
    }
    public void setLRel(){
        keyLRel = false;
    }
 
}