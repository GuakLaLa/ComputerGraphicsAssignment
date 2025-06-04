/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package computergraphicsassignment.component;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 *
 * @author user
 */
public class Rocket {
    // Rocket dimensions (scaled up from original)
    private static final float BODY_RADIUS = 1.5f;
    private static final float NOSE_LENGTH = 3.0f;
    private static final float BODY_LENGTH = 7.5f;
    private static final float FIN_LENGTH = 2.5f;
    
    private float x = 0, y = 0, z = 0;
    private float speed = 0.5f;
    private GLU glu = new GLU();
    private GLUT glut = new GLUT();
    
    // Orientation control
    private float pitchAngle = 0;
    private float yawAngle = 0;
    private float rollAngle = 0;
    private final float ROTATION_SPEED = 1.2f;
    private final float MAX_ANGLE = 35f;
    
    // Rocket components
    private boolean showDetails = true;
    private boolean showFlames = true;
    private float fuel = 100f;
    private float speedMultiplier = 1f;
    private float flamePulse = 0f;
    private boolean flameGrowing = true;
    private final float FLAME_PULSE_SPEED = 0.1f;
    private final float MIN_FLAME_SIZE = 2.5f;
    private final float MAX_FLAME_SIZE = 4.0f;

    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        
        // Apply rotations
        gl.glRotatef(rollAngle, 0, 0, 1);
        gl.glRotatef(pitchAngle, 1, 0, 0);
        gl.glRotatef(yawAngle, 0, 1, 0);
        gl.glRotatef(-90, 1, 0, 0); // Stand upright
        
        // Enable lighting
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        
        // Draw main components
        drawMainBody(gl);
        drawNoseCone(gl);
        drawFins(gl);
        drawEngines(gl);
        
        if (showDetails) {
            drawDetails(gl);
        }
        
