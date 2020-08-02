import java.util.ArrayList;
import java.util.List;

public class MainLib extends Store implements Library {

    public static List<Employee> employeesList = new ArrayList<Employee>();
    public static List<Book> bookList = new ArrayList<Book>();
    public static List<Book> generalBookList = new ArrayList<Book>();
    public static List<String> borrowedBooksList = new ArrayList<String>();
    public static List<Student> studentsList = new ArrayList<Student>();
    public static List<Professor> professorsList = new ArrayList<Professor>();
    public static String discountCode;
    public static double discountPercent;
    public static List<Book> shopList = new ArrayList<Book>();
    public static List<String> reimbursedBooks = new ArrayList<String>();
    public static List<String> soldBooks = new ArrayList<String>();
    public static List<String> storeReimbursedBooks = new ArrayList<String>();


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
    public void addEmployee(Employee employee){
        employeesList.add(employee);
    }

    @Override
    public void loanBook(String bookDetails, String memberType, String ID, String loanTime, Date loanDate, String GBD) {
        String[] details = bookDetails.split(",");
        if (findBookFromBookList(details[0],details[1])==1) {
            Book book = searchInBookList(details[0], details[1]);
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
            //todo Error!
        }
    }

    @Override
    public int giveBack(String bookDetails, String memberType, String ID, String time) {
        boolean checkResult = true;
        String[] details = bookDetails.split(",");
        if (findBookFromBookList(details[0],details[1])==1) {
            Book book = searchInBookList(details[0], details[1]);
            if (book!=null) {
                if (memberType.equalsIgnoreCase("Student")) {
                    Student student = Main.searchStudent(ID);
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
            } else {
                checkResult = false;
            }
            if (checkResult) {
                int index = searchInBorrowedBooks(book);
                String GBD = borrowedBooksList.get(index).split(",")[6];
                borrowedBooksList.remove(index);
                bookList.get(bookList.indexOf(book)).stock++;
                Employee employee = findWorker(time, Main.toDay.weekDay);
                String loanDetails = bookDetails + "," + memberType + "," + ID + "," + GBD + "," + Main.toDay.date + "," + employee.firstName;
                reimbursedBooks.add(loanDetails);
                return 1;
            }
        } else {
            return 1;
        }
        return 1;
    }

    @Override
    public void updateWorkPlan(String nationalID, int[] attendance) {
        int index=-1;
        for (Employee e : employeesList) {
            if (e.nationalID.equals(nationalID)){
                index = employeesList.indexOf(e);
                break;
            }
        }
        if (index>-1)
            employeesList.get(index).updateAttendanceList(attendance);
    }

    public static void addMember(Person person, boolean isStudent){
        if (isStudent)
            studentsList.add((Student) person);
        else
            professorsList.add((Professor) person);
    }

    @Override
    public void countPenalty() {
        int penalty = 0;
        for (String data : borrowedBooksList) {
            String[] details = data.split(",");
            Date giveBackDay = new Date(details[6]);
            int suppose = Date.deltaTime(giveBackDay,Main.toDay);
            if (suppose>0){
                penalty = suppose*1000;
                if (details[1].equalsIgnoreCase("Student"))
                    ((Student) findMember(details[2],details[1])).budget-=penalty;
                else
                    ((Professor) findMember(details[2],details[1])).budget-=penalty;
            }
        }
    }

    public static void addBookToStore (Book book){
        if (shopList.contains(book)){
            shopList.get(shopList.indexOf(book)).stockInStore++;
        } else {
            shopList.add(book);
            shopList.get(shopList.indexOf(book)).stockInStore=1;
        }
    }


    public static void charger (String ID, int money, boolean isStudent){
        if (isStudent) {
            for (Student student : studentsList) {
                if (student.studentID==Integer.parseInt(ID))
                    student.charger(money);
            }
        } else {
            for (Professor professor : professorsList) {
                if (professor.nationalID.equals(ID))
                    professor.charger(money);
            }
        }
    }

    public static int searchInLibrary(Book book) {
        boolean found = false;
        List<Book> firstSearch = new ArrayList<Book>();
        for (Book b : bookList)
            if (b.ISBN.equals(book.ISBN)) {
                firstSearch.add(b);
            }
        for (Book b2 : firstSearch) {
            if (b2.publishedYear.equals(book.publishedYear))
                found = true;
        }
        if (found)
            return 1;
        else
            return 0;
    }

    public static int searchInMembers (String ID, String type){
        if (type.equals("Student")){
            for (Student student : studentsList) {
                if (student.studentID==Integer.parseInt(ID))
                    return 1;
            }
        } else {
            for (Professor professor : professorsList) {
                if (professor.nationalID.equals(ID))
                    return 1;
            }
        }
        return 0;
    }

    public static Person findMember (String ID, String type){
        if (type.equals("Student")){
            for (Student student : studentsList) {
                if (student.studentID==Integer.parseInt(ID))
                    return student;
            }
        } else {
            for (Professor professor : professorsList) {
                if (professor.nationalID.equals(ID))
                    return professor;
            }
        }
        System.out.println("Person not found!");
        return null;//Todo
    }

    public static String findBook (String name, String ISBN, String year){
        for (Book book : bookList) {
            if (book.bookName.equals(name)&&book.ISBN.equals(ISBN)&&book.publishedYear.equals(year))
                return book.libraryName;
        }
        return "notFound";
    }

    public static Book searchInBookList (String ISBN, String year){
        List<Book> firstSearch = new ArrayList<Book>();
        for (Book book : bookList)
            if (book.ISBN.equals(ISBN)) {
                firstSearch.add(book);
            }
        for (Book b2 : firstSearch) {
            if (b2.publishedYear.equals(year))
                return b2;
        }
        return null;
    }

    public static int findBookFromBookList (String ISBN, String year){
        List<Book> firstSearch = new ArrayList<Book>();
        for (Book book : bookList)
            if (book.ISBN.equals(ISBN)) {
                firstSearch.add(book);
            }
        for (Book b2 : firstSearch) {
            if (b2.publishedYear.equals(year))
                return 1;
        }
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
            if (findBookFromBookList(details[0],details[1])==1) {
                Book book1 = searchInBookList(details[0], details[1]);
                if (book.equals(book1))
                    return borrowedBooksList.indexOf(s);
            }
        }
        return -1;
    }

    public static int searchInSoldBooks(Book book){
        for (String s : soldBooks) {
            String[] details = s.split(",");
            if (findBookFromBookList(details[0],details[1])==1) {
                Book book1 = searchInBookList(details[0], details[1]);
                if (book.equals(book1))
                    return borrowedBooksList.indexOf(s);
            }
        }
        return -1;
    }
}
