package com.meiqu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResources {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestResources.class);

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public String refresh() {
        return new TestCause().result();
    }
}