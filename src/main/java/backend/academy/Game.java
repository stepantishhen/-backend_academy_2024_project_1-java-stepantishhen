package backend.academy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.List;


@SuppressFBWarnings("IOI_USE_OF_FILE_STREAM_CONSTRUCTORS")
public class Game {
    private GameStartInterface gameStartInterface;
    private GameProcessInterface gameProcessInterface;
    private static final String WORDS_JSON_PATH = "src/main/resources/words.json";
    private final SecureRandom random;

    public Game(InputStream in, PrintStream out) {
        this.gameStartInterface = new GameStartInterface(in, out);
        this.gameProcessInterface = new GameProcessInterface(in, out);
        this.random = new SecureRandom();
    }

    public void start() throws Exception {
        gameStartInterface.render();
        Integer category = gameStartInterface.selectedCategory();
        Integer difficulty = gameStartInterface.selectedDifficultyLevel();
        List<String> word = getRandomWordByDifficultyAndCategory(category, difficulty);
        gameProcessInterface.render(word);
    }

    protected List<String> getRandomWordByDifficultyAndCategory(Integer category, Integer difficulty) {
        String randomWord = null;
        String hint = null;
        File file = new File(WORDS_JSON_PATH);
        try (InputStream is = new FileInputStream(file)) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(is);
            JsonNode categoryNode = rootNode.path(category.toString());
            JsonNode difficultyNode = categoryNode.path(difficulty.toString());

            List<JsonNode> words = mapper.convertValue(difficultyNode, List.class);
            if (!words.isEmpty()) {
                int randomIndex = random.nextInt(words.size());
                JsonNode wordNode = mapper.convertValue(words.get(randomIndex), JsonNode.class);
                randomWord = wordNode.get(0).asText();
                hint = wordNode.get(1).asText();
            }
        } catch (Exception e) {
            System.err.println("Ошибка при чтении JSON файла: " + e.getMessage());
        }
        assert randomWord != null;
        assert hint != null;
        return List.of(randomWord, hint);
    }
}

