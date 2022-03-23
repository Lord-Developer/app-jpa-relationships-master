package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.FacultyRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;

import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    AddressRepository addressRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAll(pageable);
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable int facultyId,
                                                  @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
    }

    //4. GROUP OWNER
    @GetMapping("/forGroupOwner/{groupId}")
    public Page<Student> getStudentListForGroupOwner(@PathVariable int groupId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroupId(groupId, pageable);
    }

    @PostMapping("/add")
    public String addStudent(@RequestBody StudentDto studentDto) {
        String firstName = studentDto.getFirstName();
        String lastName = studentDto.getLastName();
        int addressId = studentDto.getAddressId();
        int groupId = studentDto.getGroupId();
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isEmpty()) return "Group not found";
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if (optionalAddress.isEmpty()) return "Address not found";
        boolean b = studentRepository.existsByFirstNameAndLastNameAndAddress(firstName, lastName, optionalAddress.get());
        if (!b) return "student is exist";
        Student student = new Student();
        student.setAddress(optionalAddress.get());
        student.setGroup(optionalGroup.get());
        student.setFirstName(firstName);
        studentRepository.save(student);
        return "student added";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        try {
            studentRepository.deleteById(id);
            return "student deleted";
        } catch (Exception e) {
            return "Error when delete";
        }
    }

    @PutMapping("/edit/{id}")
    public String edit(@PathVariable int id, @RequestBody StudentDto studentDto) {
        Optional<Student> byId = studentRepository.findById(id);
        if (byId.isEmpty()) return "Student doesn't exist";
        String firstName = studentDto.getFirstName();
        String lastName = studentDto.getLastName();
        int addressId = studentDto.getAddressId();
        int groupId = studentDto.getGroupId();
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isEmpty()) return "Group not found";
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if (optionalAddress.isEmpty()) return "Address not found";
        Student student = new Student();
        student.setAddress(optionalAddress.get());
        student.setGroup(optionalGroup.get());
        student.setFirstName(firstName);
        studentRepository.save(student);
        return "student edited";
    }
}
