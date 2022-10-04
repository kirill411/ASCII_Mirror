package asciimirror;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Input file path:");
        String pathToFile = sc.nextLine();
        sc.close();

        List<String> pictureBuffer = new ArrayList<>();


        try (var fileScanner = new Scanner(new File(pathToFile))) {
            int len = 0;

            while (fileScanner.hasNext()) {
                String s = fileScanner.nextLine();
                len = Math.max(s.length(), len);
                pictureBuffer.add(s);
            }

            for (String line : pictureBuffer) {
                System.out.printf("%-" + len + "s | %" + len + "s%n", line , lineReverse(line));
            }

        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    private static String lineReverse(String input) {
        String origin = "<>{}[]()/\\";
        String reversed = "><}{][)(\\/";
        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            int index = origin.indexOf(c);
            c = index < 0 ? c : reversed.charAt(index);
            sb.append(c);
        }

        return sb.reverse().toString();
    }
    //    ./ASCII Mirror/task/test/example2.txt
}