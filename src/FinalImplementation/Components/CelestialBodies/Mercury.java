/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalImplementation.Components.CelestialBodies;

public class Mercury extends Planet {
    
    private static final float RELATIVE_SPEED = 1.6f;

    public Mercury(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/mercury.jpg", radius, distanceFromCenter, rotationSpeed, RELATIVE_SPEED);
    }
}
