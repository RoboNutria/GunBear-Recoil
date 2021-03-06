package com.robonutria.gunbear.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.robonutria.gunbear.components.BodyComponent;
import com.robonutria.gunbear.components.GunComponent;
import com.robonutria.gunbear.components.PlayerComponent;
import com.robonutria.gunbear.handlers.InputHandler;

/**
 * @author Diego Coppetti
 */
public class InputSystem extends EntitySystem {

    private ImmutableArray<Entity> playerEntities;
    private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<GunComponent> gm = ComponentMapper.getFor(GunComponent.class);

    private InputHandler input;

    public InputSystem() {
        input = new InputHandler();
    }

    @Override
    public void update(float deltaTime) {
        for(Entity entity : playerEntities) {
            BodyComponent bc = bm.get(entity);
            PlayerComponent pc = pm.get(entity);

            if(pc.canMove) {
                input.checkDpad(); // needed for detecting dpad release on joysticks
                handleInputMovement(pc, bc);
                handleDirectionChange(pc, bc);
            }
            GunComponent gc = gm.get(entity);
            if(gc != null && pc.canFire) {
                checkFireInput(gc);
            }
        }
    }

    private void handleDirectionChange(PlayerComponent pc, BodyComponent bc) {
        if(!input.actionB) return;
        pc.xDir = pc.xDir*-1;
        input.actionB = false;
    }

    private void checkFireInput(GunComponent gc) {
        gc.playerWantsToFire = input.actionA;
    }

    private void handleInputMovement(PlayerComponent pc, BodyComponent bc) {
        Vector2 vel = new Vector2();
        vel.x = input.x * pc.speed;
        vel.y = input.y * pc.speed;
        vel.nor();
        vel.x = vel.x * pc.speed;
        vel.y = vel.y * pc.speed;
        bc.body.setLinearVelocity(bc.body.getLinearVelocity().lerp(vel, pc.friction));
    }

    @Override
    public void addedToEngine(Engine engine) {
        playerEntities = engine.getEntitiesFor(Family.all(PlayerComponent.class, BodyComponent.class).get());
        Gdx.input.setInputProcessor(input);
    }

}
