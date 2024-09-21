package backend.academy;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import lombok.Getter;

public class GameProcessInterface {
    private final Scanner scanner;
    private final PrintStream out;
    // You can edit this constants
    private final static int ATTEMPTS = 7;
    private final static Integer SHOW_HINT_ON_ATTEMPT = 4;
    private final static String AVAILABLE_LETTERS = "а б в г д е ё ж з и й к л м н о п р с т у ф х ц ч ш щ ъ ы ь э ю я";
    @Getter private int attempt;
    @Getter private StringBuilder currentDisplay;
    @Getter private boolean winner;

    protected GameProcessInterface(InputStream in, PrintStream out) {
        this.scanner =
            new Scanner(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.out = out;
    }

    protected void render(List<String> currentWord) throws Exception {
        attempt = ATTEMPTS;
        String availableLetters = AVAILABLE_LETTERS;
        if (currentWord.getFirst().isEmpty()) {
            throw new IllegalArgumentException("Некорректное слово в словарике!");
        }
        out.println("""
            С каждой верной буквой ты приближаешься к победе,
            если буква введена неверно - я буду рисовать виселицу и висельника с каждым шагом"""
        );
        hideWord(currentWord.getFirst());
        out.println(currentDisplay.toString());
        winner = false;
        while (attempt > 0 && !winner) {
            out.println(availableLetters);
            if (ATTEMPTS - attempt == SHOW_HINT_ON_ATTEMPT) {
                out.println(currentWord.get(1));
            }
            out.println("Попыток осталось: " + attempt);
            out.print("буква>>> ");
            String input = scanner.next().trim().toLowerCase(); // Читаем всю строку
            if (input.length() > 1) { // Проверка длины строки
                out.println("Введите только одну букву!");
                continue; // Переход к следующему циклу
            }
            Character letter = input.charAt(0); // Получение первого символа
            if (foundLetter(currentWord.getFirst(), letter)) {
                openLetterInWord(currentWord.getFirst(), letter);
            } else {
                attempt--;
            }
            drawGallows();
            out.println(currentDisplay.toString());
            availableLetters = availableLetters.replace(letter, ' ');

            // Check for win
            if (checkWin(currentWord.getFirst())) {
                winner = true;
                out.println("Ты выиграл! Поздравляю!");
            }
        }
        if (!winner) {
            out.println("Ты проиграл!");
        }
    }

    private void drawGallows() {
        int stageIndex = GallowsStages.getTotalStages() - (ATTEMPTS - attempt) - 1;
        out.println(GallowsStages.getStage(stageIndex));
    }

    protected void hideWord(String word) {
        StringBuilder hiddenWord = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (Character.isLetter(word.charAt(i))) {
                hiddenWord.append("_ ");
            } else if (word.charAt(i) == ' ') {
                hiddenWord.append("  ");
            }
        }
        currentDisplay = hiddenWord;
    }

    protected boolean foundLetter(String word, Character letter) {
        return word.toLowerCase().indexOf(Character.toLowerCase(letter)) != -1;
    }

    protected void openLetterInWord(String word, Character letter) {
        for (int i = 0; i < word.length(); i++) {
            if (letter.equals(Character.toLowerCase(word.charAt(i)))) {
                currentDisplay.setCharAt(i * 2, word.charAt(i));
            }
        }
    }

    private boolean checkWin(String word) {
        StringBuilder spam = new StringBuilder();
        for (int i = 0; i < currentDisplay.length(); i++) {
            if (currentDisplay.charAt(i) != ' ') {
                spam.append(currentDisplay.charAt(i));
            }
        }
        return spam.toString().equals(word);
    }
}
