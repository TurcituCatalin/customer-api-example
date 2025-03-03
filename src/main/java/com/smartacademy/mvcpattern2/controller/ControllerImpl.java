package com.smartacademy.mvcpattern2.controller;

import com.smartacademy.mvcpattern2.model.addcustomer.AddCustomer;
import com.smartacademy.mvcpattern2.model.addcustomer.AddCustomerRequest;
import com.smartacademy.mvcpattern2.model.addcustomer.AddCustomerResponse;
import com.smartacademy.mvcpattern2.model.customermodel.Customer;
import com.smartacademy.mvcpattern2.model.errormodel.ErrorResponse;
import com.smartacademy.mvcpattern2.model.getcustomer.GetCustomer;
import com.smartacademy.mvcpattern2.model.getcustomer.GetCustomerResponse;
import com.smartacademy.mvcpattern2.model.updatecustomer.UpdateCustomer;
import com.smartacademy.mvcpattern2.model.updatecustomer.UpdateCustomerRequest;
import com.smartacademy.mvcpattern2.model.updatecustomer.UpdateCustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ControllerImpl implements Controller {

//    @Value("${mvcpattern.annotationexample.valueofstring}")
//    private String annotationExample;

    private Logger log = LoggerFactory.getLogger(ControllerImpl.class);

    private static List<Customer> customers = new ArrayList<>();
    private static Integer customerIdValue = 5;

    static {
        customers.add(new Customer(1, "Jack", LocalDate.now(), "NY", "jack@g.com", "1234"));
        customers.add(new Customer(2, "Marry", LocalDate.now(), "Alaska", "marry@g.com", "8087"));
        customers.add(new Customer(3, "John", LocalDate.now(), "CA", "john@g.com", "6565"));
        customers.add(new Customer(4, "Mike", LocalDate.now(), "NY", "mike@g.com", "12444"));
    }



    @Override
    public GetCustomerResponse getCustomers(Optional<Integer> customerId,
                                                  Optional<String> phoneNumber,
                                                  HttpServletResponse httpServletResponse) {
//        System.out.println("annotationExample = " + annotationExample);
        log.info("Called /getCustomer");
        log.debug("Called /getCustomer with customerId = " + customerId + " and phoneNo = " + phoneNumber);
        // customerId case
        if(customerId.isPresent() && phoneNumber.isEmpty()){
            log.info("CustomerId is present");
            log.debug("CustomerId = " + customerId);
            //processing
            // create response list
            ArrayList<GetCustomer> responseList = new ArrayList<>();
            //customerId is found in the list
            for(Customer customer: customers){

                //customer found, send back response
                if(customer.getCustomerId().equals(customerId.get())){
                    GetCustomer response = new GetCustomer();
                    //preparing response
                    response.setCustomerId(customerId.get());
                    response.setCustomerName(customer.getCustomerName());
                    response.setDateOfBirth(customer.getDateOfBirth());
                    response.setAddress(customer.getAddress());
                    response.setEmail(customer.getEmail());
                    response.setPhoneNo(customer.getPhoneNo());
                    log.info("Customer found and added to the list");
                    log.debug("Customer found and added: " + response.toString());
                    //add to list
                    responseList.add(response);
                }
            }
            //check if there any customers
            if(responseList.isEmpty()){
                log.info("No customer found");
                log.debug("No customer found with customerId = " + customerId.get());
                //no customers found
                httpServletResponse.setStatus(404);
                GetCustomerResponse response = new GetCustomerResponse();
                response.setResponseDescription("No entry found");
                return response;
            }
            //if there are customers, send 200
            else {
                log.info("Returning list of customers");
                log.debug("Returning a number of " + responseList.size() + " customers");
                httpServletResponse.setStatus(200);
                GetCustomerResponse response = new GetCustomerResponse();
                response.setGetCustomer(responseList);
                response.setResponseDescription("Result matching criteria");
                return response;
            }
        }
        // phoneNumber case
        else if(customerId.isEmpty() && phoneNumber.isPresent()){
            log.info("PhoneNumber is present");
            log.debug("PhoneNumber = " + phoneNumber);
            //processing
            // create response list
            ArrayList<GetCustomer> responseList = new ArrayList<>();
            //customerId is found in the list
            for(Customer customer: customers){

                //customer found, send back response
                if(customer.getPhoneNo().equals(phoneNumber.get())){
                    GetCustomer response = new GetCustomer();
                    //preparing response
                    response.setCustomerId(customer.getCustomerId());
                    response.setCustomerName(customer.getCustomerName());
                    response.setDateOfBirth(customer.getDateOfBirth());
                    response.setAddress(customer.getAddress());
                    response.setEmail(customer.getEmail());
                    response.setPhoneNo(phoneNumber.get());
                    log.info("Customer found and added to the list");
                    log.debug("Customer found and added: " + response.toString());
                    //add to list
                    responseList.add(response);
                }
            }
            //check if there any customers
            if(responseList.isEmpty()){
                //no customers found
                log.info("No customer found");
                log.debug("No customer found with phoneNumber = " + phoneNumber.get());
                httpServletResponse.setStatus(404);
                GetCustomerResponse response = new GetCustomerResponse();
                response.setResponseDescription("No entry found");
                return response;
            }
            //if there are customers, send 200
            else {
                log.info("Returning list of customers");
                log.debug("Returning a number of " + responseList.size() + " customers");
                httpServletResponse.setStatus(200);
                GetCustomerResponse response = new GetCustomerResponse();
                response.setGetCustomer(responseList);
                response.setResponseDescription("Result matching criteria");
                return response;
            }
        }
        //error case
        else{
            //processing
            log.info("Request is invalid");
            log.debug("No customer found with customerID = " + customerId.get() + "and/or phoneNumber = " + phoneNumber.get());
            httpServletResponse.setStatus(400); //specify that request is invalid
            GetCustomerResponse response = new GetCustomerResponse();
            response.setResponseDescription("Invalid request");
            return response;
        }
    }

    @Override
    public AddCustomerResponse addCustomer(AddCustomerRequest addCustomerRequest, HttpServletResponse response) {
        AddCustomer addCustomer = addCustomerRequest.getCustomer();
        log.info("Called /addCustomer");
        log.debug("Called /addCustomer with request: " + addCustomerRequest.toString());
        //check if customer exist
        for (Customer customer : customers) {
            //if customer already exist, return duplicate status
            if(checkAddCustomer(addCustomer, customer)){
                log.info("Customer already exist");
                log.debug("Customer with:" + addCustomer.toString() + " already exist");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                AddCustomerResponse addResponse = new AddCustomerResponse();
                addResponse.setResponseDescription("Customer already exists");
                return addResponse;
            }
        }
        //if not, add then to the list
        Customer customer = new Customer();
        customer.setCustomerId(customerIdValue);
        customer.setCustomerName(addCustomer.getCustomerName());
        customer.setDateOfBirth(addCustomer.getDateOfBirth());
        customer.setAddress(addCustomer.getAddress());
        customer.setEmail(addCustomer.getEmail());
        customer.setPhoneNo(addCustomer.getPhoneNo());
        log.info("New customer created and added to the list");
        log.debug("New customer created with: " + customer.toString());
        customers.add(customer);
        //send success response
        response.setStatus(HttpServletResponse.SC_CREATED);
        AddCustomerResponse addResponse = new AddCustomerResponse();
        addResponse.setCustomerId(customerIdValue);
        customerIdValue++;
        addResponse.setResponseDescription("Customer added");
        return addResponse;
    }

    @Override
    public ResponseEntity<?> updateCustomer(UpdateCustomerRequest customerRequest) {
        //find customer in list
        log.info("Called /updateCustomer");
        log.debug("Called /updateCustomer with: " + customerRequest.toString());
        UpdateCustomer customer = customerRequest.getCustomer();
        //find customer in the list by ID
        for (Customer customer1 : customers) {
            //checking if the ID on the request matches any ID in the list
            if (customer1.getCustomerId().equals(customer.getCustomerId())) {
                log.info("CustomerId matches with one from the list");
                log.debug("CustomerId: " + customer.getCustomerId() + " matches with the onw from the list");
                //if found, make sure that the request is not identical to the entry
                if (checkUpdate(customer, customer1)) {
                    //if identical, return error message
                    log.info("Customers are identical");
                    log.debug("Request with: " + customerRequest.toString() + " is identical");
                    UpdateCustomerResponse response = new UpdateCustomerResponse();
                    response.setResponseDescription("Request identical with entry");
                    return new ResponseEntity<UpdateCustomerResponse>(response, HttpStatus.CONFLICT);
                } else {
                    //if not identical, make update
                    customer1.setCustomerName(customer.getCustomerName());
                    customer1.setDateOfBirth(customer.getDateOfBirth());
                    customer1.setAddress(customer.getAddress());
                    customer1.setEmail(customer.getEmail());
                    customer1.setPhoneNo(customer.getPhoneNo());

                    log.info("Customer updated");
                    log.debug("Customer updated with: " + customer1.toString());
                    UpdateCustomerResponse response = new UpdateCustomerResponse();
                    response.setResponseDescription("Item updated");
                    return new ResponseEntity<UpdateCustomerResponse>(response, HttpStatus.OK);
                }
            }
        }
//      in case customer was not found, return 404
        log.info("CustomerId does not exist");
        log.debug("CustomerId = " + customer.getCustomerId());
        UpdateCustomerResponse response = new UpdateCustomerResponse();
        response.setResponseDescription("Customer with id = " + customer.getCustomerId() + " not found.");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    private boolean checkAddCustomer(AddCustomer customer1, Customer customer2){
        //validate for mandatory fields
        if(customer1.getCustomerName().equals(customer2.getCustomerName()) &&
            customer1.getDateOfBirth().equals(customer2.getDateOfBirth()) &&
            customer1.getEmail().equals(customer2.getEmail())){
            return true;
        } else {
            return false;
        }

    }

    private boolean checkUpdate(UpdateCustomer customer1, Customer customer2){
        if(customer1.getCustomerName().equals(customer2.getCustomerName()) &&
                customer1.getEmail().equals(customer2.getEmail()) &&
                customer1.getPhoneNo().equals(customer2.getPhoneNo())){
            return true;
        } else {
            return false;
        }
    }
}
