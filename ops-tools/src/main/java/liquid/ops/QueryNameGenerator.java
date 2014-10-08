package liquid.ops;

import liquid.config.JpaConfig;
import liquid.persistence.repository.CustomerRepository;
import liquid.persistence.repository.LocationRepository;
import liquid.pinyin4j.PinyinHelper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.repository.CrudRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public class QueryNameGenerator {

    public void generate() throws IOException, NoSuchFieldException, IllegalAccessException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();
        CustomerRepository customerRepository = (CustomerRepository) context.getBean(CustomerRepository.class);
        LocationRepository locationRepository = (LocationRepository) context.getBean(LocationRepository.class);

        generate(customerRepository, "/tmp/customer_qname.sql", "OPS_CUSTOMER");
        generate(locationRepository, "/tmp/location_qname.sql", "OPS_LOCATION");
    }

    public <T extends CrudRepository> void generate(T repository, String fileName, String tableName) throws IOException {
        FileWriter sql = new FileWriter(fileName);
        String newLine = System.getProperty("line.separator");
        Iterable<Object> entities = repository.findAll();
        for (Object entity : entities) {

            try {
                Long id = (Long) entity.getClass().getMethod("getId").invoke(entity);
                String name = (String) entity.getClass().getMethod("getName").invoke(entity);
                String queryName = PinyinHelper.converterToFirstSpell(name) + ";" + name;
                sql.write(String.format("UPDATE %s SET Q_NAME='%s' where ID=%s;" + newLine, tableName, queryName, id));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        sql.close();
    }

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        QueryNameGenerator queryNameGenerator = new QueryNameGenerator();
        queryNameGenerator.generate();
    }
}
