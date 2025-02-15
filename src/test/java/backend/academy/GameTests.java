package backend.academy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {

    private Game game;
    private GameProcessInterface gameProcessInterface;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        // Инициализация новой игры
        game = new Game(System.in, System.out);
    }

    @Test
    public void testGetRandomWordByDifficultyAndCategory() {
        // Тест на проверку получения случайного слова по сложности и категории
        List<String> wordWithHint = game.getRandomWordByDifficultyAndCategory(1, 1);
        assertNotNull(wordWithHint);
        assertEquals(2, wordWithHint.size());
        assertNotNull(wordWithHint.get(0)); // Проверка, что слово не null
        assertNotNull(wordWithHint.get(1)); // Проверка, что подсказка не null
    }

    @Test
    public void testCorrectDisplayUpdate() {
        GameProcessInterface gpi = new GameProcessInterface(System.in, System.out);
        gpi.hideWord("кот");
        gpi.openLetterInWord("кот", 'к');
        assertEquals("к _ _", gpi.currentDisplay().toString().trim());
    }

    @Test
    public void testCaseInsensitiveInput() {
        GameProcessInterface gpi = new GameProcessInterface(System.in, System.out);
        gpi.hideWord("кот");
        assertTrue(gpi.foundLetter("кот", 'К'));
        gpi.openLetterInWord("кот", 'к');
        assertEquals("к _ _", gpi.currentDisplay().toString().trim());
    }

    @Test
    public void testInvalidWordLengthStopsGame() {
        // Некорректное слово с длиной 1 символ
        List<String> word = List.of("", "Подсказка: некорректное слово");

        // Симуляция ввода
        ByteArrayInputStream in = new ByteArrayInputStream("к\n".getBytes(StandardCharsets.UTF_8));
        outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        gameProcessInterface = new GameProcessInterface(in, out);

        // Ожидается завершение игры с ошибкой
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gameProcessInterface.render(word);
        });
        assertEquals("Некорректное слово в словарике!", exception.getMessage());
    }

    // После превышения заданного количества попыток игра всегда возвращает поражение.
    @Test
    public void testGameEndsAfterMaxAttempts() throws Exception {
        // Симуляция ввода неправильных букв
        ByteArrayInputStream in = new ByteArrayInputStream("a\nb\nc\nd\ne\nf\ng\n".getBytes(StandardCharsets.UTF_8));
        outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        gameProcessInterface = new GameProcessInterface(in, out);

        // Симуляция слова "кот"
        List<String> word = List.of("кот", "Подсказка: домашнее животное");
        gameProcessInterface.render(word);

        // Ожидается, что игра завершится поражением после 10 попыток
        assertFalse(gameProcessInterface.winner());
        assertTrue(outContent.toString().contains("Ты проиграл!"));
    }
    // Состояние игры корректно изменяется при угадывании/не угадывании.
    @Test
    public void testGameEndsOnCorrectGuess() throws Exception {
        // Симуляция ввода правильных букв
        ByteArrayInputStream in = new ByteArrayInputStream("к\nо\nт\n".getBytes(StandardCharsets.UTF_8));
        outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        gameProcessInterface = new GameProcessInterface(in, out);

        // Симуляция слова "кот"
        List<String> word = List.of("кот", "Подсказка: домашнее животное");
        gameProcessInterface.render(word);

        // Ожидается, что игра завершится победой после отгадывания всех букв
        assertTrue(gameProcessInterface.winner());
        assertTrue(outContent.toString().contains("Ты выиграл! Поздравляю!"));
    }
    // Проверка, что при отгадывании ввод строки длиной больше чем 1 (опечатка) приводит к повторному вводу, без изменения состояния.
    @Test
    public void testIncorrectInputRePrompts() throws Exception {
        // Симуляция ввода неправильного символа
        ByteArrayInputStream in = new ByteArrayInputStream("йцу\nк\nо\nт\n".getBytes(StandardCharsets.UTF_8));
        outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        gameProcessInterface = new GameProcessInterface(in, out);

        // Симуляция слова "кот"
        List<String> word = List.of("кот", "Подсказка: домашнее животное");
        gameProcessInterface.render(word);

        // Ожидается, что игра запросит ввод буквы снова
        assertTrue(outContent.toString().contains("буква>>> "));
        // Проверка, что попытки не уменьшились, так как ввод был неверным
        assertEquals(7, gameProcessInterface.attempt());
    }
}
