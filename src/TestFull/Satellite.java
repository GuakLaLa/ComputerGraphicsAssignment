/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Asus
 */
public class Satellite {

    private float rotateAngle = 0.0f;
    private Texture bodyTexture;
    private Texture panelTexture;
    private boolean textureInit = false;
    
    private void loadTextures(GL2 gl) {
        try {
            bodyTexture = TextureIO.newTexture(getClass().getResourceAsStream("/images/satellite.jpg"), true, "jpg");
            panelTexture = TextureIO.newTexture(getClass().getResourceAsStream("/images/panel.jpg"), true, "jpg");
            textureInit = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        rotateAngle += 0.5f;
    }

    public void render(GL2 gl) {
        gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_LIGHTING_BIT);
        gl.glDisable(GL2.GL_LIGHTING); // Disable lighting temporaril
        
        if(!textureInit) {
            loadTextures(gl);
        }
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        
        // Self rotation
        gl.glPushMatrix();
        gl.glRotatef(20.0f, 1.0f, 0.0f, 0.0f); // Tilt up 20 degrees (around X-axis)
        gl.glRotatef(15.0f, 0.0f, 0.0f, 1.0f); // Side tilt 15 degrees (around Z-axis)
        gl.glRotatef(rotateAngle, 0.0f, 1.0f, 0.0f); // Rotation animation (around Y-axis)

        // Draw Body with bodyTexture
        gl.glPushMatrix();
        gl.glScalef(1.5f, 1.0f, 1.0f);
        if (bodyTexture != null) {
            bodyTexture.bind(gl);
        }
        drawTexturedCube(gl);
        gl.glPopMatrix();

        // Left Panels (3 pieces)
        float panelSpacing = 4.0f;
        float panelWidth = 2.0f;

        for (int i = 0; i < 3; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(-3.5f - i * panelSpacing, 0.0f, 0.0f);
            gl.glScalef(panelWidth, 0.1f, 1.0f);
            if (panelTexture != null) {
                panelTexture.bind(gl);
            }
            drawTexturedCube(gl);
            gl.glPopMatrix();
        }

        // Right Panels (3 pieces)
        for (int i = 0; i < 3; i++) {
            gl.glPushMatrix();
            gl.glTranslatef(3.5f + i * panelSpacing, 0.0f, 0.0f);
            gl.glScalef(panelWidth, 0.1f, 1.0f);
            if (panelTexture != null) {
                panelTexture.bind(gl);
            }
            drawTexturedCube(gl);
            gl.glPopMatrix();
        }
        
        gl.glPopMatrix();
        gl.glEnd();
    }

    private void drawTexturedCube(GL2 gl) {
        gl.glBegin(GL2.GL_QUADS);

        // Front
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-1, -1, 1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(1, -1, 1);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(1, 1, 1);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-1, 1, 1);

        // Back
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-1, -1, -1);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-1, 1, -1);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(1, 1, -1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(1, -1, -1);

        // Left
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-1, -1, -1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(-1, -1, 1);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(-1, 1, 1);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-1, 1, -1);

        // Right
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(1, -1, -1);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(1, 1, -1);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(1, 1, 1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(1, -1, 1);

        // Top
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-1, 1, -1);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-1, 1, 1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(1, 1, 1);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(1, 1, -1);

        // Bottom
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-1, -1, -1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(1, -1, -1);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(1, -1, 1);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-1, -1, 1);

        gl.glEnd();
    }
}
