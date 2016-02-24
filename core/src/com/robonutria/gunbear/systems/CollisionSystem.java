package com.robonutria.gunbear.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.*;
import com.robonutria.gunbear.components.*;

/**
 * @author Diego Coppetti
 */
public class CollisionSystem extends EntitySystem implements ContactListener {

    private ImmutableArray<Entity> box2DSprites;
    private ComponentMapper<PlayerComponent> pc = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<BodyComponent> bc = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<BulletComponent> boc = ComponentMapper.getFor(BulletComponent.class);
    private ComponentMapper<Box2DSpriteComponent> b2sc = ComponentMapper.getFor(Box2DSpriteComponent.class);
    private ComponentMapper<EnemyComponent> ec = ComponentMapper.getFor(EnemyComponent.class);

    public static short PLAYER_GROUP = -1;
    public static short ENEMY_GROUP = -2;

    @Override
    public void addedToEngine(Engine engine) {
        box2DSprites = engine.getEntitiesFor(Family.all(Box2DSpriteComponent.class).get());
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        Entity entityA = null;
        Entity entityB = null;

        for (Entity sprite : box2DSprites) {
            Box2DSpriteComponent c = b2sc.get(sprite);
            if (fa.getUserData().equals(c)) entityA = sprite;
            else if (fb.getUserData().equals(c)) entityB = sprite;
            if (entityA != null && entityB != null) break;
        }
        BulletComponent bulletComponent;
        EnemyComponent enemyComponent;
        PlayerComponent playerComponent;
        BodyComponent bodyComponent;
        // collision between bullet and enemy
        if ((bulletComponent = boc.get(entityA)) != null && (enemyComponent = ec.get(entityB)) != null) {
            handleBulletEnemyCollision(bulletComponent, enemyComponent);
        } else if ((bulletComponent = boc.get(entityB)) != null && (enemyComponent = ec.get(entityA)) != null) {
            handleBulletEnemyCollision(bulletComponent, enemyComponent);
        } else if ((playerComponent = pc.get(entityA)) != null && (enemyComponent = ec.get(entityB)) != null) {
            bodyComponent = bc.get(entityB);
            handlePlayerEnemyCollision(playerComponent, bodyComponent);
        } else if ((playerComponent = pc.get(entityB)) != null && (enemyComponent = ec.get(entityA)) != null) {
            bodyComponent = bc.get(entityA);
            handlePlayerEnemyCollision(playerComponent, bodyComponent);
        }
    }

    private void handleBulletEnemyCollision(BulletComponent bulletComponent, EnemyComponent enemyComponent) {
        enemyComponent.hp -= bulletComponent.hitPower;
        enemyComponent.wasHit = true;
        bulletComponent.destroy = true;
        if(enemyComponent.hp <= 0) {
            LevelCycleSystem.notifyHit(bulletComponent, true);
        } else {
            LevelCycleSystem.notifyHit(bulletComponent, false);
        }
    }

    private void handlePlayerEnemyCollision(PlayerComponent playerComponent, BodyComponent bodyComponent) {
        if(playerComponent.state == PlayerComponent.PlayerState.Recuperating) return;
        playerComponent.state = PlayerComponent.PlayerState.Hit;
        playerComponent.bodyHitBy = bodyComponent.body;
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        Entity entityA = null;
        Entity entityB = null;

        for (Entity sprite : box2DSprites) {
            Box2DSpriteComponent c = b2sc.get(sprite);
            if (fixtureA.getUserData().equals(c)) entityA = sprite;
            else if (fixtureB.getUserData().equals(c)) entityB = sprite;
            if (entityA != null && entityB != null) break;
        }
        BulletComponent bulletComponent;
        EnemyComponent enemyComponent;
        PlayerComponent playerComponent;
        BodyComponent bodyComponent;
        if ((playerComponent = pc.get(entityA)) != null && (enemyComponent = ec.get(entityB)) != null) {
            contact.setEnabled(false);
        } else if ((playerComponent = pc.get(entityB)) != null && (enemyComponent = ec.get(entityA)) != null) {
            contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
