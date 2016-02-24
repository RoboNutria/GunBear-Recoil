package com.robonutria.gunbear.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Diego Coppetti
 */
public class LinearVelocityComponent implements Component {
    public float speed;
    public Vector2 direction;
    public boolean enabled = true;
}
