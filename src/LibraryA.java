import java.util.ArrayList;
import java.util.List;

public class LibraryA implements Library {
    public static List<Employee> employeesList = new ArrayList<Employee>();
    public static List<Book> bookList = new ArrayList<Book>();
    public static List<String> borrowedBooksList = new ArrayList<String>();
    public static List<String> reimbursedBooks = new ArrayList<String>();

    @Override
    public void addBook(Book book) {
        int searchResult = searchInLibrary(book);
        if (searchResult==0){
            bookList.add(book);
            bookList.get(bookList.indexOf(book)).stock=1;
        } else {
            int index = bookList.indexOf(book);
            bookList.get(index).stock++;
        }
    }

    @Override
    public void loanBook(String bookDetails, String memberType, String ID, String loanTime, Date loanDate, String GBD) {
        String[] details = bookDetails.split(",");

        if (findBookFromBookList(details[0],details[1],details[2])==1) {
            Book book = searchInBookList(details[0], details[1], details[2]);
            bookList.get(bookList.indexOf(book)).stock--;
            Employee employee = findWorker(loanTime, loanDate.weekDay);
            String loanDetails = bookDetails + "," + memberType + "," + ID + "," + loanDate.date + "," + GBD + "," + employee.firstName;
            borrowedBooksList.add(loanDetails);
            if (memberType.equalsIgnoreCase("Student")) {
                Student student = Main.searchStudent(ID);
                book.borrowers.add(student.firstName);
            } else if (memberType.equalsIgnoreCase("Professor")) {
                Professor professor = Main.searchProfessor(ID);
                book.borrowers.add(professor.firstName);
            }
        } else {
            //TODO: Error!
        }
    }


    @Override
    public void addEmployee(Employee employee) {
        employeesList.add(employee);
    }

    @Override
    public int giveBack(String bookDetails, String memberType, String ID, String time) {
        boolean checkResult = true;
        String[] details = bookDetails.split(",");
        Book book = LibraryA.searchInBookList(details[0],details[1],details[2]);
        if (findBookFromBookList(details[0],details[1],details[2])==1) {
            if (!(book.equals(null))) {
                if (memberType.equalsIgnoreCase("Student")) {
                    Student student = Main.searchStudent(ID);
                    if (!book.borrowers.isEmpty()) {
                        if ((student.studentID != Integer.parseInt(ID)) ||
                                (!(book.borrowers.get(book.borrowers.size() - 1).equals(student.firstName)))) {
                            checkResult = false;
                        }
                    } else if (memberType.equalsIgnoreCase("Professor")) {
                        Professor professor = Main.searchProfessor(ID);
                        if (!((professor.nationalID.equals(ID)) &&
                                (book.borrowers.get(book.borrowers.size() - 1).equals(professor.firstName)))) {
                            checkResult = false;
                        }
                    }
                } else
                    checkResult=false;
            } else {
                checkResult = false;
            }
            if (checkResult) {
                int index = searchInBorrowedBooks(book);
                if (index>=0) {
                    String GBD = borrowedBooksList.get(index).split(",")[6];
                    borrowedBooksList.remove(index);
                    bookList.get(bookList.indexOf(book)).stock++;
                    Employee employee = findWorker(time, Main.toDay.weekDay);
                    String loanDetails = bookDetails + "," + memberType + "," + ID + "," + GBD + "," + Main.toDay.date + "," + employee.firstName;
                    reimbursedBooks.add(loanDetails);
                    return 1;
                }
            } else
                return -1;
        } else {
            return -1;
        }
        return 1;
    }

    @Override
    public void updateWorkPlan(String nationalID, int[] attendance) {

    }


    @Override
    public void countPenalty() {
        int penalty = 0;
        for (String data : borrowedBooksList) {
            String[] detals = data.split(",");
            Date giveBackDay = new Date(detals[6]);
            int suppose = Date.deltaTime(giveBackDay,Main.toDay);
            if (suppose>0){
                penalty = suppose*1000;
                if (detals[1].equalsIgnoreCase("Student"))
                    ((Student) MainLib.findMember(detals[2],detals[1])).budget-=penalty;
                else
                    ((Professor) MainLib.findMember(detals[2],detals[1])).budget-=penalty;
            }
        }
    }

    public static int searchInLibrary(Book book) {
        boolean found = false;
        List<Book> firstSearch = new ArrayList<Book>();
        List<Book> secondSearch = new ArrayList<Book>();
        for (Book b : bookList)
            if (b.bookName.equals(book.bookName)) {
                firstSearch.add(b);
            }
        for (Book b2 : firstSearch)
            if (b2.publishedYear.equals(book.publishedYear)){
                secondSearch.add(b2);
            }
        for (Book b3 : secondSearch) {
            if (b3.translatorName.equals(book.translatorName))
                found = true;
        }
        if (found)
            return 1;
        else
            return 0;
    }

    public static Book searchInBookList (String bookName, String year, String translator){
        List<Book> firstSearch = new ArrayList<Book>();
        List<Book> secondSearch = new ArrayList<Book>();
        for (Book b : bookList)
            if (b.bookName.equals(bookName)) {
                firstSearch.add(b);
            }
        for (Book b2 : firstSearch)
            if (b2.publishedYear.equals(year)){
                secondSearch.add(b2);
            }
        for (Book b3 : secondSearch) {
            if (b3.translatorName.equals(translator))
                return b3;
        }
        return null;//todo
    }

    public static int findBookFromBookList (String bookName, String year, String translator){
        boolean found=false;
        List<Book> firstSearch = new ArrayList<Book>();
        List<Book> secondSearch = new ArrayList<Book>();
        for (Book b : bookList)
            if (b.bookName.equals(bookName)) {
                firstSearch.add(b);
            }
        for (Book b2 : firstSearch)
            if (b2.publishedYear.equals(year)){
                secondSearch.add(b2);
            }
        for (Book b3 : secondSearch) {
            if (b3.translatorName.equals(translator))
                found=true;
        }
        if (found)
            return 1;
        else
            return 0;
    }

    public static Employee findWorker (String time, int weekDay){
        String[] times = time.trim().split(":");
        List<Employee> workerListInDay = new ArrayList<Employee>();
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);
        for (int i = 0; i < employeesList.size() ; i++) {
            if (employeesList.get(i).schedule[weekDay]==1)
                workerListInDay.add(employeesList.get(i));
        }
        int workTime = 12/(workerListInDay.size());
        int index = (hour-8)/workTime;
        return workerListInDay.get(index);
    }

    public static int searchInBorrowedBooks(Book book){
        for (String s : borrowedBooksList) {
            String[] details = s.split(",");
            if (findBookFromBookList(details[0],details[1],details[3])==1) {
                Book book1 = searchInBookList(details[0], details[1], details[2]);
                if (book.equals(book1))
                    return borrowedBooksList.indexOf(s);
            }
        }
        return -1;
    }
}
