package app.holdingUnits;

import app.holdingUnits.containers.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test Preloader class.
 *
 * @version 1.0.0 24 Mar 2021
 * @author Aleksey Lakhanskii
 *
 */
class PreloaderTest {

    /**
     * Tests preloading. Parses test resource and checks some nodes existence.
     */
    @Test
    public void preloadTest() {
        GraphHolder.cleanUp();
        Preloader.preload("target/test-classes/smallTestData.csv");
        assertTrue(Node.checkExistence("км"));
        assertTrue(Node.checkExistence("пм"));
    }
}