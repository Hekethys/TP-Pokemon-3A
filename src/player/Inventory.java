package player;

import java.util.HashMap;
import java.util.Map;

import battle.Terrain;
import item.Item;
import pokemon.Pokemon;

public class Inventory {
    private Map<Item, Integer> items;

    public Inventory() {
        this.items = new HashMap<>();
    }

    public void addItem(Item item, int quantity) {
        this.items.put(item, this.items.getOrDefault(item, 0) + quantity);
    }

    public boolean useItem(Item item, Pokemon target, Terrain terrain) {
        if (items.containsKey(item) && items.get(item) > 0) {
            item.use(target, terrain);
            items.put(item, items.get(item) - 1);
            return true;
        }
        return false;
    }

    public int getItemCount(Item item) {
        return items.getOrDefault(item, 0);
    }

    
    public void displayInventory() {
        System.out.println("Inventaire:");
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            System.out.println(entry.getKey().getName() + " - Quantité: " + entry.getValue());
        }
    }

    
}
