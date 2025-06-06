/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package computergraphicsassignment;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.JFrame;
// animate rocket
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Tan Zhi Wei
 */
public class ComputerGraphicsAssignment {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        // 1. Setup OpenGL profile
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // 2. Create canvas with capabilities
        GLCanvas canvas = new GLCanvas(capabilities);
        //hzw
        JoglTesting jogl = new JoglTesting();
        canvas.addGLEventListener(jogl); 
        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> jogl.rocketMove("UP");
                    case KeyEvent.VK_S -> jogl.rocketMove("DOWN");
                    case KeyEvent.VK_A -> jogl.rocketMove("LEFT");
                    case KeyEvent.VK_D -> jogl.rocketMove("RIGHT");
                    case KeyEvent.VK_UP -> jogl.rocketMove("FORWARD");
                    case KeyEvent.VK_DOWN -> jogl.rocketMove("BACKWARD");
                }
            }

            @Override public void keyReleased(KeyEvent e) {}
            @Override public void keyTyped(KeyEvent e) {}
        });
        canvas.setFocusable(true);
        canvas.requestFocus();
        //
        
        // 3. Create window (JFrame)
        JFrame frame = new JFrame("Solar System Simulation");
        frame.setSize(1000, 700);
        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // 4. Run animation at 60 FPS
        FPSAnimator animator = new FPSAnimator(canvas, 60, true);
        animator.start();
    }
    
}
