package liquid.excel;

/**
 * Created by redbrick9 on 6/15/14.
 */
public interface CellTranslator<E> {
    void translate(E entity, String r, Object value);
}
