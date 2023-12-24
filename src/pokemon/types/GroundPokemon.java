package pokemon.types;
import java.util.Random;

import battle.Attack;
import battle.Terrain;
import pokemon.Pokemon;
import status.Hidden;

public class GroundPokemon extends Pokemon {
    private double hideChance;
    private Random random = new Random();
    private int duration;

    public GroundPokemon(String name, int hp, int speed, int attack, int defense, double hideChance) {
        super(name, "Ground", hp, speed, attack, defense);
        this.hideChance = hideChance;
        this.duration=0;
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

        tryToHide();
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
        if (attack.getType().equals("Ground")) {
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
            case "Electric":
                return 2.0; 
            case "Plant":
            case "Insect":
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

    private void tryToHide() {
        if (random.nextDouble() < hideChance && !this.hasStatusEffect("Hidden")) {
            this.applyStatusEffect(new Hidden());
            this.duration = random.nextInt(3) + 1; 

        }
    }
    
    public void startTurn(Terrain terrain){
        super.startTurn(terrain);
        if (this.hasStatusEffect("Hidden")) {
            handleHiddenEffect();
        }
    }

    private void handleHiddenEffect() {
        
        if (this.duration == 0) {
            this.removeStatusByName( "Hidden");
            System.out.println(this.getName() + " sort de terre !");
            this.setDefense(defense/2);
        } else {
            this.duration--;
        }
    }

}