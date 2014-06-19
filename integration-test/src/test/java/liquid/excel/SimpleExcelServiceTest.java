package liquid.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by redbrick9 on 6/11/14.
 */
public class SimpleExcelServiceTest {
    @Test
    public void testRead() {
        InputStream inputStream = null;
        SimpleExcelService service = null;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream("book.xlsx");
            service = new SimpleExcelService();
            service.read(inputStream);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testReadBig() {
        InputStream inputStream = null;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream("container.xlsx");
            SimpleExcelService service = new SimpleExcelService();
            service.read(inputStream);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
