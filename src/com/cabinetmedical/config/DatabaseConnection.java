package com.cabinetmedical.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DatabaseConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = new Properties();
                
                // Essayer de charger depuis le fichier config.properties
                try (InputStream input = new FileInputStream("config.properties")) {
                    props.load(input);
                    System.out.println("Fichier config.properties chargé.");
                } catch (IOException e) {
                    System.out.println("⚠️ Fichier config.properties non trouvé, utilisation des valeurs par défaut.");
                    // Valeurs par défaut - À ADAPTER SELON VOTRE CONFIGURATION
                    props.setProperty("db.url", "jdbc:mysql://localhost:3306/cabinet_medical?useSSL=false&serverTimezone=UTC");
                    props.setProperty("db.user", "root");
                    props.setProperty("db.password", "H12345@"); // Remplacez par votre mot de passe
                }

                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                System.out.println("🔗 Tentative de connexion à : " + url);
                System.out.println("👤 Utilisateur : " + user);
                
                // Charger le driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Établir la connexion
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("✅ Connexion à la base de données établie avec succès !");

            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver MySQL non trouvé !");
                System.err.println("Assurez-vous que la dépendance MySQL Connector/J est dans le classpath.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("❌ Erreur de connexion SQL : " + e.getMessage());
                System.err.println("Code d'erreur : " + e.getErrorCode());
                System.err.println("État SQL : " + e.getSQLState());
                System.err.println("Vérifiez :");
                System.err.println("  1. MySQL est-il démarré ?");
                System.err.println("  2. La base de données 'cabinet_medical' existe-t-elle ?");
                System.err.println("  3. L'utilisateur et le mot de passe sont-ils corrects ?");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("🔒 Connexion fermée.");
            } catch (SQLException e) {
                System.err.println("⚠️ Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }

    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Test de connexion : SUCCÈS");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Test de connexion : ÉCHEC - " + e.getMessage());
        }
        return false;
    }

    // Méthode pour réinitialiser la connexion (utile pour les tests)
    public static void resetConnection() {
        closeConnection();
        connection = null;
        System.out.println("🔄 Connexion réinitialisée.");
    }
}