package day0105.section07;

public class LivingRoom implements Light {

    @Override
    public void switchOn() {
        System.out.println("거실 불을 켭니다");
    }

    @Override
    public void switchOff() {
        System.out.println("거실 불을 끕니다");
    }


}
