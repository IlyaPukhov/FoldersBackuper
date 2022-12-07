package com.ilyap.backup;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.ilyap.backup.ZipUtils.FORMATTER;

@UtilityClass
public class OlderBackups {

    public String getOlderDate(String backupPath) throws IOException {
        List<String> files = getFileNames(backupPath);
        List<LocalDate> dates = new ArrayList<>();

        for (String file : files) {
            dates.add(getDateFromFileName(file));
        }

        LocalDate minDate = dates.stream()
                .min(Comparator.naturalOrder())
                .orElse(null);

        return minDate == null ? "" : minDate.format(FORMATTER);
    }

    private List<String> getFileNames(String backupPath) throws IOException {
        List<String> files;
        try (Stream<Path> paths = Files.walk(Path.of(backupPath))) {
            files = paths.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .toList();
        }
        return files;
    }

    private LocalDate getDateFromFileName(String file) {
        Pattern pattern = Pattern.compile("\\d{2}-\\d{2}-\\d{2}");
        Matcher matcher = pattern.matcher(file);
        if (matcher.find()) {
            return LocalDate.parse(matcher.group(), FORMATTER);
        }
        return null;
    }
}
