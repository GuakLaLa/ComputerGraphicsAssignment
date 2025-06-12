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
public class Sun extends Planet{

    public Sun(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/sun.jpg", radius, distanceFromCenter, rotationSpeed, Planet.SelfRotateAxis.Z_Axis);
        this.orbitalSpeed = 0f;
    }
    
    @Override
    public void render(GL2 gl) {
        if (texture == null) {
            texture = loadTexture(texturePath);
        }
        
        gl.glPushMatrix();
        
        // Self-rotation
        gl.glRotatef(-90, 1f, 0f, 0f);
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

//    @Override
//    public void updateRotation() {
//    
//    }
    
}
