package com.project;

public class LevelTwo extends MainLevel {
    public LevelTwo(MainGameWindow mainGameWindow) {
        super(mainGameWindow);
        startLevel();
    }

    @Override
    public void startLevel() {
        System.out.println("Level 2 started!");
    }
}