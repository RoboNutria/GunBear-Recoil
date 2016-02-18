package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mechalpaca.gunbear.components.EnemySpawnerComponent;

/**
 * @author Diego Coppetti
 */
public class LevelCycleSystem extends EntitySystem {
    public static final int MAX_LEVEL = 999;
    public int currentLevel = 1;

    // Timers
    private float spawnTimer = 0;
    private float spawnDelay = 5; // time between each spawn being created
    private int parallelSpawns = 1; // parallel spawns to be created
    private boolean createSpawn = false;
    private Viewport worldView;

    @Override
    public void addedToEngine(Engine engine) {
        RenderSystem renderSystem = engine.getSystem(RenderSystem.class);
        worldView = renderSystem.worldView;
    }

    @Override
    public void update(float deltaTime) {
        updateLevel(deltaTime);
        createEnemySpawner(deltaTime);
    }

    private void updateLevel(float deltaTime) {
        spawnTimer += deltaTime;
        if(spawnTimer >= spawnDelay) {
            spawnTimer = 0;
            createSpawn = true;
        }
    }

    private void createEnemySpawner(float deltaTime) {
        if(!createSpawn) return;
        createSpawn = false;
        for (int i = 0 ; i < parallelSpawns ; i++) {
            Entity e = new Entity();
            e.add(getNewSpawner());
            getEngine().addEntity(e);
        }
    }

    private EnemySpawnerComponent getNewSpawner() {
        EnemySpawnerComponent esc = new EnemySpawnerComponent();
        esc.enemyMovementType = EnemySpawnerComponent.EnemyMovementType.LinearVelocity;
        esc.pointA = new Vector2(-worldView.getWorldWidth()/2f, worldView.getWorldHeight()/2f);
        esc.pointB = new Vector2(-worldView.getWorldWidth()/2f, -worldView.getWorldHeight()/2f);
        esc.spawnType = EnemySpawnerComponent.SpawnType.RandomY;
        esc.spawnDelay = 0.5f;
        esc.enemiesToSpawn = 8;
        esc.enemySpeed = 1f;
        return esc;
    }

}
