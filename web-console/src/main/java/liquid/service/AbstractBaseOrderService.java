package liquid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * Created by redbrick9 on 4/26/14.
 */
@Service
public abstract class AbstractBaseOrderService {

    @Autowired
    protected ServiceTypeService serviceTypeService;

    @Autowired
    protected SequenceService sequenceService;


    public String computeOrderNo(String role, String serviceCode) {
        String sequenceName = String.format("%1$s%2$s%3$s%4$ty",
                salesOrMarketing(role),
                "C", // TODO
                serviceCode,
                Calendar.getInstance());
        return String.format("%1$s%2$05d",
                sequenceName, sequenceService.getNextValue(sequenceName));
    }

    /**
     * @param role is like "ROLE_SALES" from LDAP.
     * @return
     */
    private String salesOrMarketing(String role) {
        return "ROLE_SALES".equals(role) ? "S" : "M";
    }
}
