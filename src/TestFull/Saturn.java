/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

/**
 *
 * @author Asus
 */
public class Saturn extends Planet {

    private float relativeSpeed = 0.33f;
    private float inclination = -15f;
    private Texture ringTexture;
    private float ringInnerRadius = 0.5f;
    private float ringOuterRadius = 2.5f;

    public Saturn(float radius, float distanceFromCenter, float rotationSpeed, float earthOrbitalSpeed) {
        super("/images/saturn.jpg", radius, distanceFromCenter, rotationSpeed, Planet.SelfRotateAxis.Z_Axis);
        this.orbitalSpeed = earthOrbitalSpeed * relativeSpeed;
        rotationAngles = new float[]{-90, inclination, 0};
        ringInnerRadius += radius + 0.1f;
        ringOuterRadius += radius + 0.1f;
    }

    @Override
    public void render(GL2 gl) {
        renderDefault(gl);
        drawRing(gl);
    }

    @Override
    public void updateRotation() {
        updateRotationDefault();
    }

    private void drawRing(GL2 gl) {
        if (ringTexture == null) {
            ringTexture = loadTexture("/images/saturn_rings.jpeg");
        }

        final int slices = 100;
        ringTexture.enable(gl);
        ringTexture.bind(gl);

        gl.glPushMatrix();

//        gl.glRotatef(orbitalAngle, 0f, 1f, 0f); // Orbital angle
//        gl.glTranslatef(distanceFromCenter, 0f, 0f); // Orbital radius
        gl.glTranslatef(X, Y, Z); // Move Earth to position

        gl.glRotatef(-90f, 1f, 0f, 0f); // Align ring in XZ plane
        gl.glRotatef(inclination, 0f, 1f, 0f); // Align ring in XZ plane

        int segments = 100;

        gl.glDisable(GL2.GL_LIGHTING);          // Disable lighting for flat texture
//        gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[1]);
        gl.glDisable(GL2.GL_BLEND);
        gl.glColor4f(1f, 1f, 1f, 1f);           // Set full opacity white color

        // Draw ring as triangle strip between inner and outer radii
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            gl.glTexCoord2f((float) i / segments, 0f);
            gl.glVertex3f(cos * ringOuterRadius, sin * ringOuterRadius, 0);

            gl.glTexCoord2f((float) i / segments, 1f);
            gl.glVertex3f(cos * ringInnerRadius, sin * ringInnerRadius, 0);
        }
        gl.glEnd();

        gl.glPopMatrix();
        ringTexture.disable(gl);
    }
}
