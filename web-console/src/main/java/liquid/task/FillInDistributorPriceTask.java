package liquid.task;

import org.springframework.validation.BindingResult;

/**
 * Created by redbrick9 on 1/29/14.
 */
public class FillInDistributorPriceTask<DistyDto> implements BusinessTask {
    @Override
    public void claim() {

    }

    @Override
    public ValidationResult validate(Object formBean, BindingResult result) {
        return null;
    }

    @Override
    public DistyDto summary() {
        return null;
    }

    @Override
    public void complete() {

    }
}
