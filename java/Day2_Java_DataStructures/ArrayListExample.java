import java.util.ArrayList;

public class ArrayListExample {
    public static void main(String[] args) {
        ArrayList<String> names = new ArrayList<>();

        names.add("Kusum");
        names.add("Rahul");
        names.add("Anita");

        for (String name : names) {
            System.out.println(name);
        }
    }
}
