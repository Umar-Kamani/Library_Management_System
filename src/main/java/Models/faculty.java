package Models;

public class faculty extends user {

    public faculty(int id, String name, String phone, String email){
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
