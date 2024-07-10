import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Question {
    private String enonce;
    private List<String> reponse = new ArrayList<>();
    private int points;
    private int bonnereponse;

    public Question(String enonce, List<String> reponse, int points, int bonnereponse) {
        this.enonce = enonce;
        this.reponse = reponse;
        this.points = points;
        this.bonnereponse = bonnereponse;
    }

    public String getEnonce() {
        return enonce;
    }

    public String getReponse() {
        String blokquestion = String.join(";", reponse);
        return blokquestion;
    }

    public int getPoints() {
        return points;
    }

    public int getBonneReponse() {
        return bonnereponse;
    }


    public void setReponse(List<String> reponse) {
        this.reponse = reponse;
    }

    public static Question creerNouvelleQuestion(Scanner scanner) {
        System.out.print("Entrez l'énoncé de la question : ");
        String enonce = scanner.nextLine();

        List<String> reponses = new ArrayList<>();
        for (int i = 0; i == 3; i++){
            System.out.print("Entrez la réponse "+ (i++) +" de la question : ");
            reponses.add(scanner.nextLine());
        }

        int bonnereponse = 5;
        while (bonnereponse > 4) {
            System.out.println("Si vous voulez revoir vos réponses tapez 5");
            System.out.print("Entrez le numéro de la Bonne réponse : ");
            bonnereponse = scanner.nextInt();
            scanner.nextLine();
        }

        System.out.print("Entrez le nombre de points pour cette question : ");
        int points = scanner.nextInt();
        scanner.nextLine();

        Question nouvelleQuestion = new Question(enonce, reponses, points, bonnereponse);

        try {
            ajouterQuestion(nouvelleQuestion);
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde de la question.");
            e.printStackTrace();
        }

        return nouvelleQuestion;
    }

    public static void ajouterQuestion(Question question) throws IOException {
        verifierFichierQuestions();

        File fichier = new File("Questions.csv");
        try (FileWriter writer = new FileWriter(fichier, true)) {
            writer.append(question.getEnonce()).append(",")
                  .append(question.getReponse()).append(",")
                  .append(Integer.toString(question.getPoints())).append(",")
                  .append(Integer.toString(question.getBonneReponse())).append("\n");
        }
    }

    public static List<Question> chargerQuestions() {
        List<Question> questions = new ArrayList<>();
        File fichier = new File("Questions.csv");
        if (!fichier.exists()) {
            System.out.println("Le fichier Questions.csv n'existe pas.");
            return questions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parties = ligne.split(",");
                if (parties.length == 4) {
                    String enonce = parties[0];
                    String[] reponseslu = parties[1].split(";");
                    List<String> reponses = Arrays.asList(reponseslu);
                    int points = Integer.parseInt(parties[2]);
                    int bonnereponse = Integer.parseInt(parties[3]);
                    questions.add(new Question(enonce, reponses, points, bonnereponse));
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier Questions.csv : " + e.getMessage());
        }
        return questions;
    }

    private static void verifierFichierQuestions() {
        String nomFichier = "Questions.csv";
        File fichier = new File(nomFichier);

        try {
            if (!fichier.exists()) {
                boolean fichierCree = fichier.createNewFile();
                if (fichierCree) {
                    System.out.println("Le fichier " + nomFichier + " a été créé avec succès.");
                } else {
                    System.out.println("Le fichier " + nomFichier + " n'a pas pu être créé.");
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la création du fichier " + nomFichier + " : " + e.getMessage());
        }
    }

    public static void initialiserFichierQuestions() {
        verifierFichierQuestions();
    }

    public static void main(String[] args) {
        try {
            Scanner reader = new Scanner(new File("Questions.csv"));
        } catch (FileNotFoundException e) {
            try {
                PrintWriter writer = new PrintWriter("Questions.csv", "UTF-8");
                writer.close();
            } catch (Exception a) {
                System.out.println("Error : File Not Found");
            }
        }
    }
}
