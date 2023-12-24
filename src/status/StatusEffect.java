package status;

import pokemon.Pokemon;

public abstract class StatusEffect {
    protected String name;

    public StatusEffect(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void applyEffect(Pokemon pokemon);

    public abstract boolean isActive();

    public abstract boolean isCompatibleWith(StatusEffect other);

    public int getDuration() {
        return 0;
    }
}
