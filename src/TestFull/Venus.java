/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

/**
 *
 * @author Asus
 */
public class Venus extends Planet {
    
    private float relativeSpeed = 1.18f;

    public Venus(float radius, float distanceFromCenter, float rotationSpeed, float earthOrbitalSpeed) {
        super("/images/venus.jpg", radius, distanceFromCenter, rotationSpeed, Planet.SelfRotateAxis.Z_Axis);
        this.orbitalSpeed = earthOrbitalSpeed * relativeSpeed;
        rotationAngles = new float[] {90, 0, 0};
    }
}