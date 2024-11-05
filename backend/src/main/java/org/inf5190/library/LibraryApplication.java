
package org.inf5190.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@SpringBootApplication
@PropertySource("classpath:firebase.properties")
public class LibraryApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryApplication.class);

    @Value("${firebase.project.id}")
    private String firebaseProjectId;

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Bean
    Firestore getFirestore() throws IOException {
        if (FirebaseApp.getApps().size() == 0) {

            String projectId = Optional.ofNullable(System.getenv("GOOGLE_CLOUD_PROJECT"))
                    .orElse(this.firebaseProjectId);

            final FirebaseOptions.Builder optionsBuilder =
                    FirebaseOptions.builder().setProjectId(projectId);

            File f = new File("firebase-key.json");
            if (f.exists()) {
                FileInputStream serviceAccount = new FileInputStream("firebase-key.json");
                optionsBuilder.setCredentials(GoogleCredentials.fromStream(serviceAccount));
            } else {
                optionsBuilder.setCredentials(GoogleCredentials.getApplicationDefault());
            }

            LOGGER.info("Initializing Firebase application.");
            FirebaseApp.initializeApp(optionsBuilder.build());

        } else {
            LOGGER.info("Firebase application already initialized.");
        }

        return FirestoreClient.getFirestore();
    }
}
