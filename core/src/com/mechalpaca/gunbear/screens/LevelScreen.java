package com.mechalpaca.gunbear.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mechalpaca.gunbear.GameConfig;
import com.mechalpaca.gunbear.components.BodyComponent;
import com.mechalpaca.gunbear.factories.EntityFactory;
import com.mechalpaca.gunbear.gui.Hud;
import com.mechalpaca.gunbear.listeners.BodyDisposeListener;
import com.mechalpaca.gunbear.systems.*;

import java.util.Iterator;

public class LevelScreen implements Screen {

	public static Engine engine;
	private float accumulator;

	private Hud hud;

	public LevelScreen() {
		engine = new Engine();
	}

	@Override
	public void show() {
		// create the systems
		PhysicsSystem physicsSystem = new PhysicsSystem();
		PlayerSystem playerSystem = new PlayerSystem();
		InputSystem inputSystem = new InputSystem();
		GunSystem gunSystem = new GunSystem();
		BulletSystem bulletSystem = new BulletSystem();
		EnemySpawnerSystem enemySpawnerSystem = new EnemySpawnerSystem();
		MovementSystem movementSystem = new MovementSystem();
		CollisionSystem collisionSystem = new CollisionSystem();
		EnemySystem enemySystem = new EnemySystem();
		RenderSystem renderSystem = new RenderSystem();
		LevelCycleSystem levelCycleSystem = new LevelCycleSystem();

		renderSystem.world = physicsSystem.world;
		physicsSystem.world.setContactListener(collisionSystem);

		// add the systems (order matters)
		engine.addSystem(physicsSystem);
		engine.addSystem(playerSystem);
		engine.addSystem(inputSystem);
		engine.addSystem(gunSystem);
		engine.addSystem(bulletSystem);
		engine.addSystem(enemySpawnerSystem);
		engine.addSystem(movementSystem);
		engine.addSystem(collisionSystem);
		engine.addSystem(enemySystem);
		engine.addSystem(renderSystem);

		hud = new Hud();
		hud.createPlayerCredits();
		hud.createTensionBar();
		hud.createHPBar();
		levelCycleSystem.hud = hud;

		engine.addSystem(levelCycleSystem);

		// add entity listeners
		Family bodyFamily = Family.all(BodyComponent.class).get();
		engine.addEntityListener(bodyFamily, new BodyDisposeListener());

		// add the entities
		World world = engine.getSystem(PhysicsSystem.class).world;
		engine.addEntity(EntityFactory.createGunBear(world, 0, 0, engine.getSystem(RenderSystem.class).worldView));


		renderSystem.hud = hud;
		playerSystem.hud = hud;
	}

	@Override
	public void render(float delta) {
		engine.update(delta);
		hud.stage.act(delta);
	}

	@Override
	public void resize(int width, int height) {
		if (engine.getSystem(RenderSystem.class) != null)
			engine.getSystem(RenderSystem.class).resize(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		engine.removeAllEntities();
		Iterator<EntitySystem> systems = engine.getSystems().iterator();
		while (systems.hasNext()) {
			EntitySystem system = systems.next();
			system.setProcessing(false);
			engine.removeSystem(system);
		}
		hud.dispose();
	}

}
