package lk.ijse.dep11.ims.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.ims.to.CourseTo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.Valid;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/courses")
@CrossOrigin
public class CourseHttpController {
    private HikariDataSource pool;

    public CourseHttpController(){
        HikariConfig config = new HikariConfig();
        config.setUsername("root");
        config.setPassword("13823Textile?");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/dep11_ims_app");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.addDataSourceProperty("maximumPoolSize",10);
        pool = new HikariDataSource(config);
    }

    @PreDestroy
    public void destroy(){
        pool.close();
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", consumes = "application/json")
    public CourseTo createCourse(@RequestBody  CourseTo course){
        System.out.printf("createCourse()");
        try(Connection connection = pool.getConnection()){
            PreparedStatement stm = connection.prepareStatement("INSERT INTO course (name,duration_in_months) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1,course.getName());
            stm.setInt(2,course.getDurationInMonths());
            stm.executeUpdate();
            ResultSet rstGeneratedKeys = stm.getGeneratedKeys();
            rstGeneratedKeys.next();
            int generatedKey = rstGeneratedKeys.getInt(1);
            course.setId(generatedKey);
            return course;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable("id") Integer courseId){
        try (Connection connection = pool.getConnection()) {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery( String.format("SELECT * FROM course WHERE id=%d",courseId));
            if(!rst.next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The course not found");
            }
            connection.createStatement().executeUpdate( String.format("DELETE FROM course WHERE id=%d",courseId));
        }catch (SQLException e){
            new RuntimeException(e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{id}", consumes = "application/json")
    public void updateCourse(@PathVariable("id") Integer courseId, @RequestBody CourseTo course){
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stmValidate = connection.prepareStatement("SELECT * FROM course WHERE id=?");
            stmValidate.setInt(1,courseId);
            ResultSet rst = stmValidate.executeQuery();

            if(!rst.next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"The course not found");
            }
            PreparedStatement stm = connection.prepareStatement("UPDATE course SET name=?, duration_in_months=? WHERE id=?");
            stm.setString(1,course.getName());
            stm.setInt(2,course.getDurationInMonths());
            stm.setInt(3,courseId);

            stm.executeUpdate();

        }catch (SQLException e){
            new RuntimeException(e);
        }
    }

    @GetMapping(produces = "application/json")
    public List<CourseTo> getAllCourses(){
        try (Connection connection = pool.getConnection()) {
            ResultSet rst = connection.createStatement().executeQuery("SELECT * FROM course");
            List<CourseTo> coursesList = new LinkedList<>();
            while (rst.next()){
                coursesList.add(new CourseTo(
                        rst.getInt("id"),rst.getString("name"),rst.getInt("duration_in_months")));
            }
            return coursesList;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/{id}",produces = "application/json")
    public CourseTo getCourse(@PathVariable("id") Integer courseId){
        try (Connection connection = pool.getConnection()) {
            ResultSet rst = connection.createStatement().executeQuery( String.format("SELECT * FROM course WHERE id=%s",courseId));
            return new CourseTo(rst.getInt("id"),rst.getString("name"),rst.getInt("duration_in_months"));
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/{courseId}/teachers", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void assignTeachersToCourses(@PathVariable Integer courseId){
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("SELECT teacher_id FROM teacher_course WHERE course_id=?");
            stm.setInt(1,courseId);
            ResultSet rst = stm.executeQuery();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
