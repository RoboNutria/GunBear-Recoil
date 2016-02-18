package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mechalpaca.gunbear.components.EnemyComponent;

/**
 * @author Diego Coppetti
 */
public class EnemySystem extends EntitySystem {
    ImmutableArray<Entity> entities;
    private ComponentMapper<EnemyComponent> em = ComponentMapper.getFor(EnemyComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(EnemyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for(Entity entity : entities) {
            EnemyComponent ec = em.get(entity);
            checkDestroy(ec, entity);
            checkIsHit(ec);
        }
    }

    private void checkDestroy(EnemyComponent ec, Entity entity) {
        if(ec.hp > 0) return;
        ec.isDead = true; // maybe es al pedo esto
        getEngine().removeEntity(entity);
    }

    private void checkIsHit(EnemyComponent ec) {
        if(ec.wasHit) {
            ec.wasHit = false;
            // TODO: Perhaps do some animation, flashing sprite, etc
        }
    }
}
