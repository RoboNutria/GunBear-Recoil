package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mechalpaca.gunbear.GunBearRecoil;
import com.mechalpaca.gunbear.utils.Assets;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import static com.mechalpaca.gunbear.GameConfig.*;

/**
 * @author Diego Coppetti
 */
public class RenderSystem extends EntitySystem {

    private SpriteBatch batch;
    public Viewport worldView;
    private OrthographicCamera worldCamera;
    private Box2DDebugRenderer debugRenderer;
    private TextureRegion background;

    public World world;

    public Color color = new Color(0, 0, 0.05f, 1);

    public RenderSystem() {
        batch = new SpriteBatch();
        worldView = new FitViewport(V_WIDTH, V_HEIGHT);
        worldCamera = (OrthographicCamera) worldView.getCamera();
        background = Assets.loadAtlas(GunBearRecoil.SPRITE_ATLAS_FILE).findRegion(GunBearRecoil.BACKGROUND_REGION);
        if(DEBUG_MODE) debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldCamera.update();
        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        batch.draw(background, -worldView.getWorldWidth()/2, -worldView.getWorldHeight()/2,
                worldView.getWorldWidth(), worldView.getWorldHeight());
        Box2DSprite.draw(batch, world, true);
        batch.end();
        if(DEBUG_MODE) debugRenderer.render(world, worldCamera.combined);
    }

    public void resize(int width, int height) {
        worldView.update(width, height);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        batch.dispose();
        if(DEBUG_MODE) debugRenderer.dispose();
    }

}
