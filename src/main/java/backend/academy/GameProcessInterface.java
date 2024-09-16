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
    private String avaliableLetters;
    private int attempt;
    @Getter private StringBuilder currentDisplay;
    @Getter private boolean winner;

    public GameProcessInterface(InputStream in, PrintStream out) {
        this.scanner =
            new Scanner(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.out = out;
        this.avaliableLetters =
            "а б в г д е ё ж з и й к л м н о п р с т у ф х ц ч ш щ ъ ы ь э ю я";
    }

    public void render(List<String> currentWord) throws Exception {
        attempt = ATTEMPTS;
        if (currentWord.getFirst().length() < attempt) {
            throw new IllegalArgumentException("Слово слишком короткое для игры");
        }
        out.println("""
            С каждой верной буквой ты приближаешься к победе,
            если буква введена неверно - я буду рисовать виселицу и висельника с каждым шагом"""
        );
        hideWord(currentWord.getFirst());
        out.println(currentDisplay.toString());
        winner = false;
        while (attempt > 0 && !winner) {
            out.println(avaliableLetters);
            if (attempt == SHOW_HINT_ON_ATTEMPT) {
                out.println(currentWord.get(1));
            }
            out.println("Попыток осталось: " + attempt);
            out.print("буква>>> ");
            Character letter =
                scanner.next().trim().toLowerCase().charAt(0); // to lowerCase
            if (foundLetter(currentWord.getFirst(), letter)) {
                openLetterInWord(currentWord.getFirst(), letter);
            } else {
                attempt--;
            }
            drawGallows();
            out.println(currentDisplay.toString());
            avaliableLetters = avaliableLetters.replace(letter, ' ');

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

    public void hideWord(String word) {
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

    public boolean foundLetter(String word, Character letter) {
        letter = letter.toString().toLowerCase().charAt(0);
        return word.toLowerCase().indexOf(letter) != -1;
    }

    public void openLetterInWord(String word, Character letter) {
        for (int i = 0; i < word.length(); i++) {
            if (letter.equals(Character.toLowerCase(word.charAt(i)))) {
                currentDisplay.setCharAt(i * 2, word.charAt(i));
            }
        }
    }

    public boolean checkWin(String word) {
        StringBuilder spam = new StringBuilder();
        for (int i = 0; i < currentDisplay.length(); i++) {
            if (currentDisplay.charAt(i) != ' ') {
                spam.append(currentDisplay.charAt(i));
            }
        }
        return spam.toString().equals(word);
    }
}
