/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

/**
 *
 * @author Asus
 */
public class Mars extends Planets {
    public Mars(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/mars.jpg", radius, distanceFromCenter, rotationSpeed, Planets.SelfRotateAxis.Z_Axis);
    }
}
