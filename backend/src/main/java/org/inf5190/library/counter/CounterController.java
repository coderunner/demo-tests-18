package org.inf5190.library.counter;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CounterController {
    private long count = 0;

    @GetMapping("counter")
    Long getCount() {
        return this.count;
    }

    @PostMapping("counter")
    Long incrementCount() {
        return ++this.count;
    }

    @DeleteMapping("counter")
    void resetCount() {
        this.count = 0;
    }
}
