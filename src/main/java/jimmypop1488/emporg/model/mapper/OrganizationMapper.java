package jimmypop1488.emporg.model.mapper;

import jimmypop1488.emporg.model.dto.OrganizationDTO;
import jimmypop1488.emporg.model.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {
    @Mappings({
            @Mapping(target = "orgid", expression = "java(org.orgid.toString())"),
            @Mapping(target = "orgname", source = "org.name"),
            @Mapping(target = "headorgid", source = "org.headorgid.name")
    })
    OrganizationDTO organizationToDTO(Organization org);

    @Mappings({
            @Mapping(target = "orgid", ignore = true),
            @Mapping(target = "name", source = "dto.orgname"),
            @Mapping(target = "headorgid", source = "org")
    })
    Organization organizationFromDTO(OrganizationDTO dto, Organization org);
}
