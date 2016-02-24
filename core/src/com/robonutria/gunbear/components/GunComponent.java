package com.robonutria.gunbear.components;

import com.badlogic.ashley.core.Component;

public class GunComponent implements Component {

	public static enum GunType {
		Player,
		Enemy,
	};

	public GunType type;
	public float recoilPower = 0.5f;
	public int hitPower = 1;
	public float bulletSpeed = 5f;
	public float shotTimer = 0;
	public float waitTime = 0.25f; // min time between shots
	public boolean readyToFire = true;
	public boolean playerWantsToFire = false;
}
