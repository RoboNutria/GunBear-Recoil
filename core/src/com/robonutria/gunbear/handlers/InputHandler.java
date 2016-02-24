package com.robonutria.gunbear.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Clean and solid directional movement
 * 
 * @author Diego Coppetti
 */
public class InputHandler extends InputAdapter {

    public short x = 0;
    public short y = 0;
    // These are stupid generic flags to map in whatever key/button you want
    public boolean actionA = false;
    public boolean actionB = false;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case(Input.Keys.UP):
                y = 1;
                break;
            case(Input.Keys.DOWN):
                y = -1;
                break;
            case(Input.Keys.LEFT):
                x = -1;
                break;
            case(Input.Keys.RIGHT):
                x = 1;
                break;
            case(Input.Keys.X):
                actionA = true;
                break;
            case(Input.Keys.Z):
                actionB = true;
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case(Input.Keys.UP):
                if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    y = -1;
                } else {
                    y = 0;
                }
                break;
            case(Input.Keys.DOWN):
                if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    y = 1;
                } else {
                    y = 0;
                }
                break;
            case(Input.Keys.LEFT):
                if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    x = 1;
                } else {
                   x = 0;
                }
                break;
            case(Input.Keys.RIGHT):
                if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    x = -1;
                } else {
                    x = 0;
                }
                break;
            case(Input.Keys.X):
                actionA = false;
                break;
            default:
                break;
        }
        return false;
    }
}
