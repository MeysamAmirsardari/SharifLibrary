import java.util.ArrayList;
import java.util.List;

public class Professor extends Person{
    int enteringYear;
    int budget;
    String department;
    public static List<Professor> professorList = new ArrayList<Professor>();
    public void charger(int money){
        budget += money;
    }
}
