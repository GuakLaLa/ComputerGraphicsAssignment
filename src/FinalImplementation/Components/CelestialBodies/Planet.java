/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalImplementation.Components.CelestialBodies;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.InputStream;
import java.util.Random;

public class Planet {

    private static final Random random = new Random();
    private static float EARTH_ORBITAL_SPEED = 0.8f;

    protected float X, Y, Z;
    protected float radius, distanceFromCenter;
    
    // Self-rotation speed
    protected float rotationSpeed;
    
    // Random initial position on XZ-plane
    protected float orbitalAngle = random.nextFloat(0, 90);
    
    // Default orbital speed around Sun of 0.75
    protected float orbitalSpeed = 0.75f;

    // Rotate -90 degrees around X-axis to adjust appearance of texture
    protected float[] rotationAngles = {-90, 0, 0}; // x, y, z
    
    protected String texturePath;
    protected Texture texture;

    public Planet(String texturePath, float radius, float distanceFromCenter, float rotationSpeed) {
        this.radius = radius;
        this.distanceFromCenter = distanceFromCenter;
        this.rotationSpeed = rotationSpeed;
        this.texturePath = texturePath;

        calculatePosition();
        Y = 0f;
    }
    
    public Planet(String texturePath, float radius, float distanceFromCenter, float rotationSpeed, float relativeEarthOrbitalSpeed) {
        this(texturePath, radius, distanceFromCenter, rotationSpeed);
        
        // Calculate orbital speed relative to Earth
        this.orbitalSpeed = EARTH_ORBITAL_SPEED * relativeEarthOrbitalSpeed;
    }

    protected Texture loadTexture(String path) {
        try (InputStream stream = getClass().getResourceAsStream(path)) {
            return TextureIO.newTexture(stream, true, "jpg");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Calculate position on XZ-plane
    protected void calculatePosition() {
        X = (float) (Math.cos(Math.toRadians(orbitalAngle)) * distanceFromCenter) * 1.2f;
        Z = (float) (Math.sin(Math.toRadians(orbitalAngle)) * distanceFromCenter) * 1.2f;
    }

    public float[] getPosition() {
        return new float[]{X, Y, Z};
    }

    public void updateRotation() {
        // Update self-rotation around Y-axis
        rotationAngles[1] += rotationSpeed;

        // Orbit the sun anti-clockwise
        orbitalAngle -= orbitalSpeed;
        calculatePosition();
    }

    public void render(GL2 gl) {
        if (texture == null) {
            texture = loadTexture(texturePath);
        }

        gl.glPushMatrix();

        // Orbit around the Sun
        gl.glTranslatef(X, Y, Z); // Move planet to position

        // For Saturn inclination or Uranus self-rotation
        gl.glRotatef(rotationAngles[2], 0f, 0f, 1f);
        // For all celestial bodies self-rotation except Uranus
        gl.glRotatef(rotationAngles[1], 0f, 1f, 0f);
        // For texture adjustment
        gl.glRotatef(rotationAngles[0], 1f, 0f, 0f);
        
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
    }

    public static void setEarthOrbitalSpeed(float earthOrbitalSpeed) {
        Planet.EARTH_ORBITAL_SPEED = earthOrbitalSpeed;
    }
}
