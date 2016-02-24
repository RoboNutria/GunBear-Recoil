package com.robonutria.gunbear;

public class GameConfig {

	public static final int FPS = 60;
	public static final float GAME_STEP = 1f/60f;
	public static final float PPM = 100;
	public static float SPEED_UP = 1;
	public static final int W_WIDTH = 800;
	public static final int W_HEIGHT = 600;
	public static final float V_WIDTH = 192/PPM;
	public static final float V_HEIGHT = 128/PPM;
	public static final int POS_ITER = 6, VEL_ITER = 2;
	public static final boolean VSYNC_ENABLED = true;
	public static final boolean DEBUG_MODE = false;
	
	private GameConfig() {};

}
