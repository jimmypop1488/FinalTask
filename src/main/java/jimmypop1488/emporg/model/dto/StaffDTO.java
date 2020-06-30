package jimmypop1488.emporg.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class StaffDTO {
    public String staffid;
    public String staffname;
    public String stafforgid;
    public String headstaffid;
}
