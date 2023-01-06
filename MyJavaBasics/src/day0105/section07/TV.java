package day0105.section07;

public interface TV {
    public static final String UNIT = "inch";

    default void descirbe(String name){
        System.out.println(getName(name)+"입니다");
    }

    private String getName(String name){
        return name + " TV";
    }

    static void showSize(int inch){
        System.out.println("TV 사이즈는 "+getSize(inch)+" 입니다.");
    }

    private static String getSize(int inch){
        return inch + UNIT;
    }

}
