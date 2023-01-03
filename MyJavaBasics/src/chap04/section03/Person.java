package chap04.section03;

public class Person {

    //속성
    private String name;
    private int age;

    //매개변수가 없는 기본 생성자
    public void Person(){}

    //매개변수가 있는 생성자
    public void Person(String name, int age){
        //생성과 동시에 필드 초기화
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    //나이가 같은지 결과를 반환하는 메소드
    public boolean isEqualAge(int age1, int age2){
        boolean result;

        if (age1 == age2){
            result = true;
        }else{
            result = false;
        }
        return result;
    }


}
