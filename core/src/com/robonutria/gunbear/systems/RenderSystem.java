package com.robonutria.gunbear.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.robonutria.gunbear.GunBearRecoil;
import com.robonutria.gunbear.components.BodyComponent;
import com.robonutria.gunbear.components.Box2DSpriteComponent;
import com.robonutria.gunbear.components.MaterialComponent;
import com.robonutria.gunbear.components.PlayerComponent;
import com.robonutria.gunbear.gui.Hud;
import com.robonutria.gunbear.utils.Assets;
import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.graphics.g2d.Box2DSprite;

import static com.robonutria.gunbear.GameConfig.*;

/**
 * @author Diego Coppetti
 */
public class RenderSystem extends EntitySystem {

    private ImmutableArray<Entity> sprites;
    private ComponentMapper<Box2DSpriteComponent> b2sm = ComponentMapper.getFor(Box2DSpriteComponent.class);
    private ComponentMapper<BodyComponent> bcm = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<MaterialComponent> mm = ComponentMapper.getFor(MaterialComponent.class);
    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);

    private SpriteBatch batch;
    public Viewport worldView;
    private OrthographicCamera worldCamera;
    private Box2DDebugRenderer debugRenderer;
    private TextureRegion starsBack;
    private final Texture backTexture;

    public World world;
    public Hud hud;

    public Color color = new Color(0, 0, 0.08f, 1);
    private FPSLogger fpsLogger = new FPSLogger();

    @Override
    public void addedToEngine(Engine engine) {
        sprites = engine.getEntitiesFor(Family.all(Box2DSpriteComponent.class, BodyComponent.class).get());
    }

    public RenderSystem() {
        batch = new SpriteBatch();
        worldView = new FitViewport(V_WIDTH, V_HEIGHT);
        worldCamera = (OrthographicCamera) worldView.getCamera();
        starsBack = Assets.loadAtlas(GunBearRecoil.SPRITE_ATLAS_FILE).findRegion(GunBearRecoil.STARS_REGION);
        backTexture = Assets.getTexture(GunBearRecoil.DARK_BLUE_TEXTURE);
        if(DEBUG_MODE) debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fpsLogger.log();

        worldCamera.update();
        worldView.apply();
        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        renderBackgrounds(deltaTime);
        renderEntities(deltaTime);
        renderHud(deltaTime);
        batch.end();
        if(DEBUG_MODE) debugRenderer.render(world, worldCamera.combined);
    }

    private void renderHud(float deltaTime) {
        if(hud.stage != null) {
            OrthographicCamera guiCamera = (OrthographicCamera) hud.stage.getCamera();
            hud.stage.getViewport().apply();
            guiCamera.update();
            batch.setProjectionMatrix(guiCamera.combined);
            hud.stage.draw();
        }
    }

    private void renderEntities(float deltaTime) {
        for (Entity entity : sprites) {
            Box2DSprite b2s = b2sm.get(entity).box2DSprite;
            Body b = bcm.get(entity).body;
            PlayerComponent pc = pm.get(entity);
            MaterialComponent mc = mm.get(entity);
            if(mc != null && mc.run) {
                batch.setShader(mc.shaderProgram);
                updateSpriteShader(mc, deltaTime);
            }
            b2s.draw(batch, b);
            batch.setShader(null);
        }
    }

    private void renderBackgrounds(float deltaTime) {
        updateBackgroundShader(deltaTime);
        // color background
        batch.disableBlending();
        batch.draw(backTexture, -worldView.getWorldWidth()/2, -worldView.getWorldHeight()/2,
                worldView.getWorldWidth(), worldView.getWorldHeight());
        // stars background
        batch.enableBlending();
        batch.setShader(null);
        batch.draw(starsBack, -worldView.getWorldWidth()/2, -worldView.getWorldHeight()/2,
                worldView.getWorldWidth(), worldView.getWorldHeight());
    }

    private void updateSpriteShader(MaterialComponent mc, float deltaTime) {
        // TODO: Cases for each shader program
    }

    private float count = 0;
    private void updateBackgroundShader(float deltaTime) {
        batch.setShader(GunBearRecoil.sm.get(GunBearRecoil.SCANLINES));
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
