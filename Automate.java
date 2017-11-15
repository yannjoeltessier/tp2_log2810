import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Automate
{
    ArrayList<String> codesPostaux = new ArrayList<String>();

    public void creerArbreAdresses(String fichier)
    {
        lireFichier(fichier);
    }

    private void lireFichier(String fichier)
    {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString() + "/src/autres/" + fichier;
        Path path = Paths.get(s);

        if(!Files.exists(path) || !Files.isRegularFile(path) || !Files.isReadable(path) || !Files.isExecutable(path)) {
            System.out.println("Nom de fichier ou chemin invalide!\n");
        } else {
            try {
                List<String> lines = Files.readAllLines(path);

                for (String line : lines) {
                    codesPostaux.add(line);
                }
            } catch (IOException e) {}
        }
    }
}
