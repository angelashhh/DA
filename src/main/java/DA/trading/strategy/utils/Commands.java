package DA.trading.strategy.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;
import java.util.stream.Stream;

public class Commands {
    public static int[] readInputAsIntervals(Scanner scanner){
        System.out.println("Enter intervals m and n: ");
        String[] inputStrings = StringUtils.split(scanner.nextLine(), " ");
        return Stream.of(inputStrings).mapToInt(Integer::parseInt).toArray();
    }

}
