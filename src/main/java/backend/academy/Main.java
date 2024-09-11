package backend.academy;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j @UtilityClass
public class Main {
    public static void main(String[] args) {
        Game game = new Game(System.in, System.out);
        game.start();
    }
}
