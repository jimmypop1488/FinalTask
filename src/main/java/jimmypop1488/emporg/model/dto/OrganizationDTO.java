package jimmypop1488.emporg.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class OrganizationDTO {
    public String orgid;
    public String orgname;
    public String headorgid;

    public int staffCount;
}
