import java.sql.*;
import java.util.Scanner;

public class QueryProgram {
    public static void main(String[] args) {
        String projectDir = System.getProperty("user.dir");
        String filePath = "jdbc:sqlite:" + projectDir + "/databases/movie_theater_sales.db";
        Scanner scan = new Scanner(System.in);
        String userInput;

        try {
            Connection db = DriverManager.getConnection(filePath);

            while (true) {
                printUserOptions();

                userInput = scan.nextLine();
                System.out.println();

                if (userInput.equalsIgnoreCase("quit")) {
                    System.out.println("Connection closed. Goodbye!");
                    break;
                } else{
                    dbLookup(userInput, db);
                }
            }

            try {
                if (db != null) {
                    db.close();
                }
                scan.close();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
        }
    }

    public static void printUserOptions() {
        System.out.println("Please choose from the following list of options:\n");
        System.out.println("1) Enter a date in the format MM/DD/YYYY to view theaters with the most sales for that date");
        System.out.println("2) Enter \'quit\' to exit the program\n");
    }

    public static void dbLookup(String userInput, Connection db) throws SQLException {
        String query = "";
        ResultSet resultSet;
        boolean emptySet = true;
        PreparedStatement ps;

        userInput = padZeros(userInput);

        query = """
            WITH Sum_Sales AS (
                SELECT SUM(sales) AS sales, theater_id
                FROM Sales
                WHERE date = ?
                GROUP BY theater_id
            ),
            Max_Sales AS (
                SELECT theater_id
                FROM Sum_Sales
                WHERE sales = (SELECT MAX(sales) FROM Sum_Sales)
            )
            SELECT name, city, state
            FROM Theaters
            WHERE id IN (SELECT theater_id FROM Max_Sales);
        """;

        ps = db.prepareStatement(query);
        ps.setString(1, userInput);
        resultSet = ps.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String city = resultSet.getString("city");
            String state = resultSet.getString("state");

            if (emptySet) {
                emptySet = false;
                System.out.println("The following movie theater(s) had the most sales on " + userInput + ":\n");
            }

            System.out.printf("%s in %s, %s%n", name, city, state);
        }

        if (emptySet) {
            System.out.println("Invalid command or date.");
        }

        System.out.println();
    }

    public static String padZeros(String rawDate) {
        String finalDate = "";
        String[] monthDayYear = rawDate.split("/");

        if (monthDayYear.length != 3) {
            return rawDate;
        }

        monthDayYear[0] = String.format("%0" + 2 + "d", Integer.parseInt(monthDayYear[0]));
        monthDayYear[1] = String.format("%0" + 2 + "d", Integer.parseInt(monthDayYear[1]));
        monthDayYear[2] = String.format("%0" + 4 + "d", Integer.parseInt(monthDayYear[2]));

        for (int i = 0; i < 3; i++) {
            finalDate += monthDayYear[i];

            if (i < 2) {
                finalDate += '/';
            }
        }

        return finalDate;
    }
}