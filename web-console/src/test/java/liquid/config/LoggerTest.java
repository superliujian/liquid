package liquid.config;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by redbrick9 on 6/11/14.
 */
public class LoggerTest {
    private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void testLog() {
        logger.debug("Test logger.");
    }
}
