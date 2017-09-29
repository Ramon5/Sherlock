package util;

import java.net.URL;

/**
 *
 * @author root
 */
public class BookInfo {

    public String bookName;
    public URL bookURL;
    

    public BookInfo(String book, URL url) {
        bookName = book;
        bookURL = url;
    }

    @Override
    public String toString() {
        return bookName;
    }
}
