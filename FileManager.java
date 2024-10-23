import java.io.*;
import java.nio.file.*;
import java.util.zip.*;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

public class FileManager {

    public static void createFile(String path, String fileName) {
        try {
            Files.createFile(Paths.get(path, fileName + ".txt"));
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
        }
    }

    public static void deleteFile(String path, String fileName) {
        try {
            Files.deleteIfExists(Paths.get(path, fileName + ".txt"));
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
    }

    public static void writeFile(String path, String fileName, String content) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path, fileName + ".txt"))) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void encryptFile(String filePath, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            try (FileInputStream fis = new FileInputStream(filePath);
                 CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(filePath + ".enc"), cipher)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    cos.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            System.err.println("Error encrypting file: " + e.getMessage());
        }
    }

    public static String decryptFile(String filePath, String key) {
        StringBuilder decryptedContent = new StringBuilder();
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            try (CipherInputStream cis = new CipherInputStream(new FileInputStream(filePath), cipher);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(cis))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    decryptedContent.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            System.err.println("Error decrypting file: " + e.getMessage());
        }
        return decryptedContent.toString();
    }

    public static void zipFiles(String zipFilePath, String[] filesToZip) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            for (String file : filesToZip) {
                File srcFile = new File(file);
                if (!srcFile.exists()) {
                    System.err.println("File not found: " + file);
                    continue;
                }

                try (FileInputStream fis = new FileInputStream(srcFile)) {
                    ZipEntry zipEntry = new ZipEntry(srcFile.getName());
                    zos.putNextEntry(zipEntry);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error zipping files: " + e.getMessage());
        }
    }

    public static void unzipFile(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs(); // Create destination directory if it does not exist

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                File newFile = new File(destDir, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    newFile.mkdirs(); // Create directories for unzipped files
                } else {
                    // Make sure parent directories exist
                    new File(newFile.getParent()).mkdirs();
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile))) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zis.read(buffer)) >= 0) {
                            bos.write(buffer, 0, length);
                        }
                    }
                }
                zis.closeEntry();
            }
        } catch (IOException e) {
            System.err.println("Error unzipping file: " + e.getMessage());
        }
    }
}
