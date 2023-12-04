package Project;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class VoterListFrame extends JFrame {
	private JTable voterTable;

	public VoterListFrame(String[][] data, String[] columnNames) {

		setTitle("List of Voters");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);

		voterTable = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(voterTable);
		add(scrollPane);
	}
}

class CandidateListFrame extends JFrame {
	private JTable candidateTable;

	public CandidateListFrame(String[][] data, String[] columnNames) {
		setTitle("List of Candidates");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);

		candidateTable = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(candidateTable);
		add(scrollPane);
	}
}

class AdminLogin extends JFrame {
	private JLabel usernameLabel, passwordLabel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JCheckBox showPasswordCheckBox;
	private JButton loginButton, clearAllButton;
	private JLabel backgroundLabel;
	private JButton backButton;
	private JLabel headingLabel;

	private Connection connection;

	public AdminLogin() {
		setTitle("Admin Login");
		setSize(400, 250);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);

		headingLabel = new JLabel("WELCOME TO ADMIN LOGIN");
		headingLabel.setFont(new Font("TAHOMA", Font.BOLD, 30));
		headingLabel.setBounds(450, 25, 750, 50);
		headingLabel.setForeground(Color.BLACK);
		add(headingLabel);

		backgroundLabel = new JLabel(new ImageIcon("Indian flag.jpg"));
		backgroundLabel.setBounds(0, 0, 1365, 702);
		add(backgroundLabel);

		usernameLabel = new JLabel("Enter Username:");
		usernameLabel.setBounds(465, 200, 100, 25);
		backgroundLabel.add(usernameLabel);

		passwordLabel = new JLabel("Enter Password:");
		passwordLabel.setBounds(465, 240, 100, 25);
		backgroundLabel.add(passwordLabel);

		usernameField = new JTextField();
		usernameField.setBounds(570, 200, 200, 25);
		backgroundLabel.add(usernameField);

		passwordField = new JPasswordField();
		passwordField.setBounds(570, 240, 200, 25);
		backgroundLabel.add(passwordField);

		showPasswordCheckBox = new JCheckBox("Show Password");
		showPasswordCheckBox.setBounds(565, 275, 150, 25);
		backgroundLabel.add(showPasswordCheckBox);

		loginButton = new JButton("Login");
		loginButton.setBounds(610, 320, 100, 30);
		backgroundLabel.add(loginButton);

		clearAllButton = new JButton("Clear all");
		clearAllButton.setBounds(610, 380, 100, 30);
		backgroundLabel.add(clearAllButton);

		backButton = new JButton("Back");
		backButton.setBounds(610, 440, 100, 30);
		backgroundLabel.add(backButton);

		showPasswordCheckBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (showPasswordCheckBox.isSelected()) {
					passwordField.setEchoChar((char) 0);
				} else {
					passwordField.setEchoChar('\u2022');
				}
			}
		});

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());

				if (username.equals("a") && password.equals("a")) {
					JOptionPane.showMessageDialog(null, "Login Successful!");
					dispose();
					SwingUtilities.invokeLater(() -> {
						JFrame resultsFrame = Results();
						resultsFrame.setVisible(true);
					});
				} else {
					JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
				}
			}
		});

		clearAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				usernameField.setText("");
				passwordField.setText("");
			}
		});

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private JFrame Results() {
		JFrame resultsFrame = new JFrame();
		resultsFrame.setSize(400, 300);
		resultsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		resultsFrame.setLayout(null);
		resultsFrame.setLocationRelativeTo(null);
		resultsFrame.setExtendedState(MAXIMIZED_BOTH);

		JButton viewVoters = new JButton("List of Voters");
		viewVoters.setBounds(590, 200, 250, 30);

		JButton viewRegisteredCandidates = new JButton("List of Candidates");
		viewRegisteredCandidates.setBounds(590, 280, 250, 30);

		JButton electionResults = new JButton("Election Results");
		electionResults.setBounds(590, 360, 250, 30);

		JButton backButton2 = new JButton("Back");
		backButton2.setBounds(665, 500, 80, 30);

		ImageIcon backgroundImage = new ImageIcon("Indian flag.jpg");
		JLabel backgroundLabel = new JLabel(backgroundImage);
		backgroundLabel.setBounds(0, 0, resultsFrame.getWidth(), resultsFrame.getHeight());
		resultsFrame.setContentPane(backgroundLabel);

		viewVoters.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/OEMS", "root",
							"admin123");
					Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					String query = "SELECT * FROM voters";
					ResultSet rs = stmt.executeQuery(query);
					rs.last();
					int rowCount = rs.getRow();
					rs.beforeFirst();
					String[][] voterData = new String[rowCount][7];
					int i = 0;
					while (rs.next()) {
						voterData[i][0] = rs.getString("SerialNo");
						voterData[i][1] = rs.getString("name");
						voterData[i][2] = rs.getString("father_name");
						voterData[i][3] = rs.getString("age");
						voterData[i][4] = rs.getString("gender");
						voterData[i][5] = rs.getString("gmail");
						voterData[i][6] = rs.getString("aadhar");

						i++;
					}

					String[] columnNames = { "name", "father_name", "age", "gender", "gmail", "aadhar" };

					VoterListFrame voterListFrame = new VoterListFrame(voterData, columnNames);
					voterListFrame.setVisible(true);

					rs.close();
					stmt.close();
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});

		resultsFrame.add(viewVoters);

		viewRegisteredCandidates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/OEMS", "root",
							"admin123");
					Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					String query = "SELECT * FROM candidates";
					ResultSet rs = stmt.executeQuery(query);
					rs.last();
					int rowCount = rs.getRow();
					rs.beforeFirst();
					String[][] candidateData = new String[rowCount][6];
					int i = 0;
					while (rs.next()) {
						candidateData[i][0] = rs.getString("S_No");
						candidateData[i][1] = rs.getString("candidate_name");
						candidateData[i][2] = rs.getString("age");
						candidateData[i][3] = rs.getString("gender");
						candidateData[i][4] = rs.getString("aadhar");
						candidateData[i][5] = rs.getString("party_name");

						i++;
					}

					String[] columnNames = { "S_No", "candidate_name", "age", "gender", "aadhar", "party_name" };

					CandidateListFrame candidateListFrame = new CandidateListFrame(candidateData, columnNames);
					candidateListFrame.setVisible(true);

					rs.close();
					stmt.close();
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});
		resultsFrame.add(viewRegisteredCandidates);

		electionResults.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Election_results sr = new Election_results();
				sr.setVisible(true);
			}

		});
		resultsFrame.add(electionResults);

		backButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resultsFrame.dispose();
			}
		});
		resultsFrame.add(backButton2);

		return resultsFrame;
	}

	public static void main(String[] args) {
		AdminLogin adminLogin = new AdminLogin();
		adminLogin.setVisible(true);

	}
}
