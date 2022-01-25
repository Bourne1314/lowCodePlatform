package com.csicit.ace.webservice.pojo;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/6 14:32
 */
@Data
@XmlRootElement(name = "LIST")
public class EmployeeList {
    private List<Employee> employees;

    public List<Employee> getEmployees() {
        return employees;
    }

    @XmlElement(name = "EMPLOYEE")
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
