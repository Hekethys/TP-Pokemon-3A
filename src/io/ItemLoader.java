package io;

import item.Item;
import item.Potion;
import item.Medicine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ItemLoader {

    public List<Item> loadItems(String filePath) {
        List<Item> items = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if ("Item".equals(line.trim())) {
                    items.add(parseItem(scanner));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        }
        return items;
    }

    private Item parseItem(Scanner scanner) {
        String name = "";
        String type = "";
        String effect = "";
        int value = 0;

    while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if ("EndItem".equals(line.trim())) {
            break;
        }
        String[] parts = line.split("\\s+");
        switch (parts[0]) {
            case "Name":
                name = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                break;
            case "Type":
                type = parts[1];
                break;
            case "Effect":
                effect = parts[1];
                break;
            case "Value":
                value = Integer.parseInt(parts[1]);
                break;
        }
    }

        if ("Potion".equals(type)) {
            return new Potion(name, effect, value);
        } else if ("Medicine".equals(type)) {
            return new Medicine(name, effect);
        } else {
            return new Item(name, type, effect);
        }
    }
}
