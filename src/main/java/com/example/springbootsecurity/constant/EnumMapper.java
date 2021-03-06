package com.example.springbootsecurity.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumMapper {

    private Map<String, List<EnumValue>> factory = new HashMap<>();

    private List<EnumValue> toEnumValues(Class<? extends EnumModel> userRoleClass) {
        return Arrays
                .stream(userRoleClass.getEnumConstants())
                .map(EnumValue::new)
                .collect(Collectors.toList());
    }

    public void put(String key, Class<? extends EnumModel> e) {
        factory.put(key, toEnumValues(e));
    }

    // 모든 enum 타입을 가져오는 기능
    public Map<String, List<EnumValue>> getAll() {
        return factory;
    }

    // 지정한 enum만 가져오는 기능
    public Map<String, List<EnumValue>> get(String keys) {
        return Arrays
                .stream(keys.split(","))
                .collect(Collectors.toMap(Function.identity(), key -> factory.get(key)));
    }

}
