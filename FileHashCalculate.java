/*import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class FileHashCalculate {

    public static byte[] getHash(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());
        return md.digest();
    }

    public static String getText(String filepath) {
        try (BufferedReader in = new BufferedReader(new FileReader(filepath))) {
            StringBuilder text = new StringBuilder();
            String str;
            while ((str = in.readLine()) != null) {
                text.append(str);
            }
            return text.toString();
        } catch (IOException e) {
            System.err.println("Error getting text: " + e.getMessage());
            return null;
        }
    }

    public static int count_zero(byte[] bytes){
        int cnt = 0;
        while(bytes[cnt] == 0){
            ++cnt;
        }
        return cnt;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // 创建文件选择器
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择一个文件");
        // 显示选择对话框，并检查用户选择
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("已选择文件: " + selectedFile.getAbsolutePath());
            // 读取文件内容
            String fileContent = getText(selectedFile.getAbsolutePath());
            if (fileContent != null) {
                // 计算文件内容的哈希值
                try {
                    int times = 0;
                    for(int i = 1 ; i <= 4 ; ++i) {
                        int cnt;
                        Random random = new Random();
                        long rdlong;
                        byte[] hashValue;
                        do {
                            random = new Random();
                            rdlong = random.nextLong();//在文件结尾随机加上一串数字
                            hashValue = getHash(fileContent + String.valueOf(rdlong));
                            cnt = count_zero(hashValue);
                            ++times;
                        } while (cnt != i);
                        System.out.println("尝试次数：" + times);
                        System.out.println("hashValue: " + bytesToHex(hashValue));
                    }
                } catch (NoSuchAlgorithmException e) {
                    System.err.println("计算哈希值时出错: " + e.getMessage());
                }
            }
        } else {
            System.out.println("未选择文件.");
        }
    }
}*/
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class FileHashCalculate {

    public static byte[] getHash(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());
        return md.digest();
    }

    public static String getText(String filepath) {
        try (BufferedReader in = new BufferedReader(new FileReader(filepath))) {
            StringBuilder text = new StringBuilder();
            String str;
            while ((str = in.readLine()) != null) {
                text.append(str);
            }
            return text.toString();
        } catch (IOException e) {
            System.err.println("Error getting text: " + e.getMessage());
            return null;
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%x", b));
        }
        return sb.toString();
    }

    public static boolean startsWithZeros(byte[] hash, int zeros) {
        // 跳过前导零的检查
        for (int i = 0; i < zeros; i++) {
            if (i >= hash.length || hash[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // 创建文件选择器
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择一个文件");

        // 显示选择对话框，并检查用户选择
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("已选择文件: " + selectedFile.getAbsolutePath());

            // 读取文件内容
            String fileContent = getText(selectedFile.getAbsolutePath());
            if (fileContent != null) {
                for (int i = 1; i <= 30; i++) {
                    findHashWithLeadingZeros(fileContent, i);
                }
            }
        } else {
            System.out.println("未选择文件.");
        }
    }
    private static void findHashWithLeadingZeros(String content, int leadingZeros) {
        byte[] hashValue;
        long attempts = 0;
        SecureRandom secureRandom = new SecureRandom(); // 使用SecureRandom生成随机数
        BigInteger randomBigInt;
        do {
            attempts++;
            randomBigInt = new BigInteger(256, secureRandom);
            try {
                hashValue = getHash(content + randomBigInt.toString()); // 将随机数添加到content后面
            } catch (NoSuchAlgorithmException e) {
                System.err.println("计算哈希值时出错: " + e.getMessage());
                return;
            }
        } while (!startsWithZeros(hashValue, leadingZeros));
        // 当找到满足条件的哈希值后， 输出结果
        System.out.println("随机生成的内容：" + randomBigInt.toString(16));
        System.out.println("哈希值: " + bytesToHex(hashValue));
        System.out.println("尝试次数: " + attempts);
    }
}

