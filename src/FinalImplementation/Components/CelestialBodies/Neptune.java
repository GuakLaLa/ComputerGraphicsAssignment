/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalImplementation.Components.CelestialBodies;

public class Neptune extends Planet {

    private static final float RELATIVE_SPEED = 0.18f;

    public Neptune(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/neptune.jpg", radius, distanceFromCenter, rotationSpeed, RELATIVE_SPEED);
    }
}
