package com.robonutria.gunbear.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * @author Diego Coppetti
 */
public class MaterialComponent implements Component {
    public ShaderProgram shaderProgram;
    public boolean run = true;
    public boolean dispose = false;
}
