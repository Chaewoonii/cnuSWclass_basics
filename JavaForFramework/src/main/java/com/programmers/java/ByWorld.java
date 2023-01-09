package com.programmers.java;

public class ByWorld {
    public static void main(String[] args) {
        //System.out.println("Bye World")를 리팩토링: 변수로 빼내기
        String byeWorld = "Bye World";
        System.out.println(byeWorld);


        //Ctrl+Alt+T:리팩토링: doSomethig이라는 메소드로 추출
        //String bye = "Bye World"를 리팩토링: 파라미터로 빼내
        doSomething("Bye World");


        /*Call by value, Call by reference*/
        int a = 100;
        doSomething2(a);
        System.out.println(a); // 100
        // int a로 선언한 변수와
        // 메소드 안의 a 변수는 다른 주소를 가지기 때문
        // 메소드 안의 a 변수는 지역변수.

        Int intA = new Int();
        doSomething3(intA);
        System.out.println(intA.a); //200
        //intA는 Int 클래스의 인스턴스이고, 주소값을 가진다.
        //매개변수로 intA, 주소값이 전달되고, doSomething3메소드의 지역변수 또한 동일한 주소를 갖게 된다.
        //따라서 intA의 필드 a에 직접적인 영향을 미치게 되고, 200이 출력된다.

        /*Constant Pool*/
        String str = "";
        for (int i = 0; i < 2; i++) {
            str += i;
        }
        /*
        * Constant pool에 "", "0", "01" 세 개의 String이 고정된 값(Constant)로 만들어진다.
        * String에 변화가 있을 경우 새로운 String으로 constant pool에 만들어진다.
        * 다른 변수에 똑같은 문자열을 초기화할 경우 constant pool에서 동일한 주소를 참조하게 된다.
        * */
        String str1 = "Hello world";
        String str2 = "Hello wolrd";
        System.out.println(str1 == str2); // True

        /*
        따라서, String 변수에는 +=연산을 사용하지 않음(변화할때마다 Constant pool에 새로 만들어지기 때문)
        StringBuffer를 이용하여 String모든 변화를 준 후 Constant pool에 추가하는 방식으로 사용
        StringBuffer과 StringBuilder의 차이는 검색하기
        * */
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++){
            sb.append(i);
        }
        System.out.println(sb.toString()); // 0123456789출력
        //0123456789가 constant pool에 한 번 등록되고 사용된다.

    }

    private static void doSomething(String message) {
        //리포멧팅(Ctrl+Alt+L): for(int i=0;i<10;i++)
        for (int i = 0; i < 10; i++) {
            System.out.println(message);
        }
    }

    private static void doSomething2(int a) {
        a *= 2;
    }

    private static void doSomething3(Int a) {
        a.a *= 2;
    }

}

class Int {
    int a = 100;
}
