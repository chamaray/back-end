package lk.ijse.dep11.ims.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.groups.Default;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherTo implements Serializable {
    private Integer id;
    private String name;
    private String contact;

    public interface Update extends Default{}
    public interface Create extends Default{}
}
