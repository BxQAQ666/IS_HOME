import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DES {
    public static void main(String[] args) {
        ArrayList<int[]> list;
        list = CreateTest();
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

    public static ArrayList<int[]> CreateTest(){
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
}
