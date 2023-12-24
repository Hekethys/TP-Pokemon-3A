package battle;

import player.Player;
import java.util.Scanner;

public class Battle {
    private Player player1;
    private Player player2;
    private TurnManager turnManager;
    private Terrain terrain;
    private Scanner scanner; 
    private int tour;

    public Battle(Player player1, Player player2, Scanner scanner) {
        this.player1 = player1;
        this.player2 = player2;
        this.scanner = scanner;
        this.terrain = new Terrain(); 
        this.turnManager = new TurnManager(player1, player2, scanner, terrain);
        this.tour = 1;
    }

    public void start() {
        System.out.println("""
                
        \\:.             .:/
         \\``._________.''/ 
          \\             / 
  .--.--, / .':.   .':. \\
 /__:  /  | '::' . '::' |
    / /   |`.   ._.   .'|
   / /    |.'         '.|
  /___-_-,|.\\  \\   /  /.|
       // |''\\.;   ;,/ '|
       `==|:=         =:|
          `.          .'
            :-._____.-:
           `''       `''""");
 
 
        while (player1.hasAlivePokemon() && player2.hasAlivePokemon()) {
            turnManager.executeTurn();

            if (!player1.getActivePokemon().isAlive()) {
                switchPokemonIfPossible(player1);
            }

            if (!player2.getActivePokemon().isAlive()) {
                switchPokemonIfPossible(player2);
            }

            terrain.updateEffects();
        }

        announceWinner();
    }

    private void switchPokemonIfPossible(Player player) {
        if (player.hasAlivePokemon()) {
            System.out.println(player.getName() + ", votre Pokémon actif a été vaincu. Choisissez un nouveau Pokémon.");
            player.chooseNewActivePokemon();
        }
    }

    private void announceWinner() {
        if (player1.hasAlivePokemon()) {
            System.out.println(player1.getName() + " gagne le combat !");
        } else if (player2.hasAlivePokemon()) {
            System.out.println(player2.getName() + " gagne le combat !");
        } else {
            System.out.println("Le combat se termine par une égalité !");
        }
    }
}
