/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

/**
 *
 * @author Asus
 */
public class Uranus extends Planet {
    
    private float relativeSpeed = 0.23f;

    public Uranus(float radius, float distanceFromCenter, float rotationSpeed, float earthOrbitalSpeed) {
        super("/images/uranus.jpg", radius, distanceFromCenter, rotationSpeed, Planet.SelfRotateAxis.Z_Axis);
        this.orbitalSpeed = earthOrbitalSpeed * relativeSpeed;
        rotationAngles = new float[] {0, 0, 0};
    }
}
