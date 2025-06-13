/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

/**
 *
 * @author Asus
 */
public class Mars extends Planet {
    
    private float relativeSpeed = 0.81f;

    public Mars(float radius, float distanceFromCenter, float rotationSpeed, float earthOrbitalSpeed) {
        super("/images/mars.jpg", radius, distanceFromCenter, rotationSpeed, Planet.SelfRotateAxis.Z_Axis);
        this.orbitalSpeed = earthOrbitalSpeed * relativeSpeed;
    }
    
}
