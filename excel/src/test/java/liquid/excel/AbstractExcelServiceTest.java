package liquid.excel;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by redbrick9 on 6/15/14.
 */
public class AbstractExcelServiceTest {

    @Test
    public void testReadBig() {
        InputStream inputStream = null;
        AbstractExcelService<Object> service = null;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream("container.xlsx");
            service = new AbstractExcelService();
            service.extract(inputStream, Object.class, new CellTranslator<Object>() {
                @Override
                public boolean translate(Object entity, String r, Object value) {
                    switch (r.substring(0, 1)) {
                        case "A": // No.
                            System.out.print(value);
                            return true;
                        case "B": // biccode
                            System.out.print(value);
                            return true;
                        case "C":
                            System.out.print(value);
                            return true;
                        case "D":
                            System.out.print(value);
                            return true;
                        case "E":
                            System.out.print(value);
                            return true;
                        case "F":
                            System.out.print(value);
                            return true;
                        case "G":
                            System.out.print(value);
                            return true;
                        case "H":
                            System.out.print(value);
                            return true;
                        case "I":
                            System.out.print(value);
                            return true;
                        case "J":
                            System.out.print(value);
                            return true;
                        case "K":
                            System.out.print(value);
                            return true;
                        case "L":
                            System.out.print(value);
                            return true;
                        case "M":
                            System.out.print(value);
                            return true;
                        case "N":
                            System.out.print(value);
                            return true;
                        case "O":
                            System.out.print(value);
                            return true;
                        case "P":
                            System.out.print(value);
                            return true;
                        case "Q":
                            System.out.print(value);
                            return true;
                        case "R":
                            System.out.print(value);
                            return true;
                        case "S":
                            System.out.print(value);
                            return true;
                        default:
                            return true;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream)
                try { inputStream.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
