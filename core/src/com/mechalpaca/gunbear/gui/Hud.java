package com.mechalpaca.gunbear.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mechalpaca.gunbear.GameConfig;
import com.mechalpaca.gunbear.GunBearRecoil;
import com.mechalpaca.gunbear.utils.Assets;

/**
 * @author Diego Coppetti
 */
public class Hud implements Disposable {
    private final Skin skin;
    private final ProgressBar.ProgressBarStyle tensionBarStyle;
    private final ProgressBar.ProgressBarStyle hpBarStyle;
    public Stage stage;
    public ProgressBar tensionBar;
    public ProgressBar hpBar;

    public int playerLives = 3;
    private Array<Image> playerLivesImages = new Array<Image>();

    public Hud() {
        stage = new Stage(new FitViewport(GameConfig.W_WIDTH, GameConfig.W_HEIGHT));
        // I'm using a skin we already have, but I perfectly could use any texture region or take shit from an atlas
        skin = new Skin(Gdx.files.internal("gui/uiskin.json"));
        tensionBarStyle = new ProgressBar.ProgressBarStyle();
        tensionBarStyle.background = new TextureRegionDrawable(skin.getRegion("textfield"));
        tensionBarStyle.knobBefore = new TextureRegionDrawable(skin.getRegion("white"));

        hpBarStyle = new ProgressBar.ProgressBarStyle();
        hpBarStyle.background = new TextureRegionDrawable(skin.getRegion("textfield"));
        hpBarStyle.knobBefore = new TextureRegionDrawable(skin.getRegion("white"));
    }

    public void createTensionBar() {
        float x = 0;
        float y = stage.getViewport().getWorldHeight()/1.14f;
        float width = 200;
        float height = 100;
        ProgressBar progressBar = new ProgressBar(1, 10, 0.001f, false, tensionBarStyle);
        progressBar.setValue(0);
        progressBar.setPosition(x, y);
        progressBar.setSize(width, height);
        progressBar.setColor(1, 1, 1, 0.5f);

        stage.addActor(progressBar);
        tensionBar = progressBar;
    }

    public void createHPBar() {
        float x = 0;
        float y = -45;
        float width = 200;
        float height = 100;
        ProgressBar progressBar = new ProgressBar(0, 10, 1f, false, hpBarStyle);
        progressBar.setValue(progressBar.getMaxValue());
        progressBar.setPosition(x, y);
        progressBar.setSize(width, height);
        progressBar.setColor(1, 1, 1, 0.5f);

        stage.addActor(progressBar);
        hpBar = progressBar;
    }

    public void createPlayerCredits() {
        TextureRegion region = Assets.getAtlas(GunBearRecoil.SPRITE_ATLAS_FILE).findRegion(GunBearRecoil.PLAYER_REGION);
        float y = region.getRegionHeight()*2;
        float x = region.getRegionWidth()*2;
        for (int i = 0; i < playerLives; i++) {
            Image playerLivesImage = new Image(region);
            playerLivesImage.scaleBy(1f);
            playerLivesImage.setPosition(i*x, y);
            playerLivesImage.setColor(1, 1, 1, 0.5f);
            playerLivesImages.add(playerLivesImage);
            stage.addActor(playerLivesImage);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public void removeCredit() {
        if(playerLives == 0) return;
        playerLives--;
        hpBar.setValue(hpBar.getMaxValue());
        Image image = playerLivesImages.get(playerLivesImages.size-1);
        playerLivesImages.removeIndex(playerLivesImages.size-1);
        image.remove();
    }
}
