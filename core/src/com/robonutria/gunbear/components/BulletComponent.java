package com.robonutria.gunbear.components;

import com.badlogic.ashley.core.Component;

/**
 * @author Diego Coppetti
 */
public class BulletComponent implements Component {

    public static enum BulletType {
        Normal,
    }

    public float speed;
    public int hitPower;
    public boolean isPlayer;
    public BulletType type;
    public boolean destroy = false;
    public float radius;
}
