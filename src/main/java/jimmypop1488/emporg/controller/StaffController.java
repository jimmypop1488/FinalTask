package jimmypop1488.emporg.controller;

import jimmypop1488.emporg.model.dto.StaffDTO;
import jimmypop1488.emporg.model.entity.Organization;
import jimmypop1488.emporg.model.entity.Staff;
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
@RequestMapping("staff")
public class StaffController {
    private final OrganizationRepo organizationRepo;
    private final StaffRepo staffRepo;
    private StaffMapper staffMapper = Mappers.getMapper(StaffMapper.class);

    @Autowired
    public StaffController(OrganizationRepo organizationRepo, StaffRepo staffRepo) {
        this.organizationRepo = organizationRepo;
        this.staffRepo = staffRepo;
    }

    @GetMapping
    public List<StaffDTO> list() {
        return staffRepo.findAll().stream()
                .map(x -> staffMapper.staffToDTO(x))
                .collect(Collectors.toList())
                ;
    }

    @GetMapping("/tree")
    public List<StaffDTO> treelist() {
        List<StaffDTO> staffs = staffRepo.findAll().stream()
                .map(x -> staffMapper.staffToDTO(x))
                .collect(Collectors.toList())
                ;
        List<StaffDTO> staffsTree = new ArrayList<>();
        for (StaffDTO st : staffs) {
            if (st.headstaffid == null) {
                staffsTree.add(st);
                sort(staffs, staffsTree, st);
            }
        }
        strings(staffsTree);
        return staffsTree;
    }

    @GetMapping("{staffid}")
    public StaffDTO getById(@PathVariable("staffid") UUID staffid) {
        Optional<Staff> st = staffRepo.findById(staffid);
        if (!st.isPresent())
            return null;
        StaffDTO staff = staffMapper.staffToDTO(st.get());
        return staff;
    }

    @PostMapping
    public Staff create(@RequestBody StaffDTO staff) {
        Optional<Organization> headorg = organizationRepo.findByName(staff.stafforgid);
        if (!headorg.isPresent())
            return null;
        Optional<Staff> headstaff = staffRepo.findByName(staff.headstaffid);
        if (!headstaff.isPresent())
            return staffRepo.save(staffMapper.staffFromDTO(staff, headorg.get(), null));
        String hso = headstaff.get().stafforgid.name;
        String so = staff.stafforgid;
        if (so.equals(hso))
            return staffRepo.save(staffMapper.staffFromDTO(staff, headorg.get(), headstaff.get()));
        return null;

    }

    @PutMapping("{staffid}")
    public StaffDTO update(
            @PathVariable("staffid") UUID staffid,
            @RequestBody StaffDTO staff
    ) {
        Optional<Staff> st = staffRepo.findById(staffid);
        Optional<Staff> headstaff = staffRepo.findByName(staff.headstaffid);
        Optional<Organization> headorg = organizationRepo.findByName(staff.stafforgid);
        if (!st.isPresent() || !headorg.isPresent())
            return null;
        Staff newstaff;
        if (!headstaff.isPresent())
            newstaff = staffMapper.staffFromDTO(staff, headorg.get(), null);
        else {
            String hso = headstaff.get().stafforgid.name;
            String so = staff.stafforgid;
            if (so.equals(hso))
                newstaff = staffMapper.staffFromDTO(staff, headorg.get(), headstaff.get());
            else
                return null;
        }
        newstaff.staffid = st.get().staffid;
        staffRepo.save(newstaff);
        return staffMapper.staffToDTO(newstaff);
    }

    @DeleteMapping("{staffid}")
    public void delete(@PathVariable("staffid") Staff staff) {
        staffRepo.delete(staff);
    }

    public void sort (List<StaffDTO> staffs, List<StaffDTO> staffsTree, StaffDTO headstaff) {
        for (StaffDTO st : staffs) {
            if (st.headstaffid == headstaff.staffname) {
                staffsTree.add(st);
                sort(staffs, staffsTree, st);
            }
        }
    }

    public void strings (List<StaffDTO> staffsTree) {
        int[] levels = new int[staffsTree.size()];
        for (int k = 0; k < staffsTree.size(); k++)
            levels[k] = 0;
        for (int i = 0; i < staffsTree.size(); i++)
            for (int j = 1; j < staffsTree.size(); j++)
                if (staffsTree.get(j).headstaffid == staffsTree.get(i).staffname) {
                    while (staffsTree.get(j).headstaffid != null) {
                        levels[j]++;
                        j++;
                        if (j == staffsTree.size())
                            break;
                    }
                    break;
                }
        for (int s = 0; s < staffsTree.size(); s++) {
            String lvl = "";
            for (int c = 0; c < levels[s]; c++)
                lvl += "--";
            staffsTree.get(s).staffname = lvl + staffsTree.get(s).staffname;
        }
    }
}