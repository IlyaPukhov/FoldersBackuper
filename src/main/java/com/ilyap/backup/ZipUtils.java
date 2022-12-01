package com.ilyap.backup;

import lombok.experimental.UtilityClass;
import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;

@UtilityClass
public class ZipUtils {
    private final String currentDate = now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));
    private final String SOURCE_PATH = String.join(File.separator, "C:", "Users", "IlyaPukhov", "");
    private final String BACKUP_PATH = String.join(File.separator, "D:", "BACKUPS");
    private final int LIMIT = 4;


    public void createBackup(String... dirs) throws IOException {
        removeOlderBackups();

        Files.createDirectories(Path.of(BACKUP_PATH));
        File[] directories = collectDirectories(dirs);

        for (File dir : directories) {
            try (ZipFile zipfile = new ZipFile(getFinalPath(currentDate).toFile())) {
                zipfile.addFolder(dir);
            }
        }
    }

    private void removeOlderBackups() throws IOException {
        File backupDir = new File(BACKUP_PATH);
        if (backupDir.list() != null && Objects.requireNonNull(backupDir.list()).length >= LIMIT) {
            String previousDate = now().minusDays(LIMIT).format(DateTimeFormatter.ofPattern("dd-MM-yy"));
            Files.deleteIfExists(getFinalPath(previousDate));
        }
    }

    private Path getFinalPath(String date) {
        return Path.of(BACKUP_PATH, "backup_" + date + ".zip");
    }

    private File[] collectDirectories(String... dirs) {
        return Stream.of(dirs)
                .map(el -> new File(SOURCE_PATH + el))
                .toArray(File[]::new);
    }
}