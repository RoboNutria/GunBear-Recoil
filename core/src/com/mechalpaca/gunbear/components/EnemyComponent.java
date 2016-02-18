package com.mechalpaca.gunbear.components;

import com.badlogic.ashley.core.Component;

/**
 * @author Diego Coppetti
 */
public class EnemyComponent implements Component {
    public float radius = 2f;
    public float hp = 5f;
    public boolean wasHit = false;
}
