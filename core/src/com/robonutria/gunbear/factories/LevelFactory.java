package com.robonutria.gunbear.factories;

import com.badlogic.gdx.math.MathUtils;
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
            level.enemiesSpawnDelay = 0.5f-levelNumber/30f;
            level.minEnemiesToSpawn = 3;
            level.maxEnemiesToSpawn = 7;
            level.spawnerLifeSpan = -1;
            level.parallelSpawns = 1;
            level.delayBetweenSpawns = Math.max(3, 5-levelNumber);
            System.out.println(level.delayBetweenSpawns);
        } else if(levelNumber <= 15) {

        }
        return level;
    }
}
