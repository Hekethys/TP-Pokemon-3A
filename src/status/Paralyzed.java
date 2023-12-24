package status;

import pokemon.Pokemon;
import java.util.Random;

public class Paralyzed extends StatusEffect {
    private int duration;
    private Random random;

    public Paralyzed() {
        super("Paralyzed");
        this.random = new Random();
        this.duration = 1 + random.nextInt(6);  
    }

    @Override
    public int getDuration() {
        return this.duration;
    }
    @Override
    public void applyEffect(Pokemon pokemon) {
        System.out.println(pokemon.getName() + " est paralysé pendant " + this.duration +" tour(s) !");
        if (isActive()) {
            if (random.nextDouble() < 0.25) {
                System.out.println(pokemon.getName() + " est paralysé il ne peut plus attaquer !");
            }
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
        if (duration > 0) {
            this.duration--;
        }
    }
}
