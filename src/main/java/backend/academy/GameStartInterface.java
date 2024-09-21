package backend.academy;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Scanner;
import lombok.Getter;

@SuppressFBWarnings("DM_EXIT")
public class GameStartInterface {
    private static final int MAX_MENU_NUMBER = 3;
    private final Scanner scanner;
    private final PrintStream out;
    private final SecureRandom random;
    private final String incorrectInputMessage = "Некорректный ввод";

    @Getter private Integer selectedDifficultyLevel;
    @Getter private Integer selectedCategory;

    protected GameStartInterface(InputStream in, PrintStream out) {
        this.scanner = new Scanner(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.out = out;
        this.random = new SecureRandom();
    }

    protected void askStart(String message) {
        out.println(message);
        String line = scanner.nextLine();
        switch (line) {
            case "1":
                break;
            case "2":
                out.println("Правила игры");
                askStart(message);
                break;
            case "3":
                System.exit(0);
                break;
            default:
                out.println(incorrectInputMessage);
                askStart(message);
        }
    }

    protected int ask(String message) {
        out.println(message);
        String line = scanner.nextLine();
        return switch (line) {
            case "1", "2", "3" -> Integer.parseInt(line);
            case "4", "" -> random.nextInt(MAX_MENU_NUMBER) + 1;
            default -> {
                out.println(incorrectInputMessage);
                yield ask(message);
            }
        };
    }

    protected void render() {
        askStart("""
            Игра Виселица
            Добро пожаловать в игру Виселица,
            для старта введи на клавиатуре 1 и нажми Enter!
            [1] - Начать
            [2] - Правила
            [3] - Выйти""");
        this.selectedDifficultyLevel = ask("""
            Отлично, давай выберем уровень сложности
            [1] - Легко
            [2] - Средне
            [3] - Сложно
            [4] - Пропустить и выбрать рандомно""");
        this.selectedCategory = ask("""
            И давай выберем категорию слов
            [1] - Животные
            [2] - Города
            [3] - Фрукты и овощи
            [4] - Пропустить и выбрать рандомно""");
    }
}
