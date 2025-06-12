/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.IOException;
import java.io.InputStream;

public abstract class TexturedPlanet {
    protected float radius;
    protected float distanceFromSun;

    protected String texturePath, ringTexturePath;
    protected boolean hasRings;
    protected float ringInnerRadius;
    protected float ringOuterRadius;

    protected Texture planetTexture, ringTexture;

    protected float rotationAngle = 0.0f;     // Rotation on own axis
    protected float rotationSpeed = 0.5f;

    protected float orbitalAngle = 0.0f;      // Revolution around sun
    protected float orbitalSpeed = 0.2f;
    
    protected abstract void drawAdditional(GL2 gl);

    public TexturedPlanet(float radius, float distanceFromSun, float rotationSpeed, String texturePath) {
        this(radius, distanceFromSun, rotationSpeed, texturePath, false, null, 0f, 0f);
    }

    public TexturedPlanet(float radius, float distanceFromSun, float rotationSpeed, String texturePath, boolean hasRings, String ringTexturePath, float ringInnerRadius, float ringOuterRadius) {
        this.radius = radius;
        this.distanceFromSun = distanceFromSun;
        this.rotationSpeed = rotationSpeed;
        this.texturePath = texturePath;
        this.hasRings = hasRings;
        this.ringTexturePath = ringTexturePath;
        this.ringInnerRadius = ringInnerRadius;
        this.ringOuterRadius = ringOuterRadius;
    }

    public void setRotationSpeed(float speed) {
        this.rotationSpeed = speed;
    }

    public void setOrbitalSpeed(float speed) {
        this.orbitalSpeed = speed;
    }

    public void updateRotation() {
        rotationAngle += rotationSpeed;
        if (rotationAngle >= 360.0f) rotationAngle -= 360.0f;

        orbitalAngle += orbitalSpeed;
        if (orbitalAngle >= 360.0f) orbitalAngle -= 360.0f;
    }

    public void render(GL2 gl) {
        if (planetTexture == null) {
            planetTexture = loadTexture(texturePath);
        }
        if (hasRings && ringTexture == null) {
            ringTexture = loadTexture(ringTexturePath);
        }

        gl.glPushMatrix();

        // Orbit around the Sun
        gl.glRotatef(orbitalAngle, 0f, 1f, 0f); // Orbital angle
        gl.glTranslatef(distanceFromSun, 0f, 0f); // Orbital radius

        // Self-rotation
        gl.glRotatef(rotationAngle, 0f, 1f, 0f);

        // Draw the planet
        drawTexturedPlanet(gl);

        // Draw rings if any
        if (hasRings) {
            drawTexturedRings(gl);
        }
        
        drawAdditional(gl);

        gl.glPopMatrix();
    }

    public void dispose(GL2 gl) {
        if (planetTexture != null) {
            planetTexture.destroy(gl);
            planetTexture = null;
        }
        if (ringTexture != null) {
            ringTexture.destroy(gl);
            ringTexture = null;
        }
    }

    protected Texture loadTexture(String path) {
        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream == null) {
                System.err.println("Texture not found: " + path);
                return null;
            }
            return TextureIO.newTexture(stream, true, TextureIO.JPG);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void drawTexturedPlanet(GL2 gl) {
        GLU glu = new GLU();
        GLUquadric quadric = glu.gluNewQuadric();

        if (planetTexture != null) {
            planetTexture.enable(gl);
            planetTexture.bind(gl);
        }

        glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
        glu.gluQuadricTexture(quadric, true);
        glu.gluSphere(quadric, radius, 64, 64);

        if (planetTexture != null) {
            planetTexture.disable(gl);
        }

        glu.gluDeleteQuadric(quadric);
    }

    protected void drawTexturedRings(GL2 gl) {
        if (ringTexture == null) return;

        final int slices = 100;
        ringTexture.enable(gl);
        ringTexture.bind(gl);

        gl.glPushMatrix();
        gl.glRotatef(90f, 1f, 0f, 0f); // Align ring in XZ plane

        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i <= slices; i++) {
            double angle = 2.0 * Math.PI * i / slices;
            double x = Math.cos(angle);
            double y = Math.sin(angle);

            // Outer ring
            gl.glTexCoord2d((x + 1) / 2.0, (y + 1) / 2.0);
            gl.glVertex3d(x * ringOuterRadius, y * ringOuterRadius, 0);

            // Inner ring
            gl.glTexCoord2d((x * ringInnerRadius / ringOuterRadius + 1) / 2.0,
                            (y * ringInnerRadius / ringOuterRadius + 1) / 2.0);
            gl.glVertex3d(x * ringInnerRadius, y * ringInnerRadius, 0);
        }
        gl.glEnd();

        gl.glPopMatrix();
        ringTexture.disable(gl);
    }
}

