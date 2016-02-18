package com.mechalpaca.gunbear.components;

import com.badlogic.ashley.core.Component;

public class GunComponent implements Component {

	public static enum GunType {
		Player,
		Enemy,
	};

	public GunType type;
	public float recoilPower = 0.5f;
	public float hitPower = 1f;
	public float bulletSpeed = 5f;
	public float shotTimer = 0;
	public float waitTime; // min time between shots
	public boolean readyToFire = true;
	public boolean playerWantsToFire = false;
}
