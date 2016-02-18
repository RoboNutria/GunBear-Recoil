package com.mechalpaca.gunbear.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
/**
 * @author Diego Coppetti
 */
public class EnemySpawnerComponent implements Component {

    public static enum SpawnType {
        FixedSpot, // Enemies would spawn from a specific position
        RandomX,
        RandomY,
        RandomXY,
    }

    public static enum EnemyType {
        LinearMovement,
        MovesToTarget,
    }

    public static enum EnemyMovementType {
        BodyFollow,
        LinearVelocity,
    }
    public SpawnType spawnType;
    public EnemyMovementType enemyMovementType;
    public EnemyMovementType enemyMoveType = EnemyMovementType.LinearVelocity;
    public Vector2 pointA;
    public Vector2 pointB;
    public float enemySpeed = 1f;
    public int enemyHP = 1;

    public float timer = 0;
    public float spawnTimer = 0;
    public float spawnDelay = 1f;
    public int enemiesToSpawn = -1; // -1 means infinite
    public float lifeSpan = -1f; // -1 means infinite
    public boolean isKill = false;



    public Vector2 getNextEnemyPos() {
        Vector2 p = null;
        switch (spawnType) {
            case FixedSpot:
                p = pointA;
                break;
            case RandomY:
                p = new Vector2(pointA.x, MathUtils.random(pointA.y, pointB.y));
                break;
            case RandomX:
                p = new Vector2(MathUtils.random(pointA.x, pointB.x), pointA.y);
                break;
            case RandomXY:
                p = new Vector2(MathUtils.random(pointA.x, pointB.x), MathUtils.random(pointA.y, pointB.y));
                break;
            default:
                break;
        }
        return p;
    }
}
