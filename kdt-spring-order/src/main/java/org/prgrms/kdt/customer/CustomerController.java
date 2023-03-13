package org.prgrms.kdt.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public ModelAndView findCustomers(){
        var allCustomers = customerService.getAllCustomers();
        // controller가 customers라는 이름의 뷰와 Map model을 반환.
        // view resolver가 customers라는 뷰 이름을 찾음
        return new ModelAndView("customers",
                Map.of("serverTime", LocalDateTime.now(),
                        "customers", allCustomers));
    }
}
