package ru.sinitsynme.analyticspro.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListUtils {

    public static List<List<Object>> mapToListOfPairs(Map<?, ?> map) {
        List<List<Object>> list = new ArrayList<>();
        map.forEach((k, v) -> list.add(List.of(k, v)));
        return list;
    }


}
