package player;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import battle.Terrain;
import item.Item;
import pokemon.Pokemon;

public class Player {
    private String name;
    private List<Pokemon> pokemons;
    private List<Item> items;
    private Inventory inventory;
    private Pokemon activePokemon; 
    private Scanner scanner; 

    public Player(String name, Scanner scanner) {
        this.name = name;
        this.pokemons = new ArrayList<>();
        this.inventory = new Inventory();
        this.activePokemon = null; 
        this.scanner = scanner; 
        this.items = new ArrayList<>();
    }
    public String getName() {
        return name;
    }

    public void addPokemons(List<Pokemon> playerPokemons) {
        this.pokemons = playerPokemons;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void useItem(Item item, Pokemon pokemon, Terrain terrain) {
        this.inventory.useItem(item, pokemon, terrain);
    }

    public Pokemon getActivePokemon() {
        return activePokemon;
    }
    public void setActivePokemon(Pokemon newActivePokemon) {
        if (pokemons.contains(newActivePokemon) && newActivePokemon.isAlive()) {
            this.activePokemon = newActivePokemon;
        }
    }

    public void switchActivePokemon(int index) {
        if (index >= 0 && index < pokemons.size()) {
            activePokemon = pokemons.get(index);
        }
    }
    public boolean hasAlivePokemon() {
        for (Pokemon pokemon : pokemons) {
            if (pokemon.isAlive()) {
                return true;
            }
        }
        return false;
    }
    public void chooseNewActivePokemon() {
        System.out.println("Choisissez un nouveau Pokémon :");
    
        
        List<Pokemon> alivePokemons = pokemons.stream()
            .filter(pokemon -> pokemon.isAlive() && !pokemon.equals(this.activePokemon))
            .collect(Collectors.toList());
    
        if (alivePokemons.isEmpty()) {
            System.out.println("Aucun Pokémon en vie pour changer.");
            return;
        }
    
        for (int i = 0; i < alivePokemons.size(); i++) {
            System.out.println((i + 1) + ". " + alivePokemons.get(i).getName());
        }
    
        int choice = scanner.nextInt() - 1;
        while (choice < 0 || choice >= alivePokemons.size()) {
            System.out.println("Choix invalide. Veuillez choisir un numéro valide.");
            choice = scanner.nextInt() - 1;
        }
    
        setActivePokemon(alivePokemons.get(choice));
        System.out.println("Vous avez choisi " + getActivePokemon().getName());
    }
    public boolean hasAlivePokemonExceptCurrent() {
        return pokemons.stream()
            .filter(pokemon -> pokemon.isAlive() && !pokemon.equals(activePokemon))
            .findAny()
            .isPresent();
    }
    public void addItem(Item item) {
        items.add(item);
    }

    
    public void removeItemByName(String itemName) {
        items.removeIf(item -> item.getName().equals(itemName));
    }

    
    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("Aucun item dans l'inventaire.");
        } else {
            System.out.println("Items dans l'inventaire:");
            for (Item item : items) {
                System.out.println("- " + item.getName());
            }
        }
    }
    public void removeItem(Item item) {
        items.remove(item);
    }
    

    

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
