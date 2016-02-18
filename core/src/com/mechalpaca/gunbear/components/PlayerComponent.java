package com.mechalpaca.gunbear.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * @author Diego Coppetti
 */
public class PlayerComponent implements Component {

    public static enum PlayerState {
        Normal,
        Recoil,
        Hit,
        Recuperating
    }

    public short lives = 3;
    public int xDir = 1;
    public float speed = 5f;
    public float friction = 0.02f;
    public float hitBackForce = 0.3f;
    public Body bodyHitBy = null;
    public float radius;
    public boolean canMove = true;
    public boolean canFire = true;
    public PlayerState state = PlayerState.Normal;
}
