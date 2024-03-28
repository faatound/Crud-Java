import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class gestion {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenue dans le programme de gestion de notes des eleves!");
        System.out.println("Vous voulez saisir les notes de quelle classe? :");
        System.out.println("1. DSTI1A");
        System.out.println("2. DSTI2B");
        System.out.println("3. LICENCE 3");
        System.out.println("4. DIC1");
        System.out.println("5. DIC2");

        System.out.print("Entrez votre choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); 

        switch (choix) {
            case 1:
                afficherMatieres("DSTI1A");
                break;
            case 2:
                afficherMatieres("DSTI2B");
                break;
            case 3:
                afficherMatieres("LICENCE 3");
                break;
            case 4:
                afficherMatieres("DIC1");
                break;
            case 5:
                afficherMatieres("DIC2");
                break;
            default:
                System.out.println("Choix invalide. Veuillez redémarrer le programme.");
                break;
        }

       
    }

    private static void afficherMatieres(String classe) {
        System.out.println("Vous avez choisi la classe " + classe + ". Voici les matières disponibles :");

        switch (classe) {
            case "DSTI1A":
                System.out.println("1. Mathématiques\n2. Informatique\n3. Economie");
                break;
            case "DSTI2B":
                System.out.println("1. Java\n2. Statistiques\n3. Base de données");
                break;
            case "LICENCE 3":
                System.out.println("1. UML\n2. XML\n3. Doit des affaires");
                break;
            case "DIC1":
                System.out.println("1. Inf appliquée\n2. Circuit logique\n3.  Électronique");
                break;
            case "DIC2":
                System.out.println("1. Marketing\n2. Gestion des ordinateurs\n3. Comptabilité");
                break;
            default:
                System.out.println("Classe non reconnue.");
                break;
        }

       
    }
}

    
}






import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class functions {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bienvenue dans le programme de gestion de notes des eleves! vous voulez saisir les notes de quelle classe? :");
        System.out.println("1. DSTI1A");
        System.out.println("2. DSTI2B");
        System.out.println("3. LICENCE 3");
        System.out.println("4. DIC1");
        System.out.println("5. DIC2");

        System.out.print("Entrez votre choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        

        switch (choix) {
            case 1:
                ajouterEtudiant();
                break;
            case 2:
                listerEtudiants();
                break;
            case 3:
                System.out.print("Entrez l'ID de l'étudiant à modifier : ");
                int idModifier = scanner.nextInt();
                modifierEtudiant(idModifier);
                break;
            case 4:
                System.out.print("Entrez l'ID de l'étudiant à supprimer : ");
                int idSupprimer = scanner.nextInt();
                supprimerEtudiant(idSupprimer);
                break;
            case 5:
                System.out.println("Programme terminé.");
                return;
            default:
                System.out.println("Choix invalide. Veuillez entrer un numéro entre 1 et 5.");
        }
    }

    private static void ajouterEtudiant() {
        System.out.println("Ajout d'un étudiant...");
        // Connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/fstudents";
        String user = "root";
        String password = "";

        System.out.print("Entrez le nom de l'étudiant : ");
        String nom = scanner.next();
        System.out.print("Entrez le prénom de l'étudiant : ");
        String prenom = scanner.next();
        System.out.print("Entrez la date de naissance (YYYY-MM-DD) : ");
        String dateNaissance = scanner.next();
        System.out.print("Entrez le sexe (M/F) : ");
        String sexe = scanner.next();

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO student (nom, prenom, datedenaissance, sexe) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nom);
                statement.setString(2, prenom);
                statement.setString(3, dateNaissance);
                statement.setString(4, sexe);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("L'étudiant a été ajouté avec succès !");
                } else {
                    System.out.println("Échec de l'ajout de l'étudiant.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'étudiant : " + e.getMessage());
        }
    }

    private static void listerEtudiants() {
        String url = "jdbc:mysql://localhost:3306/fstudents";
        String user = "root";
        String password = "";

        System.out.println("Liste des étudiants...");
        String sql = "SELECT * FROM student";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Liste des étudiants :");
            System.out.println("ID\tNom\tPrénom\tDate de naissance\tSexe");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String dateNaissance = resultSet.getString("datedenaissance");
                String sexe = resultSet.getString("sexe");
                System.out.println(id + "\t" + nom + "\t" + prenom + "\t" + dateNaissance + "\t" + sexe);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données ou de la récupération des étudiants.");
            e.printStackTrace();
        }
    }

    private static void modifierEtudiant(int id) {
        System.out.println("Modification d'un étudiant...");
        String url = "jdbc:mysql://localhost:3306/fstudents";
        String user = "root";
        String password = "";

        System.out.print("Entrez le nouveau nom de l'étudiant : ");
        String nouveauNom = scanner.nextLine();

        System.out.print("Entrez le nouveau prénom de l'étudiant : ");
        String nouveauPrenom = scanner.nextLine();

        System.out.print("Entrez la nouvelle date de naissance de l'étudiant (YYYY-MM-DD) : ");
        String nouvelleDateNaissance = scanner.nextLine();

        System.out.print("Entrez le nouveau sexe de l'étudiant (M ou F) : ");
        String nouveauSexe = scanner.nextLine();

        String sql = "UPDATE student SET nom = ?, prenom = ?, datedenaissance = ?, sexe = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nouveauNom);
            statement.setString(2, nouveauPrenom);
            statement.setString(3, nouvelleDateNaissance);
            statement.setString(4, nouveauSexe);
            statement.setInt(5, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Les informations de l'étudiant ont été mises à jour avec succès !");
            } else {
                System.out.println("Échec de la mise à jour des informations de l'étudiant.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données ou de la mise à jour des informations de l'étudiant.");
            e.printStackTrace();
        }
    }


    

    private static void supprimerEtudiant(int id) {
        System.out.println("Suppression d'un étudiant...");
        String url = "jdbc:mysql://localhost:3306/fstudents";
        String user = "root";
        String password = "";

        String sql = "DELETE FROM student WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("L'étudiant a été supprimé avec succès !");
            } else {
                System.out.println("Échec de la suppression de l'étudiant.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données ou de la suppression de l'étudiant.");
            e.printStackTrace();
        }

        
    }
}











   

