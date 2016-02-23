package com.mechalpaca.gunbear.components;

import com.badlogic.gdx.math.MathUtils;

/**
 * @author Diego Coppetti
 */
public class Level {
    public float deltaMulPercent = 0.4f; // 0.4 would mean a max of 40% faster play speed
    public float enemiesSpawnDelay = 0.5f;
    public float enemySpeed = 0.5f;
    public int minEnemiesToSpawn = 1;
    public int maxEnemiesToSpawn = 1;
    public int spawnerLifeSpan = -1;

    public int getEnemiesToSpawn() {
        return MathUtils.random(minEnemiesToSpawn, maxEnemiesToSpawn);
    }
}
