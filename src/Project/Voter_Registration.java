package Project;


import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;

public class Voter_Registration extends JFrame 
{
    private JLabel backgroundLabel;
    private JLabel nameLabel;
    private JLabel fatherNameLabel;
    private JLabel ageLabel;
    private JLabel genderLabel;
    private JLabel aadharLabel;
    private JLabel gmailLabel;
    private JLabel createpasswordLabel;
    private JLabel confirmPasswordLabel;
 

    private JTextField nameField;
    private JTextField fatherNameField;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JTextField aadharField;
    private JTextField gmailField;
    private JPasswordField createpasswordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox showPasswordCheckBox;

    private JButton registerButton;
    private JButton clearButton;
    private JButton backButton;

    public Voter_Registration() 
    {
         setTitle("Voter Registration");
        setSize(900, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        backgroundLabel = new JLabel(new ImageIcon("Indian flag.jpg"));
        backgroundLabel.setBounds(0, 0, 1365, 702);
        add(backgroundLabel);

        nameLabel = new JLabel("Name :");
        nameLabel.setBounds(512, 100, 100, 25);
        backgroundLabel.add(nameLabel);

        fatherNameLabel = new JLabel("Father's Name :");
        fatherNameLabel.setBounds(465, 150, 100, 25);
        backgroundLabel.add(fatherNameLabel);

        ageLabel = new JLabel("Age :");
        ageLabel.setBounds(525, 200, 100, 25);
        backgroundLabel.add(ageLabel);

        genderLabel = new JLabel("Gender :");
        genderLabel.setBounds(507, 250, 100, 25);
        backgroundLabel.add(genderLabel);

        aadharLabel = new JLabel("Aadhar Number :");
        aadharLabel.setBounds(459, 300, 100, 25);
        backgroundLabel.add(aadharLabel);

        gmailLabel = new JLabel("Gmail :");
        gmailLabel.setBounds(517, 350, 100, 25);
        backgroundLabel.add(gmailLabel);

        createpasswordLabel = new JLabel("Create Password :");
        createpasswordLabel.setBounds(450, 400, 200, 25);
        backgroundLabel.add(createpasswordLabel);

        confirmPasswordLabel = new JLabel("Confirm Password :");
        confirmPasswordLabel.setBounds(445, 450, 200, 25);
        backgroundLabel.add(confirmPasswordLabel);

        nameField = new JTextField();
        nameField.setBounds(560, 100, 200, 25);
        backgroundLabel.add(nameField);

        fatherNameField = new JTextField();
        fatherNameField.setBounds(560, 150, 200, 25);
        backgroundLabel.add(fatherNameField);

        ageField = new JTextField();
        ageField.setBounds(560, 200, 200, 25);
        backgroundLabel.add(ageField);

        genderComboBox = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        genderComboBox.setBounds(560, 250, 100, 25);
        backgroundLabel.add(genderComboBox);

        aadharField = new JTextField();
        aadharField.setBounds(560, 300, 200, 25);
        backgroundLabel.add(aadharField);

        gmailField = new JTextField();
        gmailField.setBounds(560, 350, 200, 25);
        backgroundLabel.add(gmailField);

        createpasswordField = new JPasswordField();
        createpasswordField.setBounds(560, 400, 200, 25);
        backgroundLabel.add(createpasswordField);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(560, 450, 200, 25);
        backgroundLabel.add(confirmPasswordField);

        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBounds(560, 490, 150, 25);
        backgroundLabel.add(showPasswordCheckBox);

        showPasswordCheckBox.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                if (showPasswordCheckBox.isSelected())
                {
                	createpasswordField.setEchoChar((char) 0);
                    confirmPasswordField.setEchoChar((char) 0);
                } else {
                	createpasswordField.setEchoChar('*');
                    confirmPasswordField.setEchoChar('*');
                }
            }
        });

        registerButton = new JButton("Register");
        registerButton.setBounds(560, 530, 100, 25);
        backgroundLabel.add(registerButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(680, 530, 90, 25);
        backgroundLabel.add(clearButton);

        backButton = new JButton("Back");
        backButton.setBounds(640,575, 70, 25);
        backgroundLabel.add(backButton);

        registerButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String fatherName = fatherNameField.getText().trim();
                String age = ageField.getText().trim();
                String gender = genderComboBox.getSelectedItem().toString();
                String aadhar = aadharField.getText().trim();
                String gmail = gmailField.getText().trim();
                String password = new String(createpasswordField.getPassword()).trim();
                String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

                if (name.isEmpty() || fatherName.isEmpty() || age.isEmpty() || aadhar.isEmpty() || gmail.isEmpty() ||
                        password.isEmpty() || confirmPassword.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(Voter_Registration.this, "Please fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                else if (!password.equals(confirmPassword)) 
                {
                    JOptionPane.showMessageDialog(Voter_Registration.this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                else if (password.length() < 6 || password.length() > 15)
                {
                    JOptionPane.showMessageDialog(Voter_Registration.this, "Password should be between 6 and 15 characters long", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (aadhar.length() < 4 || aadhar.length() > 15)
                {
                    JOptionPane.showMessageDialog(Voter_Registration.this, "Aadhar number should be more than 4 charecters and less than 15 charecters", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (Integer.parseInt(age) <= 18 || Integer.parseInt(age) >= 60) 
                {
                    JOptionPane.showMessageDialog(Voter_Registration.this, "Age should be between 18 and 60", "Error", JOptionPane.ERROR_MESSAGE);
                } else 
                {
                    if (isRegistered(gmail, aadhar))
                    {
                        JOptionPane.showMessageDialog(Voter_Registration.this, "Gmail/Aadhar already registered", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        storeVoterDetails(name, fatherName, age, gender, aadhar, gmail, password);
                        JOptionPane.showMessageDialog(Voter_Registration.this, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    }
                }
            }
        });

        clearButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                clearFields();
            }
        });

        backButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
              
            }
        });

        setVisible(true);
    }

    private boolean isRegistered(String gmail, String aadhar)
    {
        boolean registered = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/OEMS", "root", "admin123");
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM voters WHERE gmail=? OR aadhar=?");
            pstmt.setString(1, gmail);
            pstmt.setString(2, aadhar);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) 
            {
                registered = true;
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        return registered;
    }

    private void storeVoterDetails(String name, String fatherName, String age, String gender, String aadhar, String gmail, String password) 
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/OEMS", "root", "admin123");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO voters (name, father_name, age, gender, aadhar, gmail, password) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, name);
            pstmt.setString(2, fatherName);
            pstmt.setString(3, age);
            pstmt.setString(4, gender);
            pstmt.setString(5, aadhar);
            pstmt.setString(6, gmail);
            pstmt.setString(7, password);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    private void clearFields()
    {
        nameField.setText("");
        fatherNameField.setText("");
        ageField.setText("");
        genderComboBox.setSelectedIndex(0);
        aadharField.setText("");
        gmailField.setText("");
        createpasswordField.setText("");
        confirmPasswordField.setText("");
        showPasswordCheckBox.setSelected(false);
    }

    public static void main(String[] args)
    {
        new Voter_Registration();
    }
}