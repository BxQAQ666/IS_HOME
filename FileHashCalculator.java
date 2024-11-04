/*import com.sun.jdi.connect.Connector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FileHashCalculator {
    public static HashMap<byte[], ArrayList<String>> HashValue = new HashMap<>();

    public static void getFilepath(String path) throws IOException {
        String filename;// 文件名
        File file = new File(path);
        File[] files = file.listFiles();// 文件夹下的所有文件或文件夹
        if (files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                getFilepath(files[i].getAbsolutePath());// 目录，则递归文件夹！！！
            } else {
                filename = files[i].getName();
                String strFileName = files[i].getAbsolutePath();
                System.out.println(strFileName);
                byte[] temp = getHash(strFileName);
                System.out.println(temp);
                if (HashValue.containsKey(temp)) {
                    boolean same = false;
                    for (String s : HashValue.get(temp)) {// 判断hashvalue相同的文件是否文件内容也相同，内容相同则输出内容相同，否则将该地址加入该hashvalue的key中
                        if (CompareText(s, strFileName)) {
                            System.out.println("地址为：" + s + "与地址为" + strFileName + "内容一致");
                            same = true;
                            break;
                        }
                    }
                    if (!same) {
                        ArrayList<String> tp = HashValue.get(temp);
                        tp.add(strFileName);
                        HashValue.put(temp, tp);
                    }
                } else {
                    ArrayList<String> tp = new ArrayList<>();
                    tp.add(strFileName);
                    HashValue.put(temp, tp);
                }
            }
        }
    }

    public static boolean CompareText(String path1, String path2) throws FileNotFoundException, IOException {
        BufferedReader in1 = new BufferedReader(new FileReader(path1));
        BufferedReader in2 = new BufferedReader(new FileReader(path2));
        String s1, s2;
        do {
            s1 = in1.readLine();
            s2 = in2.readLine();
            if (!s1.equals(s2))
                return false;
        } while (s1 != null);
        return true;
    }

    public static byte[] getHash(String filepath) throws IOException {
        byte[] hashValue;
        try (BufferedReader in = new BufferedReader(new FileReader(filepath))) {
            String text = "";
            String str;
            hashValue = null;
            while ((str = in.readLine()) != null) {
                text += str;
            }
            try {
                // 步骤 1: 创建 MessageDigest 对象，使用 SHA-256 算法
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                // 步骤 2: 使用 update() 方法传入数据
                md.update(text.getBytes());
                // 步骤 3: 调用 digest() 方法计算哈希值
                hashValue = md.digest();
                return hashValue;
            } catch (NoSuchAlgorithmException e) {
                System.err.println("SHA-256 algorithm not available.");
                e.printStackTrace();
            }
        }
        return hashValue;
    }

    public static void main(String[] args) {
        try {
            //String path;
            //Scanner sc = new Scanner(System.in);
            //path = sc.nextLine()
            getFilepath("C:\\Users\\hello\\Desktop\\ljh\\same");
        } catch (IOException e) {
            System.err.println("发生了 IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }

}*/
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

public class FileHashCalculator {
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
        try {
            getFilepath("C:\\");
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
