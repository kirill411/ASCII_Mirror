import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        short x = scanner.nextShort();
        short y = scanner.nextShort();

        System.out.println(y / x);

        scanner.close();
    }
}
