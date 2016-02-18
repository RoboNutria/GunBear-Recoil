package com.mechalpaca.gunbear.components.movement;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * @author Diego Coppetti
 */
public class BodyFollowComponent implements Component {
    public Body target;
    public float speed;
    public boolean enabled = true;
}
