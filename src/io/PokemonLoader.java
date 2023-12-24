package io;
import pokemon.*;
import pokemon.types.ElectricPokemon;
import pokemon.types.FirePokemon;
import pokemon.types.GroundPokemon;
import pokemon.types.InsectPokemon;
import pokemon.types.PlantPokemon;
import pokemon.types.WaterPokemon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PokemonLoader {

    public List<Pokemon> loadPokemons(String filename) {
        List<Pokemon> pokemons = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("Monster")) {
                    pokemons.add(readPokemon(reader));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pokemons;
    }

    private Pokemon readPokemon(BufferedReader reader) throws IOException {
        String name = null, type = null;
        Random random = new Random();

        int hp = 0, speed = 0, attack = 0, defense = 0;
        double specialAbilityValue = 0.0;
        double specialAbilityValue2 = 0.0; 

        String line;
        while (!(line = reader.readLine()).trim().equals("EndMonster")) {
            
            String[] parts = line.trim().split("\\s+");
            if (parts.length < 2) continue; 
    
            switch (parts[0]) {
                case "Name":
                    name = parts[1];
                    break;
                case "Type":
                    type = parts[1];
                    break;
                    case "HP":
                    hp = random.nextInt(Integer.parseInt(parts[2]) - Integer.parseInt(parts[1]) + 1) + Integer.parseInt(parts[1]);
                    break;
                case "Speed":
                    speed = random.nextInt(Integer.parseInt(parts[2]) - Integer.parseInt(parts[1]) + 1) + Integer.parseInt(parts[1]);
                    break;
                case "Attack":
                    attack = random.nextInt(Integer.parseInt(parts[2]) - Integer.parseInt(parts[1]) + 1) + Integer.parseInt(parts[1]);
                    break;
                case "Defense":
                    defense = random.nextInt(Integer.parseInt(parts[2]) - Integer.parseInt(parts[1]) + 1) + Integer.parseInt(parts[1]);
                    break;
                case "Paralysis":
                case "Flood":
                case "Fall":
                    specialAbilityValue2 = Double.parseDouble(parts[1]);
                    break;
                case "Heal":
                    specialAbilityValue = Double.parseDouble(parts[1]);
                    break;
                case "Blaze":
                    specialAbilityValue = Double.parseDouble(parts[1]);
                    break;
                case "Hide":
                    specialAbilityValue = Double.parseDouble(parts[1]);
                    break;
                case "Poison":
                    specialAbilityValue = Double.parseDouble(parts[1]);
                    break;
            }

        }

        return createPokemonOfType(name, type, hp, speed, attack, defense, specialAbilityValue, specialAbilityValue2);
    }

private Pokemon createPokemonOfType(String name, String type, int hp, int speed, int attack, int defense, double specialAbility, double specialAbility2) {
    switch (type) {
        case "Electric":
            return new ElectricPokemon(name, hp, speed, attack, defense, specialAbility); 
        case "Water":
            return new WaterPokemon(name, hp, speed, attack, defense, specialAbility, specialAbility2); 
        case "Ground":
            return new GroundPokemon(name, hp, speed, attack, defense, specialAbility); 
        case "Fire":
            return new FirePokemon(name, hp, speed, attack, defense, specialAbility); 
        case "Plant":
            return new PlantPokemon(name, hp, speed, attack, defense, specialAbility); 
        case "Insect":
            return new InsectPokemon(name, hp, speed, attack, defense, specialAbility); 
        default:
            return new Pokemon(name, type, hp, speed, attack, defense); 
    }
}

}
