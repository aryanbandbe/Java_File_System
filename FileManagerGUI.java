import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FileManagerGUI extends JFrame {

    private JTextField filePathField;
    private JTextField fileNameField;
    private JTextArea fileContentArea;
    private JTextArea outputArea;
    private JTextField keyField;
    private JTextField zipFileField;
    private JTextArea filesToZipArea;
    private JTextField unzipFileField;
    private JTextField unzipDestField;

    public FileManagerGUI() {
        setTitle("File Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 248, 255));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("File Path:"), gbc);
        gbc.gridx = 1;
        filePathField = new JTextField(25);
        formPanel.add(filePathField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("File Name:"), gbc);
        gbc.gridx = 1;
        fileNameField = new JTextField(25);
        formPanel.add(fileNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("File Content:"), gbc);
        gbc.gridx = 1;
        fileContentArea = new JTextArea(4, 25);
        formPanel.add(new JScrollPane(fileContentArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Encryption Key:"), gbc);
        gbc.gridx = 1;
        keyField = new JTextField(25);
        formPanel.add(keyField, gbc);

        panel.add(formPanel, BorderLayout.NORTH);

        // Area for Output
        outputArea = new JTextArea(10, 30);
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Additional fields for zipping and unzipping
        JPanel zipPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        zipPanel.setBackground(new Color(240, 248, 255));
        zipPanel.add(new JLabel("Zip File Path:"));
        zipFileField = new JTextField(25);
        zipPanel.add(zipFileField);

        zipPanel.add(new JLabel("Files to Zip (one per line):"));
        filesToZipArea = new JTextArea(4, 25);
        zipPanel.add(new JScrollPane(filesToZipArea));

        zipPanel.add(new JLabel("Unzip File Path:"));
        unzipFileField = new JTextField(25);
        zipPanel.add(unzipFileField);

        zipPanel.add(new JLabel("Destination Directory:"));
        unzipDestField = new JTextField(25);
        zipPanel.add(unzipDestField);

        panel.add(zipPanel, BorderLayout.WEST);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton createButton = new JButton("Create File");
        createButton.addActionListener(e -> {
            String path = filePathField.getText();
            String fileName = fileNameField.getText();
            FileManager.createFile(path, fileName);
            outputArea.setText("File created.");
        });
        buttonPanel.add(createButton);

        JButton deleteButton = new JButton("Delete File");
        deleteButton.addActionListener(e -> {
            String path = filePathField.getText();
            String fileName = fileNameField.getText();
            FileManager.deleteFile(path, fileName);
            outputArea.setText("File deleted.");
        });
        buttonPanel.add(deleteButton);

        JButton writeButton = new JButton("Write Content");
        writeButton.addActionListener(e -> {
            String path = filePathField.getText();
            String fileName = fileNameField.getText();
            String content = fileContentArea.getText();
            FileManager.writeFile(path, fileName, content);
            outputArea.setText("File content written.");
        });
        buttonPanel.add(writeButton);

        JButton encryptButton = new JButton("Encrypt File");
        encryptButton.addActionListener(e -> {
            String path = filePathField.getText() + "/" + fileNameField.getText() + ".txt";
            String key = keyField.getText();
            FileManager.encryptFile(path, key);
            outputArea.setText("File encrypted.");
        });
        buttonPanel.add(encryptButton);

        JButton decryptButton = new JButton("Decrypt File");
        decryptButton.addActionListener(e -> {
            String path = filePathField.getText() + "/" + fileNameField.getText() + ".txt";
            String key = keyField.getText();

            // Popup asking for decryption key
            String userKey = JOptionPane.showInputDialog(this, "Enter decryption key:");
            if (userKey != null && userKey.equals(key)) {
                String content = FileManager.decryptFile(path, userKey);
                outputArea.setText("File decrypted: \n" + content);
            } else {
                outputArea.setText("Incorrect key. Unable to decrypt.");
            }
        });
        buttonPanel.add(decryptButton);

        // Zipping Files
        JButton zipButton = new JButton("Zip Files");
        zipButton.addActionListener(e -> {
            String zipPath = zipFileField.getText();
            String[] filesToZip = filesToZipArea.getText().split("\n");
            FileManager.zipFiles(zipPath, filesToZip);
            outputArea.setText("Files zipped into: " + zipPath);
        });
        buttonPanel.add(zipButton);

        // Unzipping Files
        JButton unzipButton = new JButton("Unzip File");
        unzipButton.addActionListener(e -> {
            String zipPath = unzipFileField.getText();
            String destDir = unzipDestField.getText();
            FileManager.unzipFile(zipPath, destDir);
            outputArea.setText("Unzipped files to: " + destDir);
        });
        buttonPanel.add(unzipButton);

        panel.add(buttonPanel, BorderLayout.SOUTH); // Place button panel at the bottom

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileManagerGUI().setVisible(true));
    }
}
