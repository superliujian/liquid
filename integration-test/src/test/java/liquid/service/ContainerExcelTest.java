package liquid.service;

import liquid.excel.AbstractExcelService;
import liquid.persistence.domain.ContainerEntity;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        ContainerService containerService = new ContainerService();
        containerService.setAbstractExcelService(new AbstractExcelService<ContainerEntity>());

        containerService.importExcel(basedir + "/src/test/resources/container.xlsx");
    }
}
