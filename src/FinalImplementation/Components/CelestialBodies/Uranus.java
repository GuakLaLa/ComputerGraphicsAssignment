/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalImplementation.Components.CelestialBodies;

/**
 *
 * @author Asus
 */
public class Uranus extends Planet {
    
    private static final float RELATIVE_SPEED = 0.23f;

    public Uranus(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/uranus.jpg", radius, distanceFromCenter, rotationSpeed, RELATIVE_SPEED);
        
        // Do not need texture adjustment around X-axis
        rotationAngles = new float[] {0, 0, 0};
    }
    
    @Override
    public void updateRotation() {
        // Update self-rotation around Z-axis
        rotationAngles[2] += rotationSpeed;
        
        // Orbit the sun anti-clockwise
        orbitalAngle -= orbitalSpeed;
        calculatePosition();
    }
}
