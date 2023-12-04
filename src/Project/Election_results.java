package Project;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Election_results extends JFrame {
    private JLabel headingLabel;
    private JButton backButton;
    private JTable resultsTable;
    private JScrollPane scrollPane;
    private JLabel winnerLabel;
    private JLabel backgroundLabel;

    private Connection connection;

    public Election_results() {
        setTitle("Election Results");
        setSize(900, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        headingLabel = new JLabel("WELCOME TO ELECTION RESULTS PAGE");
        headingLabel.setFont(new Font("TAHOMA", Font.BOLD, 30));
        headingLabel.setBounds(400, 10, 750, 50);
        headingLabel.setForeground(Color.BLACK);
        add(headingLabel);

        backgroundLabel = new JLabel(new ImageIcon("Indian flag.jpg"));
        backgroundLabel.setBounds(0, 0, 1365, 702);
        add(backgroundLabel);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(400, 80, 500, 400);
        backgroundLabel.add(scrollPane);

        resultsTable = new JTable();
        scrollPane.setViewportView(resultsTable);

        winnerLabel = new JLabel();
        winnerLabel.setFont(new Font("TAHOMA", Font.BOLD, 20));
        winnerLabel.setBounds(450, 500, 500, 30);
        backgroundLabel.add(winnerLabel);

        backButton = new JButton("Back");
        backButton.setBounds(640, 600, 100,40);
        backgroundLabel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        populateResults();
        determineWinner();
        setVisible(true);
    }

    private void populateResults() {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            String query = "SELECT party_name, vote_count FROM candidates ORDER BY vote_count DESC";

            ResultSet rs = stmt.executeQuery(query);
            DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Party Name", "No of Votes"}, 0);
            while (rs.next()) {
                String partyName = rs.getString("party_name");
                int voteCount = rs.getInt("vote_count");
                tableModel.addRow(new Object[]{partyName, voteCount});
            }
            rs.close();
            stmt.close();
            conn.close();

            resultsTable.setModel(tableModel);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void determineWinner() {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            String query = "SELECT party_name, vote_count FROM candidates ORDER BY vote_count DESC LIMIT 1";

            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                String winnerParty = rs.getString("party_name");
                int voteCount = rs.getInt("vote_count");
                winnerLabel.setText("Winner is : " + winnerParty + " (Majority Votes: " + voteCount + ")");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/OEMS";
        String username = "root";
        String password = "admin123";
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame electionResultsFrame = new Election_results();
            electionResultsFrame.setVisible(true);
        });
    }
}

