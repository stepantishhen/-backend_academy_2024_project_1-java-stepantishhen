package backend.academy;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Scanner;
import lombok.Getter;

@SuppressWarnings({"checkstyle:MultipleStringLiterals"})
@SuppressFBWarnings("DM_EXIT")
public class GameStartInterface {
    private static final int MAX_MENU_NUMBER = 3;
    private final Scanner scanner;
    private final PrintStream out;
    private final SecureRandom random;

    @Getter private Integer selectedDifficultyLevel;
    @Getter private Integer selectedCategory;

    public GameStartInterface(InputStream in, PrintStream out) {
        this.scanner = new Scanner(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.out = out;
        this.random = new SecureRandom();
    }

    private void askStart(String message) {
        out.println(message);
        String line = scanner.nextLine();
        switch (line) {
            case "1":
                break;
            case "2":
                out.println("Help info");
                askStart(message);
                break;
            case "3":
                System.exit(0);
                break;
            default:
                out.println("Invalid input");
                askStart(message);
        }
    }

    private int ask(String message) {
        out.println(message);
        String line = scanner.nextLine();
        switch (line) {
            case "1":
            case "2":
            case "3":
                return Integer.parseInt(line);
            case "4", "":
                return random.nextInt(MAX_MENU_NUMBER) + 1;
            default:
                out.println("Invalid input");
                return ask(message);
        }
    }

    public void render() {
        askStart("""
            Hangman game
            Hello, this is a Hangman game,
            to start playing, enter 1 on the keyboard!
            [1] - Start
            [2] - Help
            [3] - Exit""");
        this.selectedDifficultyLevel = ask("""
            Okay, let's choose the difficulty level
            [1] - Easy
            [2] - Medium
            [3] - Hard
            [4] - Skip and choose random""");
        this.selectedCategory = ask("""
            And choose a category
            [1] - Animals
            [2] - Cities
            [3] - Fruits and Vegetables
            [4] - Skip and choose random""");
    }
}
