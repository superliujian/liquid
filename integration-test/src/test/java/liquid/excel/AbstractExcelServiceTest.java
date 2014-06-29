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
            service.extract(inputStream, Object.class, new RowMapper<Object>() {
                @Override
                public void translate(Object entity, String r, Object value) {
                    switch (r.substring(0, 1)) {
                        case "A": // No.
                            System.out.print(value);
                            break;
                        case "B": // biccode
                            System.out.print(value);
                            break;
                        case "C":
                            System.out.print(value);
                            break;
                        case "D":
                            System.out.print(value);
                            break;
                        case "E":
                            System.out.print(value);
                            break;
                        case "F":
                            System.out.print(value);
                            break;
                        case "G":
                            System.out.print(value);
                            break;
                        case "H":
                            System.out.print(value);
                            break;
                        case "I":
                            System.out.print(value);
                            break;
                        case "J":
                            System.out.print(value);
                            break;
                        case "K":
                            System.out.print(value);
                            break;
                        case "L":
                            System.out.print(value);
                            break;
                        case "M":
                            System.out.print(value);
                            break;
                        case "N":
                            System.out.print(value);
                            break;
                        case "O":
                            System.out.print(value);
                            break;
                        case "P":
                            System.out.print(value);
                            break;
                        case "Q":
                            System.out.print(value);
                            break;
                        case "R":
                            System.out.print(value);
                            break;
                        case "S":
                            System.out.print(value);
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public Boolean validate(Object entity) {
                    return null;
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
