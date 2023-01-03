package chap01.section03;

public class Hello {
//    한줄 주석
    /*
    블록주석
    강의: 자바 입문
    Chapter01. 자바 시작하기
    */
    /*java docs 주석
    * Hello! 출력
    * @author 노지연
    * @param args
    * */
    public static void main(String[] args) {
        int x = 10;
        x = -x;
        x = +x;

        x++;
        x--;
        boolean isTrue = x == 11;
        // isTrue의 논리 부정 연산
        isTrue =!isTrue;
        System.out.println(isTrue);

    }

}
