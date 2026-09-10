package Models;

public class student extends user {

    public student(int id, String name, String phone, String email){
        super(id, name, phone, email);
    }

    @Override public int getMaxBooks() {
        return 3;
    };
    @Override public int getLoanDurationDays() {
        return 14;
    };
    @Override public String getRole() {
        return "Student";
    };

}


