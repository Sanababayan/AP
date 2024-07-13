import java.util.*;

public class palindrome {
    static int palindromeTest(List<Integer> arr, int n) {
        int flag = 0;
        for (int i = 0; i <= n / 2 && n != 0; i++) {
            if (arr.get(i).intValue() != arr.get(n - i - 1).intValue()) {
                flag = 1;
                break;
            }
        }
        if (flag == 1)
            return 4;
            //System.out.println("NO");
        else
            return 2;
            //System.out.println("YES");
    }
    static int palindromeBazgashti(int[] mainArray) {
        int arrSize = mainArray.length;
            for(int i = 0 ; i <= arrSize / 2 ; i++) {
                if(mainArray[i] != mainArray[arrSize - i - 1]) {
                    List<Integer> list1 = new ArrayList<Integer>();
                    List<Integer> list2 = new ArrayList<Integer>();
                    for(Integer text : mainArray) {
                        list1.add(text);
                    }
                    list1.removeAll(Collections.singleton(mainArray[arrSize - i - 1]));
                    if(palindromeTest(list1 , list1.size()) == 2) {
                        System.out.println("YES");
                        return 1;
                    }
                    else {
                        for(Integer text : mainArray) {
                            list2.add(text);
                        }
                        list2.removeAll(Collections.singleton(mainArray[i]));
                        if(palindromeTest(list2 , list2.size()) == 2) {
                            System.out.println("YES");
                            return 1;
                        }
                        else {
                            System.out.println("NO");
                            return 1;
                        }
                    }
                }
            }
            System.out.println("YES");
    return 1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            int arr_size = 0;
            if (sc.hasNextInt()) {
                arr_size = sc.nextInt();
            }
            int[] arr = new int[arr_size];
            for (int j = 0; j < arr_size; j++) {
                if (sc.hasNextInt()) {
                    arr[j] = sc.nextInt();
                }
            }
            palindromeBazgashti(arr);
        }
        sc.close();
    }
}
