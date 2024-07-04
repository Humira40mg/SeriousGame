import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SeriousGame {

    private static Scanner scanner = new Scanner(System.in);
    private static List<Avatar> avatars = new ArrayList<>();
    private static List<Question> questions = new ArrayList<>();
    private static List<Defi> defis = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        initialiserFichiersCSV();
        chargerDonnees();
        menuPrincipal();
        scanner.close(); // Fermeture du scanner pour éviter les fuites de ressources
    }

    private static void initialiserFichiersCSV() {
        // Création des fichiers CSV s'ils n'existent pas
        Question.initialiserFichierQuestions();
        Defi.initialiserFichierDefis();
    }

    private static void chargerDonnees() throws IOException {
        avatars = Avatar.chargerAvatars();
        questions = Question.chargerQuestions();
        defis = Defi.chargerDefis();
    }

    private static void menuPrincipal() throws IOException {
        while (true) {
            System.out.println("Bienvenue dans SeriousGame.");
            System.out.println("Quelles actions voulez-vous faire : ");
            System.out.println("1. Créer un nouvel avatar.");
            System.out.println("2. Utiliser un avatar existant.");
            System.out.println("3. Quitter.");

            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer le newline

            switch (choix) {
                case 1:
                    creerNouvelAvatar();
                    break;
                case 2:
                    utiliserAvatarExistant();
                    break;
                case 3:
                    System.out.println("Au revoir!");
                    return;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }

    private static void creerNouvelAvatar() throws IOException {
        System.out.print("Entrez le nom de l'avatar : ");
        String nom = scanner.nextLine();
        int id = avatars.size() + 1;
        Avatar avatar = Avatar.creerNouvelAvatar(id, nom);
        avatars.add(avatar);
        System.out.println("Avatar créé : " + avatar.getNom());
    }

    private static void utiliserAvatarExistant() throws IOException {
        if (avatars.isEmpty()) {
            System.out.println("Aucun avatar n'est disponible.");
            return;
        }

        System.out.println("Liste des avatars disponibles : ");
        for (int i = 0; i < avatars.size(); i++) {
            System.out.println((i + 1) + ". " + avatars.get(i).getNom());
        }

        System.out.print("Sélectionnez l'avatar par numéro : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer le newline

        if (choix < 1 || choix > avatars.size()) {
            System.out.println("Choix invalide.");
            return;
        }

        Avatar avatarSelectionne = avatars.get(choix - 1);
        System.out.println("Avatar sélectionné : " + avatarSelectionne.getNom());

        while (true) {
            System.out.println("Que voulez-vous faire : ");
            System.out.println("1. Lancer un défi.");
            System.out.println("2. Répondre à un défi.");
            System.out.println("3. Retour.");

            int action = scanner.nextInt();
            scanner.nextLine(); // Consommer le newline

            switch (action) {
                case 1:
                    lancerDefi(avatarSelectionne);
                    break;
                case 2:
                    repondreDefi(avatarSelectionne);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }

    private static void lancerDefi(Avatar initiateur) throws IOException {
        System.out.println("Lancer un défi à un autre avatar : ");
        System.out.println("Liste des avatars disponibles : ");

        // Filtrer les avatars pour exclure l'initiateur
        List<Avatar> adversaires = new ArrayList<>();
        for (Avatar avatar : avatars) {
            if (!avatar.getNom().equals(initiateur.getNom())) {
                adversaires.add(avatar);
            }
        }

        if (adversaires.isEmpty()) {
            System.out.println("Aucun adversaire disponible.");
            return;
        }

        for (int i = 0; i < adversaires.size(); i++) {
            System.out.println((i + 1) + ". " + adversaires.get(i).getNom());
        }

        System.out.print("Sélectionnez l'adversaire par numéro : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer le newline

        if (choix < 1 || choix > adversaires.size()) {
            System.out.println("Choix invalide.");
            return;
        }

        Avatar adversaire = adversaires.get(choix - 1);

        System.out.println("Liste des questions disponibles : ");
        List<Question> questionsDisponibles = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).isDisponible()) {
                questionsDisponibles.add(questions.get(i));
                System.out.println((questionsDisponibles.size()) + ". " + questions.get(i).getEnonce());
            }
        }

        if (questionsDisponibles.isEmpty()) {
            System.out.println("Aucune question disponible.");
            return;
        }

        System.out.print("Sélectionnez la question par numéro : ");
        int choixQuestion = scanner.nextInt();
        scanner.nextLine(); // Consommer le newline

        if (choixQuestion < 1 || choixQuestion > questionsDisponibles.size()) {
            System.out.println("Choix invalide.");
            return;
        }

        Question question = questionsDisponibles.get(choixQuestion - 1);
        question.setDisponible(false); // Rendre la question indisponible

        int id = defis.size() + 1;
        Defi defi = new Defi(id, initiateur, adversaire, question);
        defis.add(defi);
        defi.sauvegarderDefi();

        System.out.println("Défi lancé à " + adversaire.getNom() + " avec la question : " + question.getEnonce());
    }

    private static void repondreDefi(Avatar avatar) throws IOException {
        System.out.println(avatar.getNom() + " répond à un défi.");
        System.out.println("Liste des défis disponibles : ");

        List<Defi> defisAdversaire = new ArrayList<>();
        for (int i = 0; i < defis.size(); i++) {
            if (defis.get(i).getAdversaire().getNom().equals(avatar.getNom())) {
                defisAdversaire.add(defis.get(i));
                System.out.println((defisAdversaire.size()) + ". Défi de " + defis.get(i).getInitiateur().getNom() + " : " + defis.get(i).getQuestion().getEnonce());
            }
        }

        if (defisAdversaire.isEmpty()) {
            System.out.println("Aucun défi disponible.");
            return;
        }

        System.out.print("Sélectionnez le défi par numéro : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer le newline

        if (choix < 1 || choix > defisAdversaire.size()) {
            System.out.println("Choix invalide.");
            return;
        }

        Defi defi = defisAdversaire.get(choix - 1);

        System.out.print("Entrez votre réponse : ");
        String reponse = scanner.nextLine();

        // Sauvegarder la réponse
        sauvegarderReponseDefi(defi, reponse);

        // Vérifier la réponse
        if (defi.getQuestion().getReponse().equalsIgnoreCase(reponse)) {
            System.out.println("Bonne réponse! Vous gagnez " + defi.getQuestion().getPoints() + " points.");
            avatar.incrementerPointsDeVie(defi.getQuestion().getPoints());
        } else {
            System.out.println("Mauvaise réponse! Vous perdez " + defi.getQuestion().getPoints() + " points.");
            avatar.decrementerPointsDeVie(defi.getQuestion().getPoints());
        }

        defis.remove(defi);
        Defi.supprimerDefi(defi);
    }

    private static void sauvegarderReponseDefi(Defi defi, String reponse) throws IOException {
        // Implémentation de la sauvegarde de la réponse dans votre système
        // Exemple simplifié ici
        defi.setReponse(reponse);
        defi.sauvegarderDefi();
    }
}
