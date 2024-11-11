import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DES {
    public static void main(String[] args) {
        create();
    }

    public static void create(){
        ArrayList<int[]> list;
        list = CreateTest_Plaintext();
        for(int i = 0; i < list.size(); i++){
            if(i % 2 == 0)
                System.out.println("i = " + (i / 2 + 1));
            for(int j : list.get(i)){
                System.out.print(j);
            }
            System.out.println();
            if(i % 2 == 1)
                System.out.println();
        }
    }

    public static ArrayList<int[]> CreateTest_Plaintext(){
        ArrayList<int[]> list = new ArrayList<>();
        Random rand = new Random();
        for(int i = 1 ; i <= 64 ; ++i)
        {
            int[] arr = new int[64];
            for(int j = 0 ; j < 64 ; ++j)
            {
                arr[j] = rand.nextInt(2);
            }
            list.add(arr);
            int[] newarr = arr.clone();
            boolean[] vis = new boolean[64];
            for(int j = 0 ; j < i ; ++j)
            {
                int temp = rand.nextInt(64);
                while(vis[temp])
                {
                    temp = rand.nextInt(64);
                }
                vis[temp] = true;
                newarr[temp] ^= 1;
            }
            list.add(newarr);
        }
        return list;
    }

    public static ArrayList<int[]> CreateTest_Key(){
        ArrayList<int[]> list = new ArrayList<>();
        Random rand = new Random();
        for(int i = 1 ; i <= 64 ; ++i)
        {
            int[] arr = new int[64];
            for(int j = 0 ; j < 8 ; ++j)
            {
                int cnt = 0;
                for(int k = 0 ; k < 7 ; ++k)
                {
                    arr[j * 8 + k] = rand.nextInt(2);
                    if(arr[j * 8 + k] == 1)
                    ++cnt;
                }
                if(cnt % 2 == 0)
                arr[j * 8 + 7] = 1;
                else
                arr[j * 8 + 7] = 0; 
            }
            list.add(arr);
            int[] newarr = arr.clone();
            boolean[] vis = new boolean[64];
            for(int j = 0 ; j < i ; ++j)
            {
                int temp = rand.nextInt(64);
                while(vis[temp])
                {
                    temp = rand.nextInt(64);
                }
                vis[temp] = true;
                newarr[temp] ^= 1;
            }
            list.add(newarr);
        }
        return list;
    }
}
