/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package computergraphicsassignment.component;


import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class Mercury {
    private float radius;
    private float distanceFromSun;
    private float[] color;

    public Mercury(float radius, float distanceFromSun, float[] color) {
        this.radius = radius;
        this.distanceFromSun = distanceFromSun;
        this.color = color;
    }

    public void draw(GL2 gl, GLU glu, float angle) {
        gl.glPushMatrix();

        // Revolve around the Sun
        gl.glRotatef(angle * 2f, 0f, 1f, 0f);
        gl.glTranslatef(distanceFromSun, 0f, 0f);

        // Rotate on its own axis
        gl.glRotatef(angle * 5f, 0f, 1f, 0f);

        // Set color
        gl.glColor3f(color[0], color[1], color[2]);

        // Draw sphere
        GLUquadric quad = glu.gluNewQuadric();
        glu.gluSphere(quad, radius, 30, 30);
        glu.gluDeleteQuadric(quad);

        gl.glPopMatrix();
    }
}
