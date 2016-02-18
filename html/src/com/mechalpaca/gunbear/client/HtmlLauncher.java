package com.mechalpaca.gunbear.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.mechalpaca.gunbear.GameConfig;
import com.mechalpaca.gunbear.GunBearRecoil;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(GameConfig.W_WIDTH, GameConfig.W_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new GunBearRecoil();
        }
}