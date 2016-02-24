package com.robonutria.gunbear;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.robonutria.gunbear.components.Box2DSpriteComponent;
import com.robonutria.gunbear.screens.LevelScreen;
import com.robonutria.gunbear.utils.Assets;
import com.robonutria.gunbear.utils.shaders.ShaderManager;
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
	public static final String PASSTHROUGH_VERTEX = "passthrough.vert";

	public static final String GRAYSCALE = "grayscale";
	public static final String GRAYSCALE_VERTEX = "grayscale.vsh";
	public static final String GRAYSCALE_FRAGMENT = "grayscale.fsh";

	public static final String APESHIT = "apeshit";
	public static final String APESHIT_FRAGMENT = "apeshit.fsh";

	public static final String SCANLINES = "scanlines";
	public static final String SCANLINES_FRAGMENT = "scanlines.frag";

	public static final String WHITE = "white";
	public static final String WHITE_FRAGMENT = "white.frag";

	public static final String INVERT = "invert";
	public static final String INVERT_VERTEX = "invert.vsh";
	public static final String INVERT_FRAGMENT = "invert.fsh";

	public static ShaderManager sm;
	public static AssetManager am;

	@Override
	public void create () {
		// load assets
		am = new AssetManager();
		Assets.loadAtlas(GunBearRecoil.SPRITE_ATLAS_FILE, true);
		Assets.loadTexture(GunBearRecoil.DARK_BLUE_TEXTURE);
		Assets.loadMusic(GAME_MUSIC_FILE);
		Assets.playMusic(GAME_MUSIC_FILE);

		// load shaders
		ShaderProgram.pedantic = false;
		sm = new ShaderManager("shaders", am);
		sm.add(SCANLINES, PASSTHROUGH_VERTEX, SCANLINES_FRAGMENT);
		sm.add(WHITE, PASSTHROUGH_VERTEX, WHITE_FRAGMENT);
		sm.add(INVERT, INVERT_VERTEX, INVERT_FRAGMENT);
		am.finishLoading();

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
