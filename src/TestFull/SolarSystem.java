package TestFull;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class SolarSystem implements GLEventListener {

    private static final float earthOrbitalSpeed = 0.8f;
    private List<Planet> planets = new ArrayList<>();
    private static final Earth earth = new Earth(1.8f, 14.5f, 5f, earthOrbitalSpeed);

    // Camera control
    private boolean isTrackingEarth = false;
    private boolean isTransitioning = false;
    private float transitionProgress = 0f;
    private final float TRANSITION_DURATION = 2.0f;
    private float cameraDistance = 70f;
    private float cameraFOV = 45f;
    private float[] lookAtPosition = {0f, 0f, 0f};

    // Add these class variables
    private float earthViewDistance = 15f; // Default zoom distance for Earth view
    private float sunViewDistance = 70f;   // Default distance for Sun view
    private float earthViewFOV = 40f;      // Narrow FOV for zoomed-in view
    private float sunViewFOV = 35f;        // Wider FOV for solar system view

    // Window dimensions
    private int width = 1000;
    private int height = 800;

    public static void main(String[] args) {
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);

        SolarSystem solarSystem = new SolarSystem();
        canvas.addGLEventListener(solarSystem);

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (solarSystem.isTrackingEarth || solarSystem.isTransitioning) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W ->
                            earth.getRocket().moveUp();
                        case KeyEvent.VK_S ->
                            earth.getRocket().moveDown();
                        case KeyEvent.VK_A ->
                            earth.getRocket().moveLeft();
                        case KeyEvent.VK_D ->
                            earth.getRocket().moveRight();
                        case KeyEvent.VK_UP ->
                            earth.getRocket().moveForward();
                        case KeyEvent.VK_DOWN ->
                            earth.getRocket().moveBackward();
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !solarSystem.isTransitioning) {
                    solarSystem.toggleTracking();
                    solarSystem.isTransitioning = true;
                    solarSystem.transitionProgress = 0f;
                }
            }
        });

        JFrame frame = new JFrame("Solar System");
        frame.setSize(solarSystem.width, solarSystem.height);
        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new FPSAnimator(canvas, 60).start();
    }

    private void toggleTracking() {
        this.isTrackingEarth = !this.isTrackingEarth;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glClearColor(0, 0, 0, 1);

        // Initialize solar system
        planets.add(new Sun(6f, 0, 0.5f));
        planets.add(new Mercury(0.75f, 6.5f, 5f, earthOrbitalSpeed));
        planets.add(new Venus(1.7f, 9.5f, 5f, earthOrbitalSpeed));
        planets.add(earth);
        planets.add(new Mars(1.125f, 19.3f, 5f, earthOrbitalSpeed));
        planets.add(new Jupiter(4.2f, 25.2f, 5f, earthOrbitalSpeed));
        planets.add(new Saturn(3.9f, 34.5f, 5.5f, earthOrbitalSpeed));
        planets.add(new Uranus(2.15f, 41.8f, 6.5f, earthOrbitalSpeed));
        planets.add(new Neptune(1.94f, 46.8f, 6.5f, earthOrbitalSpeed));
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Update transition
        updateCameraTransition();

        // Set up camera
        setupCamera(gl);

        // Render scene
        renderScene(gl);
    }

    private void updateCameraTransition() {
        if (isTransitioning) {
            transitionProgress += 1f / 60f;
            if (transitionProgress >= TRANSITION_DURATION) {
                isTransitioning = false;
                transitionProgress = TRANSITION_DURATION;
            }

            float t = smoothStep(transitionProgress / TRANSITION_DURATION);
            float[] earthPos = earth.getPosition();

            if (isTrackingEarth) {
                cameraDistance = lerp(sunViewDistance, earthViewDistance, t);
                cameraFOV = lerp(sunViewFOV, earthViewFOV, t);
                lookAtPosition[0] = lerp(0, earthPos[0], t);
                lookAtPosition[1] = lerp(0, earthPos[1], t);
                lookAtPosition[2] = lerp(0, earthPos[2], t);
            } else {
                cameraDistance = lerp(earthViewDistance, sunViewDistance, t);
                cameraFOV = lerp(earthViewFOV, sunViewFOV, t);
                lookAtPosition[0] = lerp(earthPos[0], 0, t);
                lookAtPosition[1] = lerp(earthPos[1], 0, t);
                lookAtPosition[2] = lerp(earthPos[2], 0, t);
            }
        }
    }

    private void setupCamera(GL2 gl) {
        // Update projection matrix
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        new GLU().gluPerspective(cameraFOV, (float) width / height, 0.1f, 1000f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        // Set up view
        GLU glu = new GLU();
        if (isTrackingEarth) {
            float[] earthPos = earth.getPosition();
            // Adjust these multipliers to change camera position relative to Earth
            float xOffset = cameraDistance * 0.3f; // Side view
            float yOffset = cameraDistance * 0.3f; // Height
            float zOffset = cameraDistance * 1.0f; // Distance behind

            glu.gluLookAt(
                    earthPos[0] + xOffset,
                    earthPos[1] + yOffset,
                    earthPos[2] + zOffset,
                    earthPos[0], earthPos[1], earthPos[2],
                    0, 1, 0
            );
        } else {
            glu.gluLookAt(
                    0, 0, -cameraDistance,
                    lookAtPosition[0], lookAtPosition[1], lookAtPosition[2],
                    0, 1, 0
            );
        }
    }

    private void renderScene(GL2 gl) {
        if (!isTrackingEarth || (isTrackingEarth && transitionProgress < TRANSITION_DURATION - 0.5f)) {
            // Render full solar system
            for (Planet planet : planets) {
                planet.updateRotation();
                planet.render(gl);
            }
        }
        if (isTrackingEarth) {
            // Render just Earth and its objects
//            earth.updateRotation();
            earth.updateMoon();
            earth.render(gl);
//            earth.renderMoon(gl);
            earth.updateRocket();
            earth.renderRocket(gl, 0.1f);
            earth.updateAstronaut();
            earth.renderAstronaut(gl, 0.12f);
            earth.updateSatellite();
            earth.renderSatellite(gl, 0.065f);

        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL2 gl = drawable.getGL().getGL2();
        width = w;
        height = h;
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        new GLU().gluPerspective(cameraFOV, (float) w / h, 0.1f, 1000f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        for (Planet planet : planets) {
            planet.dispose(gl);
        }
    }

    // Helper methods
    private float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    private float smoothStep(float t) {
        return t * t * (3 - 2 * t);
    }
}
