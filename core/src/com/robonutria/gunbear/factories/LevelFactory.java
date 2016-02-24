package com.robonutria.gunbear.factories;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.robonutria.gunbear.components.Level;

/**
 * @author Diego Coppetti
 */
public class LevelFactory {

    public static Level getLevel(int levelNumber, ProgressBar tensionBar) {
        Level level = new Level();
        if(levelNumber <= 5) {
            level.deltaMulPercent = 0.4f;
            level.enemiesSpawnDelay = 0.5f-levelNumber/40f;
            level.minEnemiesToSpawn = 3;
            level.maxEnemiesToSpawn = 5;
            level.spawnerLifeSpan = -1;
        } else if(levelNumber <= 15) {

        }
        return level;
    }
}
