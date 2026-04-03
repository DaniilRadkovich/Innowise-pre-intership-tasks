package com.radkovich.minispring.helpers.injection;

import com.radkovich.minispring.Autowired;
import com.radkovich.minispring.InitializingBean;
import com.radkovich.minispring.Component;

@Component
public class UserController implements InitializingBean {

    @Autowired
    private UserService userService;

    private String initMessage;
    private int initInvocationCount;
    private boolean dependencyInjectedDuringInitialization;

    public UserService getUserService() {
        return userService;
    }

    public String getInitMessage() {
        return initMessage;
    }

    public int getInitInvocationCount() {
        return initInvocationCount;
    }

    public boolean isDependencyInjectedDuringInitialization() {
        return dependencyInjectedDuringInitialization;
    }

    @Override
    public void afterPropertiesSet() {
        initInvocationCount++;
        dependencyInjectedDuringInitialization = userService != null;
        initMessage = "Controller initialized with user: " + userService.getUserName();
    }
}
