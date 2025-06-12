/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.InputStream;

/**
 *
 * @author Asus
 */
public class Planets {

    protected class SelfRotateAxis {

        public static final char Y_Axis = 'Y';
        public static final char Z_Axis = 'Z';
    }

    protected float radius, distanceFromCenter;
    protected float rotationSpeed;
    protected float orbitalAngle = 30f;
    protected float orbitalSpeed = 0.75f;
    protected float[] rotationAngles = {0, 0, 0}; // x, y, z
    protected char rotateAxis = SelfRotateAxis.Y_Axis;
    protected String texturePath;
    protected Texture texture;
//    protected GLU glu = new GLU();
//    protected GLUquadric quadric;

    public void render(GL2 gl) {
        renderDefault(gl);
    }

    public void updateRotation() {
        updateRotationDefault();
    };

    public Planets(String texturePath, float radius, float distanceFromCenter, float rotationSpeed) {
        this.radius = radius;
        this.distanceFromCenter = distanceFromCenter;
        this.rotationSpeed = rotationSpeed;
        this.texturePath = texturePath;

//        try (InputStream stream = getClass().getResourceAsStream(texturePath)) {
//            texture = TextureIO.newTexture(stream, true, "jpg");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        quadric = glu.gluNewQuadric();
//        glu.gluQuadricTexture(quadric, true);
    }

    public Planets(String texturePath, float radius, float distanceFromCenter, float rotationSpeed, char rotateAxis) {
        this(texturePath, radius, distanceFromCenter, rotationSpeed);
        this.rotateAxis = rotateAxis;
        if (rotateAxis == Planets.SelfRotateAxis.Z_Axis) {
            this.rotationAngles = new float[]{-90, 0, 0};
        }
    }

    protected Texture loadTexture(String path) {
        try (InputStream stream = getClass().getResourceAsStream(path)) {
            return TextureIO.newTexture(stream, true, "jpg");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void updateRotationDefault() {
        int axis = rotateAxis == SelfRotateAxis.Y_Axis ? 1 : 2;
        rotationAngles[axis] += rotationSpeed; // Y-axis rotation
        
        orbitalAngle += orbitalSpeed;
    }

    protected void renderDefault(GL2 gl) {
        if (texture == null) {
            texture = loadTexture(texturePath);
        }

        gl.glPushMatrix();

        // Orbit around the Sun
        gl.glRotatef(orbitalAngle, 0f, 1f, 0f); // Orbital angle
        gl.glTranslatef(distanceFromCenter, 0f, 0f); // Orbital radius

        // Self-rotation
        gl.glRotatef(rotationAngles[0], 1f, 0f, 0f);
        gl.glRotatef(rotationAngles[1], 0f, 1f, 0f);
        gl.glRotatef(rotationAngles[2], 0f, 0f, 1f);

        // Draw the planet
        GLU glu = new GLU();
        GLUquadric quadric = glu.gluNewQuadric();

        if (texture != null) {
            texture.enable(gl);
            texture.bind(gl);
        }

        glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
        glu.gluQuadricTexture(quadric, true);
        glu.gluSphere(quadric, radius, 64, 64);

        if (texture != null) {
            texture.disable(gl);
        }

        glu.gluDeleteQuadric(quadric);

        gl.glPopMatrix();
    }

    public void dispose(GL2 gl) {
        if (texture != null) {
            texture.destroy(gl);
            texture = null;
        }
//        glu.gluDeleteQuadric(quadric);
    }
}
