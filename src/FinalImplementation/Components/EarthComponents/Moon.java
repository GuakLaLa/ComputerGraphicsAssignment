/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalImplementation.Components.EarthComponents;

import FinalImplementation.Components.CelestialBodies.Planet;

public class Moon extends Planet {
    public Moon(float radius, float distanceFromCenter, float rotationSpeed) {
        super("/images/moon.jpg", radius, distanceFromCenter, rotationSpeed);
    }
}
