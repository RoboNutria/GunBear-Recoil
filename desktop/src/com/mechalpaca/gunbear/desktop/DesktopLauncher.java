package com.mechalpaca.gunbear.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mechalpaca.gunbear.GameConfig;
import com.mechalpaca.gunbear.GunBearRecoil;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GameConfig.W_WIDTH;
		config.height = GameConfig.W_HEIGHT;
		config.foregroundFPS = GameConfig.FPS;
		config.vSyncEnabled = GameConfig.VSYNC_ENABLED;
		new LwjglApplication(new GunBearRecoil(), config);
	}
}
