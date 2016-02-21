package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.mechalpaca.gunbear.GameConfig;
import com.mechalpaca.gunbear.components.*;
import com.mechalpaca.gunbear.gui.Hud;

public class PlayerSystem extends EntitySystem {

    private ImmutableArray<Entity> playerEntities;
    private Box2DSpriteComponent b2sc = null;
    private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<Box2DSpriteComponent> b2sm = ComponentMapper.getFor(Box2DSpriteComponent.class);
    private ComponentMapper<GunComponent> gm = ComponentMapper.getFor(GunComponent.class);
    private ComponentMapper<BoundsComponent> bom = ComponentMapper.getFor(BoundsComponent.class); // stupid naming...

    private float playerHitTimer = 0;
    private float playerInvincibleDelay = 1.5f;
    public Hud hud;

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
            checkPlayerWasHit(entity, pc, bc, deltaTime);
            checkPlayerDirection(entity, pc);
    		checkPlayerOutOfBounds(pc, boc, bc);
            checkPlayerRecoil(entity, pc, bc);
    	}
    }

    private void checkPlayerWasHit(Entity entity, PlayerComponent pc, BodyComponent bc, float deltaTime) {
        if(pc.state == PlayerComponent.PlayerState.Hit) {
            if(b2sc == null)
                b2sc = b2sm.get(entity);
            b2sc.box2DSprite.setColor(Color.RED);
            pc.canFire = false;
            pc.canMove = false;

            ProgressBar hpBar = hud.hpBar;
            hpBar.setValue(hpBar.getValue() - pc.hpDamage);
            if(hpBar.getValue() <= 0) {
                if(hud.playerLives <= 0) {
                    System.out.println("Game Over");
                    // TODO: Do something, animation + restart game
                }
                hud.removeCredit();
            }

            float forceX = 0;
            float forceY = 0;
            Body enemyBody = pc.bodyHitBy;
            if(bc.body.getPosition().y <= enemyBody.getPosition().y) forceY = -pc.hitBackForce;
            if(bc.body.getPosition().y >= enemyBody.getPosition().y) forceY = pc.hitBackForce;
            if(bc.body.getPosition().x <= enemyBody.getPosition().x) forceX = -pc.hitBackForce;
            if(bc.body.getPosition().x >= enemyBody.getPosition().x) forceX = pc.hitBackForce;
            bc.body.applyForceToCenter(forceX, forceY, true);

            pc.state = PlayerComponent.PlayerState.Recuperating;
        } else if(pc.state == PlayerComponent.PlayerState.Recuperating) {
            playerHitTimer += deltaTime;
            if(playerHitTimer >= playerInvincibleDelay/2f) {
                b2sc.box2DSprite.setColor(Color.YELLOW);
                b2sc.box2DSprite.setColor(1, 1, 1, 0.5f);
                pc.canFire = true;
                pc.canMove = true;
            }
            if(playerHitTimer >= playerInvincibleDelay) {
                b2sc.box2DSprite.setColor(1, 1, 1, 1);
                playerHitTimer = 0;
                pc.state = PlayerComponent.PlayerState.Normal;
            }
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
