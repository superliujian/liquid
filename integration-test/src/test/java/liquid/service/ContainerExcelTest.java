package liquid.service;

import liquid.excel.AbstractExcelService;
import liquid.persistence.domain.ContainerEntity;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by redbrick9 on 6/15/14.
 */
public class ContainerExcelTest {
    @Test
    public void testImportExcel() throws IOException {
        ContainerService containerService = new ContainerService();
        containerService.setAbstractExcelService(new AbstractExcelService<ContainerEntity>());
        containerService.importExcel("/home/redbrick9/Repo/git/github/liquid/excel/src/test/resources/container.xlsx");
    }
}
