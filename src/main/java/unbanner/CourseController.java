package unbanner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class CourseController {
  @Autowired
  CourseRepository repository;

  @Autowired
  SectionRepository sectionRepository;

  @Autowired
  StudentRepository studentRepository;

  @Autowired
  ProfessorRepository professorRepository;

  @Autowired
  RoomRepository roomRepository;

  @Autowired
  SemesterRepository semesterRepository;

  @ModelAttribute("allProfessors")
  public List<Professor> getProfessors() {
    return professorRepository.findAll();
  }

  @ModelAttribute("allRooms")
  public List<Room> getRooms() {
    return roomRepository.findAll();
  }

  @ModelAttribute("allSemesters")
  public List<Semester> getSemesters() {
    return semesterRepository.findAll();
  }

  @RequestMapping(value = "/courses", method = RequestMethod.GET)
  public String coursesList(Model model) {
    model.addAttribute("courses", repository.findAll());
    return "courses";
  }

  @RequestMapping(value = "/courses/new", method = RequestMethod.GET)
  public String provideCourse(@ModelAttribute("course") Course course) {
    return "create_course";
  }

  @RequestMapping(value = "/courses/new", method = RequestMethod.POST)
  public String newCourse(@ModelAttribute("course") Course course) {
    Course newCourse = new Course();
    newCourse.name = course.name;
    newCourse.department = course.department;
    newCourse.number = course.number;
    newCourse.credits = course.credits;
    newCourse.description = course.description;
    newCourse.objectives = course.objectives;
    repository.save(newCourse);
    return "redirect:/courses";
  }

  @RequestMapping("/course/{id}")
  public String course(@PathVariable String id, Model model) {
    Course course = repository.findOne(id);
    if (course != null) {
      model.addAttribute("course", repository.findOne(id));
      return "course";
    }
    return "redirect:/";
  }

  @RequestMapping(value = "/course/{id}", method = RequestMethod.DELETE)
  public String course(@PathVariable String id) {
    Course temp = repository.findOne(id);
    List<Section> tempSections = sectionRepository.findByCourse(temp);
    for (Section section : tempSections) {
      for (Student student : section.students) {
        student.sections.remove(section);
        studentRepository.save(student);
      }
      sectionRepository.delete(section);
    }
    repository.delete(id);
    return "redirect:/courses";
  }

  @RequestMapping(value = "/course/{id}/newsection", method = RequestMethod.GET)
  public String newSection(@ModelAttribute("section") Section section,
                           @PathVariable String id, Model model) {
    Course course = repository.findOne(id);
    model.addAttribute(course);
    return "create_section";
  }

  @RequestMapping(value = "/course/{id}/newsection", method = RequestMethod.POST)
  public String newSection(@ModelAttribute("section") Section section,
                           @PathVariable String id, String startTime, String endTime) {

    section = sectionRepository.save(section); //generate an ObjectID
    section.setStartAndEndTime(startTime, endTime);
    Course course = repository.findOne(id);
    List<Section> sections = course.getSections();
    sections.add(section);
    if (Section.conflicts(sections)) {
      return "redirect:/error/Schedule Time Conflict";
    }
    section.course = course;
    //Section savedSection = sectionRepository.save(section);
    course.addSection(section);
    repository.save(course);
    sectionRepository.save(section);

    return "redirect:/courses";
  }

  @RequestMapping(value = "/course/{id}", method = RequestMethod.POST)
  public String course(@ModelAttribute("course") Course course,
                       @PathVariable String id) {
    Course tempCourse = repository.findOne(id);
    tempCourse.name = course.name;
    tempCourse.department = course.department;
    tempCourse.number = course.number;
    tempCourse.credits = course.credits;
    tempCourse.description = course.description;
    tempCourse.objectives = course.objectives;
    repository.save(tempCourse);
    return "redirect:/courses";
  }

}
