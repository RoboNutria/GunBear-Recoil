package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mechalpaca.gunbear.components.BodyComponent;
import com.mechalpaca.gunbear.components.LinearVelocityComponent;
import com.mechalpaca.gunbear.components.movement.BodyFollowComponent;

/**
 * @author Diego Coppetti
 */
public class MovementSystem extends EntitySystem {
    private ImmutableArray<Entity> linearVelEntities;
    private ImmutableArray<Entity> bodyFollowEntities;
    private ComponentMapper<BodyFollowComponent> bfm = ComponentMapper.getFor(BodyFollowComponent.class);
    private ComponentMapper<LinearVelocityComponent> lvm = ComponentMapper.getFor(LinearVelocityComponent.class);
    private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        linearVelEntities = engine.getEntitiesFor(Family.all(LinearVelocityComponent.class).get());
        bodyFollowEntities = engine.getEntitiesFor(Family.all(BodyFollowComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity e : linearVelEntities) {
            BodyComponent bc = bm.get(e);
            LinearVelocityComponent lvc = lvm.get(e);
            if(!lvc.enabled) continue;
            updateLinearMovementEntities(bc, lvc);
        }
        for (Entity e : bodyFollowEntities) {
            BodyComponent bc = bm.get(e);
            BodyFollowComponent bfc = bfm.get(e);
            if(!bfc.enabled) continue;
            updateBodyFollowEntities(bc, bfc);
        }
    }

    private void updateLinearMovementEntities(BodyComponent bc, LinearVelocityComponent lvc) {
        bc.body.setLinearVelocity(lvc.direction.x * lvc.speed, lvc.direction.y * lvc.speed );
    }

    private void updateBodyFollowEntities(BodyComponent bc, BodyFollowComponent bfc) {
        // TODO: Soon...
    }


}
