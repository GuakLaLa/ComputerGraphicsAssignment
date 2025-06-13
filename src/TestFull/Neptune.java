/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestFull;

import com.jogamp.opengl.GL2;

/**
 *
 * @author Asus
 */
public class Neptune extends Planet {

    private float relativeSpeed = 0.18f;

    public Neptune(float radius, float distanceFromCenter, float rotationSpeed, float earthOrbitalSpeed) {
        super("/images/neptune.jpg", radius, distanceFromCenter, rotationSpeed, Planet.SelfRotateAxis.Z_Axis);
        this.orbitalSpeed = earthOrbitalSpeed * relativeSpeed;
    }
}
