package Project;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class VoterLogin extends JFrame {
    private JLabel aadharLabel, passwordLabel;
    private JTextField aadharField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;
    private JButton loginButton, clearAllButton;
    private JLabel backgroundLabel;
    private JLabel headingLabel;
    private JButton backButton;
    private ButtonGroup partyButtonGroup;



    private Connection connection;

    public VoterLogin()
    {
    
        setTitle("Voter Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        headingLabel = new JLabel("WELCOME TO VOTER LOGIN");
        headingLabel.setFont(new Font("TAHOMA", Font.BOLD, 30));
        headingLabel.setBounds(450, 25, 750, 50);
        headingLabel.setForeground(Color.BLACK);
        add(headingLabel);

        backgroundLabel = new JLabel(new ImageIcon("Indian flag.jpg"));
        backgroundLabel.setBounds(0, 0, 1365, 702);
        add(backgroundLabel);

        aadharLabel = new JLabel("Enter Aadhar:");
        aadharLabel.setBounds(465, 200, 100, 25);
        backgroundLabel.add(aadharLabel);

        passwordLabel = new JLabel("Enter Password:");
        passwordLabel.setBounds(465, 240, 100, 25);
        backgroundLabel.add(passwordLabel);

        aadharField = new JTextField();
        aadharField.setBounds(570, 200, 200, 25);
        backgroundLabel.add(aadharField);

        passwordField = new JPasswordField();
        passwordField.setBounds(570, 240, 200, 25);
        backgroundLabel.add(passwordField);

        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBounds(570, 275, 150, 25);
        backgroundLabel.add(showPasswordCheckBox);

        loginButton = new JButton("Login");
        loginButton.setBounds(610, 320, 100, 30);
        backgroundLabel.add(loginButton);

        clearAllButton = new JButton("Clear all");
        clearAllButton.setBounds(500, 370, 100, 30);
        backgroundLabel.add(clearAllButton);

        backButton = new JButton("Back");
        backButton.setBounds(700, 370, 100, 30);
        backgroundLabel.add(backButton);


        showPasswordCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('\u2022');
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String aadhar = aadharField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticateVoter(aadhar, password)) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");


                    SwingUtilities.invokeLater(() -> {
                        JFrame votingPortalFrame = createVotingPortalFrame(aadhar);
                        votingPortalFrame.setVisible(true);
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Aadhar or Password. Please try again.");
                }
            }
        });

        clearAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aadharField.setText("");
                passwordField.setText("");
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
    }

    private boolean authenticateVoter(String aadhar, String password) {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM voters WHERE aadhar = '" + aadhar + "' AND password = '" + password + "'";

            ResultSet rs = stmt.executeQuery(query);
            boolean validLogin = rs.next();
            rs.close();
            stmt.close();
            conn.close();
            return validLogin;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private JFrame createVotingPortalFrame(String aadhar) {
        JFrame votingPortalFrame = new JFrame("Voting Portal");
        votingPortalFrame.setSize(600, 400);
        votingPortalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        votingPortalFrame.setLayout(null);
        votingPortalFrame.setLocationRelativeTo(null);
        votingPortalFrame.setExtendedState(MAXIMIZED_BOTH);

        JLabel headingLabel = new JLabel("WELCOME TO VOTING PORTAL");
        headingLabel.setFont(new Font("TAHOMA", Font.BOLD, 25));
        headingLabel.setBounds(490, 30, 400, 100);
        votingPortalFrame.add(headingLabel);

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            String query = "SELECT party_name FROM candidates";
            ResultSet rs = stmt.executeQuery(query);

            int yPos = 150;
            partyButtonGroup = new ButtonGroup();

            while (rs.next())
            {
                String partyName = rs.getString("party_name");
                JRadioButton partyRadioButton = new JRadioButton(partyName);
                partyRadioButton.setBounds(580, yPos, 400, 30); //this is radio button position 
                partyRadioButton.setFont(new Font("TAHOMA", Font.PLAIN, 16));
                partyButtonGroup.add(partyRadioButton);
                votingPortalFrame.add(partyRadioButton);
                yPos += 50; // it provides  gap between one radio button and another radio button
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        JButton voteButton = new JButton("Vote");
        voteButton.setBounds(600, 370, 100, 30);
        votingPortalFrame.add(voteButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(600, 430, 100, 30);
        votingPortalFrame.add(backButton);

        voteButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String selectedParty = getSelectedParty(partyButtonGroup);
                if (selectedParty != null) 
                {
                    if (hasVoted(aadhar)) 
                    {
                        JOptionPane.showMessageDialog(null, "You have already voted.");
                    } else {
                        castVote(aadhar, selectedParty);
                        JOptionPane.showMessageDialog(null, "Vote casted successfully!");

                        votingPortalFrame.dispose();
                        SwingUtilities.invokeLater(() -> 
                        {
                            JFrame voterLoginPage = new VoterLogin();
                            voterLoginPage.setVisible(true);
                        });
                    }
                } else 
                {
                    JOptionPane.showMessageDialog(null, "Please select a party to cast your vote.");
                }
            }
        });

        backButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                votingPortalFrame.dispose();
            }
        });

        return votingPortalFrame;
    }

    private String getSelectedParty(ButtonGroup buttonGroup)
    {
        Enumeration<AbstractButton> buttons = buttonGroup.getElements();
        while (buttons.hasMoreElements()) 
        {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) 
            {
                return button.getText();
            }
        }
        return null;
    }
    private boolean hasVoted(String aadhar)
    {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT has_voted FROM voters WHERE aadhar = ?");
            stmt.setString(1, aadhar);
            ResultSet rs = stmt.executeQuery();
            
            boolean hasVoted = false;
            
            if (rs.next())
            {
                hasVoted = rs.getBoolean("has_voted");
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            return hasVoted;
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    private void castVote(String aadhar, String party) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE voters SET has_voted = true WHERE aadhar = ?");
            stmt.setString(1, aadhar);
            stmt.executeUpdate();

            stmt = conn.prepareStatement("UPDATE candidates SET vote_count = vote_count + 1 WHERE party_name = ?");
            stmt.setString(1, party);
            stmt.executeUpdate();
            
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

	

    private Connection getConnection() throws SQLException
    {
        String url = "jdbc:mysql://localhost:3306/OEMS";
        String username = "root";
        String password = "admin123";
        return DriverManager.getConnection(url, username, password);
   
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame voterLoginPage = new VoterLogin();
            voterLoginPage.setVisible(true);
        });
    } 
    
}

