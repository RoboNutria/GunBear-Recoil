package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mechalpaca.gunbear.GameConfig;
import com.mechalpaca.gunbear.components.BodyComponent;
import com.mechalpaca.gunbear.components.BoundsComponent;
import com.mechalpaca.gunbear.components.BulletComponent;
import com.mechalpaca.gunbear.screens.LevelScreen;

/**
 * @author Diego Coppetti
 */
public class BulletSystem extends EntitySystem {

    private ImmutableArray<Entity> bulletEntities;
    private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<BulletComponent> bum = ComponentMapper.getFor(BulletComponent.class);
    private ComponentMapper<BoundsComponent> bom = ComponentMapper.getFor(BoundsComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        bulletEntities = engine.getEntitiesFor(Family.all(BulletComponent.class, BodyComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for(Entity entity : bulletEntities) {
            BulletComponent buc = bum.get(entity);
            BodyComponent bc = bm.get(entity);

            if(buc.destroy) destroyBullet(bc, entity);

            BoundsComponent boc = bom.get(entity);
            if(boc != null) checkBounds(buc, bc, boc);
        }
    }

    private void destroyBullet(BodyComponent bc, Entity entity) {
        LevelScreen.engine.removeEntity(entity);
    }

    private void checkBounds(BulletComponent buc, BodyComponent bc, BoundsComponent boc) {
        float bodyRadius = buc.radius/ GameConfig.PPM;
        boolean outOfRightBound = !(bc.body.getPosition().x-bodyRadius < boc.bounds.getWorldWidth()/2);
        boolean outOfLeftBound = !(bc.body.getPosition().x+bodyRadius > -boc.bounds.getWorldWidth()/2);
        boolean outOfUpperBound = !(bc.body.getPosition().y-bodyRadius < boc.bounds.getWorldHeight()/2);
        boolean outOfBottomBound = !(bc.body.getPosition().y+bodyRadius > -boc.bounds.getWorldHeight()/2);
        if(outOfLeftBound || outOfRightBound || outOfBottomBound || outOfUpperBound) {
            buc.destroy = true;
        }
    }
}
