import java.util.Scanner;
import java.sql.SQLException;

// Клас для опису ігрового поля
class GameBoard {
    private char[][] board;
    private final int SIZE = 3;

    public GameBoard() {
        board = new char[SIZE][SIZE];
        initializeBoard();
    }

    // Ініціалізація порожнього ігрового поля
    public void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '-';
            }
        }
    }

    // Вивести ігрове поле на екран
    public void displayBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Зміна стану ігрового поля
    public boolean updateBoard(int row, int col, char symbol) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == '-') {
            board[row][col] = symbol;
            return true;
        }
        return false;
    }

    // Перевірка на перемогу
    public boolean checkWin(char symbol) {
        // Перевірка рядків
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true;
            }
        }

        // Перевірка колонок
        for (int i = 0; i < SIZE; i++) {
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) {
                return true;
            }
        }

        // Перевірка діагоналей
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            return true;
        }
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) {
            return true;
        }

        return false;
    }
}

// Клас для опису гравця
class Player {
    private String name;
    private char symbol;

    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    // Метод для введення ходу гравця
    public int[] makeMove() {
        Scanner scanner = new Scanner(System.in);
        int[] move = new int[2];

        System.out.println(name + ", enter your move (row and column):");
        move[0] = scanner.nextInt();
        move[1] = scanner.nextInt();

        return move;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }
}

// Основний клас для запуску гри
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Підключаємося до бази даних
        Db db = null;
        try {
            db = new Db("jdbc:postgresql://localhost:5432/postgres", "postgres", "qW67B*p&`12");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
            return;
        }

        // Логін гравців
        Player player1 = null;
        Player player2 = null;

        while (player1 == null) {
            System.out.println("Player 1, enter your username:");
            String username = scanner.nextLine();
            System.out.println("Player 1, enter your password:");
            String password = scanner.nextLine();

            if (db.authenticatePlayer(username, password)) {
                player1 = new Player(username, 'X');
            } else {
                System.out.println("Invalid login for Player 1, try again.");
            }
        }

        while (player2 == null) {
            System.out.println("Player 2, enter your username:");
            String username = scanner.nextLine();
            System.out.println("Player 2, enter your password:");
            String password = scanner.nextLine();

            if (db.authenticatePlayer(username, password)) {
                player2 = new Player(username, 'O');
            } else {
                System.out.println("Invalid login for Player 2, try again.");
            }
        }

        // Створюємо ігрове поле
        GameBoard gameBoard = new GameBoard();

        // Починаємо гру
        boolean gameOver = false;
        Player currentPlayer = player1;

        gameBoard.displayBoard();

        while (!gameOver) {
            System.out.println(currentPlayer.getSymbol() + "'s turn!");
            int[] move = currentPlayer.makeMove();

            if (gameBoard.updateBoard(move[0], move[1], currentPlayer.getSymbol())) {
                gameBoard.displayBoard();

                if (gameBoard.checkWin(currentPlayer.getSymbol())) {
                    System.out.println("Player " + currentPlayer.getName() + " wins!");
                    gameOver = true;
                } else {
                    currentPlayer = (currentPlayer == player1) ? player2 : player1;
                }
            } else {
                System.out.println("Invalid move, try again.");
            }
        }

        System.out.println("Game over!");

        // Закриваємо з'єднання з базою даних
        db.close();
    }
}
