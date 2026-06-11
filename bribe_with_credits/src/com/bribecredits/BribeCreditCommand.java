package com.bribecredits;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;

import java.util.List;
import java.util.Map;

/**
 * Custom rule command that handles credit-based bribes for AI inspectors.
 * Usage in rules.csv: BribeCreditCommand <action>
 * Actions: "check" - checks if player can afford, "pay" - deducts credits
 */
public class BribeCreditCommand extends BaseCommandPlugin {
    
    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        if (params.isEmpty()) return false;
        
        String action = params.get(0).getString(memoryMap);
        MemoryAPI memory = memoryMap.get(MemKeys.LOCAL);
        
        float bribeCost = memory.getFloat("$bribeCostCredits");
        if (bribeCost <= 0) {
            bribeCost = 250000f; // Default fallback
        }
        
        switch (action) {
            case "check":
                // Check if player has enough credits
                boolean canAfford = Global.getSector().getPlayerFleet().getCargo().getCredits().get() >= bribeCost;
                memory.set("$canAffordBribe", canAfford);
                return true;
                
            case "pay":
                // Deduct credits and mark inspection as passed
                Global.getSector().getPlayerFleet().getCargo().getCredits().subtract(bribeCost);
                memory.set("$inspectionPassed", true);
                
                // Add a message to the player
                String message = "You bribed the inspector with " + Misc.getDGSCredits(bribeCost) + ".";
                dialog.getTextPanel().addPara(message);
                
                return true;
                
            case "getCost":
                // Set the cost string for display in dialog
                String costStr = Misc.getDGSCredits(bribeCost);
                memory.set("$bribeCostString", costStr);
                return true;
                
            default:
                return false;
        }
    }
}
