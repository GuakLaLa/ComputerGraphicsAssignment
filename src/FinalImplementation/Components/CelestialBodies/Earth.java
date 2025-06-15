/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalImplementation.Components.CelestialBodies;

import FinalImplementation.Components.EarthComponents.Astronaut;
import FinalImplementation.Components.EarthComponents.Moon;
import FinalImplementation.Components.EarthComponents.Satellite;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import FinalImplementation.Components.EarthComponents.Rocket;

public class Earth extends Planet {

    private final Moon moon;
    private final Rocket rocket = new Rocket();
    private final Astronaut astronaut = new Astronaut();
    private final Satellite satellite = new Satellite();

    public Earth(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/earth.jpg", radius, distanceFromCenter, rotationSpeed, 1);
        moon = new Moon(0.5f, radius + 0.8f, 5f);

        moon.orbitalSpeed = this.orbitalSpeed * 6;
    }

    @Override
    public void updateRotation() {
        super.updateRotation();

        // Faster Moon orbital speed around Earth when focusing on Sun
        moon.orbitalSpeed = this.orbitalSpeed * 6;
        moon.updateRotation();
    }

    public Rocket getRocket() {
        return rocket;
    }

    public void updateMoon() {
        // Slower Moon orbital speed around Earth when focusing on Earth for better visual performance
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
        gl.glTranslatef(X, Y + 2.5f, Z + 1.5f);
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
        gl.glTranslatef(X, Y + radius + 0.3f, Z);

        gl.glPushMatrix();

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

    public float[] getAstronautPosition() {
        return new float[]{X, Y + radius + 0.3f, Z};
    }

    @Override
    public void render(GL2 gl) {
        if (texture == null) {
            texture = loadTexture(texturePath);
        }

        gl.glPushMatrix();

        // Orbit around the Sun
        gl.glTranslatef(X, Y, Z); // Move Earth to position

        // Self-rotation
        gl.glRotatef(rotationAngles[1], 0f, 1f, 0f);
        // For texture adjustment
        gl.glRotatef(rotationAngles[0], 1f, 0f, 0f);

        // Draw the Earth
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

        // Render moon
        gl.glPushMatrix();

        // Move to Earth position
        gl.glTranslatef(X, Y, Z);

        // Draw the moon
        moon.render(gl);

        gl.glPopMatrix();
    }
}
