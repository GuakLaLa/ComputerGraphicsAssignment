package FinalImplementation;

import FinalImplementation.Components.CelestialBodies.Neptune;
import FinalImplementation.Components.CelestialBodies.Uranus;
import FinalImplementation.Components.CelestialBodies.Jupiter;
import FinalImplementation.Components.CelestialBodies.Venus;
import FinalImplementation.Components.CelestialBodies.Planet;
import FinalImplementation.Components.CelestialBodies.Saturn;
import FinalImplementation.Components.CelestialBodies.Mars;
import FinalImplementation.Components.CelestialBodies.Earth;
import FinalImplementation.Components.CelestialBodies.Sun;
import FinalImplementation.Components.CelestialBodies.Mercury;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class SolarSystem implements GLEventListener {

    private static final float EARTH_ORBITAL_SPEED = 0.75f;
    private List<Planet> celestialBodies = new ArrayList<>();
    private static Earth earth;

    private Texture galaxyTexture; // to add background as galaxy

    // Camera control
    private boolean isTrackingEarth = false;
    private boolean isTrackingAstronaut = false;
    private boolean isTransitioning = false;
    private float transitionProgress = 0f;
    private final float TRANSITION_DURATION = 2.0f;
    private float cameraDistance = 70f;
    private float cameraFOV = 45f;
    private float[] lookAtPosition = {0f, 0f, 0f};

    private final float sunViewDistance = 70f;   // Default distance for Sun view
    private final float earthViewDistance = 15f; // Default zoom distance for Earth view
    private final float astronautViewDistance = 5f; // Zoom distance for astronaut view

    private final float sunViewFOV = 50f;        // Wider FOV for solar system view
    private final float earthViewFOV = 40f;      // Narrow FOV for zoomed-in view
    private final float astronautViewFOV = 30f;      // Narrow FOV for zoomed-in view

    private int currentTrack = 0;

    public void trackNext() {
        currentTrack = (currentTrack + 1) % 3;
        switch(currentTrack) {
            case 0 -> {
                isTrackingEarth = false;
                isTrackingAstronaut = false;
            }
            case 1 -> {
                isTrackingEarth = true;
                isTrackingAstronaut = false;
            }
            case 2 -> {
                isTrackingEarth = true;
                isTrackingAstronaut = true;
            }
        }
        startTransition();
    }
    
//    public boolean isTrackingSun() {
//        return currentTrack == 0;
//    }
//    
//    public boolean isTrackingEarth() {
//        return currentTrack == 1;
//    }
//    
//    public boolean isTrackingAstronaut() {
//        return currentTrack == 2;
//    }

//    private final float[] astronautPosition = {0f, 0.5f, 1.5f};
    // Window dimensions
    private int width = 1000;
    private int height = 800;

    public static void main(String[] args) {
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);

        // Set the Earth orbital speed before constructing the Solar System
        Planet.setEarthOrbitalSpeed(EARTH_ORBITAL_SPEED);

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
                    solarSystem.trackNext();
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

    private void startTransition() {
        isTransitioning = true;
        transitionProgress = 0f;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glClearColor(0, 0, 0, 1);

        // Initialize background texture
        try {
            galaxyTexture = TextureIO.newTexture(getClass().getResourceAsStream("/images/galaxy.jpg"), false, "jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize Earth object
        earth = new Earth(1.8f, 14.5f, 5f);

        // Initialize solar system
        celestialBodies.add(new Sun(6f, 0, 0.5f));
        celestialBodies.add(new Mercury(0.75f, 6.5f, 5f));
        celestialBodies.add(new Venus(1.7f, 9.5f, 5f));
        celestialBodies.add(earth);
        celestialBodies.add(new Mars(1.125f, 19.3f, 5f));
        celestialBodies.add(new Jupiter(4.2f, 25.2f, 5f));
        celestialBodies.add(new Saturn(3.9f, 34.5f, 5.5f));
        celestialBodies.add(new Uranus(2.15f, 41.8f, 6.5f));
        celestialBodies.add(new Neptune(1.94f, 46.8f, 6.5f));
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

            float step = smoothStep(transitionProgress / TRANSITION_DURATION);
            float[] earthPos = earth.getPosition();
            float[] astronautPos = earth.getAstronautPosition();

            if (isTrackingAstronaut) {
                cameraDistance = lerp(earthViewDistance, astronautViewDistance, step);
                cameraFOV = lerp(earthViewFOV, astronautViewFOV, step);
                lookAtPosition[0] = lerp(earthPos[0], astronautPos[0], step);
                lookAtPosition[1] = lerp(earthPos[1], astronautPos[0], step);
                lookAtPosition[2] = lerp(earthPos[2], astronautPos[0], step);
            } else if (isTrackingEarth) {
                cameraDistance = lerp(sunViewDistance, earthViewDistance, step);
                cameraFOV = lerp(sunViewFOV, earthViewFOV, step);
                lookAtPosition[0] = lerp(0, earthPos[0], step);
                lookAtPosition[1] = lerp(0, earthPos[1], step);
                lookAtPosition[2] = lerp(0, earthPos[2], step);
            } else {
                cameraDistance = lerp(earthViewDistance, sunViewDistance, step);
                cameraFOV = lerp(earthViewFOV, sunViewFOV, step);
                lookAtPosition[0] = lerp(astronautPos[0], 0, step);
                lookAtPosition[1] = lerp(astronautPos[1], 0, step);
                lookAtPosition[2] = lerp(astronautPos[2], 0, step);
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

        if (isTrackingAstronaut) {
            float[] astronautPos = earth.getAstronautPosition();

            // Side view
            float xOffset = cameraDistance * 0.5f;
            // Height
            float yOffset = cameraDistance * 0.55f;
            // Distance behind
            float zOffset = cameraDistance * 0.6f;

            glu.gluLookAt(
                    astronautPos[0] + xOffset,
                    astronautPos[1] + yOffset,
                    astronautPos[2] + zOffset,
                    astronautPos[0], astronautPos[1], astronautPos[2],
                    0, 1, 0
            );
        } // View when focusing on Earth
        else if (isTrackingEarth) {
            float[] earthPos = earth.getPosition();

            // Side view
            float xOffset = cameraDistance * 0.5f;
            // Height
            float yOffset = cameraDistance * 0.8f;
            // Distance behind
            float zOffset = cameraDistance * 0.6f;

            glu.gluLookAt(
                    earthPos[0] + xOffset,
                    earthPos[1] + yOffset,
                    earthPos[2] + zOffset,
                    earthPos[0], earthPos[1], earthPos[2],
                    0, 1, 0
            );
        } // View when focusing on Sun
        else {
            glu.gluLookAt(
                    0, 0, -cameraDistance,
                    lookAtPosition[0], lookAtPosition[1], lookAtPosition[2],
                    0, 1, 0
            );
        }
    }

    private void renderBackground(GL2 gl) {
        float size = 200f;
        if (galaxyTexture != null) {
            galaxyTexture.bind(gl);
        }

        // Save all relevant states
        gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_CURRENT_BIT | GL2.GL_LIGHTING_BIT);

        gl.glDisable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_DEPTH_TEST); // Optional: prevents z-fighting
        gl.glEnable(GL2.GL_TEXTURE_2D);

        gl.glPushMatrix();

        gl.glBegin(GL2.GL_QUADS);

        // Front
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-size, -size, size);
        gl.glTexCoord2f(1f, 0f);
        gl.glVertex3f(size, -size, size);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(size, size, size);
        gl.glTexCoord2f(0f, 1f);
        gl.glVertex3f(-size, size, size);

        // Back
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(size, -size, -size);
        gl.glTexCoord2f(1f, 0f);
        gl.glVertex3f(-size, -size, -size);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(-size, size, -size);
        gl.glTexCoord2f(0f, 1f);
        gl.glVertex3f(size, size, -size);

        // Left
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-size, -size, -size);
        gl.glTexCoord2f(1f, 0f);
        gl.glVertex3f(-size, -size, size);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(-size, size, size);
        gl.glTexCoord2f(0f, 1f);
        gl.glVertex3f(-size, size, -size);

        // Right
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(size, -size, size);
        gl.glTexCoord2f(1f, 0f);
        gl.glVertex3f(size, -size, -size);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(size, size, -size);
        gl.glTexCoord2f(0f, 1f);
        gl.glVertex3f(size, size, size);

        // Top
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-size, size, size);
        gl.glTexCoord2f(1f, 0f);
        gl.glVertex3f(size, size, size);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(size, size, -size);
        gl.glTexCoord2f(0f, 1f);
        gl.glVertex3f(-size, size, -size);

        // Bottom
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(-size, -size, -size);
        gl.glTexCoord2f(1f, 0f);
        gl.glVertex3f(size, -size, -size);
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(size, -size, size);
        gl.glTexCoord2f(0f, 1f);
        gl.glVertex3f(-size, -size, size);

        gl.glEnd();

        gl.glPopMatrix();

        // Restore OpenGL state
        gl.glPopAttrib();
    }

    private void renderScene(GL2 gl) {
        // Render background
        renderBackground(gl);

        // Render full solar system when focusing on Sun
        // Continue render full solar system until 0.5s before completely focus on Earth
        if (!isTrackingEarth || (isTrackingEarth && !isTrackingAstronaut && transitionProgress < TRANSITION_DURATION - 0.5f)) {
            // Render full solar system
            for (Planet body : celestialBodies) {
                body.updateRotation();
                body.render(gl);
            }
        }

        // Render only Earth and its objects when focusing on Earth
        if (isTrackingEarth) {
            // Stop self-rotation of the Earth
            // Update the Moon with a slower orbital speed
            earth.updateMoon();
            earth.render(gl);

            // Enable GL_NORMALIZE to ensure scaled objects are rendered accurately
            gl.glEnable(GL2.GL_NORMALIZE);

            // Render the rocket
            earth.updateRocket();
            earth.renderRocket(gl, 0.1f);

            // Render the astronaut
            earth.updateAstronaut();
            earth.renderAstronaut(gl, 0.12f);

            // Render the satellite
            earth.updateSatellite();
            earth.renderSatellite(gl, 0.065f);

            gl.glDisable(GL2.GL_NORMALIZE);
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
        for (Planet planet : celestialBodies) {
            planet.dispose(gl);
        }
    }

    // Calculate value at the step
    // Formula: s + t(e-s)
    // Step is progress from 0 to 1
    private float lerp(float start, float end, float step) {
        return start + step * (end - start);
    }

    // Create acceleration from start and deceleration to end
    // Formula: 3t^2 - 2t^3
    // Positive value in range of 0 to 1
    private float smoothStep(float step) {
        return step * step * (3 - 2 * step);
    }
}
