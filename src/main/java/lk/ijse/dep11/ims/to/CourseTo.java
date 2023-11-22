package lk.ijse.dep11.ims.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseTo implements Serializable {

//    @Null(message = "ID should be Null")
    private Integer id;
//    @NotBlank(message = "Name shouldn't be blank",groups = Create.class)
//    @Pattern(regexp = "^[A-Za-z ]+$",message = "Name can have only letters")
    private String name;
//    @NotBlank(message = "duration shouldn't be blank",groups = Create.class)
//    @Pattern(regexp = "^[0-9]+")
    private Integer durationInMonths;

    public interface Create extends Default {}
}

