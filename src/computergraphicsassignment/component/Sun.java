/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package computergraphicsassignment.component;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class Sun {
    private float radius;
    private float[] position;
    private float[] color;

    public Sun(float radius, float[] position, float[] color) {
        this.radius = radius;
        this.position = position;
        this.color = color;
    }

    public void draw(GL2 gl, GLU glu) {
        gl.glPushMatrix();

        // Set Sun material to emit light
        float[] emission = { color[0], color[1], color[2], 1.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emission, 0);
        gl.glTranslatef(position[0], position[1], position[2]);
        gl.glColor3f(color[0], color[1], color[2]);

        GLUquadric quad = glu.gluNewQuadric();
        glu.gluSphere(quad, radius, 50, 50);
        glu.gluDeleteQuadric(quad);

        // Disable emission after drawing Sun
        float[] noEmission = { 0f, 0f, 0f, 1.0f };
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, noEmission, 0);

        gl.glPopMatrix();
    }
}
