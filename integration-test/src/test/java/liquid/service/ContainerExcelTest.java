package liquid.service;

import liquid.config.JpaConfig;
import liquid.excel.AbstractExcelService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * Created by redbrick9 on 6/15/14.
 */
public class ContainerExcelTest {
    private static final Logger logger = LoggerFactory.getLogger(ContainerExcelTest.class);

    private static String basedir;

    @BeforeClass
    public static void beforeClass() {
        basedir = System.getProperty("basedir");
        logger.info("basedir: {}", basedir);
    }

    @Test
    public void testImportExcel() throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.register(LocationService.class);
        context.register(ContainerService.class);
        context.register(AbstractExcelService.class);
        context.refresh();

        ContainerService containerService = context.getBean(ContainerService.class);

        containerService.importExcel(basedir + "/src/test/resources/container.xlsx");
    }
}
