package battle;

public class Attack {
    private String name;
    private String type;
    private int power;
    private int maxUses;
    private double failRate;

    public Attack(String name, String type, int power, int maxUses, double failRate) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.maxUses = maxUses;
        this.failRate = failRate;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public int getPower() {
        return this.power;
    }

    public double failRate() {
        return this.failRate;
    }   

    public int maxUses() {
        return this.maxUses;
    }

    public void decreaseMaxUses() {
        this.maxUses -=1;
    }

    public int getMaxUses() {
        return this.maxUses;
    }
}
