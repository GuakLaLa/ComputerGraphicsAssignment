/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

/**
 *
 * @author Asus
 */
public class Earth extends Planets {

    private Moon moon;

    public Earth(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/earth.jpg", radius, distanceFromCenter, rotationSpeed, Planets.SelfRotateAxis.Z_Axis);
        moon = new Moon(0.5f, radius + 0.8f, 5f);
        moon.orbitalSpeed = this.orbitalSpeed * 6;
    }
    
    @Override
    public void updateRotation() {
        super.updateRotation();

//        int axis = rotateAxis == SelfRotateAxis.Y_Axis ? 1 : 2;
//        rotationAngles[axis] += rotationSpeed; // Y-axis rotation
        
//        orbitalAngle += orbitalSpeed;

        moon.updateRotation();
    }

    @Override
    public void render(GL2 gl) {
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
        
//        gl.glRotatef(-rotationAngles[0], 1f, 0f, 0f);
//        gl.glRotatef(-rotationAngles[1], 0f, 1f, 0f);
//        gl.glRotatef(-rotationAngles[2], 0f, 0f, 1f);
        
        glu.gluDeleteQuadric(quadric);
        
        gl.glPopMatrix();
        
        gl.glPushMatrix();

        // Orbit around the Sun
        gl.glRotatef(orbitalAngle, 0f, 1f, 0f); // Orbital angle
        gl.glTranslatef(distanceFromCenter, 0f, 0f); // Orbital radius

        glu.gluSphere(quadric, 0.1f, 64, 64);
        
//        gl.glRotatef(-rotationAngles[0], 1f, 0f, 0f);
//        gl.glRotatef(-rotationAngles[1], 0f, 1f, 0f);
//        gl.glRotatef(-rotationAngles[2], 0f, 0f, 1f);
        
        glu.gluDeleteQuadric(quadric);
        
        moon.render(gl);
        
        gl.glPopMatrix();
    }
}
