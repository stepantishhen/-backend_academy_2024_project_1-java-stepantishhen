package backend.academy;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class GameProcessInterface {
    private final Scanner scanner;
    private final PrintStream out;
    @SuppressWarnings("checkstyle:MagicNumber")
    private Integer attempt = 7;
    private String avaliableLetters;
    private StringBuilder currentDisplay;

    public GameProcessInterface(InputStream in, PrintStream out) {
        this.scanner = new Scanner(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.out = out;
        this.avaliableLetters = "a b c d e f g h i j k l m n o p q r s t u v w x y z";
    }

    public void render(String currentWord) {
        out.println(
            """
            If you guess, you are closer to victory,
            if you try incorrectly, elements of the gallows will be added"""
        );
        hideWord(currentWord);
        out.println(currentDisplay.toString());
        boolean winner = false;
        while (attempt > 0 && !winner) {
            out.println(avaliableLetters);
            out.println("Current attempt: " + attempt);
            out.print("Write letter: ");
            Character letter = scanner.next().toLowerCase().charAt(0); // to lowerCase
            if (foundLetter(currentWord, letter)) {
                openLetterInWord(currentWord, letter);
            } else {
                attempt--;
            }
            drawGallows();
            out.println(currentDisplay.toString());
            avaliableLetters = avaliableLetters.replace(letter, ' ');

            // Check for win
            if (checkWin(currentWord)) {
                winner = true;
                out.println("You win!");
            }
        }
        if (!winner) {
            out.println("You lose!");
        }
    }

    @SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:MultipleStringLiterals"})
    private void drawGallows() {
        StringBuilder gallows = new StringBuilder();
        gallows.append("______\n");
        gallows.append("|/   |\n");
        if (attempt <= 6) {
            gallows.append("|    O\n");
        } else {
            gallows.append("|    \n");
        }
        if (attempt <= 5) {
            gallows.append("|   /|\\\n");
        } else {
            gallows.append("|    \n");
        }
        if (attempt <= 4) {
            gallows.append("|    |\n");
        } else {
            gallows.append("|    \n");
        }
        if (attempt <= 3) {
            gallows.append("|   / \\\n");
        } else {
            gallows.append("|    \n");
        }
        gallows.append("|\n");
        gallows.append("---------\n");
        out.println(gallows.toString());
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
