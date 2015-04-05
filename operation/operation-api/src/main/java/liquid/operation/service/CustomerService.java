package liquid.operation.service;

import liquid.operation.domain.Customer;

/**
 * Created by Tao Ma on 4/5/15.
 */
public interface CustomerService {

    Customer find(Long id);

    Customer findByName(String name);

    Iterable<Customer> findByQueryNameLike(String name);

    // FIXME - Remove after confirmation ReceivingOrderController doesn't make use of it.
    Iterable<Customer> findAll();
}
