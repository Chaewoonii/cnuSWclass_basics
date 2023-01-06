package day0105.section07;

public interface Light {
    //상수필드
    public static final String TYPE = "LED";
    public String UNIT = "W";

    //추상 메소드
    public abstract void switchOn();
    public void switchOff(); //abstract키워드를 생략해도 추상메소드로 인식

    //default 메소드
    default void description(){
        System.out.println("전등을 구현합니다");
        privateMethod();
    }

    // private default 메소드
    private void privateMethod(){
        System.out.println("기본 private 메소드");
    }

    //static 메소드
    static String getLightName(int watValue){
        privateStaticMethod();
        return TYPE + " " + watValue + UNIT;
    }

    //private static 메소드
    private static void privateStaticMethod(){
        System.out.println("정적 private 메소드");
    }


}