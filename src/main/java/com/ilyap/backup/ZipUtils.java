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
    private static final String currentDate = now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));
    private static final String SOURCE_PATH = String.join(File.separator, "C:", "Users", "IlyaPukhov", "");
    private static final String BACKUP_PATH = String.join(File.separator, "D:", "BACKUPS");


    public static void createBackup(String... dirs) throws IOException {
        removeOlderBackups();

        Files.createDirectories(Path.of(BACKUP_PATH));
        File[] directories = collectDirectories(dirs);

        for (File dir : directories) {
            try (ZipFile zipfile = new ZipFile(getFinalPath(currentDate).toFile())) {
                zipfile.addFolder(dir);
            }
        }
    }

    private static void removeOlderBackups() throws IOException {
        File backupDir = new File(BACKUP_PATH);
        if (Objects.requireNonNull(backupDir.list()).length >= 4) {
            String previousDate = now().minusDays(7).format(DateTimeFormatter.ofPattern("dd-MM-yy"));
            Files.deleteIfExists(getFinalPath(previousDate));
        }
    }

    private static Path getFinalPath(String date) {
        return Path.of(BACKUP_PATH, "backup_" + date + ".zip");
    }

    private static File[] collectDirectories(String... dirs) {
        return Stream.of(dirs)
                .map(el -> new File(SOURCE_PATH + el))
                .toArray(File[]::new);
    }
}