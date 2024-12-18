import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI extends JFrame {

    private JTextField guessField;
    private JTextArea resultArea;
    private JButton guessButton;
    private JButton nextButton;
    private JButton historyButton;
    private JLabel attemptsLabel;
    private int attempts;
    private int numberToGuess;
    private String playerName;
    private DatabaseManager databaseManager;

    public GameGUI() {
        // Khởi tạo cửa sổ
        setTitle("Trò Chơi Đoán Số");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Chọn font chữ chung cho các thành phần
        Font font = new Font("Arial", Font.PLAIN, 30);  // Chọn font Arial, kích thước 14

        // Khởi tạo các thành phần giao diện và thiết lập font chữ
        guessField = new JTextField(10);
        guessField.setFont(font);  // Áp dụng font cho JTextField

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setFont(font);  // Áp dụng font cho JTextArea

        guessButton = new JButton("Đoán");
        guessButton.setFont(font);  // Áp dụng font cho JButton

        nextButton = new JButton("Chơi tiếp");
        nextButton.setFont(font);  // Áp dụng font cho JButton

        historyButton = new JButton("Lịch sử");
        historyButton.setFont(font);  // Áp dụng font cho JButton

        attemptsLabel = new JLabel("Lượt đoán: 0");
        attemptsLabel.setFont(font);  // Áp dụng font cho JLabel

        // Cài đặt cơ sở dữ liệu
        databaseManager = new DatabaseManager();

        // Layout quản lý bố cục
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Nhập số bạn đoán: "));
        inputPanel.add(guessField);
        inputPanel.add(guessButton);
        inputPanel.add(attemptsLabel);

        JPanel controlPanel = new JPanel();
        controlPanel.add(nextButton);
        controlPanel.add(historyButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Cấu hình nút "Chơi tiếp" và "Lịch sử"
        nextButton.setEnabled(false); // Tắt nút "Chơi tiếp" khi chưa bắt đầu trò chơi
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHistory();
            }
        });

        // Bắt sự kiện nút "Đoán"
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleGuess();
            }
        });

        // Bắt sự kiện nút "Chơi tiếp"
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });
    }

    public void startGame(String playerName) {
        this.playerName = playerName;
        attempts = 0;
        attemptsLabel.setText("Lượt đoán: 0");
        resultArea.setText("");
        nextButton.setEnabled(false);
        guessButton.setEnabled(true);

        // Sinh số ngẫu nhiên từ 1 đến 100
        numberToGuess = (int) (Math.random() * 100) + 1;
    }

    private void handleGuess() {
        try {
            int guessedNumber = Integer.parseInt(guessField.getText());
            attempts++;
            attemptsLabel.setText("Lượt đoán: " + attempts);

            if (guessedNumber < numberToGuess) {
                resultArea.append("Lượt " + attempts + ": Bạn đoán quá thấp!\n");
            } else if (guessedNumber > numberToGuess) {
                resultArea.append("Lượt " + attempts + ": Bạn đoán quá cao!\n");
            } else {
                resultArea.append("Chúc mừng! Bạn đã đoán đúng sau " + attempts + " lượt!\n");
                // Lưu kết quả vào cơ sở dữ liệu
                databaseManager.saveGameHistory(playerName, "Win", attempts);
                nextButton.setEnabled(true);
                guessButton.setEnabled(false);
                return;  // Dừng trò chơi khi đoán đúng
            }

            // Nếu hết 10 lượt mà chưa đoán đúng
            if (attempts >= 10) {
                resultArea.append("Bạn đã hết lượt! Số đúng là " + numberToGuess + ".\n");
                databaseManager.saveGameHistory(playerName, "Lose", attempts);
                nextButton.setEnabled(true);
                guessButton.setEnabled(false);
            }
        } catch (NumberFormatException e) {
            resultArea.append("Vui lòng nhập một số hợp lệ!\n");
        }
    }

    private void startNewGame() {
        guessField.setText("");
        startGame(playerName);
    }

    private void showHistory() {
        // Lấy lịch sử từ cơ sở dữ liệu và hiển thị
        resultArea.setText("Lịch sử trò chơi:\n");
        databaseManager.getPlayerHistory(playerName);
    }

    public static void main(String[] args) {
        // Tạo giao diện và hiển thị
        GameGUI gui = new GameGUI();
        gui.setVisible(true);

        // Bắt đầu trò chơi sau khi nhập tên người chơi
        String playerName = JOptionPane.showInputDialog("Nhập tên người chơi:");
        if (playerName != null && !playerName.isEmpty()) {
            gui.startGame(playerName);
        } else {
            JOptionPane.showMessageDialog(gui, "Tên người chơi không hợp lệ!");
            System.exit(0);
        }
    }
}
