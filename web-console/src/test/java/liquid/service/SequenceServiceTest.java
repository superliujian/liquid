package liquid.service;

import liquid.config.*;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by redbrick9 on 4/26/14.
 */
public class SequenceServiceTest {
    @Test
    public void getNextValue() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SequenceService.class);
        context.register(JpaConfig.class);
        context.refresh();

        SequenceService sequenceService = context.getBean(SequenceService.class);
        System.out.println(sequenceService.getNextValue("SC014"));
        System.out.println(sequenceService.getNextValue("SC014"));
        System.out.println(sequenceService.getNextValue("SC014"));
    }

    @Test
    public void getNextValue1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SequenceService.class);
        context.register(JpaConfig.class);
        context.refresh();

        SequenceService sequenceService = context.getBean(SequenceService.class);
        System.out.println(sequenceService.getNextValue("SC114"));
    }
}
