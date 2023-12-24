package pokemon.types;


import battle.Terrain;
import pokemon.Pokemon;

public abstract class NaturePokemon extends Pokemon {
    public NaturePokemon(String name, String string, int hp, int speed, int attack, int defense) {
        super(name, string, hp, speed, attack, defense);
    }

    public void startTurn(Terrain terrain) {
        if (terrain.isFlooded()) {
            int healingAmount = getMaxHp() / 20;
            setHp(Math.min(getHp() + healingAmount, getMaxHp()));
            System.out.println(this.getName() + " récupère des points de vie grâce à l'inondation.");
        }
    }


}
