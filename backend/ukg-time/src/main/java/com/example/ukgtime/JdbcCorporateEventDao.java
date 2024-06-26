package com.example.ukgtime;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.ukgtime.Employee.Employee;

@Component
public class JdbcCorporateEventDao implements CorporateEventDao<Employee>{
    private static Logger logger = LoggerFactory.getLogger(JdbcCorporateEventDao.class);
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Employee> rowMapper = (rs, rowNum) -> {
        Employee employee = new Employee();
        employee.setEmployeeId(rs.getLong("employee_id"));
        employee.setSsn(rs.getString("ssn"));
        employee.setEmail(rs.getString("email"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setDob(rs.getString("dob"));
        employee.setManagerId(rs.getLong("manager_id"));
        return employee;
    };

    @Autowired
    public JdbcCorporateEventDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public boolean add(Employee employee) {
        String sql = "INSERT INTO employees(employee_id, first_name, last_name, ssn, dob, " +
                "manager_id, email) " +
                "VALUES (?,?,?,?,?,?,?)";
        //ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        jdbcTemplate.update(sql, employee.getEmployeeId(), employee.getFirstName(), employee.getLastName(),
                employee.getSsn(), employee.getDob(), employee.getManagerId(), employee.getEmail());
        System.out.println("inserted an employee successfully");
        return true;
    }
    public boolean find(long eId) {
        String sql = "SELECT count(*) FROM employees WHERE employee_id = ?";
        int count = this.jdbcTemplate.queryForObject(sql, Integer.class, eId);
        return count>0;
    }

    @Override
    public List<Employee> list() {
        String sql = "SELECT employee_id, first_name, last_name, ssn, dob, manager_id, email FROM employees";
        try {
            List<Employee> employees = jdbcTemplate.query(sql, rowMapper);
            logger.info("Retrieved list of employees: {}", employees);
            return employees;
        } catch (DataAccessException e) {
            logger.error("Error retrieving list of employees", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Employee> get(long id) {
        String sql = "SELECT employee_id, ssn, first_name, last_name, dob, email, manager_id " +
                "FROM employees WHERE employee_id =?";
        Employee employee = null;
        try {
            employee = jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
        } catch(DataAccessException e) {
            logger.info("Employee not found: " + id);
        }
        logger.info("employee: " + employee);
        return Optional.ofNullable(employee);
    }

    @Override
    public void update(Employee employee, long id) {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, ssn = ?, dob = ?, " +
                "manager_id = ?, email = ? WHERE employee_id = ?";
        int update = jdbcTemplate.update(sql, employee.getFirstName(), employee.getLastName(), employee.getSsn(),
                employee.getDob(), employee.getManagerId(), employee.getEmail(), id);
        if (update == 1) {
            logger.info("Employee record updated: " + id);
        }
    }

    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM employees WHERE employee_id =" + id;
        jdbcTemplate.execute(sql);
        return false;
    }
    public boolean checkIsManager(long id) {
        // select the number of employees
        String sql = "SELECT COUNT(*) FROM employees " +
                "WHERE manager_id = ? ";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return (count > 0);
    }
}
