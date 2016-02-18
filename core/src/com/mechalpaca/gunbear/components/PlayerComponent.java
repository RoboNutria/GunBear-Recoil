package com.mechalpaca.gunbear.components;

import com.badlogic.ashley.core.Component;

/**
 * @author Diego Coppetti
 */
public class PlayerComponent implements Component {

    public static enum PlayerState {
        Normal,
        Recoil
    }

    public short lives = 3;
    public int xDir = 1;
    public float speed = 5f;
    public float friction = 0.02f;
    public float radius;
    public boolean canMove = true;
    public PlayerState state = PlayerState.Normal;
}
