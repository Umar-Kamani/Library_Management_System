package mu.alche.library.Models;

public class Faculty extends User {

    public Faculty(int id, String name, String phone, String email){
        super(id, name, phone, email);
    }

    @Override public int getMaxBooks() {
        return 10;
    };
    @Override public int getLoanDurationDays() {
        return 30;
    };
    @Override public String getRole() {
        return "Faculty";
    };

}
