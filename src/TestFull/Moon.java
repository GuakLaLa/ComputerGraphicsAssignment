/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

/**
 *
 * @author Asus
 */
public class Moon extends Planets {
    public Moon(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/moon.jpg", radius, distanceFromCenter, rotationSpeed, Planets.SelfRotateAxis.Z_Axis);
    }
    
    @Override
    public void updateRotation() {
        int axis = rotateAxis == SelfRotateAxis.Y_Axis ? 1 : 2;
        rotationAngles[axis] += rotationSpeed; // Y-axis rotation
        
        orbitalAngle -= orbitalSpeed;
    }
}
