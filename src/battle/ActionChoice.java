package battle;
class ActionChoice {
    private final int action;
    private final int attackIndex;

    public ActionChoice(int action, int attackIndex) {
        this.action = action;
        this.attackIndex = attackIndex;
    }

    public int getAction() {
        return action;
    }

    public int getAttackIndex() {
        return attackIndex;
    }
}