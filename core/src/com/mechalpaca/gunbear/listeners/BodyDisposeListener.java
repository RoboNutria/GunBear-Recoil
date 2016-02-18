package com.mechalpaca.gunbear.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.physics.box2d.Body;
import com.mechalpaca.gunbear.components.BodyComponent;

/**
 * @author Diego Coppetti
 */
public class BodyDisposeListener implements EntityListener {

    private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);

    @Override
    public void entityAdded(Entity entity) {
    }

    @Override
    public void entityRemoved(Entity entity) {
        BodyComponent bc = bm.get(entity);
        Body body = bc.body;
        bc.body.getWorld().destroyBody(body);
    }
}
