import java.util.ArrayList;
import java.util.Random;

public class DES {
    public static void main(String[] args) {
        // 进行海明重量分析
        analyze_A();
        analyze_B();
    }

    public static ArrayList<int[]> CreateTest_Plaintext(){
        ArrayList<int[]> list = new ArrayList<>();
        Random rand = new Random();
        for(int i = 1 ; i <= 64 ; ++i) {
            int[] arr = new int[64];
            for(int j = 0 ; j < 64 ; ++j) {
                arr[j] = rand.nextInt(2);
            }
            list.add(arr);
            int[] newarr = arr.clone();
            boolean[] vis = new boolean[64];
            for(int j = 0 ; j < i ; ++j) {
                int temp = rand.nextInt(64);
                while(vis[temp]) {
                    temp = rand.nextInt(64);
                }
                vis[temp] = true;
                newarr[temp] ^= 1; // 通过异或改变位
            }
            list.add(newarr);
        }
        return list;
    }

    public static ArrayList<int[]> CreateTest_Key(){
        ArrayList<int[]> list = new ArrayList<>();
        Random rand = new Random();
        for(int i = 1 ; i <= 64 ; ++i) {
            int[] arr = new int[64];
            for(int j = 0 ; j < 8 ; j++) {
                int cnt = 0;
                for(int k = 0 ; k < 7 ; k++) {
                    arr[j * 8 + k] = rand.nextInt(2);
                    if(arr[j * 8 + k] == 1)
                        ++cnt;
                }
                arr[j * 8 + 7] = (cnt % 2 == 0) ? 1 : 0; // 设置奇偶校验位
            }
            list.add(arr);
            int[] newarr = arr.clone();
            boolean[] vis = new boolean[64];
            for(int j = 0 ; j < i ; ++j) {
                int temp = rand.nextInt(64);
                while(vis[temp]) {
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
        for(int j = 0 ; j < 8 ; j++) {
            int cnt = 0;
            for(int k = 0 ; k < 7 ; k++) {
                fixedKey[j * 8 + k] = rand.nextInt(2);
                if(fixedKey[j * 8 + k] == 1)
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
            System.out.println("明文1加密后海明重量：" + hammingWeight(encrypt(m1, fixedKey)));
            System.out.println("明文2加密后海明重量：" + hammingWeight(encrypt(m2, fixedKey)));
            
        }
        //System.out.println("平均海明重量：" + (totalHammingWeight / (plaintexts.size() / 2)));
    }

    // 方法B：在固定明文的情况下分析密钥
    public static void analyze_B() {
        Random rand = new Random();
        int[] fixedPlaintext = new int[64];
        for(int j = 0 ; j < 64 ; ++j) {
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
            System.out.println("明文使用密钥1加密后海明重量：" + hammingWeight(encrypt(fixedPlaintext, k1)));
            System.out.println("明文使用密钥2加密后海明重量：" + hammingWeight(encrypt(fixedPlaintext, k2))); 
        }
    }

    // 假设的加密方法，返回明文（可替换为具体加密逻辑）
    public static int[] encrypt(int[] plaintext, int[] key) {
        // 这里可以实现实际的DES加密逻辑，当前返回明文作为占位
        return plaintext.clone();
    }

    // 计算海明重量
    public static int hammingWeight(int[] array) {
        int weight = 0;
        for (int bit : array) {
            weight += bit;
        }
        return weight;
    }
}
