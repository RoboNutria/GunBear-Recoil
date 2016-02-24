package com.robonutria.gunbear.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Box2DFactory {
	
	private static BodyDef bodyDef;
	private static FixtureDef fixtureDef;

	public static Body createBody(World world, float ppm, BodyType type, Vector2 pos, float angle, float linearDamping) {
		if(bodyDef == null) bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set(convertToBox(pos.x, ppm), convertToBox(pos.y, ppm));
		bodyDef.angle = angle;
		bodyDef.linearDamping = linearDamping;
		bodyDef.fixedRotation = true;
		return world.createBody(bodyDef);
	}

	public static Fixture makeCircleFixture(float ppm, float radius, float density, float restitution, Body body) {
		fixtureDef = new FixtureDef();
		fixtureDef.density = density;
		fixtureDef.restitution = restitution;
		fixtureDef.shape = new CircleShape();
		fixtureDef.shape.setRadius(convertToBox(radius, ppm));
		Fixture fixture = body.createFixture(fixtureDef);
		fixtureDef.shape.dispose();
		return fixture;
	}

	private static float convertToBox(float x, float ppm) {
		return x / ppm;
	}

}
