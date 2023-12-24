package battle;

import player.Player;
import pokemon.Pokemon;

import java.util.Scanner;

import item.Item;
import item.Medicine;
import item.Potion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TurnManager {
    private Player player1;
    private Player player2;
    private Terrain terrain;
    private Scanner scanner;
    private int tour;
    private Random random;

    public TurnManager(Player player1, Player player2, Scanner scanner, Terrain terrain) {
        this.player1 = player1;
        this.player2 = player2;
        this.scanner = scanner;
        this.terrain = terrain;
        this.tour = 0;
        this.random = new Random();
    }

    public void displayBattleStatus() {
        Pokemon activePokemon1 = player1.getActivePokemon();
        Pokemon activePokemon2 = player2.getActivePokemon();
    
        int maxNameLength = Math.max(activePokemon1.getName().length(), activePokemon2.getName().length());
        int maxTypeLength = Math.max(activePokemon1.getType().length(), activePokemon2.getType().length());
        int maxHpLength = Math.max(
            String.valueOf(activePokemon1.getMaxHp()).length(),
            String.valueOf(activePokemon2.getMaxHp()).length()
        );
    
        String lineFormat = "| %-9s | %-"+maxNameLength+"s (%-"+maxTypeLength+"s) | HP: %"+maxHpLength+"d/%-"+maxHpLength+"d | Atk: %-2d | Def: %-2d | Spd: %-2d | Effets: %-15s |";
        String terrainFormat = "| %-9s | %-15s |"; 
    
        String player1Line = formatLine(activePokemon1, "\nJoueur 1", lineFormat);
        String player2Line = formatLine(activePokemon2, "\nJoueur 2", lineFormat);
        String terrainLine = formatTerrainLine(terrain, terrainFormat);
    
        int separatorLength = Math.max(Math.max(player1Line.length(), player2Line.length()), terrainLine.length());
    
        System.out.println("+" + "-".repeat(separatorLength) + "+");
        System.out.println(player1Line);
        System.out.println("+" + "-".repeat(separatorLength) + "+");
        System.out.println(player2Line);
        System.out.println("+" + "-".repeat(separatorLength) + "+");
        System.out.println(terrainLine);
        System.out.println("+" + "-".repeat(separatorLength) + "+");
    }
    
    private String formatTerrainLine(Terrain terrain, String terrainFormat) {
        String terrainStatus = terrain.isFlooded() ? "Flooded" : "Normal";
        return String.format(
            terrainFormat,
            "Terrain", terrainStatus
        );
    }
    
    private String formatLine(Pokemon pokemon, String playerName, String lineFormat) {
        String statusEffectsString = pokemon.getStatusEffectsString();
        return String.format(
            lineFormat,
            playerName,
            pokemon.getName(), pokemon.getType(),
            pokemon.getHp(), pokemon.getMaxHp(),
            pokemon.getAttack(), pokemon.getDefense(), pokemon.getSpeed(),
            statusEffectsString  
        );
    
    }


    public void executeTurn() {
        this.tour++;
        System.out.println("\n\n+------------------------ Tour " + tour + " ------------------------+\n\n");
        
        if (tour==1 && scanner.hasNextLine()) {
            scanner.nextLine();
        }
    
        player1.getActivePokemon().startTurn(terrain);
        if (!player1.getActivePokemon().isAlive()){
            return;
        }
    
        player2.getActivePokemon().startTurn(terrain);
        if (!player2.getActivePokemon().isAlive()){
            return;
        }
    
        displayBattleStatus();
    

        int actionChoicePlayer1 = getActionChoice(player1);
        int attackChoicePlayer1 = actionChoicePlayer1 == 1 ? getAttackChoice(player1.getActivePokemon()) : -1;
        int itemChoicePlayer1 = (actionChoicePlayer1 == 2) ? getItemChoice(player1) : -1;
        int pokemonChoicePlayer1;

        if (actionChoicePlayer1 == 2 || actionChoicePlayer1 == 3) {
            pokemonChoicePlayer1 = getPokemonChoice(player1, actionChoicePlayer1 == 2);
        } else {
            pokemonChoicePlayer1 = -1;
        }


        int actionChoicePlayer2 = getActionChoice(player2);
        int attackChoicePlayer2 = actionChoicePlayer2 == 1 ? getAttackChoice(player2.getActivePokemon()) : -1;
        int itemChoicePlayer2 = (actionChoicePlayer2 == 2) ? getItemChoice(player2) : -1;
        int pokemonChoicePlayer2;

        if (actionChoicePlayer2 == 2 || actionChoicePlayer2 == 3) {
            pokemonChoicePlayer2 = getPokemonChoice(player2, actionChoicePlayer2 == 2);
        } else {
            pokemonChoicePlayer2 = -1;
        }

        if (actionChoicePlayer1==1 && (actionChoicePlayer2 == 2 || actionChoicePlayer2 ==3)){
            executePlayerAction(player2, player2.getActivePokemon(), player1.getActivePokemon(), actionChoicePlayer2, attackChoicePlayer2, itemChoicePlayer2, pokemonChoicePlayer2);
            executePlayerAction(player1, player1.getActivePokemon(), player2.getActivePokemon(), actionChoicePlayer1, attackChoicePlayer1, itemChoicePlayer1, pokemonChoicePlayer1);
            return;
        }
        if (actionChoicePlayer1==1 && actionChoicePlayer2==1){
            if (player1.getActivePokemon().getSpeed() > player2.getActivePokemon().getSpeed()){
                executePlayerAction(player1, player1.getActivePokemon(), player2.getActivePokemon(), actionChoicePlayer1, attackChoicePlayer1, itemChoicePlayer1, pokemonChoicePlayer1);
                executePlayerAction(player2, player2.getActivePokemon(), player1.getActivePokemon(), actionChoicePlayer2, attackChoicePlayer2, itemChoicePlayer2, pokemonChoicePlayer2);
                return;
            } else {
                executePlayerAction(player2, player2.getActivePokemon(), player1.getActivePokemon(), actionChoicePlayer2, attackChoicePlayer2, itemChoicePlayer2, pokemonChoicePlayer2);
                executePlayerAction(player1, player1.getActivePokemon(), player2.getActivePokemon(), actionChoicePlayer1, attackChoicePlayer1, itemChoicePlayer1, pokemonChoicePlayer1);
                return;
            }
        }
        if (actionChoicePlayer2==1 && (actionChoicePlayer1 == 2 || actionChoicePlayer1 ==3)){
            if (actionChoicePlayer1==2){
                executePlayerAction(player1, player1.getActivePokemon(), player2.getActivePokemon(), actionChoicePlayer1, attackChoicePlayer1, itemChoicePlayer1, pokemonChoicePlayer1);
                executePlayerAction(player2, player2.getActivePokemon(), player1.getActivePokemon(), actionChoicePlayer2, attackChoicePlayer2, itemChoicePlayer2, pokemonChoicePlayer2);
                return;
            } else {
                executePlayerAction(player2, player2.getActivePokemon(), player1.getActivePokemon(), actionChoicePlayer2, attackChoicePlayer2, itemChoicePlayer2, pokemonChoicePlayer2);
                executePlayerAction(player1, player1.getActivePokemon(), player2.getActivePokemon(), actionChoicePlayer1, attackChoicePlayer1, itemChoicePlayer1, pokemonChoicePlayer1);
                return;
            }
        }

        if (actionChoicePlayer1==2){
            if (actionChoicePlayer2==2){
                executePlayerAction(player1, player1.getActivePokemon(), player2.getActivePokemon(), actionChoicePlayer1, attackChoicePlayer1, itemChoicePlayer1, pokemonChoicePlayer1);
                executePlayerAction(player2, player2.getActivePokemon(), player1.getActivePokemon(), actionChoicePlayer2, attackChoicePlayer2, itemChoicePlayer2, pokemonChoicePlayer2);
                return;

            } else {
                executePlayerAction(player2, player2.getActivePokemon(), player1.getActivePokemon(), actionChoicePlayer2, attackChoicePlayer2, itemChoicePlayer2, pokemonChoicePlayer2);
                executePlayerAction(player1, player1.getActivePokemon(), player2.getActivePokemon(), actionChoicePlayer1, attackChoicePlayer1, itemChoicePlayer1, pokemonChoicePlayer1);
                return;
            }
        }
        if (actionChoicePlayer1==3){
            if (actionChoicePlayer2==3){
                executePlayerAction(player1, player1.getActivePokemon(), player2.getActivePokemon(), actionChoicePlayer1, attackChoicePlayer1, itemChoicePlayer1, pokemonChoicePlayer1);
                executePlayerAction(player2, player2.getActivePokemon(), player1.getActivePokemon(), actionChoicePlayer2, attackChoicePlayer2, itemChoicePlayer2, pokemonChoicePlayer2);
                return;

            } else {
                executePlayerAction(player1, player1.getActivePokemon(), player2.getActivePokemon(), actionChoicePlayer1, attackChoicePlayer1, itemChoicePlayer1, pokemonChoicePlayer1);
                executePlayerAction(player2, player2.getActivePokemon(), player1.getActivePokemon(), actionChoicePlayer2, attackChoicePlayer2, itemChoicePlayer2, pokemonChoicePlayer2);
                return;
            }
        }
        

    }

    private int getAttackChoice(Pokemon pokemon) {
        List<Attack> attacks = pokemon.getAttacks();
        if (attacks.isEmpty()) {
            System.out.println("Aucune attaque disponible.");
            return -1; 
        }

        if (!pokemon.hasPP()){
            return -2;
        }
        
    
        int attackChoice = -1;
        while (attackChoice < 0 || attackChoice >= attacks.size()) {
            try {
                System.out.println("\nChoisissez une attaque:");
                for (int i = 0; i < attacks.size(); i++) {
                    System.out.println((i + 1) + ". " + attacks.get(i).getName() + " (Type : " + attacks.get(i).getType() + ") (PP : " + attacks.get(i).getUsesLeft() + "/" + attacks.get(i).getMaxUses()+")");
                }
                System.out.print("Entrez le numéro de l'attaque: ");
                attackChoice = Integer.parseInt(scanner.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            }
        }
        return attackChoice;
    }
    
    private int getItemChoice(Player player) {
        List<Item> items = player.getItems();
        if (items.isEmpty()) {
            System.out.println("Aucun objet disponible.");
            return -1;
        }
    
        System.out.println("Choisissez un objet:");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            String itemInfo = item.getName() + " (" + item.getEffect();
        
            if (item instanceof Potion) {
                Potion potion = (Potion) item;
                itemInfo += ")  (Value: " + potion.getValue();
            }
        
            itemInfo += ")";
            System.out.println((i + 1) + ". " + itemInfo);
        }
        
        int itemChoice = -1;
        while (itemChoice < 0 || itemChoice >= items.size()) {
            try {
                System.out.print("Entrez le numéro de l'objet: ");
                itemChoice = Integer.parseInt(scanner.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            }
        }
        return itemChoice;
    }


    private int getPokemonChoice(Player player, Boolean item) {
        
        List<Pokemon> pokemons = player.getPokemons();
        Map<Integer, Integer> indexMap = new HashMap<>(); 
        System.out.println("Choisissez un Pokémon:");
    
        if (item){
            
            int displayIndex = 1;
            for (int i = 0; i < pokemons.size(); i++) {
                if (pokemons.get(i).isAlive()) {
                    System.out.println(displayIndex + ". " + pokemons.get(i).getName() + " Type : " + pokemons.get(i).getType() + " HP : " + pokemons.get(i).getHp() + "/" + pokemons.get(i).getMaxHp());
                    indexMap.put(displayIndex, i);
                    displayIndex++;
                }
            }
    
        } else {
    
            int displayIndex = 1;
            for (int i = 0; i < pokemons.size(); i++) {
                if (pokemons.get(i).isAlive() && !pokemons.get(i).equals(player.getActivePokemon())) {
                    System.out.println(displayIndex + ". " + pokemons.get(i).getName());
                    indexMap.put(displayIndex, i);
                    displayIndex++;
                }
            }
        }
        if (indexMap.isEmpty()) {
            System.out.println("Aucun autre Pokémon disponible.");
            return -1;
        }
    
        int pokemonChoice = -1;
        while (!indexMap.containsKey(pokemonChoice)) {
            try {
                System.out.print("Entrez le numéro du Pokémon: ");
                pokemonChoice = Integer.parseInt(scanner.nextLine());
                if (!indexMap.containsKey(pokemonChoice)) {
                    System.out.println("Choix invalide. Veuillez choisir un numéro valide.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            }
        }
        return indexMap.get(pokemonChoice); 
    }

    private void useItem(Player player, int itemChoice, int pokemonChoice) {
        List<Pokemon> pokemons = player.getPokemons();
        List<Item> items = player.getItems();
        if (items.isEmpty() || itemChoice < 0 || itemChoice >= items.size()) {
            System.out.println("Choix d'objet invalide.");
            return;
        }
        
        Item item = items.get(itemChoice);
        if (item instanceof Medicine || item instanceof Potion) {
            Pokemon targetPokemon = pokemons.get(pokemonChoice);
            applyItemEffect(item, player, targetPokemon);
        } else {
            
            System.out.println("Type d'objet non géré.");
        }
    }
    

    private int getActionChoice(Player player) {
        int action = -1;
        while (action < 1 || action > 3) {
            try {
                System.out.println("\n" + player.getName() + ", choisissez une action:");
                System.out.println("1. Attaquer");
                System.out.println("2. Utiliser un objet");
                System.out.println("3. Changer de Pokémon");
                System.out.print("Entrez le numéro de votre choix: ");
                action = Integer.parseInt(scanner.nextLine());
    
                if (action < 1 || action > 3) {
                    System.out.println("Choix invalide. Veuillez choisir un numéro entre 1 et 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            }
        }
        return action;
    }
    
private void applyItemEffect(Item item, Player player, Pokemon targetPokemon) {
    
    if (item instanceof Medicine && ((Medicine) item).getEffect().equals("CureFlood")) {
        
        ((Medicine) item).use(null, terrain); 
        System.out.println("L'effet de l'inondation sur le terrain a été neutralisé.");
    } else {
        
        

        
        if (item instanceof Medicine) {
            
            ((Medicine) item).use(targetPokemon, terrain);
        } else if (item instanceof Potion) {
            
            ((Potion) item).use(targetPokemon, terrain);
        } else {
            
            System.out.println("Cet objet n'est pas pris en charge.");
        }
    }
    
    player.removeItem(item);
}


private void executePlayerAction(Player player, Pokemon attackingPokemon, Pokemon defendingPokemon, int actionChoice, int attackChoice,  int itemChoice, int pokemonChoice) {
    switch (actionChoice) {
        case 1: 
        Attack selectedAttack;
        if ((attackChoice >= 0 || attackChoice==-2) && attackChoice < attackingPokemon.getAttacks().size()) {
            if (attackChoice == -2){
                selectedAttack = null;
            }else{
                 selectedAttack = attackingPokemon.getAttacks().get(attackChoice);
                if (selectedAttack.getUsesLeft() <= 0) {
                    System.out.println(player.getName() + "vous n'avez plus de PP pour cette attaque. (" + selectedAttack.getName() + ")");
                    return;
                }
            }


            
            if (attackingPokemon.hasStatusEffect("Paralyzed")) {
                if (random.nextInt(4) != 0) { 
                    System.out.println(attackingPokemon.getName() + " est paralysé et ne peut pas attaquer.");
                    return;
                }
                System.out.println(attackingPokemon.getName() + " est paralysé, mais il parvient quand même à attaquer !");
            }

            
            if (terrain.isFlooded() && !attackingPokemon.getType().equals("Water")) {
                if (random.nextDouble() < terrain.getSlipChance()) {
                    System.out.println(attackingPokemon.getName() + " glisse et rate son attaque à cause de l'inondation !");
                    attackingPokemon.setHp(attackingPokemon.getHp() - attackingPokemon.getAttack()/4);
                    System.out.println(attackingPokemon.getName() + " s'est blessé dans sa chute et perd " + attackingPokemon.getAttack()/4 + " points de vie.");
                    return;
                }
            }

            if (selectedAttack==null){
                System.out.println("\n"+attackingPokemon.getName() + " utilise " + "attaque à mains nues" + " !");
                attackingPokemon.attack(defendingPokemon, null, terrain);
                return;
            }
            System.out.println("\n"+attackingPokemon.getName() + " utilise " + selectedAttack.getName() + " !");
            attackingPokemon.attack(defendingPokemon, selectedAttack, terrain);

        } else {
            System.out.println(player.getName() + ", vous n'avez pas choisi d'attaque valide.");
        }
        break;



        case 2: 
            if (itemChoice != -1 && pokemonChoice != -1) {
                useItem(player, itemChoice, pokemonChoice);
            } else {
                System.out.println("Aucun objet choisi.");
            }
            break;
        case 3: 

            changePokemon(player, pokemonChoice);
            break;
        default:
            System.out.println(player.getName() + ", choix invalide.");
            break;
    }
}




    

    private boolean changePokemon(Player player, int pokemonChoice) {
        List<Pokemon> pokemons = player.getPokemons();

        try {
            if (pokemonChoice >= 0 && pokemonChoice < pokemons.size()) {
                Pokemon selectedPokemon = pokemons.get(pokemonChoice);
                player.setActivePokemon(selectedPokemon);
                System.out.println(player.getName() + " change de Pokémon pour " + selectedPokemon.getName() + "!");
                return true;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrée invalide.");
        }
        return false;
    }


}