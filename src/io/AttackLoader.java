package io;
import battle.Attack;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttackLoader {

    public List<Attack> loadAttacks(String filename) {
        List<Attack> attacks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("Attack")) {
                    attacks.add(readAttack(reader));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attacks;
    }

    private Attack readAttack(BufferedReader reader) throws IOException {
        String name = null, type = null;
        int power = 0, maxUses = 0;
        double failRate = 0.0;

        String line;

        while (!(line = reader.readLine()).trim().equals("EndAttack")) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length < 2) continue; 
            switch (parts[0]) {
                case "Name":
                    
                    name = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
                    break;
                case "Type":
                    type = parts[1];
                    break;
                case "Power":
                    power = Integer.parseInt(parts[1]);
                    break;
                case "NbUse":
                    maxUses = Integer.parseInt(parts[1]);
                    break;
                case "Fail":
                    failRate = Double.parseDouble(parts[1]);
                    break;
            }
        }

        return new Attack(name, type, power, maxUses, failRate);
    }
}
