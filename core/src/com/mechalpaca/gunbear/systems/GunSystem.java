package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mechalpaca.gunbear.components.BodyComponent;
import com.mechalpaca.gunbear.components.GunComponent;
import com.mechalpaca.gunbear.components.PlayerComponent;
import com.mechalpaca.gunbear.factories.EntityFactory;
import com.mechalpaca.gunbear.screens.LevelScreen;

public class GunSystem extends EntitySystem {

    private ImmutableArray<Entity> gunWieldersEntities;
	private PlayerComponent pc;
    private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<GunComponent> gm = ComponentMapper.getFor(GunComponent.class);
	private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);

    @SuppressWarnings("unchecked")
	@Override
    public void addedToEngine(Engine engine) {
        gunWieldersEntities = engine.getEntitiesFor(Family.all(GunComponent.class, BodyComponent.class).get());
    }
    
    @Override
    public void update(float deltaTime) {
    	for (Entity entity : gunWieldersEntities) {
    		BodyComponent bc = bm.get(entity);
    		GunComponent gc = gm.get(entity);
    		
    		gc.shotTimer += deltaTime;
    		if(!gc.readyToFire && gc.shotTimer >= gc.waitTime) gc.readyToFire = true;
    		if(gc.readyToFire) {
    			switch (gc.type) {
				case Player:
					if(gc.playerWantsToFire) {
						if(pc == null) pc = pm.get(entity);
						if(pc.state == PlayerComponent.PlayerState.Normal) {
							firePlayerGun(pc, bc, gc);
							pc.state = PlayerComponent.PlayerState.Recoil;
						}
					}
					break;
				case Enemy:
					fireEnemyGun(bc, gc);
					break;
				default:
					break;
				}
    		}
    	}
    }
	private void fireEnemyGun(BodyComponent bc, GunComponent gc) {
		gc.shotTimer = 0;
		gc.readyToFire = false;
	}

	private void firePlayerGun(PlayerComponent pc, BodyComponent bc, GunComponent gc) {
		gc.shotTimer = 0;
		gc.readyToFire = false;
		Entity bulletEntity = EntityFactory.createPlayerBullet(
				bc.body.getWorld(), bc.body.getPosition().x, bc.body.getPosition().y,
				bc.body.getAngle(), gc, pc);
		LevelScreen.engine.addEntity(bulletEntity);
	}


}
