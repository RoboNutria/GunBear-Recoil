package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import static com.mechalpaca.gunbear.GameConfig.*;

/**
 * @author Diego Coppetti
 */
public class PhysicsSystem extends EntitySystem {

    public World world;
    public Vector2 gravity = new Vector2(0, 0);

    public PhysicsSystem() {
        world = new World(gravity, true);
    }

    @Override
    public void update(float deltaTime) {
    	world.step(GAME_STEP, VEL_ITER, POS_ITER);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        world.dispose();
    }
}
