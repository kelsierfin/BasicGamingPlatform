import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayerDataManager {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void savePlayers(List<Player> players, String filename) throws IOException {
        mapper.writeValue(new File(filename), players);
    }

    public static List<Player> loadPlayers(String filename) throws IOException {
        return List.of(mapper.readValue(new File(filename), Player[].class));
    }
}
