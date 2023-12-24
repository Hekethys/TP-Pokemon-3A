package pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import battle.Attack;
import battle.Terrain;
import status.Burned;
import status.Poisoned;
import status.Paralyzed;
import status.StatusEffect;


public class Pokemon {
    protected String name;
    protected String type;
    protected int hp;
    protected int speed;
    protected int attack;
    protected int defense;
    protected int hpMax;
    private List<Attack> attacks;
    private List<StatusEffect> statusEffects; 
    private int duration;

    public Pokemon(String name, String type, int hp, int speed, int attack, int defense) {
        this.name = name;
        this.type = type;
        this.hp = hp;
        this.speed = speed;
        this.attack = attack;
        this.defense = defense;
        this.hpMax = hp;
        this.attacks = new ArrayList<>();
        this.statusEffects = new ArrayList<>();
        this.duration = 0;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp < 0) {
            this.hp = 0;
        } if (hp>hpMax){
            this.hp = hpMax;
        } else {
            this.hp = hp;
        }
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getDefense() {
        return defense;
    }

    public void setAtack(int attack) {
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public int getMaxHp() {
        return hpMax;
    }

    public String getTypes() {
        return type;
    }


    public void attack(Pokemon opponent, Attack attack, Terrain terrain) {
    }

    public boolean hasPP(){
        int sum = 0;
        for (Attack attack : attacks) {
            sum += attack.maxUses();
        }
        return sum > 0;
    };

    public void addAttack(Attack attack) {
        if (attacks.size() < 4) {
            this.attacks.add(attack);
        }
    }

    public List<Attack> getAttacks() {
        return new ArrayList<>(attacks);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public boolean hasStatusEffect(String effectName) {
        for (StatusEffect effect : statusEffects) {
            if (effect.getName().equalsIgnoreCase(effectName)) {
                return true;
            }
        }
        return false;
    }

    public void applyStatusEffect(StatusEffect effect) {
        boolean isCompatible = statusEffects.stream().allMatch(existingEffect -> existingEffect.isCompatibleWith(effect));
        if (isCompatible) {
            statusEffects.add(effect);
            effect.applyEffect(this);
        }
    }

    public void removeStatusEffect(StatusEffect effect) {
        statusEffects.removeIf(existingEffect -> existingEffect.getName().equals(effect.getName()));
    }
    public int getDuration() {
        return duration;
    }

    public void removeAllStatusEffects() {
        List<StatusEffect> effectsToRemove = new ArrayList<>(statusEffects);
        for (StatusEffect effect : effectsToRemove) {
            removeStatusEffect(effect);
        }
    }
    public String getStatusEffectsString() {
        if (statusEffects.isEmpty()) {
            return "Aucun";
        }
        return statusEffects.stream()
                 .map(StatusEffect::getName)
                 .collect(Collectors.joining(", "));
    }

    public void removeStatusByName(String effectName) {
        statusEffects.removeIf(effect -> effect.getName().equals(effectName));
    }



    public void startTurn(Terrain terrain) {
        List<StatusEffect> toRemove = new ArrayList<>();
    
        for (StatusEffect effect : statusEffects) {
            if (effect instanceof Paralyzed) {
                if (handleParalyzedEffect()) {
                    toRemove.add(effect);
                }
            } else if (effect instanceof Burned) {
                if (terrain.isFlooded()) {
                    System.out.println(this.getName() + " est brûlé mais l'eau éteint le feu !");
                    toRemove.add(effect);
                } else {
                    handleBurnedEffect();
                }
            } else if (effect instanceof Poisoned) {
                if (terrain.isFlooded()) {
                    System.out.println(this.getName() + " est empoisonné mais l'eau dilue le poison !");
                    toRemove.add(effect);
                } else {
                    handlePoisonedEffect();
                }
            }
        }
    
        
        for (StatusEffect effect : toRemove) {
            removeStatusByName(effect.getName());
        }
    }
    
    private boolean handleParalyzedEffect() {
        duration++;
        if (duration > 0 && Math.random() < duration / 6.0) {
            duration = 0;
            System.out.println(this.getName() + " n'est plus paralysé !");
            return true; 
        } else {
            duration++;
            return false; 
        }
    }
    

    private void handleBurnedEffect() {
        int burnDamage = this.getAttack() / 10;
        this.setHp(this.getHp() - burnDamage);
        System.out.println(this.getName() + " est brûlé et subit " + burnDamage + " points de dégâts.");
    }

    private void handlePoisonedEffect() {
        int poisonDamage = this.getAttack() / 10;
        this.setHp(this.getHp() - poisonDamage);
        System.out.println(this.getName() + " est empoisonné et subit " + poisonDamage + " points de dégâts.");
    }




    public void cureStatus(String status) {
        if (getStatusEffectsString().contains(status)){
            removeStatusByName(status);
            System.out.println(this.getName() + " a été guéri de l'effet " + status + "!");
        }else{
            System.out.println("Ce médicament n'a eu aucun effet.");
        }
    }

    public void setAttack(int i) {
    }
}
