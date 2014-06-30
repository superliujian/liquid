package liquid.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquid.container.service.ContainerService;
import liquid.container.domain.ExcelFileInfo;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by redbrick9 on 6/27/14.
 */
public class ContainerServiceTest {

    @Test
    public void testWriteMetadata() throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ContainerService.class);
        context.refresh();

        ContainerService containerService = context.getBean(ContainerService.class);

        ExcelFileInfo[] excelFileInfos = new ExcelFileInfo[2];
        ExcelFileInfo excelFileInfo = new ExcelFileInfo();
        excelFileInfo.setName("Test1.xlsx");
        excelFileInfo.setModifiedDate(System.currentTimeMillis());
        excelFileInfo.setState(ExcelFileInfo.State.UPLOADED);
        excelFileInfos[0] = excelFileInfo;

        excelFileInfo = new ExcelFileInfo();
        excelFileInfo.setName("Test2.xlsx");
        excelFileInfo.setModifiedDate(System.currentTimeMillis());
        excelFileInfo.setState(ExcelFileInfo.State.IMPORTED);
        excelFileInfos[1] = excelFileInfo;
        containerService.writeMetadata(excelFileInfos);

        String content = "[{\"name\":\"Test1.xlsx\",\"modifiedDate\":1403828428969,\"state\":\"UPLOADED\"},{\"name\":\"Test2.xlsx\",\"modifiedDate\":1403828428970,\"state\":\"IMPORTED\"}]";
        ObjectMapper mapper = new ObjectMapper();
        ExcelFileInfo[] fileInfos = mapper.readValue(content.getBytes(), ExcelFileInfo[].class);
        System.out.println(Arrays.toString(fileInfos));
    }
}
