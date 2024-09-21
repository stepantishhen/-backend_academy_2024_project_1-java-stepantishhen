package backend.academy;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class GallowsStages {
    private final static String[] STAGES = {
        """
        ______
        |/   |
        |    O
        |   /|\\
        |    |
        |   / \\
        ---------
        """,
        """
        ______
        |/   |
        |    O
        |   /|\\
        |    |
        |   /
        ---------
        """,
        """
        ______
        |/   |
        |    O
        |   /|\\
        |    |
        |
        ---------
        """,
        """
        ______
        |/   |
        |    O
        |   /|\\
        |
        |
        ---------
        """,
        """
        ______
        |/   |
        |    O
        |   /|
        |
        |
        ---------
        """,
        """
        ______
        |/   |
        |    O
        |    |
        |
        |
        ---------
        """,
        """
        ______
        |/   |
        |    O
        |
        |
        |
        ---------
        """,
        """
        ______
        |/   |
        |
        |
        |
        |
        ---------
        """
    };

    protected static String getStage(int index) {
        return STAGES[index];
    }

    protected static int getTotalStages() {
        return STAGES.length;
    }
}
