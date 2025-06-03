package computergraphicsassignment.component;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Main class to create window and start OpenGL rendering of Saturn with textures and rings.
 */
public class Saturn {

    public static void main(String[] args) {
        // Setup OpenGL profile and capabilities for GL2
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // Create OpenGL canvas and attach renderer
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        RealisticSaturnRenderer renderer = new RealisticSaturnRenderer();
        glcanvas.addGLEventListener(renderer);
        glcanvas.setSize(800, 600);

        // Setup JFrame window
        final JFrame frame = new JFrame("Realistic Saturn with Textures");
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start FPS animator for smooth animation (60 FPS)
        final FPSAnimator animator = new FPSAnimator(glcanvas, 60, true);
        animator.start();
    }
}

/**
 * Renderer class implementing GLEventListener to draw Saturn and its rings with textures.
 */
class RealisticSaturnRenderer implements GLEventListener {

    private float rotation = 0.0f;       // Current rotation angle for animation
    private int[] textures = new int[2]; // Texture IDs: 0 = Saturn, 1 = Rings

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Set background color to black and enable depth testing for 3D rendering
        gl.glClearColor(0f, 0f, 0f, 1f);
        gl.glEnable(GL2.GL_DEPTH_TEST);

        // Enable lighting and configure light source
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        float[] lightPos = {5.0f, 3.0f, 5.0f, 1.0f};      // Position of light
        float[] ambientLight = {0.3f, 0.3f, 0.3f, 1.0f};  // Ambient light component
        float[] diffuseLight = {0.7f, 0.7f, 0.7f, 1.0f};  // Diffuse light component
        float[] specularLight = {0.4f, 0.4f, 0.4f, 1.0f}; // Specular light component

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);

        // Set material properties for shininess and specular reflections
        float[] matSpecular = {0.3f, 0.3f, 0.3f, 1.0f};
        float[] matShininess = {30.0f};

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpecular, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShininess, 0);

        // Enable 2D texturing and generate texture IDs
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glGenTextures(2, textures, 0);

        // Load textures from resource files
        loadTexture(gl, textures[0], "/images/saturn.jpg");       // Saturn texture
        loadTexture(gl, textures[1], "/images/saturn_rings.jpeg"); // Rings texture
    }

    /**
     * Loads an image texture from a resource path into an OpenGL texture ID.
     * 
     * @param gl        GL2 context
     * @param textureId OpenGL texture ID
     * @param resourcePath Path to image resource inside jar or project
     */
    private void loadTexture(GL2 gl, int textureId, String resourcePath) {
        try (InputStream stream = getClass().getResourceAsStream(resourcePath)) {
            if (stream == null) {
                System.err.println("Resource not found: " + resourcePath);
                return;
            }

            // Read image and extract pixel data
            BufferedImage image = ImageIO.read(stream);
            int width = image.getWidth();
            int height = image.getHeight();
            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

            // Create byte buffer for RGBA data
            ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);

            // Convert image pixels from ARGB int to RGBA bytes
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                    buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
                    buffer.put((byte) (pixel & 0xFF));         // Blue
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
                }
            }
            buffer.flip();

            // Bind texture and set parameters
            gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);

            // Upload texture data to GPU
            gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, width, height, 0,
                    GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buffer);

            // Generate mipmaps for smoother scaling
            gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading texture: " + resourcePath);
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Clear color and depth buffers
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // Reset modelview matrix and position camera
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -8.0f);
        gl.glRotatef(25, 1.0f, 0.0f, 0.0f);  // Tilt down slightly
        gl.glRotatef(rotation, 0.0f, 1.0f, 0.0f); // Rotate around Y axis
        gl.glRotatef(90, 1.0f, 0.0f, 0.0f);  // Rotate sphere to align texture properly

        // Draw the textured Saturn sphere and rings
        drawTexturedPlanet(gl, 1.0f);
        drawTexturedRings(gl, 1.5f, 2.5f);

        // Increment rotation angle for animation
        rotation += 0.3f;
    }

    /**
     * Draws a textured sphere representing the planet.
     * 
     * @param gl     GL2 context
     * @param radius Sphere radius
     */
    private void drawTexturedPlanet(GL2 gl, float radius) {
        int slices = 50;
        int stacks = 50;

        // Bind Saturn texture
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[0]);
        gl.glEnable(GL2.GL_TEXTURE_2D);

        // Draw sphere using quad strips
        for (int i = 0; i < stacks; i++) {
            float phi1 = (float) (Math.PI * i / stacks);
            float phi2 = (float) (Math.PI * (i + 1) / stacks);

            gl.glBegin(GL2.GL_QUAD_STRIP);
            for (int j = 0; j <= slices; j++) {
                float theta = (float) (2 * Math.PI * j / slices);

                // Calculate vertices on sphere surface
                float x1 = (float) (Math.sin(phi1) * Math.cos(theta));
                float y1 = (float) (Math.sin(phi1) * Math.sin(theta));
                float z1 = (float) Math.cos(phi1);

                float x2 = (float) (Math.sin(phi2) * Math.cos(theta));
                float y2 = (float) (Math.sin(phi2) * Math.sin(theta));
                float z2 = (float) Math.cos(phi2);

                // Texture coordinates (s, t)
                float s = (float) j / slices;
                float t1 = (float) i / stacks;
                float t2 = (float) (i + 1) / stacks;

                // Specify normal and vertex with texture coords for first vertex
                gl.glNormal3f(x1, y1, z1);
                gl.glTexCoord2f(s, t1);
                gl.glVertex3f(x1 * radius, y1 * radius, z1 * radius);

                // Specify normal and vertex with texture coords for second vertex
                gl.glNormal3f(x2, y2, z2);
                gl.glTexCoord2f(s, t2);
                gl.glVertex3f(x2 * radius, y2 * radius, z2 * radius);
            }
            gl.glEnd();
        }

        // Optionally draw atmospheric glow around planet
        drawAtmosphere(gl, radius);
    }

    /**
     * Draws textured rings as a flat disc around the planet.
     * 
     * @param gl           GL2 context
     * @param innerRadius  Inner radius of rings
     * @param outerRadius  Outer radius of rings
     */
    private void drawTexturedRings(GL2 gl, float innerRadius, float outerRadius) {
        int segments = 100;

        gl.glDisable(GL2.GL_LIGHTING);          // Disable lighting for flat texture
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[1]);
        gl.glDisable(GL2.GL_BLEND);
        gl.glColor4f(1f, 1f, 1f, 1f);           // Set full opacity white color

        // Draw ring as triangle strip between inner and outer radii
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            gl.glTexCoord2f((float) i / segments, 0f);
            gl.glVertex3f(cos * outerRadius, sin * outerRadius, 0);

            gl.glTexCoord2f((float) i / segments, 1f);
            gl.glVertex3f(cos * innerRadius, sin * innerRadius, 0);
        }
        gl.glEnd();
    }

    /**
     * Draws a subtle atmospheric glow as translucent discs around the planet.
     * 
     * @param gl           GL2 context
     * @param planetRadius Radius of planet sphere
     */
    private void drawAtmosphere(GL2 gl, float planetRadius) {
        int slices = 50;
        int stacks = 10;
        float atmosphereRadius = planetRadius * 1.05f;  // Slightly larger than planet

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        for (int i = 0; i < stacks; i++) {
            // Fade out alpha towards outer edge
            float alpha = 0.2f * (1 - (float) i / stacks);
            gl.glColor4f(0.5f, 0.6f, 1.0f, alpha); // Blue-ish glow

            float r1 = planetRadius + (atmosphereRadius - planetRadius) * i / stacks;
            float r2 = planetRadius + (atmosphereRadius - planetRadius) * (i + 1) / stacks;

            gl.glBegin(GL2.GL_QUAD_STRIP);
            for (int j = 0; j <= slices; j++) {
                float theta = (float) (2 * Math.PI * j / slices);
                float x = (float) Math.cos(theta);
                float y = (float) Math.sin(theta);

                gl.glNormal3f(x, y, 0);
                gl.glVertex3f(x * r1, y * r1, 0);
                gl.glVertex3f(x * r2, y * r2, 0);
            }
            gl.glEnd();
        }
        gl.glDisable(GL2.GL_BLEND);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        // Set viewport to entire window
        gl.glViewport(0, 0, width, height);

        // Set up perspective projection matrix
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        float aspect = (float) width / height;
        float fov = 45.0f;  // Field of view angle
        float near = 0.1f;
        float far = 100.0f;

        // Calculate frustum bounds
        float top = (float) (Math.tan(Math.toRadians(fov) / 2) * near);
        float bottom = -top;
        float right = top * aspect;
        float left = -right;

        // Set frustum for perspective projection
        gl.glFrustum(left, right, bottom, top, near, far);

        // Switch back to modelview matrix for rendering
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Clean up OpenGL textures
        GL2 gl = drawable.getGL().getGL2();
        gl.glDeleteTextures(2, textures, 0);
    }
}
