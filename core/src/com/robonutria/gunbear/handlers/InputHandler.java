package com.robonutria.gunbear.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.math.Vector3;
import com.robonutria.gunbear.utils.controllers.Generic;
import com.robonutria.gunbear.utils.controllers.PS4;

/**
 * Generic input handler with the purpose to handle all input (?)
 * 
 * @author Diego Coppetti
 */
public class InputHandler extends InputAdapter implements ControllerListener {

    private boolean usingKeyboard = true;

    public static enum ControllerType {
        PS4,
        XBOX,
        Generic
    }

    public static boolean HAS_JOYSTICK = true;
    private Controller controller;
    public static ControllerType controllerType;
    public static float AXIS_DEADZONE = 0.3f;

    public float x = 0;
    public float y = 0;
    // These are stupid generic flags to map in whatever key/button you want
    public boolean actionA = false;
    public boolean actionB = false;

    public InputHandler() {
        Controllers.addListener(this);
        if(Controllers.getControllers().size == 0) {
            HAS_JOYSTICK = false;
        } else {
            controller = Controllers.getControllers().first();
            setControllerType(controller);
        }
    }

    private void setControllerType(Controller controller) {
        if (PS4.isPs4Controller(controller)) {
            controllerType = ControllerType.PS4;
        } else if (Xbox.isXboxController(controller)) {
            controllerType = ControllerType.XBOX;
        } else {
            controllerType = ControllerType.Generic;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        usingKeyboard = true;
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

    @Override
    public void connected(Controller controller) {
        HAS_JOYSTICK = true;
        setControllerType(controller);
        this.controller = Controllers.getControllers().first();
    }

    @Override
    public void disconnected(Controller controller) {
        HAS_JOYSTICK = false;
        this.controller = null;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if(controllerType == ControllerType.PS4) {
            if(buttonCode == PS4.BUTTON_CROSS || buttonCode == PS4.BUTTON_SQUARE) {
                actionA = true;
            } else if(buttonCode == PS4.BUTTON_L1 || buttonCode == PS4.BUTTON_R1) {
                actionB = true;
            }
        } else if(controllerType == ControllerType.XBOX) {
            if(buttonCode == Xbox.A || buttonCode == Xbox.X) {
                actionA = true;
            } else if(buttonCode == Xbox.L_BUMPER || buttonCode == Xbox.R_BUMPER) {
                actionB = true;
            }
        } else if(controllerType == ControllerType.Generic) {
            if(buttonCode == Generic.B || buttonCode == Generic.Y) {
                actionA = true;
            } else if(buttonCode == Generic.L || buttonCode == Generic.R) {
                actionB = true;
            }
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if(controllerType == ControllerType.PS4) {
            if(buttonCode == PS4.BUTTON_CROSS || buttonCode == PS4.BUTTON_SQUARE) {
                actionA = false;
            } else if(buttonCode == PS4.BUTTON_L1 || buttonCode == PS4.BUTTON_R1) {
                actionB = false;
            }
        } else if(controllerType == ControllerType.XBOX) {
            if(buttonCode == Xbox.A || buttonCode == Xbox.X) {
                actionA = false;
            } else if(buttonCode == Xbox.L_BUMPER || buttonCode == Xbox.R_BUMPER) {
                actionB = false;
            }
        } else if(controllerType == ControllerType.Generic) {
            if(buttonCode == Generic.B || buttonCode == Generic.Y) {
                actionA = false;
            } else if(buttonCode == Generic.L || buttonCode == Generic.R) {
                actionB = false;
            }
        }
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (controllerType == ControllerType.PS4 || controllerType == ControllerType.Generic) {
            if (axisCode == 1) {
                if(value > AXIS_DEADZONE || value < -AXIS_DEADZONE) {
                    // this using keyboard thing is to let controller and keyboard coexist peacfully
                    usingKeyboard = false;
                    y = value * -1;
                } else {
                    if(!usingKeyboard) {
                        y = 0;
                    }
                }
            }
            if(axisCode == 0) {
                if(value > AXIS_DEADZONE || value < -AXIS_DEADZONE) {
                    usingKeyboard = false;
                    x = value;
                } else {
                    if(!usingKeyboard) {
                        x = 0;
                    }
                }
            }
            return false;
        }
        // TODO: Official Xbox drivers and Ouya (?)
//        if(axisCode == Xbox.L_STICK_HORIZONTAL_AXIS) {
//        } else if(axisCode == Xbox.L_STICK_VERTICAL_AXIS) {
//            y = value;
//        }
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    public void checkDpad() {
        if(controller == null) return;
        PovDirection dpadDir = controller.getPov(0);
        if(dpadDir == PovDirection.center) {
            float axisA = controller.getAxis(0);
            float axisB = controller.getAxis(1);
            if(axisA > AXIS_DEADZONE || axisA < -AXIS_DEADZONE) {
                return;
            }
            if(axisB > AXIS_DEADZONE || axisB < -AXIS_DEADZONE) {
                return;
            }
            if(!usingKeyboard) {
                x = 0;
                y = 0;
            }
            return;
        }
        usingKeyboard = false;
        if(dpadDir == PovDirection.north) {
            y = 1;
        } else if(dpadDir == PovDirection.south){
            y = -1;
        } else {
            y = 0;
        }
        if(dpadDir == PovDirection.west) {
            x = -1;
        } else if(dpadDir == PovDirection.east){
            x = 1;
        } else {
            x = 0;
        }
        if (dpadDir == PovDirection.northWest) {
            x = -1;
            y = 1;
        }
        if(dpadDir == PovDirection.southEast) {
            x = 1;
            y = -1;
        }
        if(dpadDir == PovDirection.northEast) {
            x = 1;
            y = 1;
        }
        if(dpadDir == PovDirection.southWest) {
            x = -1;
            y = -1;
        }
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
