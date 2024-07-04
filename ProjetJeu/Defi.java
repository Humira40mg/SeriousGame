import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Defi {
    private int id;
    private Avatar initiateur;
    private Avatar adversaire;
    private Question question;

    public Defi(int id, Avatar initiateur, Avatar adversaire, Question question) {
        this.id = id;
        this.initiateur = initiateur;
        this.adversaire = adversaire;
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public Avatar getInitiateur() {
        return initiateur;
    }

    public Avatar getAdversaire() {
        return adversaire;
    }

    public Question getQuestion() {
        return question;
    }

    /**
     * Sauvegarde le défi dans le fichier Defis.csv.
     */
    public void sauvegarderDefi() {
        File fichier = new File("Defis.csv");

        try (PrintWriter writer = new PrintWriter(new FileWriter(fichier, true))) {
            writer.println(String.format("%d,%s,%s,%s,%s,%d",
                    id, initiateur.getNom(), adversaire.getNom(),
                    question.getEnonce(), question.getReponse(), question.getPoints()));
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du défi : " + e.getMessage());
        }
    }

    /**
     * Charge tous les défis depuis le fichier Defis.csv.
     * Si le fichier n'existe pas ou s'il y a une erreur de lecture,
     * une liste vide est retournée.
     *
     * @return Liste des défis chargés depuis le fichier Defis.csv.
     */
    public static List<Defi> chargerDefis() {
        List<Defi> defis = new ArrayList<>();
        File fichier = new File("Defis.csv");

        if (!fichier.exists()) {
            System.out.println("Le fichier Defis.csv n'existe pas.");
            return defis; // Retourner une liste vide si le fichier n'existe pas
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] donnees = ligne.split(",");
                int id = Integer.parseInt(donnees[0]);
                String initiateurNom = donnees[1];
                String adversaireNom = donnees[2];
                String enonce = donnees[3];
                String reponse = donnees[4];
                int points = Integer.parseInt(donnees[5]);

                Avatar initiateur = Avatar.chargerAvatar(initiateurNom);
                Avatar adversaire = Avatar.chargerAvatar(adversaireNom);
                Question question = new Question(enonce, reponse, points, true); // Utilisation du constructeur correct
                Defi defi = new Defi(id, initiateur, adversaire, question);
                defis.add(defi);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier Defis.csv : " + e.getMessage());
        }

        return defis;
    }

    /**
     * Vérifie et crée le fichier Defis.csv s'il n'existe pas.
     */
    private static void verifierFichierDefis() {
        File fichier = new File("Defis.csv");
        if (!fichier.exists()) {
            try {
                fichier.createNewFile();
            } catch (IOException e) {
                System.out.println("Erreur lors de la création du fichier Defis.csv : " + e.getMessage());
            }
        }
    }

    /**
     * Supprime le défi spécifié du fichier Defis.csv.
     *
     * @param defi Le défi à supprimer.
     */
    public static void supprimerDefi(Defi defi) {
        File fichier = new File("Defis.csv");
        File fichierTemporaire = new File("Defis_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier));
             PrintWriter writer = new PrintWriter(new FileWriter(fichierTemporaire))) {

            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] donnees = ligne.split(",");
                int id = Integer.parseInt(donnees[0]);

                // Écrire la ligne dans le fichier temporaire sauf si l'ID correspond à celui du défi à supprimer
                if (id != defi.getId()) {
                    writer.println(ligne);
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression du défi : " + e.getMessage());
            return;
        }

        // Supprimer l'ancien fichier et renommer le fichier temporaire
        if (fichier.delete()) {
            if (!fichierTemporaire.renameTo(fichier)) {
                System.err.println("Erreur lors du renommage du fichier temporaire.");
            }
        } else {
            System.err.println("Erreur lors de la suppression du fichier original.");
        }
    }

    public static void main(String[] args) {
        try {
            Scanner reader = new Scanner(new File("Defis.csv"));
        }
        catch (FileNotFoundException e){
            try {
            PrintWriter writer = new PrintWriter("Defis.csv","UTF-8");
            writer.close();
            }
            catch(Exception a){
                System.out.println("Error : File Not Found");
            }
        }
    }
}