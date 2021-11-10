package com.example.springbootsecurity.controller;

import com.example.springbootsecurity.constant.EnumMapper;
import com.example.springbootsecurity.constant.UserRole;
import com.example.springbootsecurity.dto.EnumValue;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor // 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 준다.
public class EnumController {

    private final EnumMapper enumMapper;

    @GetMapping("/enum")
    public Map<String, Object> getEnum() {
        Map<String, Object> enums = new LinkedHashMap<>();
        Class userRole = UserRole.class;
        enums.put("userRole", userRole.getEnumConstants());

        return enums;
    }

    @GetMapping("/mapper")
    public Map<String, List<EnumValue>> getEnumValue() {
        return enumMapper.getAll();
    }

}
