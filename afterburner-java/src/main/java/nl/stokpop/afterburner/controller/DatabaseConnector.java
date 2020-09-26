package nl.stokpop.afterburner.controller;

import io.swagger.annotations.ApiOperation;
import nl.stokpop.afterburner.AfterburnerProperties;
import nl.stokpop.afterburner.mybatis.Employee;
import nl.stokpop.afterburner.mybatis.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DatabaseConnector {

    private final JdbcTemplate template;

    private final AfterburnerProperties props;

    private final EmployeeMapper employeeMapper;

    @Autowired
    public DatabaseConnector(JdbcTemplate template, AfterburnerProperties props, EmployeeMapper employeeMapper) {
        this.template = template;
        this.props = props;
        this.employeeMapper = employeeMapper;
    }

    @ApiOperation(value = "Execute trivial query on remote database and measure the response time.")
    @GetMapping("/db/connect")
    public BurnerMessage checkDatabaseConnection() {
        long startTime = System.currentTimeMillis();

        String query = props.getDatabaseConnectQuery();

        long nanoStartTime = System.nanoTime();
        template.execute(query);
        long estimatedQueryTime = System.nanoTime() - nanoStartTime;

        long durationMillis = System.currentTimeMillis() - startTime;

        String message = String.format("{ 'db-call':'success','query-duration-nanos':%d }", estimatedQueryTime);

        return new BurnerMessage(message, props.getName(), durationMillis);
    }

    @ApiOperation(value = "Find employees by first name.")
    @GetMapping("/db/employee/name")
    public List<Employee> findEmployeeByFirstName(@RequestParam(value = "firstName", defaultValue = "Anneke") String firstName) {
        return employeeMapper.selectEmployeeWithFirstName(firstName);
    }

}
