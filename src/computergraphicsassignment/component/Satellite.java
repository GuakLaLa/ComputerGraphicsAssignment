/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package computergraphicsassignment.component;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.jogamp.opengl.util.FPSAnimator;

public class Satellite implements GLEventListener {
    private float rotateAngle = 0.0f;
    private Texture bodyTexture;
    private Texture panelTexture;
    private float cameraX = 0, cameraY = 5, cameraZ = 15;
    private float cameraSpeed = 0.5f; // Declare cameraSpeed h
    

    public static void main(String[] args) {
        JFrame frame = new JFrame("Textured Satellite");
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);
        Satellite satellite = new Satellite();

        canvas.addGLEventListener(satellite);
        canvas.setSize(800, 600);
        frame.getContentPane().add(canvas);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);

        try {
            bodyTexture = TextureIO.newTexture(new File("src/images/satellite.jpg"), true); //https://nasa3d.arc.nasa.gov/detail/cubesat-1RU
            panelTexture = TextureIO.newTexture(new File("src/images/panel.jpg"), true);
        } catch (IOException e) {
            System.err.println("Texture loading failed: " + e.getMessage());
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -10.0f);

        rotateAngle += 0.5f;
        gl.glRotatef(20.0f, 1.0f, 0.0f, 0.0f); // 向上倾斜 20 度（绕 X 轴）
        gl.glRotatef(15.0f, 0.0f, 0.0f, 1.0f); // 侧倾 15 度（绕 Z 轴）
        gl.glRotatef(rotateAngle, 0.0f, 1.0f, 0.0f); // 旋转动画（绕 Y 轴）


        drawSatellite(gl);

        gl.glFlush();
    }

    private void drawSatellite(GL2 gl) {
        // Draw Body with bodyTexture
        gl.glPushMatrix();
        gl.glScalef(1.5f, 1.0f, 1.0f);
        if (bodyTexture != null) bodyTexture.bind(gl);
        drawTexturedCube(gl);
        gl.glPopMatrix();

        // Left Panels (2 pieces)
        float panelSpacing = 4.0f;
        float panelWidth = 2.0f;

        for (int i = 0; i < 3; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(-3.5f - i * panelSpacing, 0.0f, 0.0f);
            gl.glScalef(panelWidth, 0.1f, 1.0f);
            if (panelTexture != null) panelTexture.bind(gl);
            drawTexturedCube(gl);
            gl.glPopMatrix();
        }

        // Right Panels (2 pieces)
        for (int i = 0; i < 3; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(3.5f + i * panelSpacing, 0.0f, 0.0f);
            gl.glScalef(panelWidth, 0.1f, 1.0f);
            if (panelTexture != null) panelTexture.bind(gl);
            drawTexturedCube(gl);
            gl.glPopMatrix();        }
    }

    private void drawTexturedCube(GL2 gl) {
        gl.glBegin(GL2.GL_QUADS);

        // Front
        gl.glTexCoord2f(0, 0); gl.glVertex3f(-1, -1, 1);
        gl.glTexCoord2f(1, 0); gl.glVertex3f(1, -1, 1);
        gl.glTexCoord2f(1, 1); gl.glVertex3f(1, 1, 1);
        gl.glTexCoord2f(0, 1); gl.glVertex3f(-1, 1, 1);

        // Back
        gl.glTexCoord2f(0, 0); gl.glVertex3f(-1, -1, -1);
        gl.glTexCoord2f(0, 1); gl.glVertex3f(-1, 1, -1);
        gl.glTexCoord2f(1, 1); gl.glVertex3f(1, 1, -1);
        gl.glTexCoord2f(1, 0); gl.glVertex3f(1, -1, -1);

        // Left
        gl.glTexCoord2f(0, 0); gl.glVertex3f(-1, -1, -1);
        gl.glTexCoord2f(1, 0); gl.glVertex3f(-1, -1, 1);
        gl.glTexCoord2f(1, 1); gl.glVertex3f(-1, 1, 1);
        gl.glTexCoord2f(0, 1); gl.glVertex3f(-1, 1, -1);

        // Right
        gl.glTexCoord2f(0, 0); gl.glVertex3f(1, -1, -1);
        gl.glTexCoord2f(0, 1); gl.glVertex3f(1, 1, -1);
        gl.glTexCoord2f(1, 1); gl.glVertex3f(1, 1, 1);
        gl.glTexCoord2f(1, 0); gl.glVertex3f(1, -1, 1);

        // Top
        gl.glTexCoord2f(0, 1); gl.glVertex3f(-1, 1, -1);
        gl.glTexCoord2f(0, 0); gl.glVertex3f(-1, 1, 1);
        gl.glTexCoord2f(1, 0); gl.glVertex3f(1, 1, 1);
        gl.glTexCoord2f(1, 1); gl.glVertex3f(1, 1, -1);

        // Bottom
        gl.glTexCoord2f(0, 0); gl.glVertex3f(-1, -1, -1);
        gl.glTexCoord2f(1, 0); gl.glVertex3f(1, -1, -1);
        gl.glTexCoord2f(1, 1); gl.glVertex3f(1, -1, 1);
        gl.glTexCoord2f(0, 1); gl.glVertex3f(-1, -1, 1);

        gl.glEnd();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Cleanup if needed
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        if (height <= 0) height = 1;
        float aspect = (float) width / height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        new GLU().gluPerspective(45.0, aspect, 1.0, 100.0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
