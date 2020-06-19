import java.util.ArrayList;
import java.util.List;

public class Book {
    String bookName;
    int bookPageNum;
    String publishedYear;
    String authorName;
    String language;
    String ISBN;
    String translatorName;
    int stock=1;
    int stockInStore=1;
    String libraryName;
    int price;
    List<String> borrowers = new ArrayList<String>();
}
