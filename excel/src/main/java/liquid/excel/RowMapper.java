package liquid.excel;


import org.apache.poi.ss.usermodel.Row;

/**
 * Created by redbrick9 on 6/12/14.
 */
public interface RowMapper<E> extends CellTranslator<E> {
    Boolean validate(E entity);
}
