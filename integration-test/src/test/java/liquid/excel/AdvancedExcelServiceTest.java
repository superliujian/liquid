package liquid.excel;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by redbrick9 on 6/12/14.
 */
public class AdvancedExcelServiceTest {
    @Test
    public void testRead() {
        InputStream inputStream = null;
        AdvancedExcelService service = null;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream("book.xlsx");
            service = new AdvancedExcelService();
            service.read(inputStream);
        } catch (IOException | OpenXML4JException | SAXException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream)
                try { inputStream.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
