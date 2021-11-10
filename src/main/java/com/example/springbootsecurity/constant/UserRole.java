package com.example.springbootsecurity.constant;

// Enum을 사용하면서 얻을 수 있는 이점
/*
    1.코드가 단순해지며, 가독성이 좋다.
    2.인스턴스 생성과 상속을 방지하여 상수값의 타입안정성이 보장된다.
    3.enum class를 사용해 새로운 상수들의 타입을 정의함으로 정의한 타입이외의 타입을 가진 데이터값을 컴파일시 체크한다.
    4.키워드 enum을 사용하기 때문에 구현의 의도가 열거임을 분명하게 알 수 있다.
 */
public enum UserRole implements EnumModel {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_ADMIN");

    private final String role_user;

    UserRole(String role_user) {
        this.role_user = role_user;
    }

    // Returns the name of this enum constant, exactly as declared in its enum declaration.
    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return role_user;
    }
}
