package com.bribecredits;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

public class BribeCreditsPlugin extends BaseModPlugin {
    
    @Override
    public void onApplicationLoad() {
        // Called when the game starts
    }
    
    @Override
    public void onGameLoad(boolean newGame) {
        // Called when a save is loaded or new game started
        Global.getSector().addTransientScript(new AIInspectorInterceptor());
    }
}
