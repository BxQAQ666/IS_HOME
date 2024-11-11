import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Random;

public class DES {
    public static void main(String[] args) {
        // 进行海明重量分析
        analyze_A();
        analyze_B();
    }

    public static ArrayList<int[]> CreateTest_Plaintext() {
        ArrayList<int[]> list = new ArrayList<>();
        Random rand = new Random();
        for (int i = 1; i <= 64; ++i) {
            int[] arr = new int[64];
            for (int j = 0; j < 64; ++j) {
                arr[j] = rand.nextInt(2);
            }
            list.add(arr);
            int[] newarr = arr.clone();
            boolean[] vis = new boolean[64];
            for (int j = 0; j < i; j++) {
                int temp = rand.nextInt(64);
                while (vis[temp]) {
                    temp = rand.nextInt(64);
                }
                vis[temp] = true;
                newarr[temp] ^= 1; // 通过异或改变位
            }
            list.add(newarr);
        }
        return list;
    }

    public static ArrayList<int[]> CreateTest_Key() {
        ArrayList<int[]> list = new ArrayList<>();
        Random rand = new Random();
        for (int i = 1; i <= 64; ++i) {
            int[] arr = new int[64];
            for (int j = 0; j < 8; j++) {
                int cnt = 0;
                for (int k = 0; k < 7; k++) {
                    arr[j * 8 + k] = rand.nextInt(2);
                    if (arr[j * 8 + k] == 1)
                        ++cnt;
                }
                arr[j * 8 + 7] = (cnt % 2 == 0) ? 1 : 0; // 设置奇偶校验位
            }
            list.add(arr);
            int[] newarr = arr.clone();
            boolean[] vis = new boolean[64];
            for (int j = 0; j < i; j++) {
                int temp = rand.nextInt(64);
                while (vis[temp]) {
                    temp = rand.nextInt(64);
                }
                vis[temp] = true;
                newarr[temp] ^= 1; // 通过异或改变位
            }
            list.add(newarr);
        }
        return list;
    }

    // 方法A：在固定密钥的情况下分析明文
    public static void analyze_A() {
        Random rand = new Random();
        int[] fixedKey = new int[64];
        for (int j = 0; j < 8; j++) {
            int cnt = 0;
            for (int k = 0; k < 7; k++) {
                fixedKey[j * 8 + k] = rand.nextInt(2);
                if (fixedKey[j * 8 + k] == 1)
                    ++cnt;
            }
            fixedKey[j * 8 + 7] = (cnt % 2 == 0) ? 1 : 0; // 设置奇偶校验位
        }
        System.out.println("固定密钥: ");
        for (int bit : fixedKey) {
            System.out.print(bit);
        }
        System.out.println();

        ArrayList<int[]> plaintexts = CreateTest_Plaintext();

        for (int i = 0; i < plaintexts.size(); i += 2) {
            int[] m1 = plaintexts.get(i);
            int[] m2 = plaintexts.get(i + 1);
            System.out.println("差分海明重量：" + (i / 2 + 1));
            System.out.println("明文1: ");
            for (int b : m1) {
                System.out.print(b);
            }
            System.out.println();

            System.out.println("明文2: ");
            for (int b : m2) {
                System.out.print(b);
            }
            System.out.println();

            byte[] encryptedM1 = encrypt(m1, fixedKey);
            byte[] encryptedM2 = encrypt(m2, fixedKey);
            System.out.println("明文1加密后海明重量：" + hammingWeight(encryptedM1));
            System.out.println("明文2加密后海明重量：" + hammingWeight(encryptedM2));
        }
    }

    // 方法B：在固定明文的情况下分析密钥
    public static void analyze_B() {
        Random rand = new Random();
        int[] fixedPlaintext = new int[64];
        for (int j = 0; j < 64; ++j) {
            fixedPlaintext[j] = rand.nextInt(2);
        }
        System.out.println("固定明文: ");
        for (int bit : fixedPlaintext) {
            System.out.print(bit);
        }
        System.out.println();

        ArrayList<int[]> Key = CreateTest_Key();

        for (int i = 0; i < Key.size(); i += 2) {
            int[] k1 = Key.get(i);
            int[] k2 = Key.get(i + 1);
            System.out.println("差分海明重量：" + (i / 2 + 1));
            System.out.println("密钥1: ");
            for (int b : k1) {
                System.out.print(b);
            }
            System.out.println();

            System.out.println("密钥2: ");
            for (int b : k2) {
                System.out.print(b);
            }
            System.out.println();

            byte[] encryptedWithK1 = encrypt(fixedPlaintext, k1);
            byte[] encryptedWithK2 = encrypt(fixedPlaintext, k2);
            System.out.println("明文使用密钥1加密后海明重量：" + hammingWeight(encryptedWithK1));
            System.out.println("明文使用密钥2加密后海明重量：" + hammingWeight(encryptedWithK2));
        }
    }

    // 使用 EncryptDES 进行加密 DES算法来源：https://blog.csdn.net/ToBeMaybe_/article/details/105272228
    public static byte[] encrypt(int[] plaintext, int[] key) {
        StringBuilder textBuilder = new StringBuilder();
        for (int bit : plaintext) {
            textBuilder.append(bit);
        }
        String content = textBuilder.toString();

        // 将密钥转为字符串
        StringBuilder keyBuilder = new StringBuilder();
        for (int bit : key) {
            keyBuilder.append(bit);
        }
        String keyString = keyBuilder.toString();

        // 执行 DES 加密
        return EncryptDES.desEncrypt(content, keyString);
    }

    // 计算海明重量
    public static int hammingWeight(byte[] array) {
        int weight = 0;
        for (byte b : array) {
            for (int i = 0; i < 8; i++) {
                weight += (b >> i) & 1; // 计算每个字节的位
            }
        }
        return weight;
    }

    // 加密实现类
    public static class EncryptDES {
        private static final String KEY = "Hello Ketty";

        /**
         * DES 加密
         * @param content
         * @param key
         * @return
         */
        public static byte[] desEncrypt(String content, String key) {
            try {
                KeyGenerator kGen = KeyGenerator.getInstance("DES");
                kGen.init(56, new SecureRandom(key.getBytes()));
                SecretKey secretKey = kGen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "DES");
                Cipher cipher = Cipher.getInstance("DES"); // 创建密码器
                byte[] byteContent = content.getBytes("utf-8");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec); // 初始化
                return cipher.doFinal(byteContent); // 加密
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
