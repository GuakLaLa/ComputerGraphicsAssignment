/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;

public class Astronaut {

    private final GLU glu;

    // Animation parameters
    private float time = 0.0f;
    private float breathingAmplitude = 2.0f;
    private float swayAmplitude = 1.5f;
    private float headTiltAmplitude = 3.0f;
    private float armSwingAmplitude = 15.0f;
    private float floatSpeed = 0.5f;
    private float floatAmplitude = 0.5f;
    private float rotationVariation = 5.0f;
    private float driftAmplitudeX = 0.2f;
    private float driftAmplitudeZ = 0.2f;
    private float driftSpeedX = 0.25f;
    private float driftSpeedZ = 0.18f;

    // Textures
    private Texture texture, texture2, textureLeg, textureFlag;
    private float textureTop, textureBottom, textureLeft, textureRight;
    private float textureTop2, textureBottom2, textureLeft2, textureRight2;
    private float textureTopLeg, textureBottomLeg, textureLeftLeg, textureRightLeg;
    
    private boolean texturesInit = false;

    public Astronaut() {
        this.glu = new GLU();
//        loadTextures(gl);
    }

    private void loadTextures(GL2 gl) {
        try {
            texture = TextureIO.newTexture(getClass().getResource("/images/body.png"), false, ".png");
            texture2 = TextureIO.newTexture(getClass().getResource("/images/backpack.png"), false, ".png");
            textureLeg = TextureIO.newTexture(getClass().getResource("/images/leg.png"), false, ".png");
            textureFlag = TextureIO.newTexture(getClass().getResource("/images/flag.jpg"), false, ".jpg");

            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);

            TextureCoords textureCoords = texture.getImageTexCoords();
            textureTop = textureCoords.top();
            textureBottom = textureCoords.bottom();
            textureLeft = textureCoords.left();
            textureRight = textureCoords.right();

            TextureCoords textureCoords2 = texture2.getImageTexCoords();
            textureTop2 = textureCoords2.top();
            textureBottom2 = textureCoords2.bottom();
            textureLeft2 = textureCoords2.left();
            textureRight2 = textureCoords2.right();

            TextureCoords textureCoordsLeg = textureLeg.getImageTexCoords();
            textureTopLeg = textureCoordsLeg.top();
            textureBottomLeg = textureCoordsLeg.bottom();
            textureLeftLeg = textureCoordsLeg.left();
            textureRightLeg = textureCoordsLeg.right();
            
            texturesInit = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(float deltaTime) {
        time += deltaTime;
    }

    public void render(GL2 gl) {
        // Calculate animation parameters
        float breathing = (float) Math.sin(time * 0.5f) * breathingAmplitude;
        float sway = (float) Math.sin(time * 0.3f) * swayAmplitude;
        float headTilt = (float) Math.sin(time * 0.4f) * headTiltAmplitude;
        float armSwing = (float) Math.sin(time * 0.7f) * armSwingAmplitude;
        float leftArmSwing = (float) Math.sin(time * 1.0f) * 10.0f;
        float legSwing = (float) Math.sin(time * 0.8f) * 5.0f;
        float kneeBend = (float) Math.sin(time * 1.2f) * 10.0f;
        float floatY = (float) Math.sin(time * floatSpeed) * floatAmplitude;
        float driftX = (float) Math.sin(time * driftSpeedX) * driftAmplitudeX;
        float driftZ = (float) Math.cos(time * driftSpeedZ) * driftAmplitudeZ;

        // Calculate rotation angles
        float angleX = breathing * 0.5f + sway * 0.3f + (float) Math.sin(time * 0.15f) * rotationVariation;
        float angleY = (float) Math.sin(time * 0.12f) * rotationVariation * 2.0f;
        float angleZ = sway * 0.2f + (float) Math.cos(time * 0.18f) * rotationVariation;
        
        if(!texturesInit) {
            loadTextures(gl);
        }

        // Set up view with floating and drifting position
        gl.glPushMatrix();
        gl.glTranslatef(driftX, floatY, -8.0f + driftZ);
        gl.glRotatef(180, -0.5f, 1.0f, 0.0f);

        // Apply rotations
        gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(angleZ, 0.0f, 0.0f, 1.0f);

        // Draw all components
        drawBody(gl, breathing);
        drawHead(gl, headTilt);
        drawLeftArm(gl, leftArmSwing);
        drawRightArm(gl, armSwing);
        drawFlag(gl);
        drawLeg(gl, -0.2f, legSwing, kneeBend);   // Left leg
        drawLeg(gl, 0.2f, -legSwing, -kneeBend);  // Right leg
        drawBackpack(gl);

        gl.glPopMatrix();
    }

    // Example of one method:
    private void drawBody(GL2 gl, float breathing) {
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.2f, 0.0f);
        float breathScale = 1.0f + (float) Math.sin(time * 0.5f) * 0.02f;
        gl.glScalef(0.8f * breathScale, 1.2f * breathScale, 0.4f);

        texture.enable(gl);
        texture.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        // Front face
        gl.glTexCoord2f(textureLeft, textureBottom);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glTexCoord2f(textureRight, textureBottom);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glTexCoord2f(textureRight, textureTop);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glTexCoord2f(textureLeft, textureTop);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        // Back face
        gl.glTexCoord2f(textureRight, textureBottom);
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glTexCoord2f(textureLeft, textureBottom);
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        gl.glTexCoord2f(textureLeft, textureTop);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        gl.glTexCoord2f(textureRight, textureTop);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        // Left face
        gl.glTexCoord2f(textureLeft, textureBottom);
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glTexCoord2f(textureRight, textureBottom);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glTexCoord2f(textureRight, textureTop);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glTexCoord2f(textureLeft, textureTop);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        // Right face
        gl.glTexCoord2f(textureLeft, textureBottom);
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        gl.glTexCoord2f(textureRight, textureBottom);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        gl.glTexCoord2f(textureRight, textureTop);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glTexCoord2f(textureLeft, textureTop);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        // Top face
        gl.glTexCoord2f(textureLeft, textureBottom);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        gl.glTexCoord2f(textureRight, textureBottom);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glTexCoord2f(textureRight, textureTop);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glTexCoord2f(textureLeft, textureTop);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        // Bottom face
        gl.glTexCoord2f(textureLeft, textureBottom);
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glTexCoord2f(textureRight, textureBottom);
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        gl.glTexCoord2f(textureRight, textureTop);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glTexCoord2f(textureLeft, textureTop);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glEnd();
        gl.glPopMatrix();
        texture.disable(gl);
    }

