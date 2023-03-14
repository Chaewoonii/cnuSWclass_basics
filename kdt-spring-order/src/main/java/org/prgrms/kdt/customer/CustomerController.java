package org.prgrms.kdt.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    @GetMapping("api/v1/customers")
    @ResponseBody
    public List<Customer> findCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("api/v1/customers/{customerId}")
    @ResponseBody
    public ResponseEntity<Customer> findCustomer(@PathVariable("customerId")UUID customerId){
        var customer = customerService.getCustomer(customerId);
        return customer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); // customer service로 부터 받은 값을 ResponseEntity에 전달
//        return customer.map(ResponseEntity::ok).orElse(ResponseEntity.status(404).body(something~~));// status를 직접 지정하고 body를 전달
    }

//    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    @GetMapping("/customers")
    public String viewCustomersPage(Model model){
        var allCustomers = customerService.getAllCustomers();
        model.addAttribute("serverTime", LocalDateTime.now());
        model.addAttribute("customers", allCustomers);

        // controller가 customers라는 이름의 뷰와 Map model을 반환.
        // view resolver가 customers라는 뷰 이름을 찾음
        /*return new ModelAndView("views/customers",
                Map.of("serverTime", LocalDateTime.now(),
                        "customers", allCustomers));*/

        return "views/customers";
    }

    @GetMapping("/customers/{customerId}")
    public String findCustomer(@PathVariable("customerId") UUID customerId, Model model){
        var mayBeCustomer = customerService.getCustomer(customerId);
        if (mayBeCustomer.isPresent()){
            model.addAttribute("customer", mayBeCustomer.get());
            return "views/customer-details";
        }else {
            return "views/404";
        }
    }

    //Form 화면 매핑
    @GetMapping("/customers/new")
    public String viewNewCustomerPage(){
        return "views/new-customers";
    }

    @PostMapping("/customers/new")
    public String addNewCustomer(CreateCustomerRequest createCustomerRequest){
        //CreateCustomerRequest::일종의 DTO
        customerService.createCustomer(createCustomerRequest.email(), createCustomerRequest.name());
        return "redirect:/customers";
    }

}
