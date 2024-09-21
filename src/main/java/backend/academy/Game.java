package backend.academy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

@SuppressFBWarnings("IOI_USE_OF_FILE_STREAM_CONSTRUCTORS")
public class Game {
    private GameStartInterface gameStartInterface;
    private GameProcessInterface gameProcessInterface;
    private static final String WORDS_JSON_PATH = "words.json";
    private final SecureRandom random;
    private Map<Integer, Map<Integer, List<Word>>> wordDictionary;

    protected Game(InputStream in, PrintStream out) {
        this.gameStartInterface = new GameStartInterface(in, out);
        this.gameProcessInterface = new GameProcessInterface(in, out);
        this.random = new SecureRandom();
    }

    protected void start() throws Exception {
        gameStartInterface.render();
        Integer category = gameStartInterface.selectedCategory();
        Integer difficulty = gameStartInterface.selectedDifficultyLevel();
        List<String> word = getRandomWordByDifficultyAndCategory(category, difficulty);
        gameProcessInterface.render(word);
    }

    protected List<String> getRandomWordByDifficultyAndCategory(Integer category, Integer difficulty) {
        String randomWord = null;
        String hint = null;

        try (InputStream is = getFileFromResourceAsStream(WORDS_JSON_PATH)) {
            if (is == null) {
                throw new Exception("File not found: " + WORDS_JSON_PATH);
            }
            ObjectMapper mapper = new ObjectMapper();
            // Load the word dictionary here
            wordDictionary = mapper.readValue(is, new TypeReference<Map<Integer, Map<Integer, List<Word>>>>() {});

            List<Word> words = wordDictionary.get(category).get(difficulty);
            if (!words.isEmpty()) {
                int randomIndex = random.nextInt(words.size());
                Word selectedWord = words.get(randomIndex);
                randomWord = selectedWord.word();
                hint = selectedWord.hint();
            }
        } catch (Exception e) {
            System.err.println("Ошибка при чтении JSON файла: " + e.getMessage());
        }
        assert randomWord != null;
        assert hint != null;
        return List.of(randomWord, hint);
    }


    private InputStream getFileFromResourceAsStream(String fileName) {
        // The class loader that loaded the current class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        return inputStream;
    }
}

