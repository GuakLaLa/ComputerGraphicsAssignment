/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package TestFull;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Asus
 */
public class SolarSystem implements GLEventListener {
    private List<Planets> planets = new ArrayList<>();

    public static void main(String[] args) {
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);

        SolarSystem system = new SolarSystem();
        canvas.addGLEventListener(system);

        JFrame frame = new JFrame("Solar System");
        frame.setSize(1000, 800);
        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new FPSAnimator(canvas, 60).start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);

        planets.add(new Sun(6f, 0, 0));
        
        planets.add(new Mercury(0.75f, 7.5f, 5f));
        planets.add(new Venus(1.7f, 10.2f, 5f));
        planets.add(new Earth(1.8f, 14f, 5f));
        planets.add(new Mars(1.125f, 17.5f, 5f));
        planets.add(new Jupiter(4.2f, 23.8f, 5f));
        planets.add(new Saturn(3.9f, 34.3f, 5.5f));
        planets.add(new Uranus(2.15f, 41.8f, 6.5f));  
        planets.add(new Neptune(1.94f, 46.8f, 6.5f));        

        gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0f, 0f, -50f);

        for (Planets planet : planets) {
            planet.updateRotation();
            planet.render(gl);
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        new GLU().gluPerspective(45.0, (double) w / h, 0.1, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        for (Planets planet : planets) {
            planet.dispose(gl);
        }
    }
}

