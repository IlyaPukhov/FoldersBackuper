package com.ilyap.backup;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            ZipUtils.createBackup(
                    "IdeaProjects",
                    "Postman",
                    "source",
                    "Videos",
                    "Documents",
                    "Downloads",
                    "Pictures",
                    "Music",
                    "Desktop"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}