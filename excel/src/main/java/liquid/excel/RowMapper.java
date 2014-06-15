package liquid.excel;


import org.apache.poi.ss.usermodel.Row;

/**
 * Created by redbrick9 on 6/12/14.
 */
public interface RowMapper<T> {
    T mapFromAttributes(Row row);
}
