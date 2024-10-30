/*import java.math.BigInteger;
import java.util.Random;

public class RSA {
    public static void main(String[] args) {
        BigInteger p;
        BigInteger q;
        Random rand = new Random();
        
        // 指定要生成的素数的位数
        int bitLength = 512;

        // 生成随机素数 p 和 q
        p = BigInteger.probablePrime(bitLength, rand);
        q = BigInteger.probablePrime(bitLength, rand);
        
        System.out.println("p : " + p);
        System.out.println("q : " + q);
        
        BigInteger[] bi1 = rsa_CalculateA(p, q);
        System.out.println("公钥 : (" + bi1[1] + "," + bi1[0] + ")");
        System.out.println("私钥 : (" + bi1[2] + "," + bi1[0] + ")");
        
        BigInteger[] bi2 = EncyptionMessage(bi1[1], bi1[0]);
        System.out.println("随机生成m: " + bi2[1]);
        System.out.println("加密结果: " + bi2[0]);
        
        BigInteger decrypted = bi2[0].modPow(bi1[2], bi1[0]);
        System.out.println("解密结果: " + decrypted);
    }

    public static BigInteger[] rsa_CalculateA(BigInteger p, BigInteger q) { // 生成密钥
        BigInteger[] ret = new BigInteger[3]; // n = ret[0], e = ret[1], d = ret[2]
        BigInteger n = p.multiply(q);
        BigInteger phi_n = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        
        BigInteger e = BigInteger.valueOf(65537); // 使用一个常见的公钥值
        // 确保 e 和 phi_n 是互质的
        while (!gcd(e, phi_n).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.TWO); // 增加到下一个奇数
        }
        BigInteger d = e.modInverse(phi_n);
        ret[0] = n;
        ret[1] = e;
        ret[2] = d;
        return ret;
    }

    public static BigInteger[] EncyptionMessage(BigInteger e, BigInteger n) {
        BigInteger[] ret = new BigInteger[2]; // ret[0] 为加密信息
        BigInteger m;

        Random rand = new Random();
        // 生成一个随机消息 m，确保 m < n
        do {
            m = new BigInteger(512, rand);
        } while (m.compareTo(n) >= 0);
        
        ret[0] = m.modPow(e, n);
        ret[1] = m;
        return ret;
    }

    public static BigInteger gcd(BigInteger a, BigInteger b) { // 辗转相除法
        BigInteger c;
        while (!b.equals(BigInteger.ZERO)) {
            c = a.mod(b);
            a = b;
            b = c;
        }
        return a;
    }
}*/


import java.math.BigInteger;
import java.util.Random;

public class RSA{
    public static void main(String[] args){
        BigInteger p;
        BigInteger q;
        Random rand = new Random();
        int lenp , lenq;
        lenp = rand.nextInt(512);
        p = BigInteger.probablePrime(lenp, rand);         
        lenq = rand.nextInt(512);
        q = new BigInteger(lenq , rand);     
        System.out.println("p : " + p);
        System.out.println("q : " + q);      
        BigInteger[] bi1 = rsa_CalculateA(p, q);
        System.out.println("公钥 : (" + bi1[1] + "," + bi1[0] + ")");
        System.out.println("私钥 : (" + bi1[2] + "," + bi1[0] + ")");
        
        BigInteger[] bi2 = EncyptionMessage(bi1[1], bi1[0]);
        System.out.println("随机生成m: " + bi2[1]);
        System.out.println("加密结果: " + bi2[0]);
        
        BigInteger decrypted = bi2[0].modPow(bi1[2], bi1[0]);
        System.out.println("解密结果: " + decrypted);
        //gg
    }


    public static boolean pd_ss(BigInteger x){
        if(x.compareTo(BigInteger.valueOf(2)) < 0)
            return false;
        for(BigInteger i = BigInteger.valueOf(2); i.multiply(i).compareTo(x) <= 0; i = i.add(BigInteger.ONE)) {
            if(x.mod(i).equals(BigInteger.ZERO))
                return false;
        }
        return true;
    }

    public static BigInteger[] rsa_CalculateA(BigInteger p , BigInteger q){//生成密钥
        BigInteger[] ret = new BigInteger[3];//n = ret[0] , e = ret[1] , d = ret[2]
        BigInteger n = p.multiply(q);
        BigInteger phi_n = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        Random rand = new Random();
        int len = phi_n.bitLength();
        int rd = rand.nextInt(len);
        BigInteger e = BigInteger.probablePrime(len, rand);
        while (!gcd(e, phi_n).equals(BigInteger.ONE)) {
            e = BigInteger.probablePrime(len, rand);
        }
        BigInteger d = e.modInverse(phi_n);
        ret[0] = n;
        ret[1] = e;
        ret[2] = d;
        return ret;
    }

    public static BigInteger[] EncyptionMessage(BigInteger e , BigInteger n){
        BigInteger[] ret = new BigInteger[2];//ret[0] 
        BigInteger m;
        Random rand = new Random();
        int len;
        // 生成一个随机消息 m，确保 m < n
        do {
            len = rand.nextInt(512);
            m = new BigInteger(len, rand);
        } while (m.compareTo(BigInteger.ZERO) <= 0 || m.compareTo(n) >= 0);
        ret[0] = m.modPow(e , n);
        ret[1] = m;
        return ret;
    }

     public static BigInteger gcd(BigInteger a, BigInteger b) {// 辗转相除法
        BigInteger c = a.mod(b); 
        while (!c.equals(BigInteger.ZERO)) {
            a = b;
            b = c;
            c = a.mod(b);
        }
        return b;
    }
}