package com.mechalpaca.gunbear.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mechalpaca.gunbear.GunBearRecoil;
import com.mechalpaca.gunbear.components.BodyComponent;
import com.mechalpaca.gunbear.components.Box2DSpriteComponent;
import com.mechalpaca.gunbear.components.MaterialComponent;
import com.mechalpaca.gunbear.gui.Hud;
import com.mechalpaca.gunbear.utils.Assets;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import static com.mechalpaca.gunbear.GameConfig.*;

/**
 * @author Diego Coppetti
 */
public class RenderSystem extends EntitySystem {

    private ImmutableArray<Entity> sprites;
    private ComponentMapper<Box2DSpriteComponent> b2sm = ComponentMapper.getFor(Box2DSpriteComponent.class);
    private ComponentMapper<BodyComponent> bcm = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<MaterialComponent> mm = ComponentMapper.getFor(MaterialComponent.class);

    private SpriteBatch batch;
    public Viewport worldView;
    private OrthographicCamera worldCamera;
    private Box2DDebugRenderer debugRenderer;
    private TextureRegion background;

    public World world;
    public Hud hud;

    public Color color = new Color(0, 0, 0.05f, 1);

    @Override
    public void addedToEngine(Engine engine) {
        sprites = engine.getEntitiesFor(Family.all(Box2DSpriteComponent.class, BodyComponent.class).get());
    }

    public RenderSystem() {
        batch = new SpriteBatch();
        worldView = new FitViewport(V_WIDTH, V_HEIGHT);
        worldCamera = (OrthographicCamera) worldView.getCamera();
        background = Assets.loadAtlas(GunBearRecoil.SPRITE_ATLAS_FILE).findRegion(GunBearRecoil.BACKGROUND_REGION);
        ShaderProgram.pedantic = false;
        if(DEBUG_MODE) debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldCamera.update();
        worldView.apply();
        batch.setProjectionMatrix(worldCamera.combined);
        updateBackgroundShader(deltaTime);
        batch.begin();
        batch.draw(background, -worldView.getWorldWidth()/2, -worldView.getWorldHeight()/2,
                worldView.getWorldWidth(), worldView.getWorldHeight());
        batch.setShader(null);
        for (Entity entity : sprites) {
            Box2DSprite b2s = b2sm.get(entity).box2DSprite;
            Body b = bcm.get(entity).body;
            MaterialComponent mc = mm.get(entity);
            if(mc != null && mc.run) {
                batch.setShader(mc.shaderProgram);
                updateSpriteShader(mc, deltaTime);
            }
            b2s.draw(batch, b);
            batch.setShader(null);
        }
        if(hud.stage != null) {
            OrthographicCamera guiCamera = (OrthographicCamera) hud.stage.getCamera();
            hud.stage.getViewport().apply();
            guiCamera.update();
            batch.setProjectionMatrix(guiCamera.combined);
            hud.stage.draw();
        }
        batch.end();
        if(DEBUG_MODE) debugRenderer.render(world, worldCamera.combined);
    }

    private void updateSpriteShader(MaterialComponent mc, float deltaTime) {
        // TODO: Cases for each shader program
        mc.shaderProgram.begin();
        mc.shaderProgram.setUniformf("resolution", new Vector2(320, 240));
        mc.shaderProgram.setUniformf("time", deltaTime);
        mc.shaderProgram.end();
    }

    private void updateBackgroundShader(float deltaTime) {
        // TODO: Something (?)
    }

    public void resize(int width, int height) {
        worldView.update(width, height);
        hud.stage.getViewport().update(width, height);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        batch.dispose();
        if(DEBUG_MODE) debugRenderer.dispose();
    }

}
