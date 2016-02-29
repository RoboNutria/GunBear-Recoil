package com.robonutria.gunbear.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robonutria.gunbear.GameConfig;
import com.robonutria.gunbear.components.BulletComponent;
import com.robonutria.gunbear.components.EnemySpawnerComponent;
import com.robonutria.gunbear.components.Level;
import com.robonutria.gunbear.factories.LevelFactory;
import com.robonutria.gunbear.gui.Hud;

/**
 * @author Diego Coppetti
 */
public class LevelCycleSystem extends EntitySystem {

    public Hud hud;

    public static final int MAX_LEVEL = 999;
    public static boolean SPEED_UP_MODE = true;
    public int currentLevel = 1;
    private Level level;

    // Timers
    private float spawnTimer = 0;
    private float spawnDelay = 5; // time between each spawn being created
    private int parallelSpawns = 1; // parallel spawns to be created
    private boolean createSpawn = false;
    private Viewport worldView;
    private static boolean enemyHit = false;
    private static boolean enemyKill = false;
    private float tensionBarIncreaseRate = 0.25f;
    private float tensionBarEnemyKillMul = 2;
    private float tensionDivider = 60f;

    @Override
    public void addedToEngine(Engine engine) {
        RenderSystem renderSystem = engine.getSystem(RenderSystem.class);
        worldView = renderSystem.worldView;
        changeLevel();
    }

    private void changeLevel() {
        level = LevelFactory.getLevel(currentLevel, hud.tensionBar);
        parallelSpawns = level.parallelSpawns;
        spawnDelay = level.delayBetweenSpawns;
    }

    @Override
    public void update(float deltaTime) {
        updateLevel(deltaTime);
        updateTensionBar(deltaTime);
        createEnemySpawner(deltaTime);
    }

    private void updateTensionBar(float deltaTime) {
        if(enemyHit) {
            // add progress
            enemyHit = false;
            float value = enemyKill == true ? tensionBarIncreaseRate * tensionBarEnemyKillMul : tensionBarIncreaseRate;
            enemyKill = false;
            hud.tensionBar.setValue(hud.tensionBar.getValue() + value);
        } else {
            // decrease progress
            if(hud.tensionBar.getPercent() > 0) {
                hud.tensionBar.setValue(hud.tensionBar.getValue() - tensionBarIncreaseRate/tensionDivider);
            }
        }
    }

    private void updateLevel(float deltaTime) {
        deltaTime = deltaTime * GameConfig.SPEED_UP;
        spawnTimer += deltaTime;
        if(spawnTimer >= spawnDelay) {
            spawnTimer = 0;
            createSpawn = true;
        }
        if(SPEED_UP_MODE) {
            GameConfig.SPEED_UP = 1+(level.deltaMulPercent * hud.tensionBar.getPercent());
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

        // TODO: Randomize this too
        esc.enemyMovementType = EnemySpawnerComponent.EnemyMovementType.LinearVelocity;

        esc.spawnType = getRandomSpawnType();

        if(esc.spawnType == EnemySpawnerComponent.SpawnType.RandomY) {
            if (MathUtils.randomBoolean() == true) {
                esc.pointA = new Vector2(worldView.getWorldWidth() / 2f, (worldView.getWorldHeight() / 2f) - 0.02f);
                esc.pointB = new Vector2(worldView.getWorldWidth() / 2f, (-worldView.getWorldHeight() / 2f) + 0.02f);
            } else {
                esc.pointA = new Vector2(-worldView.getWorldWidth() / 2f, (worldView.getWorldHeight() / 2f) - 0.02f);
                esc.pointB = new Vector2(-worldView.getWorldWidth() / 2f, (-worldView.getWorldHeight() / 2f) + 0.02f);
            }
        } else if(esc.spawnType == EnemySpawnerComponent.SpawnType.RandomX) {
            if (MathUtils.randomBoolean() == true) {
                esc.pointA = new Vector2(-worldView.getWorldWidth() / 2f, (-worldView.getWorldHeight() / 2f) - 0.02f);
                esc.pointB = new Vector2(worldView.getWorldWidth() / 2f, (-worldView.getWorldHeight() / 2f) + 0.02f);
            } else {
                esc.pointA = new Vector2(-worldView.getWorldWidth() / 2f, (worldView.getWorldHeight() / 2f) - 0.02f);
                esc.pointB = new Vector2(worldView.getWorldWidth() / 2f, (worldView.getWorldHeight() / 2f) + 0.02f);
            }
        } else if (esc.spawnType == EnemySpawnerComponent.SpawnType.FixedY) {
            if (MathUtils.randomBoolean() == true) {
                esc.pointA = new Vector2(-worldView.getWorldWidth() / 2f, MathUtils.random(-worldView.getWorldHeight()/2f, worldView.getWorldHeight()/2f));
            } else {
                esc.pointA = new Vector2(worldView.getWorldWidth() / 2f, MathUtils.random(-worldView.getWorldHeight()/2f, worldView.getWorldHeight()/2f));
            }
        }
        esc.lifeSpan = level.spawnerLifeSpan;
        esc.spawnDelay = level.enemiesSpawnDelay;
        esc.enemiesToSpawn = level.getEnemiesToSpawn();
        esc.enemySpeed = level.enemySpeed;
        return esc;
    }

    public EnemySpawnerComponent.SpawnType getRandomSpawnType() {
        int r = MathUtils.random(0, EnemySpawnerComponent.SpawnType.values().length-1);
        return EnemySpawnerComponent.SpawnType.values()[r];
    }

    public static void notifyHit(BulletComponent bulletComponent, boolean enemyKilled) {
        enemyHit = true;
        enemyKill = enemyKilled;
    }
}
