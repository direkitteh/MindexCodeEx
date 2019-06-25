package com.mindex.challenge.data;

import java.util.Set;
import java.util.HashSet;

public class ReportingStructure {
    private Employee employee = null;
    private int numberOfReports;

    public ReportingStructure() {
    }
    
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        // Note: updating employee Object won't update numberOfReports
        this.numberOfReports = calculateNumberOfReports();
    }
    
    public int getNumberOfReports() {
      return numberOfReports;
    }
    
    private int calculateNumberOfReports() {
      Set<Employee> reports = new HashSet<Employee>();
      reports.add(employee);
      
      // remove 1 since employees don't count themselves
      return getReports(employee, reports);
    }
    
    private int getReports(Employee employee, Set<Employee> checked) {
      int count = 0;
      if (employee != null && employee.getDirectReports() != null) {
         count = employee.getDirectReports().size();
         for (Employee report : employee.getDirectReports()) {
            // prevent infinite loops
            // pretty sure the set is passed - should verify
            if (!checked.contains(report)) {
               checked.add(report);
               count += getReports(report, checked);
            }
         }
      }
      return count;
    }
}
