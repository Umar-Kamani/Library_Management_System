public class book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int genreid;
    private int locationid;
    private int totalcopies;
    private int availablecopies;

    public book(int id, String title, String author, String isbn, int genreId,
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
    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public String getIsbn() {return isbn;}
    public int getGenreId() {return genreid;}
    public int getLocationId() {return locationid;}
    public int getTotalCopies() {return totalcopies;}
    public int getAvailableCopies() {return availablecopies;}
}
