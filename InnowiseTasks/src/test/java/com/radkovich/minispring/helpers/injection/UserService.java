package com.radkovich.minispring.helpers.injection;

import com.radkovich.minispring.Component;

@Component
public class UserService {
    public String getUserName() {
        return "Test User";
    }
}
