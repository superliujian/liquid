package liquid.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by redbrick9 on 7/1/14.
 */
public class DateUtilTest {

    @Test
    public void testStringOf() {
        assertEquals("Date to String", "2014-07-01 08:11", DateUtil.stringOf(new Date(1404173502609L)));
    }

    @Test
    public void testDateOf() {
        assertEquals("String to Date", 1380811800000L, DateUtil.dateOf("2013-10-03 22:50").getTime());
    }
}
