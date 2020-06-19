import java.util.ArrayList;
import java.util.List;

public class Employee extends Person {
    String library;
    int[] schedule = new int[7];
    public void updateAttendanceList(int[] newSchedule){
        schedule = newSchedule.clone();
    }
}
