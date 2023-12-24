package battle;

import status.Flooded;

public class Terrain {
    private Flooded floodedEffect;
    private double slipChance;

    public Terrain() {
        this.floodedEffect = null; 
    }

    public void flood(double slipChance) {
        this.slipChance = slipChance;   
        this.floodedEffect = new Flooded();
    }

    public double getSlipChance() {
        return slipChance;
    }
    
    public boolean isFlooded() {
        return floodedEffect != null && floodedEffect.isActive();
    }

    public Flooded getFloodedEffect() {
        return floodedEffect;
    }

    public void clearFlooding() {
        floodedEffect.setDuration(0);
    }

    
    public void updateEffects() {
        if (floodedEffect != null) {
            floodedEffect.reduceDuration();
            if (!floodedEffect.isActive()) {
                floodedEffect = null;
            }
        }
    }

}
