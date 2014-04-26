package liquid.persistence;

import liquid.config.JpaConfig;
import liquid.persistence.repository.SequenceRepository;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by redbrick9 on 4/26/14.
 */
public class SequenceTest {

    @Test
    public void increment() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();

        SequenceRepository sequenceRepository = (SequenceRepository) context.getBean(SequenceRepository.class);
        sequenceRepository.increment("SC014");
    }

    @Test
    public void increment1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();

        SequenceRepository sequenceRepository = (SequenceRepository) context.getBean(SequenceRepository.class);
        sequenceRepository.increment("SC114");
    }

    @Test
    public void getValue() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();

        SequenceRepository sequenceRepository = (SequenceRepository) context.getBean(SequenceRepository.class);
        System.out.println(sequenceRepository.getValue());
    }
}
