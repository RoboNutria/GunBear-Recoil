package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mechalpaca.gunbear.components.EnemyComponent;
import com.mechalpaca.gunbear.components.MaterialComponent;

/**
 * @author Diego Coppetti
 */
public class EnemySystem extends EntitySystem {
    ImmutableArray<Entity> entities;
    private ComponentMapper<EnemyComponent> em = ComponentMapper.getFor(EnemyComponent.class);
    private ComponentMapper<MaterialComponent> mm = ComponentMapper.getFor(MaterialComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(EnemyComponent.class, MaterialComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for(Entity entity : entities) {
            EnemyComponent ec = em.get(entity);
            MaterialComponent mc = mm.get(entity);
            checkDestroy(ec, entity);
            checkIsHit(deltaTime, ec, mc);
        }
    }

    private void checkDestroy(EnemyComponent ec, Entity entity) {
        if(ec.hp > 0) return;
        ec.isDead = true; // maybe es al pedo esto
        getEngine().removeEntity(entity);
    }

    private float mcTimer = 0;
    private float mcTimerRestart = 0.1f;
    private void checkIsHit(float deltaTime, EnemyComponent ec, MaterialComponent mc) {
        if(mc.run && !ec.wasHit) {
            mcTimer += deltaTime;
            if(mcTimer >= mcTimerRestart) {
                mcTimer = 0;
                mc.run = false;
            }
        }
        if(ec.wasHit) {
            mc.run = true;
            ec.wasHit = false;
            // TODO: Perhaps do some animation, flashing sprite, etc
        }
    }
}
