package com.programmers.java.collectionsStudy;

import com.programmers.java.iterator.MyIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class MyCollection<T> {
    private List<T> list;

    public MyCollection(List<T> list){
        this.list = list;
    }

    public void foreach(Consumer<T> consumer){
        for(int i=0; i <list.size(); i++){
            //무언가 작업 수행
            T data = list.get(i);
            //list의 data로 무언가를 하고, 리턴은 없음
            consumer.accept(data);
        }
    }

    //T타입으로부터 U타입으로 반환되는 Function을 인자로 받음
    //<U>: U는 클래스에 존재하지 않는 type임. 앞에 <U>를 붙여 이 함수에서만 U를 제너릭으로 사용해라 라는 의미로 사용
    //U는 이 메소드에서만 유효한 제너릭 타입이 됨.
    public <U> MyCollection<U> map(Function<T, U> function){
        List<U> newList = new ArrayList<>();
        //Function에 d를 apply(d): T가 U 타입으로 변환됨, 그 결과를 newList에 추가
        foreach(d -> newList.add(function.apply(d)));
        return new MyCollection<>(newList);
    }

    //필터링하는 기능
    public MyCollection<T> filter(Predicate<T> predicate){
        List<T> newList = new ArrayList<>();
        //조건 확인 후 newList에 추가
        foreach(d -> {
            if(predicate.test(d)) newList.add(d);
        });
        return new MyCollection<>(newList);
    }

    public int size(){
        return list.size();
    }

    //Iterator 직접 만들기
    public MyIterator<T> iterator(){
        //익명클래스 MyIterator를 반환
        return new MyIterator<T>() {
            //익명클래스의 필드
            private int index = 0;

            @Override
            public boolean hasNext() {
                //인덱스가 리스트 사이즈보다 작으면 True
                return index < list.size();
            }

            @Override
            public T next() {
                //list의 index번째에 있는 값을 가져오고 인덱스를 다음으로 넘김.(+1)
                return list.get(index++);
            }
        };
    }
}
