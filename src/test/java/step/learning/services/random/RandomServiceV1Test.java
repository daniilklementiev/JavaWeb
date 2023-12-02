package step.learning.services.random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomServiceV1Test {

    @Test
    void seed() {
        RandomService randomService1 = new RandomServiceV1();
        RandomService randomService2 = new RandomServiceV1();
        String seed = "123";
        randomService1.seed(seed);
        randomService2.seed(seed);
        assertEquals(randomService1.randomHex(10), randomService2.randomHex(10), "Same data from same seed");

        randomService1.seed(seed + "1");
        randomService2.seed(seed + "2");
        assertNotEquals(randomService1.randomHex(10), randomService2.randomHex(10), "Different data from different seed");
    }

    @Test
    void randomHex() {
        RandomService randomService = new RandomServiceV1();
        for (int i = 0; i < 20; i++) {
            String hex = randomService.randomHex(i);
            assertEquals(i, randomService.randomHex(i).length(), "Random hex string length should be equal to the input");
            assertTrue(hex.matches("^[0-9A-Fa-f]*$"), "Random hex string should contain only hexodecimal characters");
        }



    }
}