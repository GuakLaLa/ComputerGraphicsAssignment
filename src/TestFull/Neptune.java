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
public class Neptune extends Planets {

    public Neptune(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/neptune.jpg", radius, distanceFromCenter, rotationSpeed, Planets.SelfRotateAxis.Z_Axis);
    }
}
