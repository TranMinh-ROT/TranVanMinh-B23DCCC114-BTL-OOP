public class Game {
    private Player player;
    private int secretNumber;
    private int attempts;
    private boolean isGameOver;

    public Game(Player player) {
        this.player = player;
        this.secretNumber = (int) (Math.random() * 100) + 1;  // Số ngẫu nhiên từ 1 đến 100
        this.attempts = 0;
        this.isGameOver = false;
    }

    public void makeGuess(int guess) {
        attempts++;

        if (guess < secretNumber) {
            System.out.println("Bạn đoán quá thấp!");
        } else if (guess > secretNumber) {
            System.out.println("Bạn đoán quá cao!");
        } else {
            isGameOver = true;
            System.out.println("Chúc mừng! Bạn đã đoán đúng!");
            // Lưu lịch sử trò chơi vào cơ sở dữ liệu
            DatabaseManager.saveGameHistory(player.getName(), "Win", attempts);
        }

        if (attempts >= 10 && !isGameOver) {
            isGameOver = true;
            System.out.println("Bạn đã hết lượt! Số đúng là " + secretNumber);
            // Lưu lịch sử trò chơi vào cơ sở dữ liệu
            DatabaseManager.saveGameHistory(player.getName(), "Lose", attempts);
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getAttempts() {
        return attempts;
    }
}
