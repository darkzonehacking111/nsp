import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class LeaderBoardFrame extends JFrame {

    private JTable table;

    public LeaderBoardFrame() {

        setTitle("GamesMania - Leaderboard");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        createUI();

        setVisible(true);
    }

    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        JPanel mainPanel =
                new JPanel(new BorderLayout(15, 15));

        mainPanel.setBackground(
                new Color(18, 22, 30)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 25, 25, 25
                )
        );

        // =====================================================
        // TITLE
        // =====================================================

        JLabel title =
                new JLabel(
                        "🏆 LEADERBOARD",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        32
                )
        );

        title.setForeground(
                new Color(
                        255,
                        215,
                        0
                )
        );

        mainPanel.add(
                title,
                BorderLayout.NORTH
        );

        // =====================================================
        // TABLE
        // =====================================================

        String[] columns = {
                "Rank",
                "Player",
                "Game",
                "Difficulty",
                "Score"
        };

        java.util.List<Score> scoreList = new java.util.ArrayList<>();
        try {
            ScoreDAO dao = new ScoreDAO();
            scoreList = dao.getTopScoreObjects(50);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Object[][] data;
        if (scoreList.isEmpty()) {
            data = new Object[][] {
                {"-", "GamesMania", "All Games", "Normal", "No scores recorded yet - play any game!"}
            };
        } else {
            data = new Object[scoreList.size()][5];
            for (int i = 0; i < scoreList.size(); i++) {
                Score s = scoreList.get(i);
                data[i][0] = String.valueOf(i + 1);
                data[i][1] = s.getPlayerName();
                data[i][2] = s.getGameName();
                data[i][3] = s.getDifficulty();
                data[i][4] = String.valueOf(s.getScore());
            }
        }

        table =
                new JTable(
                        data,
                        columns
                );

        table.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        table.setRowHeight(40);

        table.setBackground(
                new Color(
                        35,
                        40,
                        52
                )
        );

        table.setForeground(Color.WHITE);

        table.setGridColor(
                new Color(
                        70,
                        75,
                        90
                )
        );

        table.setSelectionBackground(
                new Color(
                        55,
                        120,
                        220
                )
        );

        table.setSelectionForeground(
                Color.WHITE
        );

        // =====================================================
        // HEADER
        // =====================================================

        table.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                16
                        )
                );

        table.getTableHeader()
                .setBackground(
                        new Color(
                                50,
                                60,
                                80
                        )
                );

        table.getTableHeader()
                .setForeground(Color.WHITE);

        table.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                40
                        )
                );

        // =====================================================
        // CENTER ALIGNMENT
        // =====================================================

        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        for (int i = 0; i < table.getColumnCount(); i++) {

            table.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(center);
        }

        // Column widths

        table.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(60);

        table.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(160);

        table.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(160);

        table.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(110);

        table.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(100);

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTTOM
        // =====================================================

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        bottom.setBackground(
                new Color(
                        18,
                        22,
                        30
                )
        );

        JButton closeButton =
                new JButton("CLOSE");

        closeButton.setPreferredSize(
                new Dimension(
                        130,
                        42
                )
        );

        closeButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        closeButton.setFocusPainted(false);

        closeButton.addActionListener(
                e -> dispose()
        );

        bottom.add(closeButton);

        mainPanel.add(
                bottom,
                BorderLayout.SOUTH
        );

        setContentPane(mainPanel);
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> new LeaderBoardFrame()
        );
    }
}