    private void drawJointSphere(GL2 gl) {
        float[] jointColor = {0.9f, 0.9f, 0.9f, 1.0f};
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, jointColor, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, jointColor, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 50f);
        GLUquadric joint = glu.gluNewQuadric();
        glu.gluQuadricNormals(joint, GLU.GLU_SMOOTH);
        glu.gluSphere(joint, 0.1f, 20, 20);
    }

    private void drawFlag(GL2 gl) {
        gl.glPushMatrix();
        // Position flag at shoulder
        gl.glTranslatef(0.6f, 0.3f, 0.0f);

        // Match arm rotations
        gl.glRotatef(20, 1, 0, 0); // Forward tilt
        gl.glRotatef(30 + (float) Math.sin(time * 2.0) * 30, 0, 0, 1); // Side wave

        // Position flag pole
        gl.glRotatef(90, 1, 0, 0); // Rotate for arm cylinder
        gl.glTranslatef(0, 0, 0.7f); // Move to elbow
        gl.glRotatef(30 + (float) Math.sin(time * 2.0) * 20, 1, 0, 0);
        gl.glTranslatef(0, 0, 0.6f); // Move to hand position
        gl.glTranslatef(0.08f, 0, 0); // Move to side of hand
        gl.glRotatef(90, 0, 1, 0); // Rotate pole to vertical

        // Draw flag pole
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glColor3f(0.7f, 0.7f, 0.7f); // Gray color
        GLUquadric pole = glu.gluNewQuadric();
        glu.gluQuadricTexture(pole, false);
        glu.gluCylinder(pole, 0.025f, 0.02f, 0.7f, 10, 10);
        gl.glEnable(GL2.GL_TEXTURE_2D);

        // Draw flag
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, 0.5f); // Position flag

        // Orient flag and add waving motion
        gl.glRotatef(180, 0, 1, 0); // Face outward
        gl.glRotatef((float) Math.sin(time * 3.0) * 15.0f, 0, 1, 0); // Waving motion
        gl.glRotatef(90, 1, 0, 0); // Make flag horizontal

        // Draw flag rectangle
        textureFlag.enable(gl);
        textureFlag.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(0, -0.2f, 0);  // Bottom-left
        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(0.6f, -0.2f, 0); // Bottom-right
        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(0.6f, 0.2f, 0);  // Top-right
        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(0, 0.2f, 0);     // Top-left
        gl.glEnd();
        textureFlag.disable(gl);

        gl.glPopMatrix();
        gl.glPopMatrix();
    }

    private void drawHead(GL2 gl, float headTilt) {
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 1.0f, 0.0f);
        gl.glRotatef(headTilt, 0.0f, 0.0f, 1.0f);

        // Head sphere
        GLUquadric head = glu.gluNewQuadric();
        glu.gluQuadricNormals(head, GLU.GLU_SMOOTH);
        glu.gluSphere(head, 0.35f, 30, 30);

        // Helmet (semi-transparent)
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glColor4f(1f, 1f, 1f, 0.3f);
        GLUquadric dome = glu.gluNewQuadric();
        glu.gluQuadricNormals(dome, GLU.GLU_SMOOTH);
        glu.gluSphere(dome, 0.4f, 30, 30);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_BLEND);
        gl.glColor3f(1f, 1f, 1f);
        gl.glPopMatrix();
    }

    private void drawLeftArm(GL2 gl, float leftArmSwing) {
        gl.glPushMatrix();
        gl.glTranslatef(-0.6f, 0.3f, 0.0f);
        gl.glRotatef(20 + leftArmSwing, 1, 0, 0);

        textureLeg.enable(gl);
        textureLeg.bind(gl);
        GLUquadric shoulderL = glu.gluNewQuadric();
        glu.gluQuadricTexture(shoulderL, true);
        glu.gluQuadricNormals(shoulderL, GLU.GLU_SMOOTH);
        glu.gluSphere(shoulderL, 0.1f, 20, 20);

        // Upper arm
        gl.glRotatef(90, 1f, 0f, 0f);
        GLUquadric armQuadricL = glu.gluNewQuadric();
        glu.gluQuadricTexture(armQuadricL, true);
        glu.gluQuadricNormals(armQuadricL, GLU.GLU_SMOOTH);
        glu.gluCylinder(armQuadricL, 0.1f, 0.1f, 0.7f, 20, 20);

        // Elbow joint
        gl.glTranslatef(0.0f, 0.0f, 0.7f);
        drawJointSphere(gl);

        // Lower arm
        glu.gluCylinder(armQuadricL, 0.08f, 0.08f, 0.6f, 20, 20);
        gl.glTranslatef(0.0f, 0.0f, 0.6f);
        drawJointSphere(gl);
        textureLeg.disable(gl);
        gl.glPopMatrix();
    }

    private void drawRightArm(GL2 gl, float armSwing) {
        gl.glPushMatrix();
        gl.glTranslatef(0.6f, 0.3f, 0.0f);

        // Base arm position with waving motion
        gl.glRotatef(20, 1, 0, 0); // Arm slightly forward
        gl.glRotatef(30 + (float) Math.sin(time * 2.0) * 30, 0, 0, 1); // Side-to-side wave

        textureLeg.enable(gl);
        textureLeg.bind(gl);
        GLUquadric shoulderR = glu.gluNewQuadric();
        glu.gluQuadricTexture(shoulderR, true);
        glu.gluQuadricNormals(shoulderR, GLU.GLU_SMOOTH);
        glu.gluSphere(shoulderR, 0.1f, 20, 20);

        // Upper arm
        gl.glRotatef(90, 1f, 0f, 0f);
        GLUquadric armQuadricR = glu.gluNewQuadric();
        glu.gluQuadricTexture(armQuadricR, true);
        glu.gluQuadricNormals(armQuadricR, GLU.GLU_SMOOTH);
        glu.gluCylinder(armQuadricR, 0.1f, 0.1f, 0.7f, 20, 20);

        // Elbow joint
        gl.glTranslatef(0.0f, 0.0f, 0.7f);
        drawJointSphere(gl);

        // Lower arm with bending during wave
        gl.glRotatef(30 + (float) Math.sin(time * 2.0) * 20, 1, 0, 0);
        glu.gluCylinder(armQuadricR, 0.08f, 0.08f, 0.6f, 20, 20);
        gl.glTranslatef(0.0f, 0.0f, 0.6f);
        drawJointSphere(gl);
        textureLeg.disable(gl);
        gl.glPopMatrix();
    }

    private void drawLeg(GL2 gl, float xPos, float legSwing, float kneeBend) {
        gl.glPushMatrix();
        gl.glTranslatef(xPos, -0.5f, 0.0f);
        gl.glRotatef(legSwing, 1, 0, 0);  // Leg swing animation
        drawJointSphere(gl);

        gl.glTranslatef(0.0f, -0.3f, 0.0f);
        gl.glRotatef(90, 1f, 0f, 0f);

        textureLeg.enable(gl);
        textureLeg.bind(gl);

        // Thigh
        GLUquadric thigh = glu.gluNewQuadric();
        glu.gluQuadricTexture(thigh, true);
        glu.gluCylinder(thigh, 0.15f, 0.15f, 1.0f, 20, 20);

        // Knee
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        drawJointSphere(gl);

        // Knee bend animation
        gl.glRotatef(kneeBend, 1, 0, 0);

        // Shin
        GLUquadric shin = glu.gluNewQuadric();
        glu.gluQuadricTexture(shin, true);
        glu.gluCylinder(shin, 0.12f, 0.12f, 1.0f, 20, 20);

        // Ankle
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        drawJointSphere(gl);

        textureLeg.disable(gl);
        gl.glPopMatrix();
    }

    private void drawBackpack(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, -0.2f, 0.35f);
        gl.glScalef(1.0f, 1.4f, 0.5f);

        texture2.enable(gl);
        texture2.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        // Front face
        gl.glTexCoord2f(textureLeft2, textureBottom2);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glTexCoord2f(textureRight2, textureBottom2);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glTexCoord2f(textureRight2, textureTop2);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glTexCoord2f(textureLeft2, textureTop2);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        // Back face
        gl.glTexCoord2f(textureRight2, textureBottom2);
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glTexCoord2f(textureLeft2, textureBottom2);
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        gl.glTexCoord2f(textureLeft2, textureTop2);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        gl.glTexCoord2f(textureRight2, textureTop2);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        // Left face
        gl.glTexCoord2f(textureLeft2, textureBottom2);
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glTexCoord2f(textureRight2, textureBottom2);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glTexCoord2f(textureRight2, textureTop2);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glTexCoord2f(textureLeft2, textureTop2);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        // Right face
        gl.glTexCoord2f(textureLeft2, textureBottom2);
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        gl.glTexCoord2f(textureRight2, textureBottom2);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        gl.glTexCoord2f(textureRight2, textureTop2);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glTexCoord2f(textureLeft2, textureTop2);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        // Top face
        gl.glTexCoord2f(textureLeft2, textureBottom2);
        gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        gl.glTexCoord2f(textureRight2, textureBottom2);
        gl.glVertex3f(-0.5f, 0.5f, 0.5f);
        gl.glTexCoord2f(textureRight2, textureTop2);
        gl.glVertex3f(0.5f, 0.5f, 0.5f);
        gl.glTexCoord2f(textureLeft2, textureTop2);
        gl.glVertex3f(0.5f, 0.5f, -0.5f);
        // Bottom face
        gl.glTexCoord2f(textureLeft2, textureBottom2);
        gl.glVertex3f(-0.5f, -0.5f, -0.5f);
        gl.glTexCoord2f(textureRight2, textureBottom2);
        gl.glVertex3f(0.5f, -0.5f, -0.5f);
        gl.glTexCoord2f(textureRight2, textureTop2);
        gl.glVertex3f(0.5f, -0.5f, 0.5f);
        gl.glTexCoord2f(textureLeft2, textureTop2);
        gl.glVertex3f(-0.5f, -0.5f, 0.5f);
        gl.glEnd();
        texture2.disable(gl);
        gl.glPopMatrix();
    }
}
