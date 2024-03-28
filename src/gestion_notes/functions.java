package gestion_notes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;



public class functions {
    static String url  = "jdbc:mysql://localhost:3306/fstudents";
    static String user = "root";
    static String password = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenue dans le programme de gestion de notes des élèves!");
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
        scanner.close();
    }

    private static void afficherMatieres(String classe) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Vous avez choisi la classe " + classe + ". Voici les matières disponibles :");
        
        switch (classe) {
            case "DSTI1A":
                System.out.println("1. Mathématiques\n2. Informatique\n3. Economie");
                break;
            case "DSTI2B":
                System.out.println("1. Java\n2. Statistiques\n3. Base de données");
                break;
            case "LICENCE3":
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
       
        System.out.print("Entrez votre choix : ");
        int matiere = scanner.nextInt();
        scanner.nextLine(); 
       
        System.out.println("Quelles options souhaitez-vous effectuer:");
        System.out.println("1. Liste Etudiants ");
        System.out.println("2. Ajouter Note");
        System.out.println("3. Modifier Note");
        System.out.println("4. Ajouter un etudiant non  inscrit");

        System.out.print("Entrez votre choix : ");
        int choixMatiere = scanner.nextInt();
        scanner.nextLine(); 

        switch (choixMatiere) {
            case 1:
                listeEtudiants(classe);
                break;
            case 2:
                System.out.println("ID de l'étudiant dont on va remplir la note : ");
                int idEtudiant = scanner.nextInt();
                scanner.nextLine(); 
                AjoutezNote(idEtudiant, scanner);
                break;
            case 3:
                System.out.print("Entrez l'ID de l'étudiant dont on doit modifier la note : ");
                int idModifier = scanner.nextInt();
                scanner.nextLine(); 
                modifierNote(idModifier, scanner);
                break;
            case 4:
                AjoutEtudiant(scanner);
                break;
            default:
                System.out.println("Choix invalide. Veuillez entrer un numéro entre 1 et 3.");
        }
    }

   
    private static Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private static void listeEtudiants(String classe) {
        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM student WHERE classe = ?");
        ) {
            statement.setString(1, classe);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Liste des étudiants de la classe " + classe + ":");
            System.out.println("ID\tNom\tPrénom\tNote_cc\tNote_ds");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                float note_cc = resultSet.getFloat("note_cc");
                float note_ds = resultSet.getFloat("note_ds");
                System.out.println(id + "\t" + nom + "\t" + prenom + "\t" + note_cc + "\t" + note_ds);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données ou de la récupération des étudiants.");
            e.printStackTrace();
        }
    }

    private static void AjoutezNote(int idEtudiant, Scanner scanner) {
        System.out.println("Ajout des Notes...");
        
        System.out.print("Entrez la note de cc de l'étudiant : ");
        float note_cc = scanner.nextFloat();
        scanner.nextLine(); // Pour consommer la nouvelle ligne
        
        System.out.print("Entrez la note de ds de l'étudiant : ");
        float note_ds = scanner.nextFloat();
        scanner.nextLine(); // Pour consommer la nouvelle ligne
    
        String sql = "UPDATE student SET note_cc = ?, note_ds = ? WHERE id = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {
               
            statement.setFloat(1, note_cc);
            statement.setFloat(2, note_ds);
            statement.setInt(3, idEtudiant);
    
            int lignesModifiees = statement.executeUpdate();
            if (lignesModifiees > 0) {
                System.out.println("La note a été mise à jour avec succès !");
            } else {
                System.out.println("Échec de la mise à jour de la note. L'étudiant avec l'ID spécifié n'existe pas.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la note : " + e.getMessage());
        }
    }
    

    private static void modifierNote(int idModifier, Scanner scanner) {
        System.out.println("Modification d'une note");

        System.out.print("Entrez la nouvelle note de cc de l'étudiant : ");
        float nouvelleNotecc = scanner.nextFloat();
        scanner.nextLine(); 
        System.out.print("Entrez la nouvelle note de ds de l'étudiant : ");
        float nouvelleNoteds = scanner.nextFloat();
        scanner.nextLine(); 

        String sql = "UPDATE student SET note_cc = ?, note_ds = ? WHERE id = ?";
        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setFloat(1, nouvelleNotecc);
            statement.setFloat(2, nouvelleNoteds);
            statement.setInt(3, idModifier);

            int lignesModifiees = statement.executeUpdate();
            if (lignesModifiees > 0) {
                System.out.println("Les informations de l'étudiant ont été mises à jour avec succès !");
            } else {
                System.out.println("Échec de la mise à jour des informations de l'étudiant.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour des informations de l'étudiant : " + e.getMessage());
        }
    }

    private static void AjoutEtudiant(Scanner scanner) {
         
        Etu etudiant = new Etu();
        System.out.print("Entrez le nom de l'étudiant : ");
        etudiant.setNom(" "+scanner.nextLine());
        System.out.print("Entrez le prénom de l'étudiant : ");
        etudiant.setPrenom(""+scanner.nextLine());
        System.out.print("Entrez la classe de l'étudiant : ");
        etudiant.setClasse(""+scanner.nextLine());
        

        String sql = "INSERT INTO student (nom, prenom, classe) VALUES (?, ?, ?)";
        try (Connection connection = connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {
               
            statement.setString(1, etudiant.getNom());
            statement.setString(2, etudiant.getPrenom());
            statement.setString(3, etudiant.getClasse());
          
            int lignesModifiees = statement.executeUpdate();
            if (lignesModifiees > 0) {
                System.out.println("L'étudiant a été ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout de l'étudiant.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'étudiant : " + e.getMessage());
        }
    }

    }




