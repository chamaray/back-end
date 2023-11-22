package lk.ijse.dep11.ims.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.ims.to.TeacherTo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/teachers")
@CrossOrigin
public class TeacherHttpController {

    private final HikariDataSource pool;
    public TeacherHttpController(){
        HikariConfig config = new HikariConfig();
        config.setUsername("root");
        config.setPassword("13823Textile?");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/dep11_ims_app");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.addDataSourceProperty("maximumPoolSize",10);
        pool = new HikariDataSource(config);
    }

    @PreDestroy
    public void destroy(){pool.close();}

    @PostMapping(produces = "application/json",consumes = "application/json")
    public TeacherTo createTeacher(@RequestBody @Validated(TeacherTo.Create.class)TeacherTo teacher){
       try{
           Connection connection = pool.getConnection();
           PreparedStatement stm = connection.prepareStatement("INSERT INTO teacher(name, contact) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
           stm.setString(1,teacher.getName());
           stm.setString(2,teacher.getContact());
           stm.executeUpdate();
           ResultSet generatedKeys = stm.getGeneratedKeys();
           generatedKeys.next();
           int id = generatedKeys.getInt(1);
           teacher.setId(id);
           return teacher;
       }catch (SQLException e){
           throw new RuntimeException(e);
       }
    }

    @DeleteMapping({"/{id}"})
    public void deleteTeacher(@PathVariable int id){
        try(Connection connection = pool.getConnection()){
            PreparedStatement stmExist = connection.prepareStatement("SELECT * FROM teacher WHERE id=?");
            stmExist.setInt(1,id);
            if(!stmExist.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task Not Found");
            }

            PreparedStatement stm = connection.prepareStatement("DELETE FROM teacher WHERE id=?");
            stm.setInt(1,id);
            stm.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{id}",consumes = "application/json")
    public void updateTeacher(@PathVariable int id,
                              @RequestBody @Validated(TeacherTo.Update.class)TeacherTo teacher){
        try(Connection connection = pool.getConnection()){
            PreparedStatement stmExist = connection.prepareStatement("SELECT * FROM teacher WHERE id=?");
            stmExist.setInt(1,id);
            if(!stmExist.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task Not Found");
            }
            PreparedStatement stm = connection.prepareStatement("UPDATE teacher SET name=?,contact=? WHERE id=?");
            stm.setString(1,teacher.getName());
            stm.setString(2,teacher.getContact());
            stm.setInt(3,teacher.getId());
            stm.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping(produces = "application/json")
    public List<TeacherTo> getAllTeachers(){
        try(Connection connection = pool.getConnection()){
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM teacher");
            List<TeacherTo> teacherList = new ArrayList<>();
            while (rst.next()){
                int id = rst.getInt("id");
                String name = rst.getString("name");
                String contact = rst.getString("contact");
                teacherList.add(new TeacherTo(id,name,contact));
            }
            return teacherList;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/{id}",produces = "application/json")
    public TeacherTo getTeacher(@PathVariable int id){
        try(Connection connection = pool.getConnection()){
            Statement stm = connection.createStatement();
            return null;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }


    }
}

