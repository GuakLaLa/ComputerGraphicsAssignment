/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalImplementation.Components.CelestialBodies;

public class Sun extends Planet{

    public Sun(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/sun.jpg", radius, distanceFromCenter, rotationSpeed);
        this.orbitalSpeed = 0f;
    }
}
