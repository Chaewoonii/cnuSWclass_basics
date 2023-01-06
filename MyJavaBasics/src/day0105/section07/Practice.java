package day0105.section07;

public class Practice {
    public static void main(String[] args) {
        TV tv = new SmartTv();

        tv.descirbe("Smart");

        //showSize는 static메소드이므로 인터페이스에서 직접 불러오기가 가능하다.
        TV.showSize(10);

    }
}

