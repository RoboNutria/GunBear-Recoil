package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mechalpaca.gunbear.GameConfig;
import com.mechalpaca.gunbear.components.*;

public class PlayerSystem extends EntitySystem {

    private ImmutableArray<Entity> playerEntities;
    private Box2DSpriteComponent b2sc = null;
    private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<Box2DSpriteComponent> b2sm = ComponentMapper.getFor(Box2DSpriteComponent.class);
    private ComponentMapper<GunComponent> gm = ComponentMapper.getFor(GunComponent.class);
    private ComponentMapper<BoundsComponent> bom = ComponentMapper.getFor(BoundsComponent.class); // stupid naming...

    @Override
    public void addedToEngine(Engine engine) {
        playerEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
    	for(Entity entity : playerEntities) {
            PlayerComponent pc = pm.get(entity);
            BoundsComponent boc = bom.get(entity);
    		BodyComponent bc = bm.get(entity);
            checkPlayerDirection(entity, pc);
    		checkPlayerOutOfBounds(pc, boc, bc);
            checkPlayerRecoil(entity, pc, bc);
    	}
    }

    private void checkPlayerDirection(Entity entity, PlayerComponent pc) {
        if(b2sc == null)
            b2sc = b2sm.get(entity);
        b2sc.box2DSprite.setFlip(pc.xDir == 1 ? false : true, false);
    }

    private void checkPlayerOutOfBounds(PlayerComponent pc, BoundsComponent boc, BodyComponent bc) {
        if(boc.bounds == null) return;
        float bodyRadius = pc.radius/ GameConfig.PPM;
        boolean outOfRightBound = !(bc.body.getPosition().x+bodyRadius < boc.bounds.getWorldWidth()/2);
        boolean outOfLeftBound = !(bc.body.getPosition().x-bodyRadius > -boc.bounds.getWorldWidth()/2);
        boolean outOfUpperBound = !(bc.body.getPosition().y+bodyRadius < boc.bounds.getWorldHeight()/2);
        boolean outOfBottomBound = !(bc.body.getPosition().y-bodyRadius > -boc.bounds.getWorldHeight()/2);
        if(outOfLeftBound) {
            bc.body.setTransform(-boc.bounds.getWorldWidth()/2+bodyRadius, bc.body.getPosition().y, bc.body.getAngle());
            bc.body.setLinearVelocity(0, bc.body.getLinearVelocity().y);
        } else if(outOfRightBound) {
            bc.body.setTransform(boc.bounds.getWorldWidth()/2-bodyRadius, bc.body.getPosition().y, bc.body.getAngle());
            bc.body.setLinearVelocity(0, bc.body.getLinearVelocity().y);
        }
        if(outOfUpperBound) {
            bc.body.setTransform(bc.body.getPosition().x, boc.bounds.getWorldHeight()/2-bodyRadius, bc.body.getAngle());
            bc.body.setLinearVelocity(bc.body.getLinearVelocity().x, 0);
        } else if(outOfBottomBound) {
            bc.body.setTransform(bc.body.getPosition().x, -boc.bounds.getWorldHeight()/2+bodyRadius, bc.body.getAngle());
            bc.body.setLinearVelocity(bc.body.getLinearVelocity().x, 0);
        }
    }

    private void checkPlayerRecoil(Entity e, PlayerComponent pc, BodyComponent bc) {
        if(pc.state != PlayerComponent.PlayerState.Recoil) return;
        GunComponent gc = gm.get(e);
        if(gc == null) return;
        bc.body.applyForceToCenter(gc.recoilPower * (pc.xDir * -1), 0, true);
        pc.state = PlayerComponent.PlayerState.Normal;
    }

}
