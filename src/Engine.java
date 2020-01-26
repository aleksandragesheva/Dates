import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Engine implements Runnable{

    private static Scanner scanner = new Scanner(System.in);
    private static final int TOTAL_MINUTES_ALL_DAY = 1440;

    public Engine() {

    }

    @Override
    public void run() {

        Pattern pattern = Pattern.compile("(?<fYear>[0-9]+)-(?<fMonth>[0-9]+)-(?<fDay>[0-9]+)" +
                "\\s(?<fHour>[0-9]+):(?<fMinutes>[0-9]+):(?<fSeconds>[0-9]+);" +
                "(?<sYear>[0-9]+)-(?<sMonth>[0-9]+)-(?<sDay>[0-9]+)" +
                "\\s(?<sHour>[0-9]+):(?<sMinutes>[0-9]+):(?<sSeconds>[0-9]+)");

        String input = this.scanner.nextLine();

        Matcher matcher = pattern.matcher(input);

        if (matcher.find()){
            int fYear = Integer.parseInt(matcher.group(1));
            int fMonth = Integer.parseInt(matcher.group(2));
            int fDay = Integer.parseInt(matcher.group(3));
            int fHour = Integer.parseInt(matcher.group(4));
            int fMinutes = Integer.parseInt(matcher.group(5));
            int fSeconds = Integer.parseInt(matcher.group(6));
            int sYear = Integer.parseInt(matcher.group(7));
            int sMonth = Integer.parseInt(matcher.group(8));
            int sDay = Integer.parseInt(matcher.group(9));
            int sHour = Integer.parseInt(matcher.group(10));
            int sMinutes = Integer.parseInt(matcher.group(11));
            int sSeconds = Integer.parseInt(matcher.group(12));

            LocalDate startFirstDate = date(fYear, fMonth, fDay);
            LocalDate endFirstDate = date(sYear, sMonth, sDay);

            int minutes = 0;
            int totalMinutes = 0;
            StringBuilder sb = new StringBuilder();


            String start = String.format("%02d:%02d:%02d",
                    fHour, fMinutes, fSeconds);
            String end = "00:00:00";

            minutes = getMinutes(start, end);

            getDatesBetween(startFirstDate, endFirstDate).stream().limit(1).forEach(System.out::print);
            System.out.print(" " + start);
            System.out.print(" - ");
            getDatesBetween(startFirstDate.plusDays(1), endFirstDate.plusDays(1)).stream().limit(1).forEach(System.out::print);
            System.out.print(" " + end);
            System.out.println(" " + minutes);
            totalMinutes += minutes;

            for (int i = 1; i < getDatesBetween(startFirstDate, endFirstDate).size(); i++) {
                minutes = TOTAL_MINUTES_ALL_DAY;
                System.out.print(getDatesBetween(startFirstDate, endFirstDate).get(i));
                System.out.print(" " + end);
                System.out.print(" - ");
                System.out.print(getDatesBetween(startFirstDate.plusDays(1), endFirstDate.plusDays(1)).get(i));
                System.out.print(" " + end);
                System.out.println(" " + minutes);
                totalMinutes += minutes;
            }

            String startTime = "00:00:00";
            String endTime = String.format("%02d:%02d:%02d",
                    sHour, sMinutes, sSeconds);

            minutes = getMinutes(startTime, endTime);

            for (int i = getDatesBetween(startFirstDate, endFirstDate.plusDays(1)).size() - 1; i >= getDatesBetween(startFirstDate, endFirstDate.plusDays(1)).size() - 1; i--) {
                System.out.print(getDatesBetween(startFirstDate, endFirstDate.plusDays(1)).get(i));
                System.out.print(" " + startTime);
                System.out.print(" - ");
                System.out.print(getDatesBetween(startFirstDate, endFirstDate.plusDays(1)).get(i));
                System.out.print(" " + endTime);
                System.out.println(" " + minutes);
                totalMinutes += minutes;
            }

            System.out.println(totalMinutes);
        }
    }

    private List<LocalDate> getDatesBetween(LocalDate start, LocalDate end){
        return start.datesUntil(end).collect(Collectors.toList());
    }

    private LocalDate date(int year, int month, int day){
        return LocalDate.of(year, month, day);
    }

    private int getMinutes(String start, String end) {
        int minutes;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(start);
            date2 = format.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = date2.getTime() - date1.getTime();

        minutes = (int) TimeUnit.MILLISECONDS.toMinutes(difference);

        if (minutes < 0) {
            minutes += TOTAL_MINUTES_ALL_DAY;
        }
        return minutes;
    }
}
