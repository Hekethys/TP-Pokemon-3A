package item;

import battle.Terrain;
import pokemon.Pokemon;

public class Medicine extends Item {
    private String effect;

    public Medicine(String name, String effect) {
        super(name, "Medicine", effect);
        this.effect = effect;
    }

    public String getEffect() {
        return effect;
    }
    public void use(Pokemon target, Terrain terrain) {
        if ("CurePoison".equals(effect)) {
            target.cureStatus("Poisoned");
        } else if ("CureBurn".equals(effect)) {
            target.cureStatus("Burned");
        } else if ("CureParalysis".equals(effect)) {
            target.cureStatus("Paralyzed");
        }else if ("CureFlood".equals(effect)) {
            terrain.clearFlooding();
        }
    }
}
