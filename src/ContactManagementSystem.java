import java.sql.*;
import java.util.Scanner;

public class ContactManagementSystem {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ContactDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "adishree";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to the database!");

            while (true) {
                System.out.println("\n=== Contact Management System ===");
                System.out.println("1. Add Contact");
                System.out.println("2. View Contacts");
                System.out.println("3. Search Contact");
                System.out.println("4. Update Contact");
                System.out.println("5. Delete Contact");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> addContact(conn, scanner);
                    case 2 -> viewContacts(conn);
                    case 3 -> searchContact(conn, scanner);
                    case 4 -> updateContact(conn, scanner);
                    case 5 -> deleteContact(conn, scanner);
                    case 6 -> {
                        System.out.println("Exiting...");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice, try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addContact(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO contacts (name, phone_number, email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            System.out.println("Contact added successfully!");
        }
    }

    private static void viewContacts(Connection conn) throws SQLException {
        String sql = "SELECT * FROM contacts";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nContacts:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Phone: " + rs.getString("phone_number"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("---------------------------");
            }
        }
    }

    private static void searchContact(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine();

        String sql = "SELECT * FROM contacts WHERE name LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Phone: " + rs.getString("phone_number"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("---------------------------");
                }
            }
        }
    }

    private static void updateContact(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter ID of the contact to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();

        String sql = "UPDATE contacts SET name = ?, phone_number = ?, email = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setInt(4, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Contact updated successfully!");
            } else {
                System.out.println("No contact found with the given ID.");
            }
        }
    }

    private static void deleteContact(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter ID of the contact to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM contacts WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Contact deleted successfully!");
            } else {
                System.out.println("No contact found with the given ID.");
            }
        }
    }
}
