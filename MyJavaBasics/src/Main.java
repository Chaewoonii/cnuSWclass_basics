import java.util.Arrays;

public class Main {

    public static int[] solution(int[][] array){
        int[] answer = new int[array.length];

        for(int i=0; i<array.length; i++){
            int max=0;
            for(int j=0; j<array[i].length; j++){
                if (max < array[i][j]){
                    max = array[i][j];
                }
            }
            answer[i] = max;
        }

        return answer;
    }

    public static void main(String[] args) {
        int[][] array = {{1, 8, 5}, {2, 11, 5}, {3, 2, 1}};

        int[] answer = solution(array);
        System.out.println(Arrays.toString(answer));

    }
}