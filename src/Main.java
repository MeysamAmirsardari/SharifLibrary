import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static List<Person> personList = new ArrayList<Person>();
    public static List<Professor> professorList = new ArrayList<Professor>();
    public static List<Student> studentList = new ArrayList<Student>();
    public static Date toDay;

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        String[] input = new String[2000];
        String[] dateArray = new String[5];
        List<String[]> inputLine = new ArrayList<String[]>();
        int i=0;
        MainLib mainLib = new MainLib();
        LibraryA libraryA = new LibraryA();
        LibraryB libraryB = new LibraryB();

        input[0] = inputScanner.nextLine();
        toDay = new Date(input[0].split(" ")[2],0);
        Date firstDate = toDay.clone();
        i++;
        int j=0;

        while (!(input[i] = inputScanner.nextLine()).endsWith("END")){
            inputLine.add(input[i].split(" "));
            if (inputLine.get(j)[0].equals("Create")){
                if (inputLine.get(j)[1].equals("Book")){
                    Book book = createBook(inputLine.get(j));
                    int st = MainLib.searchInLibrary(book);
                    if (st==0)
                        MainLib.generalBookList.add(book);
                    else
                        MainLib.generalBookList.get(MainLib.generalBookList.indexOf(book)).stock++;
                } else if (inputLine.get(j)[1].equals("Person")){
                    Person person = createPerson(inputLine.get(j));
                    personList.add(person);
                }
            }else if (inputLine.get(j)[0].equals("Add")){
                if (inputLine.get(j)[1].equals("Book")){
                    if (inputLine.get(j)[2].equalsIgnoreCase("Store")){
                        String[] bookDetails = inputLine.get(j)[4].split(",");
                        Book book = searchInBookList(bookDetails[0],bookDetails[1],bookDetails[2]);
                        MainLib.addBookToStore(book);
                    } else {
                        String[] bookDetails = inputLine.get(j)[3].split(",");
                        Book book = searchInBookList(bookDetails[0],bookDetails[1],bookDetails[2]);
                        MainLib.generalBookList.add(book);
                        if (inputLine.get(j)[2].equalsIgnoreCase("MainLibrary"))
                            mainLib.addBook(book);
                        else if (inputLine.get(j)[2].equalsIgnoreCase("a"))
                            libraryA.addBook(book);
                        else if (inputLine.get(j)[2].equalsIgnoreCase("b"))
                            libraryB.addBook(book);
                    }
                } else if (inputLine.get(j)[1].equals("Person")){
                    if (inputLine.get(j)[2].equals("Student")){
                        Person student = searchStudent(inputLine.get(j)[3]);
                        mainLib.addMember(student,true);
                    } else if (inputLine.get(j)[2].equals("Professor")){
                        Professor professor = searchProfessor(inputLine.get(j)[3]);
                        mainLib.addMember(professor, false);
                    }
                } else if (inputLine.get(j)[1].equals("Worker")){
                    Employee employee = (Employee) searchInPersonList(inputLine.get(j)[2]);
                    if (inputLine.get(j)[3].equalsIgnoreCase("mainLibrary")){
                        mainLib.addEmployee(employee);
                    } else if (inputLine.get(j)[3].equalsIgnoreCase("a")){
                        libraryA.addEmployee(employee);
                    } else if (inputLine.get(j)[3].equalsIgnoreCase("b")){
                        libraryB.addEmployee(employee);
                    }
                }
            } else if (inputLine.get(j)[0].equals("Deposit")){
                if (inputLine.get(j)[1].equals("Student")){
                    MainLib.charger(inputLine.get(j)[2],Integer.parseInt(inputLine.get(j)[3]),true);
                } else if (inputLine.get(j)[1].equals("Professor")){
                    MainLib.charger(inputLine.get(j)[2],Integer.parseInt(inputLine.get(j)[3]),false);
                }
            } else if (inputLine.get(j)[0].equals("Set")){
                if (inputLine.get(j)[1].equalsIgnoreCase("Schedule")) {
                    setSchedule(inputLine.get(j));
                } else if (inputLine.get(j)[1].equalsIgnoreCase("DiscountCode")){
                    MainLib.discountCode = inputLine.get(j)[2];
                    MainLib.discountPercent = Double.parseDouble(inputLine.get(j)[3]);
                }
            } else if (inputLine.get(j)[0].equals("Find")){
                if (MainLib.searchInMembers(inputLine.get(j)[6],inputLine.get(j)[5])==1){
                    String[] bookData = inputLine.get(j)[2].split(",");
                    String lib = MainLib.findBook(bookData[0],bookData[1],bookData[2]);
                    if (!(lib.equals("notFound"))){
                        System.out.println(lib);
                    }
                }
            } else if (inputLine.get(j)[0].equals("Loan")) {
                if (MainLib.searchInMembers(inputLine.get(j)[6], inputLine.get(j)[5]) == 1) {
                    double applicantsBudget = 0;
                    if (inputLine.get(j)[4].equalsIgnoreCase("Student"))
                        applicantsBudget = searchStudent(inputLine.get(j)[5]).budget;
                    else if (inputLine.get(j)[4].equalsIgnoreCase("Professor"))
                        applicantsBudget = searchProfessor(inputLine.get(j)[5]).budget;
                    if (applicantsBudget > -10000) {
                        if (inputLine.get(j)[3].equalsIgnoreCase("mainLibrary")) {
                            mainLib.loanBook(inputLine.get(j)[2], inputLine.get(j)[4],
                                    inputLine.get(j)[5], inputLine.get(j)[6], toDay, inputLine.get(j)[7]);
                        } else if (inputLine.get(j)[3].equalsIgnoreCase("a")) {
                            libraryA.loanBook(inputLine.get(j)[2], inputLine.get(j)[4],
                                    inputLine.get(j)[5], inputLine.get(j)[6], toDay, inputLine.get(j)[7]);
                        } else if (inputLine.get(j)[3].equalsIgnoreCase("b")) {
                            libraryB.loanBook(inputLine.get(j)[2], inputLine.get(j)[4],
                                    inputLine.get(j)[5], inputLine.get(j)[6], toDay, inputLine.get(j)[7]);
                        }
                    }
                }
            }else if (inputLine.get(j)[0].equalsIgnoreCase("GiveBack")){
                if (inputLine.get(j)[1].equalsIgnoreCase("Store")){
                    int re = MainLib.giveBackToStore(inputLine.get(j)[2],inputLine.get(j)[3],inputLine.get(j)[4],inputLine.get(j)[5]);
                    if (re==0)
                        System.out.println(j+")"+"Wrong input!");
                } else {
                    if (inputLine.get(j)[3].equalsIgnoreCase("mainLibrary")) {
                        mainLib.giveBack(inputLine.get(j)[2], inputLine.get(j)[4], inputLine.get(j)[5], inputLine.get(j)[6]);
                    } else if (inputLine.get(j)[3].equalsIgnoreCase("a")) {
                        libraryA.giveBack(inputLine.get(j)[2], inputLine.get(j)[4], inputLine.get(j)[5], inputLine.get(j)[6]);
                    } else if (inputLine.get(j)[3].equalsIgnoreCase("b")) {
                        libraryB.giveBack(inputLine.get(j)[2], inputLine.get(j)[4], inputLine.get(j)[5], inputLine.get(j)[6]);
                    }
                }
            } else if (inputLine.get(j)[0].equals("Next")){
                int nextNum=0;
                if (inputLine.get(j).length==2){
                    nextNum=1;
                } else {
                    nextNum = Integer.parseInt(inputLine.get(j)[3]);
                }
                toDay.updateDate(nextNum);
                mainLib.countPenalty();
                libraryA.countPenalty();
                libraryB.countPenalty();
            } else if (inputLine.get(j)[0].equals("Sell")){
                String[] bookDetails = inputLine.get(j)[2].split(",");
                Book book = searchInBookList(bookDetails[0],bookDetails[1],bookDetails[2]);
                double price = book.price;
                if (inputLine.get(j).length==7){
                    if (MainLib.discountCode.equals(inputLine.get(j)[6])){
                        price = ((100-MainLib.discountPercent)*price)/100;
                    } else {
                        System.out.println("invalid discount code!");
                    }
                    MainLib.sellBook(book,inputLine.get(j)[3],inputLine.get(j)[4],inputLine.get(j)[5],price);
                }
            }
            i++;
            j++;
        }
        // outputs:
        System.out.println("** Students data:");
        for (Student student : studentList) {
            System.out.println(student.firstName+" : ("+student.studentID+") budget:"+student.budget);
        }
        System.out.println("** Professors data:");
        for (Professor professor : professorList) {
            System.out.println(professor.firstName+" : ("+professor.nationalID+") budget:"+professor.budget);
        }
        System.out.println("*** MainLibrary ***");
        System.out.println("Sold books:");
        for (String soldBook : MainLib.soldBooks) {
            System.out.println(soldBook);
        }
        System.out.println("Borrowed books:");
        for (String s : MainLib.borrowedBooksList) {
            System.out.println(s);
        }
        System.out.println("Reimbursed Books:");
        for (String reimbursedBook : MainLib.reimbursedBooks) {
            System.out.println(reimbursedBook);
        }
        System.out.println("Reimbursed Books to stores:");
        for (String storeReimbursedBook : MainLib.storeReimbursedBooks) {
            System.out.println(storeReimbursedBook);
        }

        System.out.println("*** LibraryA ***");
        System.out.println("Borrowed books:");
        for (String s : LibraryA.borrowedBooksList) {
            System.out.println(s);
        }
        System.out.println("Reimbursed Books:");
        for (String reimbursedBook : LibraryA.reimbursedBooks) {
            System.out.println(reimbursedBook);
        }

        System.out.println("*** LibraryB ***");
        System.out.println("Borrowed books:");
        for (String s : LibraryB.borrowedBooksList) {
            System.out.println(s);
        }
        System.out.println("Reimbursed Books:");
        for (String reimbursedBook : LibraryB.reimbursedBooks) {
            System.out.println(reimbursedBook);
        }
        System.out.println("MainLibrarys workers data:");
        for (Employee employee : MainLib.employeesList) {
            System.out.println("Schedule -> "employee.firstName+" : "+employee.schedule.toString());
        }
    }

    public static Book createBook (String[] input){
        Book book = new Book();
        book.bookName = input[2];
        book.bookPageNum = Integer.parseInt(input[3]);
        book.publishedYear = input[4];
        book.authorName = input[5];
        book.language = input[6];
        book.ISBN = input[7];
        book.price = Integer.parseInt(input[8]);
        if (input.length==10)
            book.translatorName = input[9];
        return book;
    }

    public static Person createPerson (String[] input){
        if (input[2].equals("Student")){
            Student student = new Student();
            student.firstName = input[3];
            student.age = Integer.parseInt(input[4]);
            student.nationalID = input[5];
            student.isMale = input[6].equals("M");
            student.studentID = Integer.parseInt(input[7]);
            student.enteringYear = Integer.parseInt(input[8]);
            student.grade = input[9];
            student.budget = Integer.parseInt(input[10]);
            student.department = input[11];
            studentList.add(student);
            return student;
        } else if (input[2].equals("Professor")){
            Professor professor = new Professor();
            professor.firstName = input[3];
            professor.age = Integer.parseInt(input[4]);
            professor.nationalID = input[5];
            professor.isMale = input[6].equals("M");
            professor.enteringYear = Integer.parseInt(input[7]);
            professor.budget = Integer.parseInt(input[8]);
            professor.department = input[9];
            professorList.add(professor);
            return professor;
        } else if (input[2].equals("Worker")){
            Employee employee = new Employee();
            employee.firstName = input[3];
            employee.age = Integer.parseInt(input[4]);
            employee.nationalID = input[5];
            employee.isMale = input[6].equals("M");
            employee.library = input[7];
            return employee;
        } else return null;
    }

    public static Book searchInBookList(String name, String ISBN, String year){
        int publishedYear = Integer.parseInt(year);
        for (Book book : MainLib.generalBookList) {
            if (book.ISBN.equals(ISBN)&&(book.publishedYear.equals(year))&&book.bookName.equals(name)){
                return book;
            }
        }
        return null;
    }

    public static Student searchStudent(String studentID){
        for (Student student : studentList) {
            if (student.studentID==Integer.parseInt(studentID)){
                return student;
            }
        }
        return null;
    }

    public static Professor searchProfessor (String NC){
        for (Professor professor : professorList) {
            if (professor.nationalID.equals(NC))
                return professor;
        }
        return null;
    }

    public static Person searchInPersonList(String NC){
        for (Person person : personList) {
            if (person.nationalID.equals(NC))
                return person;
        }
        return null;
    }

    public static void setSchedule (String[] input){
        Employee employee = (Employee) searchInPersonList(input[2]);
        int[] schedule = new int[7];
        for (int i = 0; i < 6 ; i++) {
            schedule[i] = Integer.parseInt(input[4+i]);
        }
        employee.updateAttendanceList(schedule);
    }
}
