package liquid.domain;

import liquid.task.web.domain.TaskForm;
import liquid.transport.web.domain.TruckForm;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Tao Ma on 12/31/14.
 */
public class SendingTruckForm extends TaskForm {
    @Valid
    private List<TruckForm> truckList;

    public List<TruckForm> getTruckList() {
        return truckList;
    }

    public void setTruckList(List<TruckForm> truckList) {
        this.truckList = truckList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=SendingTruckForm");
        sb.append(", truckList=").append(truckList);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
