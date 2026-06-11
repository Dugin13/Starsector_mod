package com.bribecredits;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.FireBest;
import com.fs.starfarer.api.util.Misc;

/**
 * Intercepts AI inspector interactions and replaces story point bribe options with credit-based options.
 */
public class AIInspectorInterceptor implements EveryFrameScript {
    
    private static final float BRIBE_COST_BASE = 250000f;
    private static final String AI_INSPECTION_TAG = "HegemonyInspection";
    
    private InteractionDialogAPI currentDialog = null;
    
    @Override
    public boolean isDone() {
        return false; // Never remove this script
    }

    @Override
    public boolean runWhilePaused() {
        return true;
    }

    @Override
    public void advance(float amount) {
        // Check if player is in an interaction
        if (Global.getSector().getCampaignUI().getCurrentInteractionDialog() == null) {
            currentDialog = null;
            return;
        }
        
        InteractionDialogAPI dialog = Global.getSector().getCampaignUI().getCurrentInteractionDialog();
        
        // Only process new dialogs
        if (dialog == currentDialog) {
            return;
        }
        
        currentDialog = dialog;
        
        // Check if this is an AI inspection
        InteractionDialogPlugin plugin = dialog.getPlugin();
        if (plugin == null) return;
        
        // Try to detect AI inspection through memory flags or interaction target
        SectorEntityToken target = dialog.getInteractionTarget();
        if (target == null) return;
        
        // Check if it's a Hegemony fleet doing an inspection
        if (target.getFaction() == null || !target.getFaction().getId().equals("hegemony")) {
            return;
        }
        
        // IMPORTANT: Only modify if this is specifically a COLONY AI inspection
        // Check for colony AI inspection markers
        MemoryAPI memory = target.getMemoryWithoutUpdate();
        if (memory == null) return;
        
        // Look for colony AI inspection-specific flags/tags
        boolean isColonyAIInspection = false;
        
        // Check for HegemonyInspection tag (colony inspections)
        if (target.hasTag(AI_INSPECTION_TAG)) {
            isColonyAIInspection = true;
        }
        
        // Check for memory flags indicating colony AI inspection
        if (memory.contains("$isColonyInspection") || memory.contains("$hegInspectionMarket")) {
            isColonyAIInspection = true;
        }
        
        // Only modify bribe option if this is confirmed to be a colony AI inspection
        if (isColonyAIInspection) {
            modifyBribeOption(dialog);
        }
    }
    
    private void modifyBribeOption(InteractionDialogAPI dialog) {
        // This method hooks into the dialog and replaces story point costs
        // Note: This is a simplified implementation. The actual implementation would need
        // to use reflection or custom rule commands to properly intercept the bribe option.
        
        MemoryAPI memory = dialog.getInteractionTarget().getMemoryWithoutUpdate();
        
        // Set a flag that custom rule commands can check
        memory.set("$bribeWithCredits", true);
        memory.set("$bribeCostCredits", calculateBribeCost());
    }
    
    private float calculateBribeCost() {
        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
        
        // Base cost
        float cost = BRIBE_COST_BASE;
        
        // Increase cost based on fleet size
        int fleetPoints = playerFleet.getFleetPoints();
        cost += fleetPoints * 1000;
        
        // Increase cost based on illegal cargo value
        float cargoValue = 0;
        CargoAPI cargo = playerFleet.getCargo();
        if (cargo != null) {
            // This is simplified - would need to check for AI cores and other contraband
            cargoValue = cargo.getMaxCapacity() * 5000;
        }
        cost += cargoValue;
        
        return Math.min(cost, 1000000); // Cap at 1 million credits
    }
}
