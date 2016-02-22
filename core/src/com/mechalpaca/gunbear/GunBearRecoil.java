package com.mechalpaca.gunbear;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.mechalpaca.gunbear.components.Box2DSpriteComponent;
import com.mechalpaca.gunbear.screens.LevelScreen;
import com.mechalpaca.gunbear.utils.Assets;
import com.mechalpaca.gunbear.utils.shaders.ShaderManager;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import net.dermetfan.utils.Function;

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
	public static final String PASSTHROUGH_VERTEX = "passthrough.vsh";

	public static final String GRAYSCALE_GLSL = "grayscaleGLSL";
	public static final String GRAYSCALE_VERTEX = "grayscale.vsh";
	public static final String GRAYSCALE_FRAGMENT = "grayscale.fsh";

	public static final String APESHIT_GLSL = "apeshitGLSL";
	public static final String APESHIT_FRAGMENT = "apeshit.fsh";

	public static ShaderManager sm;
	public static AssetManager am;

	@Override
	public void create () {
		// load assets and shader programs, set user data accessor for dermetfan's box2d sprite cuz we want components
		am = new AssetManager();
		Assets.loadAtlas(GunBearRecoil.SPRITE_ATLAS_FILE, true);
		Assets.loadTexture(GunBearRecoil.DARK_BLUE_TEXTURE);

		// load shaders
		ShaderProgram.pedantic = false;
		sm = new ShaderManager("shaders", am);
		sm.add(APESHIT_GLSL, PASSTHROUGH_VERTEX, APESHIT_FRAGMENT);
		am.finishLoading();


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
		sm.dispose();
		am.dispose();
	}

}
