package status;

import pokemon.Pokemon;

public class Burned extends StatusEffect {
    private int duration;

    public Burned() {
        super("Burned");
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
