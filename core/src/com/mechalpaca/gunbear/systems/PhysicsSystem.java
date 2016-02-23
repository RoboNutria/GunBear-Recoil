package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mechalpaca.gunbear.GameConfig;

import static com.mechalpaca.gunbear.GameConfig.*;

/**
 * @author Diego Coppetti
 */
public class PhysicsSystem extends EntitySystem {

    public World world;
    public Vector2 gravity = new Vector2(0, 0);
    private float accumulator;

    public PhysicsSystem() {
        world = new World(gravity, true);
    }

    @Override
    public void update(float deltaTime) {
		deltaTime = deltaTime * GameConfig.SPEED_UP;
		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		while (accumulator >= GameConfig.GAME_STEP) {
            world.step(GAME_STEP, VEL_ITER, POS_ITER);
			accumulator -= GameConfig.GAME_STEP;
		}
    }

    @Override
    public void removedFromEngine(Engine engine) {
        world.dispose();
    }
}
