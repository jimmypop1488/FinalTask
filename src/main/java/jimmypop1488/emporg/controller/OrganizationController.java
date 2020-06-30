package jimmypop1488.emporg.controller;

import jimmypop1488.emporg.model.dto.OrganizationDTO;
import jimmypop1488.emporg.model.dto.StaffDTO;
import jimmypop1488.emporg.model.entity.Organization;
import jimmypop1488.emporg.model.entity.Staff;
import jimmypop1488.emporg.model.mapper.OrganizationMapper;
import jimmypop1488.emporg.model.mapper.StaffMapper;
import jimmypop1488.emporg.repo.OrganizationRepo;
import jimmypop1488.emporg.repo.StaffRepo;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("organization")
public class OrganizationController {
    private final OrganizationRepo organizationRepo;
    private final StaffRepo staffRepo;
    private OrganizationMapper organizationMapper = Mappers.getMapper(OrganizationMapper.class);
    private StaffMapper staffMapper = Mappers.getMapper(StaffMapper.class);

    @Autowired
    public OrganizationController(OrganizationRepo organizationRepo, StaffRepo staffRepo) {
        this.organizationRepo = organizationRepo;
        this.staffRepo = staffRepo;
    }

//    @GetMapping
//    public List<OrganizationDTO> list() {
//        return organizationRepo.findAll().stream()
//                .map(x -> organizationMapper.organizationToDTO(x))
//                .collect(Collectors.toList())
//                ;
//    }

    @GetMapping
    public List<OrganizationDTO> list() {
        List<OrganizationDTO> organizations = organizationRepo.findAll().stream()
                .map(x -> organizationMapper.organizationToDTO(x))
                .collect(Collectors.toList())
                ;
        List<StaffDTO> staffs = staffRepo.findAll().stream()
                .map(x -> staffMapper.staffToDTO(x))
                .collect(Collectors.toList())
                ;
        stCount (organizations, staffs);
        return organizations;
    }

    @GetMapping("/tree")
    public List<OrganizationDTO> treelist() {
        List<OrganizationDTO> organizations = organizationRepo.findAll().stream()
                .map(x -> organizationMapper.organizationToDTO(x))
                .collect(Collectors.toList())
                ;
        List<OrganizationDTO> organizationsTree = new ArrayList<>();
        for (OrganizationDTO org : organizations) {
            if (org.headorgid == null) {
                organizationsTree.add(org);
                sort(organizations, organizationsTree, org);
            }
        }
        strings(organizationsTree);
        return organizationsTree;
    }

    @GetMapping("{orgid}")
    public OrganizationDTO getById(@PathVariable("orgid") UUID orgid) {
        Optional<Organization> org = organizationRepo.findById(orgid);
        if (!org.isPresent())
            return null;
        OrganizationDTO organization = organizationMapper.organizationToDTO(org.get());
        return organization;
    }

    @PostMapping
    public Organization create(@RequestBody OrganizationDTO organization) {
        Optional<Organization> headorg = organizationRepo.findByName(organization.headorgid);
        if (!headorg.isPresent())
            return organizationRepo.save(organizationMapper.organizationFromDTO(organization, null));
        return organizationRepo.save(organizationMapper.organizationFromDTO(organization, headorg.get()));
    }

    @PutMapping("{orgid}")
    public OrganizationDTO update(
            @PathVariable("orgid") UUID orgid,
            @RequestBody OrganizationDTO organization
    ) {
        Optional<Organization> org = organizationRepo.findById(orgid);
        Optional<Organization> headorg = organizationRepo.findByName(organization.headorgid);
        if (!org.isPresent())
            return null;
        Organization neworg;
        if (!headorg.isPresent())
            neworg = organizationMapper.organizationFromDTO(organization, null);
        else
            neworg = organizationMapper.organizationFromDTO(organization, headorg.get());
        neworg.orgid = org.get().orgid;
        organizationRepo.save(neworg);
        return organizationMapper.organizationToDTO(neworg);
    }

    @DeleteMapping("{orgid}")
    public void delete(@PathVariable("orgid") Organization organization) {
        organizationRepo.delete(organization);
    }

    public void stCount(List<OrganizationDTO> organizations, List<StaffDTO> staffs) {
        for (OrganizationDTO org : organizations) {
            int count = 0;
            for (StaffDTO st : staffs) {
                if (org.orgname == st.stafforgid)
                    count++;
            }
            org.staffCount = count;
        }
    }

    public void sort (List<OrganizationDTO> organizations, List<OrganizationDTO> organizationsTree, OrganizationDTO headorg) {
        for (OrganizationDTO org : organizations) {
            if (org.headorgid == headorg.orgname) {
                organizationsTree.add(org);
                sort(organizations, organizationsTree, org);
            }
        }
    }

    public void strings (List<OrganizationDTO> organizationsTree) {
        int[] levels = new int[organizationsTree.size()];
        for (int k = 0; k < organizationsTree.size(); k++)
            levels[k] = 0;
        for (int i = 0; i < organizationsTree.size(); i++)
            for (int j = 1; j < organizationsTree.size(); j++)
                if (organizationsTree.get(j).headorgid == organizationsTree.get(i).orgname) {
                    while (organizationsTree.get(j).headorgid != null) {
                        levels[j]++;
                        j++;
                        if (j == organizationsTree.size())
                            break;
                    }
                    break;
                }
        for (int s = 0; s < organizationsTree.size(); s++) {
            String lvl = "";
            for (int c = 0; c < levels[s]; c++)
                lvl += "--";
            organizationsTree.get(s).orgname = lvl + organizationsTree.get(s).orgname;
        }
    }
}