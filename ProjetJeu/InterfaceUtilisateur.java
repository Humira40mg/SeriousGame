import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class InterfaceUtilisateur {
    private static Scanner scanner = new Scanner(System.in);

    public static void gererMenuPrincipal(List<Avatar> avatars, List<Question> questions, List<Defi> defis) throws IOException {
        boolean quitter = false;
        while (!quitter) {
            afficherMenuPrincipal();
            int choix = obtenirChoixUtilisateur();
            switch (choix) {
                case 1:
                    creerNouvelAvatar(avatars);
                    break;
                case 2:
                    utiliserAvatarExistant(avatars, questions, defis);
                    break;
                case 3:
                    quitter = true;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }

    private static void afficherMenuPrincipal() {
        System.out.println("Bienvenue dans SeriousGame.");
        System.out.println("Quelles actions voulez-vous faire : ");
        System.out.println("1. Créer un nouvel avatar.");
        System.out.println("2. Utiliser un avatar existant.");
        System.out.println("3. Quitter.");
    }

    private static int obtenirChoixUtilisateur() {
        int choix = 0;
        boolean choixValide = false;
        
        while (!choixValide) {
            try {
                System.out.print("Entrez votre choix : ");
                choix = scanner.nextInt();
                choixValide = true;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Veuillez entrer un nombre entier.");
                scanner.nextLine(); 
            }
        }
        
        return choix;
    }
    

    private static void creerNouvelAvatar(List<Avatar> avatars) throws IOException {
        scanner.nextLine(); // Pour consommer le newline restant après nextInt()
        System.out.print("Entrez le nom de l'avatar : ");
        String nom = scanner.nextLine();
        SeriousGame.creerNouvelAvatar(nom);
    }

    private static void utiliserAvatarExistant(List<Avatar> avatars, List<Question> questions, List<Defi> defis) throws IOException {
        if (avatars.isEmpty()) {
            System.out.println("Aucun avatar n'est disponible.");
            return;
        }

        afficherAvatars(avatars);

        System.out.print("Sélectionnez l'avatar par numéro : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Pour consommer le newline restant après nextInt()

        if (choix < 1 || choix > avatars.size()) {
            System.out.println("Choix invalide.");
            return;
        }

        Avatar avatarSelectionne = avatars.get(choix - 1);
        System.out.println("Avatar sélectionné : " + avatarSelectionne.getNom());

        gererActionsAvatar(avatarSelectionne, avatars, questions, defis);
    }

    private static void afficherAvatars(List<Avatar> avatars) {
        System.out.println("Liste des avatars disponibles : ");
        for (int i = 0; i < avatars.size(); i++) {
            System.out.println((i + 1) + ". " + avatars.get(i).getNom());
        }
    }

    private static void gererActionsAvatar(Avatar avatar, List<Avatar> avatars, List<Question> questions, List<Defi> defis) throws IOException {
        boolean retour = false;
        while (!retour) {
            afficherActionsAvatar();
            int choix = obtenirChoixUtilisateur();
            switch (choix) {
                case 1:
                    lancerDefi(avatar, avatars, questions, defis);
                    break;
                case 2:
                    repondreDefi(avatar, defis);
                    break;
                case 3:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }

    private static void afficherActionsAvatar() {
        System.out.println("Que voulez-vous faire : ");
        System.out.println("1. Lancer un défi.");
        System.out.println("2. Répondre à un défi.");
        System.out.println("3. Retour.");
    }

    private static void lancerDefi(Avatar initiateur, List<Avatar> avatars, List<Question> questions, List<Defi> defis) throws IOException {
        afficherAvatarsExcluantInitiateur(initiateur, avatars);

        System.out.print("Sélectionnez l'adversaire par numéro : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Pour consommer le newline restant après nextInt()

        if (choix < 1 || choix > avatars.size() - 1) {
            System.out.println("Choix invalide.");
            return;
        }

        Avatar adversaire = avatars.get(choix - 1);
        System.out.println("Avatar adversaire sélectionné : " + adversaire.getNom());
        

        afficherQuestionsDisponibles(questions);

        System.out.print("Sélectionnez la question par numéro : ");
        int choixQuestion = scanner.nextInt();
        scanner.nextLine(); // Pour consommer le newline restant après nextInt()

        if (choixQuestion < 1 || choixQuestion > questions.size()) {
            System.out.println("Choix invalide.");
            return;
        }

        Question question = questions.get(choixQuestion - 1);

        SeriousGame.lancerDefi(initiateur, adversaire, question);
    }

    private static void afficherAvatarsExcluantInitiateur(Avatar initiateur, List<Avatar> avatars) {
        System.out.println("Liste des avatars disponibles (excluant " + initiateur.getNom() + ") : ");
        for (Avatar avatar : avatars) {
            if (!avatar.getNom().equals(initiateur.getNom())) {
                System.out.println((avatars.indexOf(avatar) + 1) + ". " + avatar.getNom());
            }
        }   
    }   

    private static void afficherQuestionsDisponibles(List<Question> questions) {
        System.out.println("Liste des questions disponibles : ");
        for (int i = 0; i < questions.size(); i++) {
            System.out.println((i + 1) + ". " + questions.get(i).getEnonce());
        }
    }

    private static void repondreDefi(Avatar avatar, List<Defi> defis) throws IOException {
        afficherDefisAdversaire(avatar, defis);

        System.out.print("Sélectionnez le défi par numéro : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Pour consommer le newline restant après nextInt()

        if (choix < 1 || choix > defis.size()) {
            System.out.println("Choix invalide.");
            return;
        }

        Defi defi = defis.get(choix - 1);

        System.out.print("Entrez le numéro de la réponse réponse : ");
        String reponse = scanner.nextLine();

        SeriousGame.repondreDefi(avatar, defi, reponse);
    }

    private static void afficherDefisAdversaire(Avatar avatar, List<Defi> defis) {
        System.out.println(avatar.getNom() + " répond à un défi.");
        System.out.println("Liste des défis disponibles : ");
        for (Defi defi : defis) {
            if (defi.getAdversaire().getNom().equals(avatar.getNom())) {
                System.out.println((defis.indexOf(defi) + 1) + ". Défi de " + defi.getInitiateur().getNom() + " : " + defi.getQuestion().getEnonce());
            }
        }
    }
}
