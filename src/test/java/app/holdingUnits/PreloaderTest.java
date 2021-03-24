package app.holdingUnits;

import app.holdingUnits.containers.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreloaderTest {

    @Test
    public void preloadTest() {
        Preloader.preload("target/test-classes/smallTestData.csv");
        assertTrue(Node.checkExistence("км"));
        assertTrue(Node.checkExistence("пм"));
    }
}