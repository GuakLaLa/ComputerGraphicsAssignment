/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package computergraphicsassignment.component;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Neptune implements GLEventListener {

    private float rotationAngle = 0;
    private Texture neptuneTexture;
    private final GLU glu = new GLU();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Neptune - JOGL");
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);

        Neptune neptune = new Neptune();
        canvas.addGLEventListener(neptune);
        canvas.setSize(800, 600);

        frame.getContentPane().add(canvas);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glClearColor(0f, 0f, 0f, 1f); // Black background

        // Load Neptune texture
        try {
            neptuneTexture = TextureIO.newTexture(new File("src/images/neptune.jpg"), true);
        } catch (IOException e) {
            System.err.println("Texture loading failed: " + e.getMessage());
            System.exit(1);
        }

        gl.glEnable(GL2.GL_TEXTURE_2D);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Move back and rotate
        gl.glTranslatef(0f, 0f, -5f);
        gl.glRotatef(rotationAngle, 0.0f, 1.0f, 0.0f);

        // Bind and draw Neptune sphere with texture
        neptuneTexture.enable(gl);
        neptuneTexture.bind(gl);

        GLUquadric quad = glu.gluNewQuadric();
        glu.gluQuadricTexture(quad, true);
        glu.gluQuadricDrawStyle(quad, GLU.GLU_FILL);
        glu.gluQuadricNormals(quad, GLU.GLU_SMOOTH);
        glu.gluSphere(quad, 1.0, 50, 50);
        glu.gluDeleteQuadric(quad);

        neptuneTexture.disable(gl);

        rotationAngle += 0.5f;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if (height == 0) height = 1;
        float aspect = (float) width / height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 1.0, 100.0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Clean up if needed
    }
}
