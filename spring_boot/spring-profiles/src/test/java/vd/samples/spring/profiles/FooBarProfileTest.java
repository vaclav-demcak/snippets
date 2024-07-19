package vd.samples.spring.profiles;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"foo", "bar"})
public class FooBarProfileTest {

    @Test
    void test() {
    }

}
