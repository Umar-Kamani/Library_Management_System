package mu.alche.library.Models;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int genreid;
    private int locationid;
    private int totalcopies;
    private int availablecopies;

    public Book(int id, String title, String author, String isbn, int genreId,
                int locationId, int totalCopies, int availableCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genreid = genreId;
        this.locationid = locationId;
        this.totalcopies = totalCopies;
        this.availablecopies = availableCopies;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}

    public String getIsbn() {return isbn;}
    public void setIsbn(String isbn) {this.isbn = isbn;}

    public int getGenreId() {return genreid;}
    public void setGenreId(int genreId) {this.genreid = genreId;}

    public int getLocationId() {return locationid;}
    public void setLocationId(int locationId) {this.locationid = locationId;}

    public int getTotalCopies() {return totalcopies;}
    public void setTotalCopies(int totalCopies) {this.totalcopies = totalcopies;}

    public int getAvailableCopies() {return availablecopies;}
    public void setAvailableCopies(int availableCopies) {this.availablecopies = availablecopies;}

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", genre=" + genreid +
                ", location=" + locationid +
                ", totalCopies=" + totalcopies +
                ", availableCopies=" + availablecopies +
                '}';
    }


}
