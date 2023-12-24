package status;

import java.util.Random;

import pokemon.Pokemon;

public class Hidden extends StatusEffect {
    private int duration;
    private Random random;

    public Hidden() {
        super("Hidden");
        this.random = new Random();
        this.duration = random.nextInt(3) + 1;  
    }
    @Override
    public int getDuration() {
        return this.duration;
    }
    @Override
    public void applyEffect(Pokemon pokemon) {
        if (isActive()) {
            System.out.println(pokemon.getName() + " se cache sous terre pendant " + this.duration + " tour(s) !");
            pokemon.setDefense(pokemon.getDefense() * 2);
        }
    }

    @Override
    public boolean isActive() {
        return this.duration > 0;
    }

    @Override
    public boolean isCompatibleWith(StatusEffect other) {
        return true;
    }

    public void reduceDuration() {
        if (this.duration > 0) {
            this.duration--;
        }
    }
}
