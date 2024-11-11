import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class FilleHash30min {

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
                long startTime = System.currentTimeMillis();
                int maxLeadingZeros = 0;

                for (int i = 1; i <= 30; i++) {
                    long currentTime = System.currentTimeMillis();
                    // 检查是否超过30分钟，单位为毫秒
                    if ((currentTime - startTime) >= 30 * 60 * 1000) {
                        break;
                    }

                    if (findHashWithLeadingZeros(fileContent, i)) {
                        maxLeadingZeros = i; // 更新找到的最大连续零的位数
                    }
                }

                System.out.println("在30分钟内找到的最大连续零的位数: " + maxLeadingZeros);
            }
        } else {
            System.out.println("未选择文件.");
        }
    }

    private static boolean findHashWithLeadingZeros(String content, int leadingZeros) {
        byte[] hashValue;
        long attempts = 0;
        SecureRandom secureRandom = new SecureRandom(); // 使用SecureRandom生成随机数
        BigInteger randomBigInt;
        long startTime = System.currentTimeMillis();
        do {
            attempts++;
            randomBigInt = new BigInteger(256, secureRandom);
            try {
                hashValue = getHash(content + randomBigInt.toString()); // 将随机数添加到content后面
            } catch (NoSuchAlgorithmException e) {
                System.err.println("计算哈希值时出错: " + e.getMessage());
                return false;
            }

            // 检查时间是否超过30分钟
            if ((System.currentTimeMillis() - startTime) >= 30 * 60 * 1000) {//30 * 60 * 1000
                return false; // 超过时间限制，返回
            }
        } while (!startsWithZeros(hashValue, leadingZeros));

        // 当找到满足条件的哈希值后， 输出结果
        System.out.println("随机生成的内容：" + randomBigInt.toString(16));
        System.out.println("哈希值: " + bytesToHex(hashValue));
        System.out.println("尝试次数: " + attempts);
        return true; // 找到满足条件的哈希值
    }
}
