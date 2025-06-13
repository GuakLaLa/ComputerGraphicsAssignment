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
public class Mercury extends Planet {
    
    private float relativeSpeed = 1.6f;

    public Mercury(float radius, float distanceFromCenter, float rotationSpeed, float earthOrbitalSpeed) {
        super("/images/mercury.jpg", radius, distanceFromCenter, rotationSpeed, Planet.SelfRotateAxis.Z_Axis);
        this.orbitalSpeed = earthOrbitalSpeed * relativeSpeed;
    }
}
