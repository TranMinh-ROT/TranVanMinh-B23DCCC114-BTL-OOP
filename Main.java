import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Tạo đối tượng người chơi
        System.out.println("Nhập tên người chơi:");
        String playerName = scanner.nextLine();
        Player player = new Player(playerName);

        // Bắt đầu trò chơi
        Game game = new Game(player);

        while (!game.isGameOver()) {
            System.out.println("Nhập số đoán (1 đến 100):");
            int guess = scanner.nextInt();
            game.makeGuess(guess);
        }

        // Lấy lịch sử trò chơi
        DatabaseManager.getPlayerHistory(playerName);
    }
}
