package lk.ijse.dep11.ims.api;

import lk.ijse.dep11.ims.to.CourseTo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@CrossOrigin
public class CourseHttpController {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", consumes = "application/json")
    public void createCourse(@RequestBody @Validated(CourseTo.Create.class) CourseTo course){

    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteCourse(@PathVariable("id") Integer courseId){

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "{id}", consumes = "application/json")
    public void updateCourse(@PathVariable("id") Integer courseId, @RequestBody CourseTo course){

    }

    @GetMapping(produces = "application/json")
    public void getAllCourses(){

    }

    @GetMapping(value = "{id}",produces = "application/json")
    public void getCourse(@PathVariable("id") Integer courseId){

    }
}
