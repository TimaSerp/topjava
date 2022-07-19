package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractController {
    private static final Logger log = getLogger(AbstractController.class);

    @GetMapping("/")
    public String root() {
        log.info("root");
        return "index";
    }
}
