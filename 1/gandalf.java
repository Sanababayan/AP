import java.util.*;

public class gandalf {
    private static boolean sameChars(String firstStr, String secondStr) {
        char[] first = firstStr.toCharArray();
        char[] second = secondStr.toCharArray();
        Arrays.sort(first);
        Arrays.sort(second);
        return Arrays.equals(first, second);
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String[] output = str.split("-");//Now they are in an array called output
        //Arrays.stream(output).forEach(part -> System.out.println(part));
        Set<String> letter = new LinkedHashSet<String>();
        for (int i = 0; i < output.length; i++) {
            char[] chars = output[i].toCharArray();
            Arrays.sort(chars);
            letter.add(new String(chars));
        }
        //System.out.println(letter.size() + " letters must be sent to: " + letter);
        //for(int i = 0;i < letter.size();i++) {
        for (String element :letter) {
            for(int j = 0;j < output.length;j++) {
                //System.out.println(element + " " + output[j]);
                if(sameChars(element,output[j])) {
                    System.out.print("*" + output[j] + "* ");
                }
            }System.out.println();
        }
    }
}
