import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Avatar {
    private int id;
    private String nom;
    private int pointsDeVie;

    public Avatar(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.pointsDeVie = 0; // Points de vie initialisés à 0
    }

    private int calculerPointsDeVie(int[] notes) {
        int total = 0;
        for (int note : notes) {
            int noteCorrigee = Math.min(Math.max(note, 0), 20);
            total += noteCorrigee;
        }
        return total;
    }

    public void incrementerPointsDeVie(int points) {
        this.pointsDeVie += points;
    }

    public void decrementerPointsDeVie(int points) {
        this.pointsDeVie -= points;
        if (this.pointsDeVie < 0) {
            this.pointsDeVie = 0; // S'assurer que les points de vie ne deviennent pas négatifs
        }
    }

    public void sauvegarderAvatar() throws IOException {
        File fichier = new File("Avatars.csv");
        if (!fichier.exists()) {
            fichier.createNewFile();
        }

        try (FileWriter writer = new FileWriter(fichier, true)) {
            writer.append(String.join(",", Integer.toString(id), nom, Integer.toString(pointsDeVie)));
            writer.append("\n");
        }
    }

    public static List<Avatar> chargerAvatars() throws IOException {
        List<Avatar> avatars = new ArrayList<>();
        File fichier = new File("Avatars.csv");
        if (!fichier.exists()) {
            fichier.createNewFile();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parties = ligne.split(",");
                int id = Integer.parseInt(parties[0]);
                String nom = parties[1];
                int pointsDeVie = Integer.parseInt(parties[2]);
                avatars.add(new Avatar(id, nom, pointsDeVie));
            }
        }
        return avatars;
    }

    public static Avatar creerNouvelAvatar(int id, String nom) {
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez le nombre de notes : ");
        int nombreNotes = scanner.nextInt();
        int[] notes = new int[nombreNotes];
        for (int i = 0; i < nombreNotes; i++) {
            System.out.print("Entrez la note " + (i + 1) + " : ");
            notes[i] = scanner.nextInt();
        }
        

        Avatar avatar = new Avatar(id, nom);
        avatar.pointsDeVie = avatar.calculerPointsDeVie(notes); // Calculer et attribuer les points de vie après la création

        try {
            avatar.sauvegarderAvatar();
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde de l'avatar.");
            e.printStackTrace();
        }

        return avatar;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public void setPointsDeVie(int pointsDeVie) {
        this.pointsDeVie = pointsDeVie;
    }

    // Nouveau constructeur pour charger un avatar avec les points de vie
    public Avatar(int id, String nom, int pointsDeVie) {
        this.id = id;
        this.nom = nom;
        this.pointsDeVie = pointsDeVie;
    }
}
