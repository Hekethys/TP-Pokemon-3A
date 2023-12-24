package item;

import battle.Terrain;
import pokemon.Pokemon;

public class Item {
    protected String name;
    protected String type;
    private String effect;

    public Item(String name, String type, String effect) {
        this.name = name;
        this.type = type;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getEffect() {
        return effect;
    }

    
    public void use(Pokemon pokemon, Terrain terrain) {
        
    }
}
