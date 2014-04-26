package liquid.service;

import liquid.config.JpaConfig;
import liquid.persistence.domain.Sequence;
import liquid.persistence.repository.SequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by redbrick9 on 4/26/14.
 */
@Service
public class SequenceService {

    @Autowired
    private SequenceRepository sequenceRepository;

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public int getNextValue(String name) {
        sequenceRepository.increment(name);
        int value = sequenceRepository.getValue();
        if (0 == value) {
            Sequence sequence = new Sequence();
            sequence.setName(name);
            sequence.setPrefix(name);
            sequence.setValue(0L);
            sequenceRepository.save(sequence);
            sequenceRepository.increment(name);
            value = sequenceRepository.getValue();
        }
        return value;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SequenceService.class);
        context.register(JpaConfig.class);
        context.refresh();

        final SequenceService sequenceService = context.getBean(SequenceService.class);

        int threadNumber = 10;

        for (int i = 0; i < threadNumber; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    int value = sequenceService.getNextValue("SC014");
                    System.out.println(value + "," + (System.currentTimeMillis() - start));
                }
            }).start();
        }
    }
}
