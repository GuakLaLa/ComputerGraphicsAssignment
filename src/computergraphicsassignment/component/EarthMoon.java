/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package computergraphicsassignment.component;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author user
 */
public class EarthMoon {
    private float orbitRadius;
    private float size;
    private float orbitAngle;
    private float rotationAngle;
    private float orbitSpeed;
    private float rotationSpeed;

    private float axialTilt = 23.5f;

    private float moonOrbitRadius = 3.0f;
    private float moonSize = 0.27f;
    private float moonOrbitAngle = 0;
    private float moonOrbitSpeed = 2.0f;
    private float moonInclination = 5.0f;

    private Texture earthTexture;
    private Texture moonTexture;

    private GLU glu = new GLU();

    public EarthMoon(float orbitRadius, float size, float orbitSpeed, float rotationSpeed) {
        this.orbitRadius = orbitRadius;
        this.size = size;
        this.orbitSpeed = orbitSpeed;
        this.rotationSpeed = rotationSpeed;
    }

    public void update() {
        orbitAngle += orbitSpeed;
        rotationAngle += rotationSpeed;
        moonOrbitAngle += moonOrbitSpeed;
    }

    public void draw(GL2 gl) {
        gl.glPushMatrix();

        // Earth orbiting the Sun
        gl.glRotatef(orbitAngle, 0, 1, 0);
        gl.glTranslatef(orbitRadius, 0, 0);

        // Earth tilt and self-rotation
        gl.glRotatef(axialTilt, 0, 0, 1); // tilt axis (Z)
        gl.glRotatef(rotationAngle, 0, 1, 0);

//        // Draw Earth
//        gl.glColor3f(0.0f, 0.4f, 1.0f); // Deep blue Earth
//        GLUquadric earth = glu.gluNewQuadric();
//        glu.gluSphere(earth, size, 40, 40);
//        glu.gluDeleteQuadric(earth);
        
        // Draw Earth with texture
        if (earthTexture != null) {
            earthTexture.enable(gl);
            earthTexture.bind(gl);
        }
        
        gl.glColor3f(1f, 1f, 1f);//Set White Color Before Drawing Textured Sphere
        GLUquadric earth = glu.gluNewQuadric();
        glu.gluQuadricTexture(earth, true);
        glu.gluQuadricNormals(earth, GLU.GLU_SMOOTH);// include normals
        glu.gluSphere(earth, size, 40, 40);
        glu.gluDeleteQuadric(earth);

        if (earthTexture != null) {
            earthTexture.disable(gl);
        }

        drawMoon(gl);

        gl.glPopMatrix();
    }

    private void drawMoon(GL2 gl) {
        gl.glPushMatrix();

        // Slightly inclined orbit plane for the Moon
        gl.glRotatef(moonInclination, 0, 0, 1); // tilt orbit
        gl.glRotatef(moonOrbitAngle, 0, 1, 0);
        gl.glTranslatef(moonOrbitRadius, 0, 0);

//        gl.glColor3f(0.85f, 0.85f, 0.85f); // Light gray
//        GLUquadric moon = glu.gluNewQuadric();
//        glu.gluSphere(moon, moonSize, 20, 20);
//        glu.gluDeleteQuadric(moon);
        // Draw Moon with texture
        
        if (moonTexture != null) {
            moonTexture.enable(gl);
            moonTexture.bind(gl);
        }
        
        gl.glColor3f(1f, 1f, 1f);//Set White Color Before Drawing Textured Sphere
        GLUquadric moon = glu.gluNewQuadric();
        glu.gluQuadricTexture(moon, true);
        glu.gluQuadricNormals(moon, GLU.GLU_SMOOTH); // include normals
        glu.gluSphere(moon, moonSize, 20, 20);
        glu.gluDeleteQuadric(moon);

        if (moonTexture != null) {
            moonTexture.disable(gl);
        }

        gl.glPopMatrix();
    }
    
    public void loadTextures(GL2 gl) {
        try {
            InputStream earthStream = getClass().getResourceAsStream("/images/earth.jpg");
            InputStream moonStream = getClass().getResourceAsStream("/images/moon.jpg");

            earthTexture = TextureIO.newTexture(earthStream, true, TextureIO.JPG);
            moonTexture = TextureIO.newTexture(moonStream, true, TextureIO.JPG);

            gl.glEnable(GL2.GL_TEXTURE_2D);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