        if (showFlames) {
            drawExhaust(gl);
        }
        
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glPopMatrix();
    }
    
    private void drawMainBody(GL2 gl) {
        // Enable texture mapping if you're using textures
        gl.glEnable(GL2.GL_TEXTURE_2D);

        // Main cylindrical body with panel detailing
        gl.glColor3f(0.75f, 0.75f, 0.8f); // Brushed metal color
        GLUquadric body = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(body, GLU.GLU_FILL);
        glu.gluQuadricNormals(body, GLU.GLU_SMOOTH);
        glu.gluCylinder(body, BODY_RADIUS, BODY_RADIUS * 0.97f, BODY_LENGTH, 32, 32);

        // Add panel lines and structural details
        drawPanelDetails(gl);

        // Add thermal protection tiles (like Space Shuttle)
        drawThermalTiles(gl);

        // Add warning labels and markings
        drawWarningLabels(gl);

        // Add access panels and hatches
        drawAccessPanels(gl);

        // Add rivets and fasteners
        drawRivets(gl);

        // Add corrugated metal sections
        drawCorrugatedSections(gl);

        glu.gluDeleteQuadric(body);
        gl.glDisable(GL2.GL_TEXTURE_2D);
    }

    private void drawPanelDetails(GL2 gl) {
        gl.glColor3f(0.3f, 0.3f, 0.35f); // Dark gray for panel lines

        // Vertical panel lines
        int verticalPanels = 8;
        for (int i = 0; i < verticalPanels; i++) {
            gl.glPushMatrix();
            gl.glRotatef(i * (360f/verticalPanels), 0, 0, 1);
            gl.glBegin(GL2.GL_QUAD_STRIP);
            for (float z = 0; z <= BODY_LENGTH; z += 0.1f) {
                float width = 0.02f;
                float xOuter = (BODY_RADIUS + width) * 1.01f;
                float xInner = (BODY_RADIUS - width) * 0.99f;
                gl.glVertex3f(xOuter, 0, z);
                gl.glVertex3f(xInner, 0, z);
            }
            gl.glEnd();
            gl.glPopMatrix();
        }

        // Horizontal panel lines every meter
        int horizontalPanels = (int)BODY_LENGTH;
        for (int i = 0; i < horizontalPanels; i++) {
            float z = i * 1.0f;
            drawRing(gl, BODY_RADIUS * 1.02f, BODY_RADIUS * 0.98f, z, 0.03f);
        }
    }

    private void drawThermalTiles(GL2 gl) {
        gl.glColor3f(0.2f, 0.2f, 0.25f); // Dark gray/black tiles

        // Tile pattern on lower section (near engines)
        float tileStartZ = BODY_LENGTH * 0.7f;
        int tileRows = 5;
        int tilesPerRow = 16;

        for (int row = 0; row < tileRows; row++) {
            float z = tileStartZ - (row * 0.3f);
            for (int i = 0; i < tilesPerRow; i++) {
                gl.glPushMatrix();
                gl.glRotatef(i * (360f/tilesPerRow), 0, 0, 1);
                gl.glTranslatef(BODY_RADIUS * 0.95f, 0, z);

                // Draw individual tile
                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex3f(0, 0.15f, 0);
                gl.glVertex3f(0.2f, 0.15f, 0);
                gl.glVertex3f(0.2f, -0.15f, 0);
                gl.glVertex3f(0, -0.15f, 0);
                gl.glEnd();

                gl.glPopMatrix();
            }
        }
    }

    private void drawWarningLabels(GL2 gl) {
        gl.glColor3f(1f, 0f, 0f); // Bright red for warnings

        // Danger stripes near engines
        for (float z = 0; z < BODY_LENGTH * 0.3f; z += 0.2f) {
            drawRing(gl, BODY_RADIUS * 1.01f, BODY_RADIUS * 0.99f, z, 0.1f);
        }

        // White/black "NO STEP" areas
        gl.glColor3f(1f, 1f, 1f);
        for (int i = 0; i < 4; i++) {
            gl.glPushMatrix();
            gl.glRotatef(i * 90 + 45, 0, 0, 1);
            gl.glTranslatef(BODY_RADIUS * 0.98f, 0, BODY_LENGTH * 0.5f);

            // Draw warning area
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(1f, 1f, 1f);
            gl.glVertex3f(0, 0.3f, 0);
            gl.glVertex3f(0.4f, 0.3f, 0);
            gl.glColor3f(0f, 0f, 0f);
            gl.glVertex3f(0.4f, -0.3f, 0);
            gl.glVertex3f(0, -0.3f, 0);
            gl.glEnd();

            gl.glPopMatrix();
        }
    }

    private void drawAccessPanels(GL2 gl) {
        gl.glColor3f(0.5f, 0.5f, 0.55f); // Slightly different metal color

        // Main access panel
        gl.glPushMatrix();
        gl.glRotatef(30, 0, 0, 1);
        gl.glTranslatef(BODY_RADIUS * 0.9f, 0, BODY_LENGTH * 0.4f);

        // Panel with raised edges
        gl.glBegin(GL2.GL_QUADS);
        // Panel surface
        gl.glVertex3f(-0.5f, 0.3f, 0);
        gl.glVertex3f(0.5f, 0.3f, 0);
        gl.glVertex3f(0.5f, -0.3f, 0);
        gl.glVertex3f(-0.5f, -0.3f, 0);

        // Raised edge
        gl.glColor3f(0.4f, 0.4f, 0.45f);
        gl.glVertex3f(-0.5f, 0.3f, 0);
        gl.glVertex3f(-0.5f, 0.3f, -0.05f);
        gl.glVertex3f(0.5f, 0.3f, -0.05f);
        gl.glVertex3f(0.5f, 0.3f, 0);

        gl.glEnd();
        gl.glPopMatrix();
    }

    private void drawRivets(GL2 gl) {
        gl.glColor3f(0.4f, 0.4f, 0.4f); // Dark gray rivets

        int rivetRows = (int)(BODY_LENGTH * 2);
        int rivetsPerRow = 24;

        for (int row = 0; row < rivetRows; row++) {
            float z = row * 0.5f;
            for (int i = 0; i < rivetsPerRow; i++) {
                gl.glPushMatrix();
                gl.glRotatef(i * (360f/rivetsPerRow), 0, 0, 1);
                gl.glTranslatef(BODY_RADIUS * 0.99f, 0, z);

                // Draw simple rivet (could be replaced with small sphere)
                gl.glBegin(GL2.GL_POINTS);
                gl.glVertex3f(0, 0, 0);
                gl.glEnd();

                gl.glPopMatrix();
            }
        }
    }

    private void drawCorrugatedSections(GL2 gl) {
        // Add corrugated metal sections for structural areas
        gl.glColor3f(0.7f, 0.7f, 0.75f);

        // Section near middle of rocket
        float startZ = BODY_LENGTH * 0.3f;
        float endZ = BODY_LENGTH * 0.4f;
        int segments = 20;

        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i <= segments; i++) {
            float z = startZ + (endZ - startZ) * (i / (float)segments);
            float wave = (float)Math.sin(z * 20) * 0.03f;
            for (int j = 0; j <= 32; j++) {
                float angle = (float)(j * 2 * Math.PI / 32);
                float x = (BODY_RADIUS + wave) * (float)Math.cos(angle);
                float y = (BODY_RADIUS + wave) * (float)Math.sin(angle);
                gl.glVertex3f(x, y, z);
                gl.glVertex3f(x, y, z + 0.01f);
            }
        }
        gl.glEnd();
    }
    
    private void drawNoseCone(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, BODY_LENGTH);
        
        // Main nose cone
        gl.glColor3f(0.8f, 0.8f, 0.85f);
        GLUquadric nose = glu.gluNewQuadric();
        glu.gluCylinder(nose, BODY_RADIUS * 0.95f, 0, NOSE_LENGTH, 32, 32);
        
        // Tip
        gl.glColor3f(0.9f, 0.9f, 0.95f);
        glu.gluSphere(nose, BODY_RADIUS * 0.15f, 16, 16);
        
        // Antenna
        gl.glTranslatef(0, 0, NOSE_LENGTH * 0.8f);
        glu.gluCylinder(nose, 0.05f, 0.05f, 0.8f, 8, 1);
        
        glu.gluDeleteQuadric(nose);
        gl.glPopMatrix();
    }
    
    private void drawFins(GL2 gl) {
        gl.glColor3f(0.6f, 0.6f, 0.65f);
        
        for (int i = 0; i < 4; i++) {
            gl.glPushMatrix();
            gl.glRotatef(i * 90, 0, 1, 0);
            gl.glTranslatef(BODY_RADIUS, 0, 0);
            
            // Dynamic fin movement
            if (i == 0 || i == 2) { // Side fins
                float tilt = (i == 0) ? -rollAngle : rollAngle;
                gl.glRotatef(tilt, 0, 0, 1);
            }
            
            // Fin shape
            gl.glBegin(GL2.GL_TRIANGLES);
            gl.glVertex3f(0, 0, 0);
            gl.glVertex3f(FIN_LENGTH * 0.7f, 0, -FIN_LENGTH);
            gl.glVertex3f(0, 0, -FIN_LENGTH);
            gl.glEnd();
            
            // Fin thickness
            gl.glBegin(GL2.GL_QUAD_STRIP);
            gl.glVertex3f(0, 0, 0);
            gl.glVertex3f(0, 0.2f, 0);
            gl.glVertex3f(FIN_LENGTH * 0.7f, 0, -FIN_LENGTH);
            gl.glVertex3f(FIN_LENGTH * 0.7f, 0.2f, -FIN_LENGTH);
            gl.glVertex3f(0, 0, -FIN_LENGTH);
            gl.glVertex3f(0, 0.2f, -FIN_LENGTH);
            gl.glEnd();
            
            gl.glPopMatrix();
        }
    }
    
    private void drawEngines(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, -0.5f);
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        
        // Main engine cluster
        GLUquadric engine = glu.gluNewQuadric();
        glu.gluDisk(engine, 0, BODY_RADIUS * 0.8f, 32, 1);
        
        // Draw individual engines
        for (int i = 0; i < 5; i++) {
            gl.glPushMatrix();
            float angle = (float)(i * (2 * Math.PI / 5));
            float offset = BODY_RADIUS * 0.6f;
            gl.glTranslatef((float)Math.cos(angle) * offset, 
                           (float)Math.sin(angle) * offset, 
                           0);
            
            // Engine nozzle
            glu.gluCylinder(engine, 0.3f, 0.2f, 0.5f, 16, 1);
            gl.glPopMatrix();
        }
        
        glu.gluDeleteQuadric(engine);
        gl.glPopMatrix();
    }
    
    private void drawDetails(GL2 gl) {
        // Windows
        gl.glColor3f(0.4f, 0.6f, 0.9f);
        for (int i = 0; i < 3; i++) {
            gl.glPushMatrix();
            gl.glRotatef(i * 120, 0, 1, 0);
            gl.glTranslatef(BODY_RADIUS * 0.9f, 0, BODY_LENGTH * 0.7f);
            
            GLUquadric window = glu.gluNewQuadric();
            glu.gluDisk(window, 0, 0.3f, 16, 1);
            glu.gluDeleteQuadric(window);
            gl.glPopMatrix();
        }
        
        // RCS thrusters
        gl.glColor3f(0.4f, 0.4f, 0.4f);
        for (int i = 0; i < 8; i++) {
            gl.glPushMatrix();
            float angle = (float)(i * (2 * Math.PI / 8));
            gl.glRotatef((float)Math.toDegrees(angle), 0, 1, 0);
            gl.glTranslatef(BODY_RADIUS * 0.95f, 0, BODY_LENGTH * 0.3f);
            
            GLUquadric thruster = glu.gluNewQuadric();
            glu.gluCylinder(thruster, 0.1f, 0.1f, 0.2f, 8, 1);
            glu.gluDeleteQuadric(thruster);
            gl.glPopMatrix();
        }
    }
    
