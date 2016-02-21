package com.mechalpaca.gunbear.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mechalpaca.gunbear.GunBearRecoil;
import com.mechalpaca.gunbear.components.*;
import com.mechalpaca.gunbear.components.GunComponent.GunType;
import com.mechalpaca.gunbear.components.movement.BodyFollowComponent;
import com.mechalpaca.gunbear.screens.LevelScreen;
import com.mechalpaca.gunbear.systems.CollisionSystem;
import com.mechalpaca.gunbear.systems.RenderSystem;
import com.mechalpaca.gunbear.utils.Assets;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import static com.mechalpaca.gunbear.GameConfig.PPM;

/**
 * @author Diego Coppetti
 */
public class EntityFactory {

    // TODO: Pooling for fucking bullets: enemy and player

    private static TextureRegion playerRegion = Assets.getAtlas(GunBearRecoil.SPRITE_ATLAS_FILE).findRegion(GunBearRecoil.PLAYER_REGION);
    private static TextureRegion playerBulletRegion = Assets.getAtlas(GunBearRecoil.SPRITE_ATLAS_FILE).findRegion(GunBearRecoil.BULLET_2_REGION);
    private static Array<TextureRegion> enemyRegions = Assets.getAtlasRegions(GunBearRecoil.SPRITE_ATLAS_FILE, GunBearRecoil.ENEMY_REGION_PREFIX, "-", 1);

    private EntityFactory() {}

    // There is no fixed/formal definition for what the player (gun bear) is, but an entity
    // made up of certain components.
    // So we don't really have "game objects"
    public static Entity createGunBear(World world, float x, float y, Viewport bounds) {

        Entity entity = new Entity();

        GunComponent gc = new GunComponent();
        gc.type = GunType.Player;
        gc.waitTime = 0.25f;
        gc.hitPower = 1;
        gc.bulletSpeed = 2f;
        gc.recoilPower = 0.35f;

        PlayerComponent pc = new PlayerComponent();
        pc.speed = 5f;
        pc.friction = 0.02f;
        pc.radius = playerRegion.getRegionWidth()/2;

        BoundsComponent boc = new BoundsComponent();
        boc.bounds = bounds;

        Box2DSpriteComponent bsc = new Box2DSpriteComponent();
        BodyComponent bc = new BodyComponent();

        bsc.box2DSprite = new Box2DSprite(playerRegion);
        bc.body = Box2DFactory.createBody(world, PPM, BodyDef.BodyType.DynamicBody, new Vector2(x, y), 0, 4f);
        bc.body.setFixedRotation(true);
        Fixture fixture = Box2DFactory.makeCircleFixture(PPM, pc.radius, 1, 0, bc.body);
        Filter filter = new Filter();
        filter.groupIndex = CollisionSystem.PLAYER_GROUP;
        fixture.setFilterData(filter);
        fixture.setUserData(bsc);

        // so the player is composed of all this shit
        entity.add(bc);
        entity.add(bsc);
        entity.add(boc);
        entity.add(pc);
        entity.add(gc);
        return entity;
    }

    public static Entity createPlayerBullet(World world, float x, float y, float angle, GunComponent gc, PlayerComponent pc) {

        float bulletRadius = 2f;
        Entity entity = new Entity();

        BodyComponent bc = new BodyComponent();
        bc.body = Box2DFactory.createBody(world, 1, BodyDef.BodyType.DynamicBody, new Vector2(x, y), angle, 0);
        bc.body.setBullet(false);
        Fixture fixture = Box2DFactory.makeCircleFixture(PPM, bulletRadius, 1, 0, bc.body);
        fixture.setSensor(false);
        Filter filter = new Filter();
        filter.groupIndex = CollisionSystem.PLAYER_GROUP;
        fixture.setFilterData(filter);
        bc.body.setLinearVelocity(gc.bulletSpeed * pc.xDir, 0);

        BulletComponent buc = new BulletComponent();
        buc.hitPower = gc.hitPower;
        buc.isPlayer = true;
        buc.radius = bulletRadius;
        buc.speed = gc.bulletSpeed; // do I need this? Because linear velocity, fuck you
        buc.type = BulletComponent.BulletType.Normal;

        // For example, no bounds means no checking if is off screen, get it carlos?
        BoundsComponent boc = new BoundsComponent();
        Viewport bounds = LevelScreen.engine.getSystem(RenderSystem.class).worldView;
        boc.bounds = bounds;

        Box2DSpriteComponent b2sc = new Box2DSpriteComponent();
        b2sc.box2DSprite = new Box2DSprite(playerBulletRegion);
        b2sc.box2DSprite.setRotation(bc.body.getAngle() * MathUtils.degRad);
        if(bc.body.getLinearVelocity().x > 0) b2sc.box2DSprite.setFlip(true, false);
        fixture.setUserData(b2sc);

        entity.add(bc);
        entity.add(b2sc);
        entity.add(buc);
        entity.add(boc);

        return entity;
    }

    public static Entity createEnemy(World world, EnemySpawnerComponent esc,
                                     BodyComponent playerBodyComponent) {
        Entity e = new Entity();

        EnemyComponent ec = new EnemyComponent();
        ec.radius = enemyRegions.first().getRegionWidth()/2;
        ec.hp = esc.enemyHP;

        BodyComponent bc = new BodyComponent();
        Vector2 pos = esc.getNextEnemyPos();
        bc.body = Box2DFactory.createBody(world, 1, BodyDef.BodyType.DynamicBody, new Vector2(pos.x, pos.y), 0, 4f);
        bc.body.setFixedRotation(true);
        Fixture fixture = Box2DFactory.makeCircleFixture(PPM, ec.radius, 0.5f, 2, bc.body);
        // TODO: Collision filtering - is this ok?
        Filter filter = new Filter();
        filter.groupIndex = CollisionSystem.ENEMY_GROUP;
        fixture.setFilterData(filter);


        Box2DSpriteComponent b2sc = new Box2DSpriteComponent();
        b2sc.box2DSprite = new Box2DSprite(enemyRegions.first());
        fixture.setUserData(b2sc);

        if(esc.enemyMovementType == EnemySpawnerComponent.EnemyMovementType.LinearVelocity) {
            LinearVelocityComponent lvc = new LinearVelocityComponent();
            lvc.speed = esc.enemySpeed;
            float xDir = 0;
            float yDir = 0;
            if(esc.spawnType == EnemySpawnerComponent.SpawnType.RandomY) {
                if(pos.x < 0) xDir = 1;
                else if(pos.x > 0) xDir = -1;
            } else if(esc.spawnType == EnemySpawnerComponent.SpawnType.RandomX) {
                if(pos.y < 0) yDir = 1;
                else if(pos.y > 0) yDir = -1;
            } else if(esc.spawnType == EnemySpawnerComponent.SpawnType.RandomXY) {
                // TODO: RandomXY
            }
            lvc.direction = new Vector2(xDir, yDir);
            e.add(lvc);
        } else if (esc.enemyMovementType == EnemySpawnerComponent.EnemyMovementType.BodyFollow) {
            BodyFollowComponent bfc = new BodyFollowComponent();
            bfc.target = playerBodyComponent.body;
            bfc.speed = esc.enemySpeed;
            e.add(bfc);
        }

        e.add(b2sc);
        e.add(ec);
        e.add(bc);
        return e;
    }

}
