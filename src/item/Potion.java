package item;

import battle.Terrain;
import pokemon.Pokemon;

public class Potion extends Item {
    private String effect;
    private int value;

    public Potion(String name, String effect, int value) {
        super(name, "Potion", effect);
        this.effect = effect;
        this.value = value;
    }

    public String getEffect() {
        return effect;
    }

    public int getValue() {
        return value;
    }   
    @Override
    public void use(Pokemon target, Terrain terrain) {
        if ("Heal".equals(effect)) {
            target.setHp(target.getHp() + value);
            System.out.println("\n"+target.getName() + " a été soigné de " + value + " points de vie.");
        } else if ("IncreaseAttack".equals(effect)) {
            target.setAttack(target.getAttack() + value);
            System.out.println("\n"+target.getName() + " a augmenté son attaque de " + value + ".");
        } else if ("IncreaseDefense".equals(effect)) {
            target.setDefense(target.getDefense() + value);
            System.out.println("\n"+target.getName() + " a augmenté sa défense de " + value + ".");
        } else if ("IncreaseSpeed".equals(effect)) {
            target.setSpeed(target.getSpeed() + value);
            System.out.println("\n"+target.getName() + " a augmenté sa vitesse de " + value + ".");
        }
    }
}
