import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    int studentID;
    int enteringYear;
    String grade;
    double budget;
    String department;
    public static List<Student> studentList = new ArrayList<Student>();
    public void charger(int money){
        budget += money;
    }
}
