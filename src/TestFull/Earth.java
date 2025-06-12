/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import computergraphicsassignment.component.Rocket;

/**
 *
 * @author Asus
 */
public class Earth extends Planet {

    // Position of Earth in orbit
    private float earthX, earthZ, earthY;

    private Moon moon;
    private Rocket rocket;
    private Astronaut astronaut;
    private Satellite satellite;

    public Earth(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/earth.jpg", radius, distanceFromCenter, rotationSpeed, Planet.SelfRotateAxis.Z_Axis);
        moon = new Moon(0.5f, radius + 0.8f, 5f);
        rocket = new Rocket();
        astronaut = new Astronaut();
        satellite = new Satellite();
        // Position of Earth in orbit
        earthX = (float) (Math.cos(Math.toRadians(orbitalAngle)) * distanceFromCenter);
        earthZ = (float) (Math.sin(Math.toRadians(orbitalAngle)) * distanceFromCenter);
        earthY = 0f;
        moon.orbitalSpeed = this.orbitalSpeed * 6;
    }

    @Override
    public void updateRotation() {
        super.updateRotation();
//        int axis = rotateAxis == SelfRotateAxis.Y_Axis ? 1 : 2;
//        rotationAngles[axis] += rotationSpeed; // Y-axis rotation
//
//        orbitalAngle -= orbitalSpeed;
//        earthX = (float) (Math.cos(Math.toRadians(orbitalAngle)) * distanceFromCenter);
//        earthZ = (float) (Math.sin(Math.toRadians(orbitalAngle)) * distanceFromCenter);

        moon.orbitalSpeed = this.orbitalSpeed * 6;
        moon.updateRotation();
    }

    public Rocket getRocket() {
        return rocket;
    }

    public void updateMoon() {
        moon.orbitalSpeed = this.orbitalSpeed * 2;
        moon.updateRotation();
    }

    public void updateRocket() {
        rocket.update();
    }

    public void updateSatellite() {
        satellite.update();
    }

    public void updateAstronaut() {
        astronaut.update(0.05f);
    }

    public void renderRocket(GL2 gl, float scaleFactor) {
        gl.glPushMatrix();

        // Move Earth to position
        // Y for offset above Earth's surface
        gl.glTranslatef(X, Y + radius + 0.5f, Z);

        // Scale the rocket
        gl.glScalef(scaleFactor, scaleFactor, scaleFactor);

        // Render rocket
        rocket.draw(gl);

        // Reset color to avoid other component being colored
        gl.glColor3f(1f, 1f, 1f);

        gl.glPopMatrix();
    }

    public void renderSatellite(GL2 gl, float scaleFactor) {
        gl.glPushMatrix();

        // Move Earth to position
        // Y for offset above Earth's surface
        
        gl.glTranslatef(X + 2f, Y + 4f, Z + 3.5f);
        gl.glRotatef(180, 0f, 1f, 0f);

        gl.glPushMatrix();

        gl.glRotatef(-50, 1f, 0f, 0f);
        gl.glRotatef(200, 0f, 1f, 0f);
        gl.glRotatef(-180, 0f, 0f, 1f);

        // Scale the rocket
        gl.glScalef(scaleFactor, scaleFactor, scaleFactor);

        // Render rocket
        satellite.render(gl);

        // Reset color to avoid other component being colored
        gl.glColor3f(1f, 1f, 1f);

        gl.glPopMatrix();
        gl.glPopMatrix();

    }

    public void renderAstronaut(GL2 gl, float scaleFactor) {
        gl.glPushMatrix();

        // Move Earth to position
        // Y for offset above Earth's surface
//        gl.glTranslatef(0, 0, 0);
        gl.glTranslatef(X, Y + radius + 0.3f, Z);

        gl.glPushMatrix();

//        gl.glRotatef(-30, 1f, 0f, 0f);
        gl.glRotatef(-20, 0f, 1f, 0f);
        gl.glRotatef(-50, 0f, 0f, 1f);

        // Scale the astronaut
        gl.glScalef(scaleFactor, scaleFactor, scaleFactor);

        // Render astronaut
        astronaut.render(gl);

        // Reset color to avoid other component being colored
        gl.glColor3f(1f, 1f, 1f);

        gl.glPopMatrix();
        gl.glPopMatrix();
    }

    @Override
    public void render(GL2 gl) {
        if (texture == null) {
            texture = loadTexture(texturePath);
        }

        gl.glPushMatrix();

        // Orbit around the Sun
//        gl.glRotatef(orbitalAngle, 0f, 1f, 0f); // Orbital angle
//        gl.glTranslatef(distanceFromCenter, 0f, 0f); // Orbital radius
        gl.glTranslatef(X, Y, Z); // Move Earth to position
//        System.out.println(orbitalAngle+" "+earthX+" "+earthZ);

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

        // Add a small sphere to make moon follow Earth's orbit around sun
        gl.glPushMatrix();

        // Orbit around the Sun
        gl.glTranslatef(X, Y, Z); // Move Earth to position

        glu.gluSphere(quadric, 0.1f, 64, 64);

        glu.gluDeleteQuadric(quadric);

        moon.render(gl);

//        gl.glPushMatrix();
//        
//        gl.glTranslatef(0f, radius + 1.0f, 0f); // Offset above Earth’s surface
//        gl.glScalef(0.15f, 0.15f, 0.15f);
//        rocket.draw(gl); // Render the rocket relative to Earth
//        gl.glColor3f(1f, 1f, 1f); // Default white or whatever Earth needs
//        gl.glPopMatrix();
        gl.glPopMatrix();
    }
}
