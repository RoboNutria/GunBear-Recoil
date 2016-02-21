package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mechalpaca.gunbear.components.BulletComponent;
import com.mechalpaca.gunbear.components.EnemySpawnerComponent;
import com.mechalpaca.gunbear.components.scene2d.ProgressBarComponent;

/**
 * @author Diego Coppetti
 */
public class LevelCycleSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ProgressBarComponent pbc;
    private ComponentMapper<ProgressBarComponent> pbm = ComponentMapper.getFor(ProgressBarComponent.class);

    public static final int MAX_LEVEL = 999;
    public int currentLevel = 1;

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
        entities = engine.getEntitiesFor(Family.all(ProgressBarComponent.class).get());
        RenderSystem renderSystem = engine.getSystem(RenderSystem.class);
        worldView = renderSystem.worldView;
    }

    @Override
    public void update(float deltaTime) {
        if(pbc == null) {
            for(Entity e : entities) {
                pbc = pbm.get(e);
                if(e != null) break;
            }
        }
        updateLevel(deltaTime);
        updateTensionBar(deltaTime);
        createEnemySpawner(deltaTime);
    }

    private void updateTensionBar(float deltaTime) {
        ProgressBar pb = pbc.progressBar;
        if(enemyHit) {
            // add progress
            enemyHit = false;
            float value = enemyKill == true ? tensionBarIncreaseRate * tensionBarEnemyKillMul : tensionBarIncreaseRate;
            enemyKill = false;
            pb.setValue(pb.getValue() + value);
        } else {
            // decrease progress
            if(pb.getPercent() > 0) {
                pb.setValue(pb.getValue() - tensionBarIncreaseRate/tensionDivider);
            }
        }
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

        // TODO: Randomize this too
        esc.enemyMovementType = EnemySpawnerComponent.EnemyMovementType.LinearVelocity;

        esc.spawnType = getRandomSpawnType();

        if(esc.spawnType == EnemySpawnerComponent.SpawnType.RandomY) {
            if(MathUtils.randomBoolean() == true) {
                esc.pointA = new Vector2(worldView.getWorldWidth()/2f, (worldView.getWorldHeight()/2f)-0.02f);
                esc.pointB = new Vector2(worldView.getWorldWidth()/2f, (-worldView.getWorldHeight()/2f)+0.02f);
            } else {
                esc.pointA = new Vector2(-worldView.getWorldWidth()/2f, (worldView.getWorldHeight()/2f)-0.02f);
                esc.pointB = new Vector2(-worldView.getWorldWidth()/2f, (-worldView.getWorldHeight()/2f)+0.02f);
            }
        }
        esc.lifeSpan = -1;
        esc.spawnDelay = 0.5f;
        esc.enemiesToSpawn = 5;
        esc.enemySpeed = 0.5f;
        return esc;
    }

    public EnemySpawnerComponent.SpawnType getRandomSpawnType() {
        // TODO: Make it random not just RandomY
        return EnemySpawnerComponent.SpawnType.RandomY;
    }

    public static void notifyHit(BulletComponent bulletComponent, boolean enemyKilled) {
        enemyHit = true;
        enemyKill = enemyKilled;
    }
}
