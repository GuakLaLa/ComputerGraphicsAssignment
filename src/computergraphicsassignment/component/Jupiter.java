package computergraphicsassignment.component;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;

/**
 * Jupiter class renders a textured rotating Jupiter sphere.
 * Allows user rotation control via arrow keys.
 */
public class Jupiter implements GLEventListener{

    private float rotationX = 0;       // Rotation angle around X-axis controlled by user
    private float rotationY = 0;       // Rotation angle around Y-axis controlled by user
    private float autoRotationY = 0;   // Automatic continuous rotation around Y-axis

    private Texture jupiterTexture;    // Texture for Jupiter surface
    private GLU glu = new GLU();       // GLU utilities
    private GLUquadric quadric;        // Quadric object for drawing sphere

    public static void main(String[] args) {
        // Initialize OpenGL profile and capabilities for GL2
        GLProfile glProfile = GLProfile.get(GLProfile.GL2);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        // Create canvas and attach event listeners
        GLCanvas glCanvas = new GLCanvas(glCapabilities);
        Jupiter jupiter = new Jupiter();
        glCanvas.addGLEventListener(jupiter);
        glCanvas.setFocusable(true);
        glCanvas.requestFocus();  // Request focus for key events

        // Setup JFrame window
        JFrame frame = new JFrame("Jupiter");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(glCanvas);
        frame.setVisible(true);

        // Start animator to repaint at 60 FPS
        FPSAnimator animator = new FPSAnimator(glCanvas, 60, true);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Enable depth test for correct 3D rendering and texture mapping
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);

        // Create quadric object and enable texturing on it
        quadric = glu.gluNewQuadric();
        glu.gluQuadricTexture(quadric, true);

        // Load texture image from resources using class loader
        try (InputStream stream = getClass().getResourceAsStream("/images/jupiter.jpg")) {
            if (stream == null) {
                System.err.println("Cannot find texture file: /images/jupiter.jpg");
                System.exit(1);
            }
            // Load texture from input stream, generate mipmaps
            jupiterTexture = TextureIO.newTexture(stream, true, "jpg");
            jupiterTexture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
            jupiterTexture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Optional lighting setup to illuminate the sphere
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        float[] lightPos = {0f, 0f, 5f, 1f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);

        // Set background color to black
        gl.glClearColor(0f, 0f, 0f, 1f);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Clear color and depth buffers for new frame
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // Reset modelview matrix
        gl.glLoadIdentity();

        // Move camera back to view the sphere
        gl.glTranslatef(0f, 0f, -5f);

        // Rotate sphere 90 degrees to align texture properly
        gl.glRotatef(90, 1f, 0f, 0f);

        // Apply automatic rotation around Z axis (Y in sphere coords)
        gl.glRotatef(autoRotationY, 0f, 0f, 1f);

        // Apply user-controlled rotations around X and Y axes
        gl.glRotatef(rotationX, 1f, 0f, 0f);
        gl.glRotatef(rotationY, 0f, 1f, 0f);

        // Bind texture and draw textured sphere
        jupiterTexture.bind(gl);
        glu.gluSphere(quadric, 1.0f, 64, 64);

        // Increment automatic rotation for animation
        autoRotationY += 0.5f;
        if (autoRotationY >= 360f) {
            autoRotationY -= 360f;
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        // Set viewport to new window size
        gl.glViewport(0, 0, width, height);

        // Setup projection matrix
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        double aspect = (double) width / height;

        // Setup perspective projection with 45 degree field of view
        glu.gluPerspective(45.0, aspect, 0.1, 100.0);

        // Return to modelview matrix for rendering
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Cleanup resources when disposing
        if (jupiterTexture != null) {
            jupiterTexture.destroy(drawable.getGL().getGL2());
        }
        if (quadric != null) {
            glu.gluDeleteQuadric(quadric);
        }
    }

}
