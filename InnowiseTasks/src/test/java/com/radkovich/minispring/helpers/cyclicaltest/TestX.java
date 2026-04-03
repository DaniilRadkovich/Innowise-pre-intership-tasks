package com.radkovich.minispring.helpers.cyclicaltest;

import com.radkovich.minispring.Autowired;
import com.radkovich.minispring.Component;

@Component
public class TestX {
    @Autowired
    private TestY testY;
}
