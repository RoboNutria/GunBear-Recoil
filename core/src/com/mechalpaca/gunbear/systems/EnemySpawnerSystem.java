package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mechalpaca.gunbear.components.BodyComponent;
import com.mechalpaca.gunbear.components.EnemySpawnerComponent;
import com.mechalpaca.gunbear.components.PlayerComponent;
import com.mechalpaca.gunbear.factories.EntityFactory;

/**
 * @author Diego Coppetti
 */
public class EnemySpawnerSystem extends EntitySystem {
    private PhysicsSystem physicsSystem;

    private ImmutableArray<Entity> enemySpawnerEntities;
    private ImmutableArray<Entity> playerBodyEntities;

    private ComponentMapper<EnemySpawnerComponent> esm = ComponentMapper.getFor(EnemySpawnerComponent.class);
    private ComponentMapper<PlayerComponent> pc = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<BodyComponent> bc = ComponentMapper.getFor(BodyComponent.class);
    private BodyComponent playerBodyComponent;

    @Override
    public void addedToEngine(Engine engine) {
        enemySpawnerEntities = engine.getEntitiesFor(Family.all(EnemySpawnerComponent.class).get());
        playerBodyEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class, BodyComponent.class).get());
        physicsSystem = engine.getSystem(PhysicsSystem.class);
    }

    @Override
    public void update(float deltaTime) {
        for(Entity entity : enemySpawnerEntities) {
            EnemySpawnerComponent esc = esm.get(entity);
            checkIsKill(esc);
            if(esc.isKill) {
                getEngine().removeEntity(entity);
                continue;
            }
            esc.timer += deltaTime;
            esc.spawnTimer += deltaTime;
            if(playerBodyComponent == null) {
                playerBodyComponent = bc.get(playerBodyEntities.first());
            }
            createEnemy(esc, playerBodyComponent);
        }
    }

    private void checkIsKill(EnemySpawnerComponent esc) {
        esc.isKill = (esc.timer >= esc.lifeSpan && esc.lifeSpan != -1) || esc.enemiesToSpawn == 0;
    }

    private void createEnemy(EnemySpawnerComponent esc, BodyComponent bc) {
        if(esc.spawnTimer < esc.spawnDelay) return;
        if(esc.enemiesToSpawn != -1) {
            if(esc.enemiesToSpawn <= 0) return;
            esc.enemiesToSpawn--;
        }
        esc.spawnTimer = 0;
        getEngine().addEntity(EntityFactory.createEnemy(physicsSystem.world, esc, bc));
    }
}
