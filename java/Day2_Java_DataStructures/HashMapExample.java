import java.util.HashMap;

public class HashMapExample {
    public static void main(String[] args) {
        HashMap<Integer, String> students = new HashMap<>();

        students.put(1, "Kusum");
        students.put(2, "Aman");
        students.put(3, "Neha");

        System.out.println(students.get(1));
    }
}
