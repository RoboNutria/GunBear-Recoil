package com.mechalpaca.gunbear;

import com.badlogic.gdx.Game;
import com.mechalpaca.gunbear.components.Box2DSpriteComponent;
import com.mechalpaca.gunbear.screens.LevelScreen;
import com.mechalpaca.gunbear.utils.Assets;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import net.dermetfan.utils.Function;

public class GunBearRecoil extends Game {
	public static final String SPRITE_ATLAS_FILE = "sprites.atlas";
	public static final String PLAYER_REGION = "chobi-bear";
	public static final String BACKGROUND_REGION = "ass-stars";
	public static final String BULLET_1_REGION = "bullet-1";
	public static final String BULLET_2_REGION = "bullet-2";
	public static final String ENEMY_REGION_PREFIX = "enemy-chobi";
	private static final String GAME_MUSIC_FILE = "merry-dance-psg.ogg";

	@Override
	public void create () {
		// load assets, set user data accessor for dermetfan's box2d sprite cuz we want components
		Assets.loadAtlas(GunBearRecoil.SPRITE_ATLAS_FILE, true);
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
