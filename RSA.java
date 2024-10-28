
import java.math.BigInteger;
import java.util.Random;

public class RSA{
    public static void main(String[] args){
        BigInteger p;
        BigInteger q;
        Random rand = new Random();
        int lenp , lenq;
        lenp = rand.nextInt();
        p = new BigInteger(lenp , rand);
        while(pd_ss(p))
        {
            lenp = rand.nextInt();
            p = new BigInteger(lenp , rand);           
        }
        lenq = rand.nextInt();
        q = new BigInteger(lenq , rand);
        while(pd_ss(q))
        {
            lenq = rand.nextInt();
            q = new BigInteger(lenq , rand);           
        }
        BigInteger[] bi1 = new BigInteger[3];
        bi1 = rsa_CalculateA(p, q);
        BigInteger[] bi2 = new BigInteger[2];
        bi2 = EncyptionMessage(bi1[1] , bi1[0]);
        BigInteger ans = bi2[0].modPow(bi1[2], bi1[0]);
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
        BigInteger e = new BigInteger(rd, rand);
        while(gcd(e, phi_n).equals(BigInteger.ONE))
        {
            rd = rand.nextInt(len);
            e = new BigInteger(rd, rand);
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
        len = rand.nextInt();
        m = new BigInteger(len , rand);
        while(pd_ss(m))
        {
            len = rand.nextInt();
            m = new BigInteger(len , rand);           
        }
        ret[0] = m.modPow(e , n);
        ret[1] = m;
        return ret;
    }

     public static BigInteger gcd(BigInteger a, BigInteger b) {// 辗转相除法
        BigInteger c = a.mod(b); 
        while (c.equals(BigInteger.ZERO)) {
            a = b;
            b = c;
            c = a.mod(b);
        }
        return b;
    }
}