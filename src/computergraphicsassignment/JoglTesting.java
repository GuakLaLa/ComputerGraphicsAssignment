/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package computergraphicsassignment;


import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import computergraphicsassignment.component.EarthMoon;
import computergraphicsassignment.component.Rocket;
import computergraphicsassignment.component.Sun;

public class JoglTesting implements GLEventListener {

    private float angle = 0;
    private GLU glu = new GLU();
    private Sun sun;
//    private Mercury mercury;
    
    //hzw
    private EarthMoon earthMoon;
    private Rocket rocket;
    
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        earthMoon = new EarthMoon(16f, 1.0f, 0.5f, 2.0f);//hzw
        earthMoon.loadTextures(gl);
        rocket = new Rocket();
        
        // Basic settings
        gl.glClearColor(0f, 0f, 0f, 1f);
        gl.glEnable(GL2.GL_DEPTH_TEST);

        // Lighting
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        float[] lightPos = {0.0f, 50.0f, 100.0f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);

        // Enable color tracking for material
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

        // Define Sun and Mercury
        sun = new Sun(10f, new float[]{0f, 0f, 0f}, new float[]{1.0f, 0.6f, 0.0f});
//        mercury = new Mercury(0.38f, 8f, new float[]{0.5f, 0.5f, 0.5f}); // gray color
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Cleanup if needed
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Set camera
        glu.gluLookAt(0, 50, 120, 0, 0, 0, 0, 1, 0);

        //Draw Sun
        sun.draw(gl, glu);

//        // Draw Mercury
//        mercury.draw(gl, glu, angle);

        // Other planets
        float[][] planets = {
            {0.38f, 8f, 4f, 0.5f, 0.5f},  // Mercury
            {0.95f, 12f, 3f, 0.9f, 0.4f}, // Venus
//            {1.0f, 16f, 0f, 0f, 1f},      // Earth - hzw
            {0.53f, 20f, 1f, 0.3f, 0.3f}, // Mars
            {2.0f, 26f, 1f, 0.6f, 0.2f},  // Jupiter
            {1.6f, 32f, 0.9f, 0.9f, 0.5f},// Saturn
            {1.3f, 36f, 0.5f, 1f, 1f},    // Uranus
            {1.2f, 40f, 0.2f, 0.2f, 1f}   // Neptune
        };

        for (int i = 0; i < planets.length; i++) {
            float radius = planets[i][0];
            float distance = planets[i][1];
            float r = planets[i][2], g = planets[i][3], b = planets[i][4];
            drawRevolvingPlanet(gl, radius, distance, r, g, b, angle * (i + 1));
        }
            earthMoon.update();//hzw
            earthMoon.draw(gl);
            rocket.update();
            rocket.draw(gl);
            
        angle += 0.2f;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if (height <= 0) height = 1;
        float aspect = (float) width / height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 1.0, 300.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private void drawPlanet(GL2 gl, float x, float y, float z, float radius, float r, float g, float b) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glColor3f(r, g, b);
        GLUquadric planet = glu.gluNewQuadric();
        glu.gluSphere(planet, radius, 30, 30);
        glu.gluDeleteQuadric(planet);
        gl.glPopMatrix();
    }

    private void drawRevolvingPlanet(GL2 gl, float radius, float distance, float r, float g, float b, float angle) {
        gl.glPushMatrix();
        gl.glRotatef(angle, 0, 1, 0);
        gl.glTranslatef(distance, 0, 0);
        gl.glRotatef(angle * 2, 0, 1, 0);
        drawPlanet(gl, 0, 0, 0, radius, r, g, b);
        gl.glPopMatrix();
    }
    
    //hzw
    public void rocketMove(String direction) {
        if (rocket == null) return;
        switch (direction) {
            case "UP" -> rocket.moveUp();
            case "DOWN" -> rocket.moveDown();
            case "LEFT" -> rocket.moveLeft();
            case "RIGHT" -> rocket.moveRight();
            case "FORWARD" -> rocket.moveForward();
            case "BACKWARD" -> rocket.moveBackward();
        }
    }
}
