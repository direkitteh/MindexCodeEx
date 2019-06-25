package com.mindex.challenge.service.impl;

import java.util.Arrays;
import java.util.List;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportIdUrl;
    private Employee createdEmployee;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportIdUrl = "http://localhost:" + port + "/report/{id}";
        
        setupEmployees();
    }

    public void setupEmployees() {
        Employee pete = new Employee();
        pete.setFirstName("Pete");
        pete.setLastName("Best");
        pete.setDepartment("Engineering");
        pete.setPosition("Developer");
        pete = createEmployee(pete);
        
        Employee george = new Employee();
        george.setFirstName("George");
        george.setLastName("Harrison");
        george.setDepartment("Engineering");
        george.setPosition("Developer");
        george = createEmployee(george);
        
        Employee ringo = new Employee();
        ringo.setFirstName("Ringo");
        ringo.setLastName("Starr");
        ringo.setDepartment("Engineering");
        ringo.setPosition("Developer");
        ringo.setDirectReports(Arrays.asList(pete,george));
        ringo = createEmployee(ringo);
        
        Employee paul = new Employee();
        paul.setFirstName("Paul");
        paul.setLastName("McCartney");
        paul.setDepartment("Engineering");
        paul.setPosition("Developer");
        paul = createEmployee(paul);
        
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Lennon");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee.setDirectReports(Arrays.asList(ringo,paul));
        createdEmployee = createEmployee(testEmployee);
    }
    
    private Employee createEmployee(Employee emp) {
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, emp, Employee.class).getBody();
        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(emp, createdEmployee);
        return createdEmployee;
    }
    
    @Test
    public void testCreate() {
        // Create check
        ReportingStructure createdReport = restTemplate.postForEntity(reportIdUrl, createdEmployee.getEmployeeId(), ReportingStructure.class, createdEmployee.getEmployeeId()).getBody();
        assertNotNull("No report structure", createdReport);
        assertNotNull("No employee", createdReport.getEmployee());
        assertEmployeeEquivalence(createdEmployee, createdReport.getEmployee());
        assertEquals(4, createdReport.getNumberOfReports());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
