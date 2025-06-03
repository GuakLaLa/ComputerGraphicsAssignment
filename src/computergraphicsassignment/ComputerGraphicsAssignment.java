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
        canvas.addGLEventListener(new JoglTesting()); // Attach your scene class

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
