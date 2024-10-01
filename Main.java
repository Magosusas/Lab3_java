import java.util.Scanner;

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

        // Створюємо ігрове поле
        GameBoard gameBoard = new GameBoard();

        // Створюємо двох гравців
        System.out.println("Enter name for Player 1 (X):");
        String player1Name = scanner.nextLine();
        Player player1 = new Player(player1Name, 'X');

        System.out.println("Enter name for Player 2 (O):");
        String player2Name = scanner.nextLine();
        Player player2 = new Player(player2Name, 'O');

        // Починаємо гру
        boolean gameOver = false;
        Player currentPlayer = player1;

        // Виводимо початкове ігрове поле
        gameBoard.displayBoard();

        while (!gameOver) {
            System.out.println(currentPlayer.getSymbol() + "'s turn!");
            int[] move = currentPlayer.makeMove();

            // Оновлюємо ігрове поле, якщо хід валідний
            if (gameBoard.updateBoard(move[0], move[1], currentPlayer.getSymbol())) {
                gameBoard.displayBoard();

                // Перевіряємо чи є переможець
                if (gameBoard.checkWin(currentPlayer.getSymbol())) {
                    System.out.println("Player " + currentPlayer.getName() + " wins!");
                    gameOver = true;
                } else {
                    // Перемикаємо гравця
                    currentPlayer = (currentPlayer == player1) ? player2 : player1;
                }
            } else {
                System.out.println("Invalid move, try again.");
            }
        }

        System.out.println("Game over!");
    }
}