//    private void drawExhaust(GL2 gl) {
//        gl.glPushMatrix();
//        gl.glTranslatef(0, 0, -0.5f);
//        gl.glRotatef(180, 1, 0, 0);
//        gl.glDisable(GL2.GL_LIGHTING);
//        
//        // Main exhaust plume
//        gl.glColor3f(1.0f, 0.5f, 0.0f);
//        GLUquadric flame = glu.gluNewQuadric();
//        glu.gluCylinder(flame, BODY_RADIUS * 0.7f, BODY_RADIUS * 0.3f, 3.0f, 32, 1);
//        
//        // Inner core flame
//        gl.glColor3f(1.0f, 0.9f, 0.0f);
//        glu.gluCylinder(flame, BODY_RADIUS * 0.4f, BODY_RADIUS * 0.1f, 4.0f, 32, 1);
//        
//        glu.gluDeleteQuadric(flame);
//        gl.glEnable(GL2.GL_LIGHTING);
//        gl.glPopMatrix();
//    }
    
    private void drawRing(GL2 gl, float outerRadius, float innerRadius, float zPos, float width) {
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, zPos);
        
        GLUquadric ring = glu.gluNewQuadric();
        glu.gluDisk(ring, innerRadius, outerRadius, 32, 1);
        
        // Sides of the ring
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i <= 32; i++) {
            float angle = (float)(i * 2 * Math.PI / 32);
            float x = outerRadius * (float)Math.cos(angle);
            float y = outerRadius * (float)Math.sin(angle);
            gl.glVertex3f(x, y, 0);
            gl.glVertex3f(x, y, width);
        }
        gl.glEnd();
        
        glu.gluDeleteQuadric(ring);
        gl.glPopMatrix();
    }
    
    // Movement methods (similar to original but adjusted for size)
    public void moveForward() {
        pitchAngle = Math.min(pitchAngle + ROTATION_SPEED, MAX_ANGLE);
        z -= speed * Math.cos(Math.toRadians(pitchAngle));
        y -= speed * Math.sin(Math.toRadians(pitchAngle));
    }
    
    public void moveBackward() {
        pitchAngle = Math.max(pitchAngle - ROTATION_SPEED, -MAX_ANGLE);
        z += speed * Math.cos(Math.toRadians(pitchAngle));
        y += speed * Math.sin(Math.toRadians(pitchAngle));
    }
    
    public void moveLeft() {
        yawAngle = Math.min(yawAngle + ROTATION_SPEED, MAX_ANGLE);
        rollAngle = Math.min(rollAngle + ROTATION_SPEED, MAX_ANGLE);
        x -= speed * Math.cos(Math.toRadians(yawAngle));
    }
    
    public void moveRight() {
        yawAngle = Math.max(yawAngle - ROTATION_SPEED, -MAX_ANGLE);
        rollAngle = Math.max(rollAngle - ROTATION_SPEED, -MAX_ANGLE);
        x += speed * Math.cos(Math.toRadians(yawAngle));
    }
    
    public void moveUp() {
        // Adjust pitch upward (nose moves up)
        pitchAngle = Math.max(pitchAngle - ROTATION_SPEED, -MAX_ANGLE);
        // Move upward while considering current orientation
        y += speed * Math.cos(Math.toRadians(pitchAngle));
        // Also move forward slightly when pitching up
        z -= speed * 0.3f * Math.sin(Math.toRadians(pitchAngle));
        consumeFuel(0.05f);
    }

    public void moveDown() {
        // Adjust pitch downward (nose moves down)
        pitchAngle = Math.min(pitchAngle + ROTATION_SPEED, MAX_ANGLE);
        // Move downward while considering current orientation
        y -= speed * Math.cos(Math.toRadians(pitchAngle));
        // Also move backward slightly when pitching down
        z += speed * 0.3f * Math.sin(Math.toRadians(pitchAngle));
        consumeFuel(0.05f);
    }
    
    private void consumeFuel(float amount) {
        fuel = Math.max(0, fuel - amount);
        if (fuel <= 0) {
            speedMultiplier = 0.5f; // Reduced speed when out of fuel
        }
    }
    
    public void update() {
        // Gradual stabilization
        pitchAngle *= 0.95f;
        yawAngle *= 0.95f;
        rollAngle *= 0.95f;
        
        // Deadzone
        if (Math.abs(pitchAngle) < 0.5f) pitchAngle = 0;
        if (Math.abs(yawAngle) < 0.5f) yawAngle = 0;
        if (Math.abs(rollAngle) < 0.5f) rollAngle = 0;
    }
    
    private void drawExhaust(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, -0.5f);
        gl.glRotatef(180, 1, 0, 0);
        gl.glDisable(GL2.GL_LIGHTING);

        // Update flame animation
        updateFlamePulse();

        // Draw multi-layered flame with gradient colors
        drawFlameCore(gl);
        drawFlameMiddleLayer(gl);
        drawFlameOuterLayer(gl);

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glPopMatrix();
    }

    private void updateFlamePulse() {
        if (flameGrowing) {
            flamePulse += FLAME_PULSE_SPEED;
            if (flamePulse >= 1.0f) flameGrowing = false;
        } else {
            flamePulse -= FLAME_PULSE_SPEED;
            if (flamePulse <= 0.0f) flameGrowing = true;
        }
    }

    private void drawFlameCore(GL2 gl) {
        // Core (hottest part - yellow/white)
        float coreSize = MIN_FLAME_SIZE + (MAX_FLAME_SIZE - MIN_FLAME_SIZE) * flamePulse * 0.7f;
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glColor4f(1.0f, 1.0f, 0.7f, 0.9f); // Bright yellow-white
        gl.glVertex3f(0, 0, 0); // Center point

        // Create gradient to orange
        int segments = 16;
        for (int i = 0; i <= segments; i++) {
            float angle = (float)(i * 2 * Math.PI / segments);
            float x = (float)Math.cos(angle) * BODY_RADIUS * 0.15f;
            float y = (float)Math.sin(angle) * BODY_RADIUS * 0.15f;
            gl.glColor4f(1.0f, 0.7f, 0.2f, 0.7f); // Orange
            gl.glVertex3f(x, y, coreSize * 0.3f);
        }
        gl.glEnd();

        // Main core body
        GLUquadric core = glu.gluNewQuadric();
        gl.glColor4f(1.0f, 0.9f, 0.3f, 0.8f);
        glu.gluCylinder(core, BODY_RADIUS * 0.15f, BODY_RADIUS * 0.05f, coreSize, 16, 1);
        glu.gluDeleteQuadric(core);
    }

    private void drawFlameMiddleLayer(GL2 gl) {
        // Middle layer (orange)
        float middleSize = MIN_FLAME_SIZE * 0.8f + (MAX_FLAME_SIZE * 0.8f - MIN_FLAME_SIZE * 0.8f) * flamePulse;
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glColor4f(1.0f, 0.7f, 0.2f, 0.7f); // Orange
        gl.glVertex3f(0, 0, 0); // Center point

        // Create gradient to red
        int segments = 16;
        for (int i = 0; i <= segments; i++) {
            float angle = (float)(i * 2 * Math.PI / segments);
            float x = (float)Math.cos(angle) * BODY_RADIUS * 0.4f;
            float y = (float)Math.sin(angle) * BODY_RADIUS * 0.4f;
            gl.glColor4f(1.0f, 0.4f, 0.1f, 0.5f); // Red-orange
            gl.glVertex3f(x, y, middleSize * 0.5f);
        }
        gl.glEnd();

        // Main middle body
        GLUquadric middle = glu.gluNewQuadric();
        gl.glColor4f(1.0f, 0.6f, 0.2f, 0.6f);
        glu.gluCylinder(middle, BODY_RADIUS * 0.4f, BODY_RADIUS * 0.2f, middleSize, 16, 1);
        glu.gluDeleteQuadric(middle);
    }

    private void drawFlameOuterLayer(GL2 gl) {
        // Outer layer (red with transparency)
        float outerSize = MIN_FLAME_SIZE * 0.6f + (MAX_FLAME_SIZE * 0.6f - MIN_FLAME_SIZE * 0.6f) * flamePulse;

        // Enable blending for transparency
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        GLUquadric outer = glu.gluNewQuadric();
        gl.glColor4f(1.0f, 0.3f, 0.1f, 0.4f); // Red with transparency
        glu.gluCylinder(outer, BODY_RADIUS * 0.7f, BODY_RADIUS * 0.3f, outerSize, 24, 1);

        // Add some turbulence to the flame tips
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glColor4f(1.0f, 0.3f, 0.1f, 0.3f);
        gl.glVertex3f(0, 0, outerSize * 0.8f);

        int segments = 24;
        for (int i = 0; i <= segments; i++) {
            float angle = (float)(i * 2 * Math.PI / segments);
            float turbulence = 0.1f * (float)Math.sin(angle * 3 + System.currentTimeMillis() * 0.003f);
            float x = (float)Math.cos(angle) * (BODY_RADIUS * 0.3f + turbulence);
            float y = (float)Math.sin(angle) * (BODY_RADIUS * 0.3f + turbulence);
            gl.glColor4f(1.0f, 0.2f, 0.1f, 0.1f); // Faint red at edges
            gl.glVertex3f(x, y, outerSize * 1.2f);
        }
        gl.glEnd();

        glu.gluDeleteQuadric(outer);
        gl.glDisable(GL2.GL_BLEND);
    }
}
