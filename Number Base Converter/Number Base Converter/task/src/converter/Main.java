
package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

public class Main {
    final static Scanner scanner = new Scanner(System.in);
    final static String symbols = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {

        while (true) {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            String input = scanner.nextLine();
            if ("/exit".equals(input)) {
                return;
            }
            numberBaseConverter(input);
        }
    }

    static void numberBaseConverter(String input) {
        String[] arr = input.split(" ");
        int sourceBase = Integer.parseInt(arr[0]);
        int targetBase = Integer.parseInt(arr[1]);

        while (true) {
            System.out.printf("Enter number in base %d to convert to base %d (To go back type /back) ",
                    sourceBase, targetBase);
            String s = scanner.nextLine();
            if ("/back".equals(s)) {
                return;
            }

            BigDecimal sourceNumber = sourceBase == 10 ? new BigDecimal(s) : toDecimalConverter(s, sourceBase);
            String result = fromDecimalConverter(sourceNumber, targetBase);

            System.out.println("Conversion result: " + result);
            System.out.println();
        }
    }

    static String fromDecimalConverter(BigDecimal n, int base) {
        if (base == 10) {
            return n.toPlainString();
        }

        StringBuilder sb = new StringBuilder();
        BigInteger bigBase = BigInteger.valueOf(base);
        BigInteger intPart = n.toBigInteger();
        BigDecimal fractPart = n.subtract(new BigDecimal(intPart));

        while (true) {
            int remainder = intPart.remainder(bigBase).intValue();
            sb.append(symbols.charAt(remainder));

            if (intPart.divide(bigBase).compareTo(BigInteger.ZERO) == 0) {
                break;
            }
            intPart = intPart.divide(bigBase);
        }

        sb.reverse();
        if (n.toString().contains(".")) { //!!!!!
            sb.append(".");
        }
        

        for (int i = 0; i < n.scale(); i++) {
            fractPart = fractPart.multiply(BigDecimal.valueOf(base));
            int value = fractPart.intValue();
            sb.append(symbols.charAt(value));

            if (fractPart.compareTo(BigDecimal.ONE) > 0) {
                fractPart = fractPart.subtract(new BigDecimal(fractPart.toBigInteger()));
            }


        }
        return sb.toString();
    }

    static BigDecimal toDecimalConverter(String source, int base) {
        int scale = source.indexOf('.') < 0 ? 0 : source.length() - 1 - source.indexOf('.');

        Deque<String> deque = new ArrayDeque<>(List.of(source.split("")));
        int power = source.indexOf('.') > 0 ? source.indexOf('.') : deque.size();
        BigDecimal res = new BigDecimal("0");

        while (!deque.isEmpty()) {
            if (deque.peek().equals(".")) {
                deque.poll();
                continue;
            }
            int n = symbols.indexOf(deque.poll());
            BigDecimal temp = BigDecimal.valueOf(n * Math.pow(base, --power));
            res = res.add(temp);
        }
        return res.setScale(Math.min(scale, 5), RoundingMode.HALF_UP);
    }
}
