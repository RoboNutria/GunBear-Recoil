package com.mechalpaca.gunbear.components;

import com.badlogic.ashley.core.Component;

/**
 * @author Diego Coppetti
 */
public class EnemyComponent implements Component {
    public float radius = 2f;
    public int hp = 1;
    public boolean wasHit = false;
    public boolean isDead = false;
}
