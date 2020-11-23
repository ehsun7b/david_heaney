package util;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MapUtilTest {

    @Test
    void sortByValueInteger() {
        final Map<String, Integer> result = MapUtil.sortByValue(Map.of("Apple", 10, "Orange", 20, "Banana", 15), Integer::compareTo);
        assertThat(result.size()).isEqualTo(3);
        final Iterator<String> iterator = result.keySet().iterator();
        assertThat(iterator.next()).isEqualTo("Apple");
        assertThat(iterator.next()).isEqualTo("Banana");
        assertThat(iterator.next()).isEqualTo("Orange");
    }

    @Test
    void sortByValueDouble() {
        final Map<String, Double> result = MapUtil.sortByValue(Map.of("Apple", 1.0, "Orange", 2.0, "Banana", 1.5), Double::compareTo);
        assertThat(result.size()).isEqualTo(3);
        final Iterator<String> iterator = result.keySet().iterator();
        assertThat(iterator.next()).isEqualTo("Apple");
        assertThat(iterator.next()).isEqualTo("Banana");
        assertThat(iterator.next()).isEqualTo("Orange");
    }

    @Test
    void sortByValueString() {
        final Map<String, String> result = MapUtil.sortByValue(Map.of("Apple", "X", "Orange", "A", "Banana", "C"), String::compareTo);
        assertThat(result.size()).isEqualTo(3);
        final Iterator<String> iterator = result.keySet().iterator();
        assertThat(iterator.next()).isEqualTo("Orange");
        assertThat(iterator.next()).isEqualTo("Banana");
        assertThat(iterator.next()).isEqualTo("Apple");
    }

    @Test
    void topOfMap_count0Size3() {
        final Map<String, Double> result = MapUtil.topOfMap(Map.of("Apple", 1.0, "Orange", 2.0, "Banana", 1.5), 0);
        assertThat(result).isEqualTo(Map.of());
    }

    @Test
    void topOfMap_count0Size0() {
        final Map<String, Double> result = MapUtil.topOfMap(Map.of(), 0);
        assertThat(result).isEqualTo(Map.of());
    }

    @Test
    void topOfMap_count2Size3() {
        final Map<String, Integer> map = new LinkedHashMap<>();
        map.put("one", 1);
        map.put("three", 3);
        map.put("two", 2);
        final Map<String, Integer> result = MapUtil.topOfMap(map, 2);
        final Map<String, Integer> expectedResult = new LinkedHashMap<>();
        expectedResult.put("one", 1);
        expectedResult.put("three", 3);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void topOfMap_count4Size3() {
        final Map<String, Integer> map = new LinkedHashMap<>();
        map.put("one", 1);
        map.put("three", 3);
        map.put("two", 2);
        final Map<String, Integer> result = MapUtil.topOfMap(map, 4);
        final Map<String, Integer> expectedResult = new LinkedHashMap<>();
        expectedResult.put("one", 1);
        expectedResult.put("three", 3);
        expectedResult.put("two", 2);
        assertThat(result).isEqualTo(expectedResult);
    }
}
