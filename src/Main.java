import battle.Attack;
import battle.Battle;
import io.AttackLoader;
import io.ItemLoader;
import item.Item;
import item.Potion;
import player.Player;
import pokemon.Pokemon;
import pokemon.types.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        AttackLoader attackLoader = new AttackLoader();
        ItemLoader itemLoader = new ItemLoader();

        List<Attack> availableAttacks = attackLoader.loadAttacks("lib/attacks.txt");
        List<Item> availableItems = itemLoader.loadItems("lib/items.txt");

        Player player1 = new Player("Joueur 1", scanner);
        Player player2 = new Player("Joueur 2", scanner);

        Map<String, Map<String, Object>> availablePokemons = getAvailablePokemons();

        System.out.println("Joueur 1, choisissez vos 3 Pokémon:");
        choosePokemonsForPlayer(player1, availablePokemons, scanner);
        System.out.println("Joueur 1, choisissez vos items:");
        chooseItemsForPlayer(player1, availableItems, scanner);

        System.out.println("\nJoueur 2, choisissez vos 3 Pokémon:");
        choosePokemonsForPlayer(player2, availablePokemons, scanner);
        System.out.println("\nJoueur 2, choisissez vos items:");
        chooseItemsForPlayer(player2, availableItems, scanner);

        assignAttacksToPokemon(player1, availableAttacks, random);
        assignAttacksToPokemon(player2, availableAttacks, random);
        displayPlayerPokemons(player1);
        displayPlayerPokemons(player2);

        Battle battle = new Battle(player1, player2, scanner);
        battle.start();
    }

    private static void chooseItemsForPlayer(Player player, List<Item> availableItems, Scanner scanner) {
        System.out.println("Voici la liste des items disponibles:");
        for (int i = 0; i < availableItems.size(); i++) {
            Item item = availableItems.get(i);
            String itemInfo = item.getName() + " (" + item.getEffect();

            if (item instanceof Potion) {
                Potion potion = (Potion) item;
                itemInfo += ")  (Value: " + potion.getValue();
            }

            itemInfo += ")";
            System.out.println((i + 1) + ". " + itemInfo);
        }

        List<Item> playerItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            System.out.println("Entrez le numéro d'un item:");
            int itemId = scanner.nextInt();
            if (itemId >= 1 && itemId <= availableItems.size()) {
                Item selectedItem = availableItems.get(itemId - 1);
                playerItems.add(selectedItem);
                System.out.println("Vous avez choisi: " + selectedItem.getName());
            } else {
                System.out.println("Numéro d'item invalide. Veuillez réessayer.");
                i--; 
            }
        }

        player.setItems(playerItems); 
    }
    private static Map<String, Map<String, Object>> getAvailablePokemons(){
        Map<String, Map<String, Object>> availablePokemons = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("lib/pokemons.txt"));
            String line;
            String monsterName = null;
            Map<String, Object> currentMonster = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Monster")) {
                    currentMonster = new HashMap<>();
                } else if (line.startsWith("EndMonster")) {
                    if (monsterName != null && currentMonster != null) {
                        availablePokemons.put(monsterName, currentMonster);
                        monsterName = null;
                    }
                } else {
                    Pattern pattern = Pattern.compile("\\s+(\\w+)\\s+(.*)");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find() && currentMonster != null) {
                        String key = matcher.group(1);
                        String value = matcher.group(2);
                        if (key.equals("Name")) {
                            monsterName = value;
                        } else {
                            currentMonster.put(key, value);
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return availablePokemons;
    }

    private static void assignAttacksToPokemon(Player player, List<Attack> availableAttacks, Random random) {
        for (Pokemon pokemon : player.getPokemons()) {
            List<Attack> compatibleAttacks = getCompatibleAttacks(pokemon, availableAttacks);
            List<Attack> attacksToChooseFrom = new ArrayList<>(compatibleAttacks);
    
            for (int i = 0; i < 4; i++) {
                if (attacksToChooseFrom.isEmpty()) {
                    break;
                }
    
                int index = random.nextInt(attacksToChooseFrom.size());
                Attack selectedAttack = attacksToChooseFrom.get(index);
                pokemon.addAttack(selectedAttack);
                System.out.println(pokemon.getName() + " a reçu l'attaque " + selectedAttack.getName() + " (" + selectedAttack.getType() + ")");
    
                attacksToChooseFrom.remove(index); 
            }
        }
    }
    

    private static List<Attack> getCompatibleAttacks(Pokemon pokemon, List<Attack> availableAttacks) {
        List<Attack> compatibleAttacks = new ArrayList<>();
        for (Attack attack : availableAttacks) {
            if (attack.getType().equals("Normal") || attack.getType().equals(pokemon.getType())) {
                compatibleAttacks.add(attack);
            }
        }
        return compatibleAttacks;
    }

    private static int getRandomValue(String range) {
        String[] values = range.split(" ");
        int min = Integer.parseInt(values[0]);
        int max = Integer.parseInt(values[1]);
        return (int) (Math.random() * (max - min + 1)) + min;
    };

    private static void choosePokemonsForPlayer(Player player, Map<String, Map<String, Object>> monsterData, Scanner scanner) {
        System.out.println("Voici la liste des Pokémon disponibles :");
        int index = 1;
        for (String monsterName : monsterData.keySet()) {
            Map<String, Object> data = monsterData.get(monsterName);
            String type = String.valueOf(data.get("Type"));
            System.out.println(index + " " + monsterName + " (Type: " + type + ")");
            index++;
        }
        List<Pokemon> playerPokemons = new ArrayList<Pokemon>();
        List<String> monsterNames = new ArrayList<>(monsterData.keySet());

        do {
            boolean playerPokemonIsValid = false;
            while(!playerPokemonIsValid){
                System.out.println("Entrez le numéro d'un Pokémon :");
                int pokemonId = scanner.nextInt();
                if (pokemonId >= 1 && pokemonId <= monsterNames.size()) {
                    String pokemonName = monsterNames.get(pokemonId - 1);

                    boolean found = false;
                    for (Map.Entry<String, Map<String, Object>> entry : monsterData.entrySet()) {
                        String pokeName = entry.getKey();
                        if (Objects.equals(pokemonName, pokeName)) {
                            Map<String, Object> data = monsterData.get(pokeName);
                            int hp = getRandomValue((String) data.get("HP"));
                            int speed = getRandomValue((String) data.get("Speed"));
                            int attack = getRandomValue((String) data.get("Attack"));
                            int defense = getRandomValue((String) data.get("Defense"));
                            String type = String.valueOf(data.get("Type"));
                            Pokemon newPokemon = null;

                            switch (type) {
                                case "Fire":
                                    float blaze = Float.parseFloat((String) data.get("Blaze"));
                                    newPokemon = new FirePokemon(pokemonName, hp, speed, attack, defense, blaze);
                                    break;
                                case "Water":
                                    float flood = Float.parseFloat((String) data.get("Flood"));
                                    float fall = Float.parseFloat((String) data.get("Fall"));
                                    newPokemon = new WaterPokemon(pokemonName, hp, speed, attack, defense, flood, fall);
                                    break;
                                case "Electric":
                                    float paralysis = Float.parseFloat((String) data.get("Paralysis"));
                                    newPokemon = new ElectricPokemon(pokemonName, hp, speed, attack, defense, paralysis);
                                    break;
                                case "Ground":
                                    float hide = Float.parseFloat((String) data.get("Hide"));
                                    newPokemon = new GroundPokemon(pokemonName, hp, speed, attack, defense, hide);
                                    break;
                                case "Insect":
                                    float poison = Float.parseFloat((String) data.get("Poison"));
                                    newPokemon = new InsectPokemon(pokemonName, hp, speed, attack, defense, poison);
                                    break;
                                case "Plant":
                                    float heal = Float.parseFloat((String) data.get("Heal"));
                                    newPokemon = new PlantPokemon(pokemonName, hp, speed, attack, defense, heal);
                                    break;
                                default :
                                    break;
                            }

                            if (playerPokemons.stream().anyMatch(p -> p.getName().equals(pokemonName))) {
                                System.out.println("Vous avez déjà ce Pokémon dans votre équipe.");
                                found = true;
                            } else if (newPokemon != null) {
                                playerPokemons.add(newPokemon);
                                found = true;
                                playerPokemonIsValid = true;
                            }
                        }
                    }

                    if (!found) {
                        System.out.println("Pokémon introuvable.");
                    }
                }else {
                    System.out.println("Ce numéro ne correspond à aucun Pokémon.");
                }
            }
        }while (playerPokemons.size() < 3);

        player.addPokemons(playerPokemons);
        player.setActivePokemon(playerPokemons.get(0));
    }
    
    private static void displayPlayerPokemons(Player player) {
        System.out.println("\n\n"+player.getName() + " a choisi les Pokémon suivants :");
        for (Pokemon pokemon : player.getPokemons()) {
            System.out.println("\n- " + pokemon.getName() + " (Type: " + pokemon.getType() + ", HP: " + pokemon.getHp() + 
                               ", Attack: " + pokemon.getAttack() + ", Defense: " + pokemon.getDefense() + 
                               ", Speed: " + pokemon.getSpeed() + ")");
            
            System.out.println("  Attaques:");
            for (Attack attack : pokemon.getAttacks()) {
                System.out.println("  - " + attack.getName() + " (Type: " + attack.getType() + 
                                   ", Power: " + attack.getPower() + ", Max Uses: " + attack.maxUses() + 
                                   ", Fail Rate: " + attack.failRate() + ")");
            }
        }
    }
    
    
}
