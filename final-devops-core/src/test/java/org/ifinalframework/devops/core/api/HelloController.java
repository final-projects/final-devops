package org.ifinalframework.devops.core.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    String hello(String world) {
        return "hello " + world;
    }

}
