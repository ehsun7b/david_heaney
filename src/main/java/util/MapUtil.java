package util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MapUtil {
    private MapUtil() {
    }

    public static <K, V> LinkedHashMap<K, V> sortByValue(Map<K, V> map, Comparator<? super V> comparator) {
        final ArrayList<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(comparator));

        final LinkedHashMap<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static <K, V> LinkedHashMap<K, V> topOfMap(Map<K, V> map, int count) {
        final LinkedHashMap<K, V> result = new LinkedHashMap<>();
        final Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        int counter = 0;

        while (iterator.hasNext() && counter++ < count) {
            final Map.Entry<K, V> next = iterator.next();
            result.put(next.getKey(), next.getValue());
        }

        return result;
    }
}
