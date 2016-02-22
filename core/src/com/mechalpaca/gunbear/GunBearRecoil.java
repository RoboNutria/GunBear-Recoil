package com.mechalpaca.gunbear;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mechalpaca.gunbear.components.Box2DSpriteComponent;
import com.mechalpaca.gunbear.screens.LevelScreen;
import com.mechalpaca.gunbear.utils.Assets;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import net.dermetfan.utils.Function;

import java.rmi.UnexpectedException;

public class GunBearRecoil extends Game {
	public static final String SPRITE_ATLAS_FILE = "sprites.atlas";
	public static final String PLAYER_REGION = "chobi-bear";
	public static final String STARS_REGION = "ass-stars";
	public static final String DARK_BLUE_TEXTURE = "bkg-blue.png";
	public static final String BULLET_1_REGION = "bullet-1";
	public static final String BULLET_2_REGION = "bullet-2";
	public static final String ENEMY_REGION_PREFIX = "enemy-chobi";
	private static final String GAME_MUSIC_FILE = "merry-dance-psg.ogg";

	// shader programs
	public static final String PASSTHROUGH_VERTEX = "shaders/passthrough.vsh";

	public static final String GRAYSCALE_GLSL = "grayscaleGLSL";
	public static final String GRAYSCALE_VERTEX = "shaders/grayscale.vsh";
	public static final String GRAYSCALE_FRAGMENT = "shaders/grayscale.fsh";

	public static final String APESHIT_GLSL = "apeshitGLSL";
	public static final String APESHIT_FRAGMENT = "shaders/apeshit.fsh";

	public static final String EARTHQUAKE_GLSL = "earthquakeGLSL";
	public static final String EARTHQUAKE_VERTEX = "shaders/earthquake.vsh";
	public static final String EARTHQUAKE_FRAGMENT = "shaders/earthquake.fsh";

	public static final String GREENFLASH_GLSL = "greenflashGLSL";
	public static final String GREENFLASH_FRAGMENT = "shaders/green-flash.fsh";

	public static final String TVDISTORTION_GLSL = "tvDistortionGLSL";
	public static final String TVDISTORTION_FRAGMENT = "shaders/tvdistortion.fsh";

	public static final String TVDISTORTION2_GLSL = "tvDistortion2GLSL";
	public static final String TVDISTORTION2_FRAGMENT = "shaders/tvdistortion2.fsh";

	@Override
	public void create () {
		// load assets and shader programs, set user data accessor for dermetfan's box2d sprite cuz we want components
		Assets.loadAtlas(GunBearRecoil.SPRITE_ATLAS_FILE, true);
		Assets.loadTexture(GunBearRecoil.DARK_BLUE_TEXTURE);

		// load and compile shaders
		//Assets.loadShader(GREENFLASH_GLSL, PASSTHROUGH_VERTEX, GREENFLASH_FRAGMENT);
		//Assets.loadShader(GREENFLASH_GLSL, PASSTHROUGH_VERTEX, GREENFLASH_FRAGMENT);
		Assets.loadShader(TVDISTORTION2_GLSL, PASSTHROUGH_VERTEX, TVDISTORTION2_FRAGMENT);

		Assets.loadMusic(GAME_MUSIC_FILE);
		Assets.playMusic(GAME_MUSIC_FILE);
		Box2DSprite.setUserDataAccessor(new Function<Box2DSprite, Object>() {
			@Override
			public Box2DSprite apply(Object arg) {
				Box2DSpriteComponent b2sc = (Box2DSpriteComponent) arg;
				return b2sc == null ? null : b2sc.box2DSprite;
			}
		});
		setScreen(new LevelScreen());
	}

	@Override
	public void dispose() {
		super.dispose();
		Assets.dispose();
	}
}
