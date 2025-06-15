/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalImplementation.Components.CelestialBodies;

/**
 *
 * @author Asus
 */
public class Venus extends Planet {
    
    private static final float RELATIVE_SPEED = 1.18f;

    public Venus(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/venus.jpg", radius, distanceFromCenter, rotationSpeed, RELATIVE_SPEED);
        this.rotationSpeed = rotationSpeed * -1;
    }
}