package unbanner;


import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentControllerTest {

    @Autowired
    public StudentRepository repo;

    @Test //Not Empty!
    public void studentsList() throws Exception {
        repo.deleteAll();
        repo.save(new Student("Student1", "One", 90011));
        repo.save(new Student("Student2", "Two", 90022));
        repo.save(new Student("Student3", "Three", 90033));

        List<Student> listStudents = repo.findAll();
        int numStudents = listStudents.size();
        assertEquals(3, numStudents);
    }

    @Test
    public void studentsList_FalsePositive() throws Exception {
        repo.deleteAll();
        repo.save(new Student("Student1", "One", 90011));
        repo.save(new Student("Student2", "Two", 90022));
        repo.save(new Student("Student3", "Three", 90033));
        boolean mustNotExist =  repo.exists("1234567890!!!");
        assertFalse(mustNotExist);
    }

    @Test
    public void studentsList_empty() throws Exception {
        repo.deleteAll();
        List<Student> listStudents = repo.findAll();
        int numStudents = listStudents.size();
        assertEquals(0, numStudents);
    }


    @Test
    public void studentsList_findOne() throws Exception {
        repo.deleteAll();
        Student nStudent = new Student("Student1", "One", 900123);
        repo.save(nStudent);
        Student getStudent = repo.findOne(nStudent.getId());
        assertEquals(getStudent.firstName,nStudent.firstName);
        assertEquals(getStudent.lastName,nStudent.lastName);
        assertEquals(getStudent.getId(), nStudent.getId());
    }

    @Test
    public void provideStudents() throws Exception {
        // ?????
    }

    @Test
    public void newStudent() throws Exception {
        Student stud = new Student("new Student1", "One", 900);

        Student nStudent = new Student();
        nStudent.studentNum = stud.studentNum;
        nStudent.firstName = stud.firstName;
        nStudent.lastName = stud.lastName;
        repo.save(nStudent);
        assertTrue(repo.exists(nStudent.getId()));

        Student getStudent = repo.findOne(nStudent.getId());
        assertEquals(getStudent.firstName,nStudent.firstName);
        assertEquals(getStudent.lastName,nStudent.lastName);
        assertEquals(getStudent.getId(), nStudent.getId());


    }

    @Test
    public void student() throws Exception {
        // ??????
    }

    @Test  //By Id
    public void deleteStudent() throws  Exception {

        repo.deleteAll();
        Student nStudent = new Student("newStudent1", "One1", 900123);
        repo.save(nStudent);
        repo.delete(nStudent.getId());
        assertFalse(repo.exists(nStudent.getId()));
    }

    @Test
    public void deleteStudent_byObj() throws  Exception {

        repo.deleteAll();
        Student nStudent = new Student("newStudent1", "One1", 900123);
        repo.save(nStudent);
        repo.delete(nStudent);
        assertFalse( repo.exists(nStudent.getId()));
    }

    @Test  // Equals
    public void updateStudent() throws Exception {
        repo.deleteAll();
        Student nStudent = new Student("newStudent1", "One1", 900123);
        repo.save(nStudent);
        nStudent.firstName = "Modified first name";
        nStudent.lastName = "Modified last name";
        nStudent.studentNum = 999999;
        repo.save(nStudent);
        Student getStudent = repo.findOne(nStudent.getId());
        assertEquals(getStudent.firstName, nStudent.firstName);
    }

    @Test
    public void updateStudent_NotEquals() throws Exception {
        repo.deleteAll();
        Student nStudent = new Student("newStudent1", "One1", 900123);
        repo.save(nStudent);
        nStudent.firstName = "Modified first name";
        nStudent.lastName = "Modified last name";
        nStudent.studentNum = 999999;
        Student getStudent = repo.findOne(nStudent.getId());
        assertNotEquals(getStudent.firstName, nStudent.firstName);
    }

}