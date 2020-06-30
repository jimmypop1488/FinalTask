package jimmypop1488.emporg.model.mapper;

import jimmypop1488.emporg.model.dto.StaffDTO;
import jimmypop1488.emporg.model.entity.Organization;
import jimmypop1488.emporg.model.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    @Mappings({
            @Mapping(target = "staffid", expression = "java(st.staffid.toString())"),
            @Mapping(target = "staffname", source = "st.name"),
            @Mapping(target = "stafforgid", source = "st.stafforgid.name"),
            @Mapping(target = "headstaffid", source = "st.headstaffid.name")
    })
    StaffDTO staffToDTO(Staff st);

    @Mappings({
            @Mapping(target = "staffid", ignore = true),
            @Mapping(target = "name", source = "dto.staffname"),
            @Mapping(target = "stafforgid", source = "org"),
            @Mapping(target = "headstaffid", source = "st")
    })
    Staff staffFromDTO(StaffDTO dto, Organization org, Staff st);
}
