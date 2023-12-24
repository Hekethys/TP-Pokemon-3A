package pokemon.types;
import java.util.Random;

import battle.Attack;
import battle.Terrain;
import pokemon.Pokemon;

public class PlantPokemon extends NaturePokemon {
    private double healChance;
    private Random random = new Random();
    private String type;

    public PlantPokemon(String name, int hp, int speed, int attack, int defense, double healChance) {
        super(name, "Plant", hp, speed, attack, defense);
        this.healChance = healChance;
        this.type = "Plant";
    }

    @Override
    public String getTypes() {
        return type;
    }
    @Override
    public void attack(Pokemon opponent, Attack attack, Terrain terrain) {
        if (attack != null && attackFails(attack)) {
            System.out.println(this.getName() + " tente d'utiliser " + attack.getName() + " mais l'attaque échoue !");
            return;
        }
        
        if (attack == null) {
            
            performBareHandAttack(opponent);
        } else {
            
            performSpecialAttack(opponent, attack);
        }

        attack.decreaseMaxUses();
        tryToHealItself();
    }

    
    private boolean attackFails(Attack attack) {
        return random.nextDouble() < attack.failRate();
    }

    private void performBareHandAttack(Pokemon opponent) {
        double damage = calculateBareHandDamage(opponent);
        int damageInt = (int) damage;
        opponent.setHp(opponent.getHp() - (int) damageInt);
        System.out.println(this.getName() + " attaque à mains nues et inflige " + damageInt  + " dégâts à " + opponent.getName());
    }

    private double calculateBareHandDamage(Pokemon opponent) {
        double baseDamage = 20;
        double damageMultiplier = getAttack() / (double) opponent.getDefense();
        return baseDamage * damageMultiplier * (0.85 + 0.15 * random.nextDouble());
    }



    private void performSpecialAttack(Pokemon opponent, Attack attack) {
        double advantageMultiplier = 0.0;
        if (attack.getType().equals("Fire")) {
            advantageMultiplier = determineTypeAdvantage(opponent);
            
        }
        if (attack.getType().equals("Normal")) {
            advantageMultiplier = 1.0;
        }
        double damage = calculateSpecialAttackDamage(attack, opponent, advantageMultiplier);
        int damageInt = (int) damage;
        opponent.setHp(opponent.getHp() - (int) damageInt);
        System.out.println(this.getName() + " utilise " + attack.getName() + " et inflige " + damageInt  + " dégâts à " + opponent.getName());
    }

    private double determineTypeAdvantage(Pokemon opponent) {
        switch (opponent.getType()) {
            case "Ground":
                return 2.0; 
            case "Fire":
                return 0.5; 
            default:
                return 1.0; 
        }
    }

    private double calculateSpecialAttackDamage(Attack attack, Pokemon opponent, double advantageMultiplier) {
        double coef = 0.85 + 0.15 * random.nextDouble();
        double damage = (11 * getAttack() * attack.getPower()) / (25 * opponent.getDefense() + 2);
        return damage * advantageMultiplier * coef;
    }

    private void tryToHealItself( ) {
        if (random.nextDouble() < healChance) {
            this.removeAllStatusEffects(); 
            System.out.println(this.getName() + " a utilisé ses pouvoirs de plante pour se soigner de tous les états!");
        }
    }
    
}
