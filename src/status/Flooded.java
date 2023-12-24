package status;

import java.util.Random;

import pokemon.Pokemon;

public class Flooded extends StatusEffect {
    private int duration;
    private Random random;

    public Flooded() {
        super("Flooded");
        this.random = new Random();
        this.duration = random.nextInt(3) + 1; 
    }

    public int setDuration(int duration) {
        return this.duration = duration;
    }

    @Override
    public void applyEffect(Pokemon pokemon) {
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
