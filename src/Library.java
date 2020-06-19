import java.util.List;

public interface Library {
    int employeeNum = 15;
    int libArea = 500;
    int roomNum = 8;
    int bookLimit = 30;

    public void addBook(Book book);
    public void addEmployee(Employee employee);
    public void loanBook(String bookDetails, String memberType, String ID, String loanTime, Date loanDate, String GBD);
    public void giveBack(String bookDetails, String memberType, String ID, String time);
    public void updateWorkPlan(String nationalID, int[] attendance);
    public void countPenalty();
}
