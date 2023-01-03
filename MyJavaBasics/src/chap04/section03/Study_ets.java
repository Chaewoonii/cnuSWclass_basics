package chap04.section03;

public class Study_ets {

    public static boolean isEquals(String a, String b){
        boolean result = a.equals(b);
        return result;
    }
    public static void main(String[] args) {
        System.out.println(isEquals("A", "A"));
        System.out.println(isEquals("A", "a"));
        System.out.println(isEquals("A", null));

        int a = 0;
//   초기화; 조건; 증감
        for(int i=0; i<10; i++){
            a += i;
            System.out.println(a);
        }

    }

    public static boolean method(){
        boolean result=true;
        return result;
    }
}
