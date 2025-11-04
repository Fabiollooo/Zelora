package zelora.util;

public class ZeloraBanner {

    // ANSI escape codes for colours
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String YELLOW = "\u001B[33m";
    public static final String WHITE = "\u001B[37m";

    public static void main(String[] args) {
        printBanner();
        // Then launch your main menu, e.g.:
        // Menu.start();
    }

    public static void printBanner() {
        System.out.println(CYAN +
                "ZZZZZ  EEEEE  L      OOOOO  RRRR   AAAAA\n" +
                "   ZZ  E      L     O     O R   R  A   A\n" +
                "  ZZ   EEEE   L     O     O RRRR   AAAAA\n" +
                " ZZ    E      L     O     O R  R   A   A\n" +
                "ZZZZZ  EEEEE  LLLLL  OOOOO  R   R  A   A\n" +
                RESET);

        System.out.println(PURPLE + "Welcome to the Zelora Retail Management System" + RESET);
        System.out.println(YELLOW + "------------------------------------------------" + RESET);
    }
}
