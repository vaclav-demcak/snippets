package vd.samples.springboot.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import vd.samples.springboot.config.FelixConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = PluginService.class)
class PluginServiceTests {

    @MockBean
    private SpringAwareBundleListener springAwareBundleListener;
    @MockBean
    private FelixConfiguration felixConfiguration;
    @Autowired
    private PluginService cut;

    @Test
    @DisplayName(
            "when BundleService is initialized, then the Felix framework is created using the Spring ApplicationReadyEvent")
    void testFelixFrameworkCreation() {
        assertAll(
                () -> assertNotNull(cut),
                () -> assertNotNull(cut.getFramework())
        );
    }

    @Test
    @DisplayName("When find package is called with a specific package, then subpackages are provided too")
    void testExtraPackageConfiguration() {
        String result = cut.findPackageNamesStartingWith(List.of("vd.samples.springboot"));
        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.contains("vd.samples.springboot.service")),
                () -> assertTrue(result.contains("vd.samples.springboot.commons.plugins.dto"))
        );
    }
}
