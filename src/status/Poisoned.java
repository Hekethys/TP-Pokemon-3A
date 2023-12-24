package status;

import pokemon.Pokemon;

public class Poisoned extends StatusEffect {
    private int duration;

    public Poisoned() {
        super("Poisoned");
    }

    @Override
    public void applyEffect(Pokemon pokemon) {
    }

    @Override
    public boolean isActive() {
        return duration > 0;
    }

    @Override
    public boolean isCompatibleWith(StatusEffect other) {
        return !(other instanceof Flooded);
    }

}
