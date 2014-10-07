package liquid.ops;

import liquid.config.JpaConfig;
import liquid.persistence.domain.CustomerEntity;
import liquid.persistence.repository.CustomerRepository;
import liquid.pinyin4j.PinyinHelper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 */
public class QueryNameGenerator {

    public void generate() throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();
        CustomerRepository customerRepository = (CustomerRepository) context.getBean(CustomerRepository.class);

//        int number = 0;
//        int size = 10;
//        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.ASC, "id"));
//        Page<CustomerEntity> page = customerRepository.findAll(pageRequest);
//        List<CustomerEntity> customers = page.getContent();


        FileWriter sql = new FileWriter("/tmp/qname.sql");
        String newLine = System.getProperty("line.separator");

        Iterable<CustomerEntity> customers = customerRepository.findAll();
        for (CustomerEntity customer : customers) {
            Long id = customer.getId();
            String customerName = customer.getName();
            String queryName = PinyinHelper.converterToFirstSpell(customerName) + ";" + customerName;
            System.out.println("UPDATE OPS_CUSTOMER SET Q_NAME='" + queryName + "' where ID=" + id + ";");
            sql.write("UPDATE OPS_CUSTOMER SET Q_NAME='" + queryName + "' where ID=" + id + ";" + newLine);
        }
        sql.close();
    }

    public static void main(String[] args) throws IOException {
        QueryNameGenerator queryNameGenerator = new QueryNameGenerator();
        queryNameGenerator.generate();
    }
}
