import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class FileHashCompare {
    public static HashMap<Integer, ArrayList<String>> HashValue = new HashMap<>();
    public static ArrayList<String[]> list = new ArrayList<>();

    public static void getFilepath(String path) throws IOException {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                getFilepath(f.getAbsolutePath());
            } else {
                String strFileName = f.getAbsolutePath();
                System.out.println(strFileName);
                byte[] temp = getHash(strFileName);
                if (temp != null) {
                    int hashKey = Arrays.hashCode(temp);
                    if (HashValue.containsKey(hashKey)) {
                        boolean same = false;
                        for (String s : HashValue.get(hashKey)) {
                            if (CompareText(s, strFileName)) {
                                String[] li = new String[2];
                                li[0] = s;
                                li[1] = strFileName;
                                list.add(li);
                                same = true;
                                break;
                            }
                        }
                        if (!same) {
                            HashValue.get(hashKey).add(strFileName);
                        }
                    } else {
                        ArrayList<String> tp = new ArrayList<>();
                        tp.add(strFileName);
                        HashValue.put(hashKey, tp);
                    }
                }
            }
        }
    }

    public static boolean CompareText(String path1, String path2) throws IOException {
        try (BufferedReader in1 = new BufferedReader(new FileReader(path1));
             BufferedReader in2 = new BufferedReader(new FileReader(path2))) {
            String s1, s2;
            while ((s1 = in1.readLine()) != null) {
                s2 = in2.readLine();
                if (s2 == null || !s1.equals(s2)) {
                    return false;
                }
            }
            return in2.readLine() == null;
        }
    }

    public static byte[] getHash(String filepath) {
        try (BufferedReader in = new BufferedReader(new FileReader(filepath))) {
            StringBuilder text = new StringBuilder();
            String str;
            while ((str = in.readLine()) != null) {
                text.append(str);
            }
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.toString().getBytes());
            return md.digest();
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println("Error calculating hash: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        // 创建一个文件选择框
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("选择一个文件夹");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = chooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = chooser.getSelectedFile();
            String path = selectedDirectory.getAbsolutePath();
            try {
                getFilepath(path);
                for(int i = 0 ; i < list.size() ; ++i)
                {
                    String[] s = list.get(i);
                    System.out.println("地址为：" + s[0] + "与地址为" + s[1] + "内容一致");
                }
            } catch (IOException e) {
                System.err.println("发生了 IOException: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

