package liquid.web.domain;

import liquid.util.DateUtil;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by mat on 10/18/14.
 */
public class SearchBarForm {
    @Deprecated
    private String action;
    @Deprecated
    private String types[][];

    /**
     * order id or customer id.
     */
    private Long id;

    /**
     * order or customer
     */
    private String type;

    /**
     * type ahead
     */
    private String text;

    @NotNull
    @NotEmpty
    private String startDate;

    @NotNull
    @NotEmpty
    private String endDate;

    /**
     * The number of page.
     */
    private int number;

    public SearchBarForm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
        startDate = DateUtil.dayStrOf(calendar.getTime());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0, 0, 0, 0);
        endDate = DateUtil.dayStrOf(calendar.getTime());
    }

    @Deprecated
    public String getAction() {
        return action;
    }

    @Deprecated
    public void setAction(String action) {
        this.action = action;
    }

    @Deprecated
    public String[][] getTypes() {
        return types;
    }

    @Deprecated
    public void setTypes(String[][] types) {
        this.types = types;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public static String toQueryStrings(SearchBarForm searchBarForm) {
        StringBuilder sb = new StringBuilder("?");
        sb.append("startDate=").append(searchBarForm.getStartDate()).append("&");
        sb.append("endDate=").append(searchBarForm.getEndDate()).append("&");
        sb.append("type=").append(null == searchBarForm.getType() ? "" : searchBarForm.getType()).append("&");
        sb.append("id=").append(null == searchBarForm.getId() ? "" : searchBarForm.getId()).append("&");
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=SearchBarForm");
        sb.append(", action='").append(action).append('\'');
        sb.append(", types=").append(Arrays.toString(types));
        sb.append(", id=").append(id);
        sb.append(", type='").append(type).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", startDate='").append(startDate).append('\'');
        sb.append(", endDate='").append(endDate).append('\'');
        sb.append(", number=").append(number);
        sb.append('}');
        return sb.toString();
    }
}
