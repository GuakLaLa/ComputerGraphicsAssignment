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
public class Jupiter extends Planet {

    public Jupiter(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/jupiter.jpg", radius, distanceFromCenter, rotationSpeed, SelfRotateAxis.Z_Axis);
    }
}
