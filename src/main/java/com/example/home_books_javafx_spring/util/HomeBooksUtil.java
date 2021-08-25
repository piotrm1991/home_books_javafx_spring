package com.example.home_books_javafx_spring.util;

import java.util.List;
import java.util.stream.IntStream;

public class HomeBooksUtil {
    public static <EntityClass> void setTopItem(List<EntityClass> t, int position) {
        t.add(0, t.remove(position));
    }

    public static <EntityClass> int findPosition(List<EntityClass> t, EntityClass entity) {
        int position = IntStream.range(0, t.size()).filter(i -> entity.equals(t.get(i))).findFirst().orElse(-1);
        return position;
    }

}
