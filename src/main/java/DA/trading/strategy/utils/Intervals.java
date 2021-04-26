package DA.trading.strategy.utils;

import DA.trading.strategy.prices.TimestampPrice;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Intervals {
    private int m;
    private int n;

    public int getM() {return m;}
    public int getN() {return n;}

    public static int[] readInputAsIntervals(){
        System.out.println("Enter intervals m and n: ");
        Scanner scanner = new Scanner(System.in);
        String[] inputStrings = StringUtils.split(scanner.nextLine(), " ");
        return Stream.of(inputStrings).mapToInt(Integer::parseInt).toArray();
    }
}
