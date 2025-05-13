package stackup.stack_snapshot_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StackSnapshotBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(StackSnapshotBackApplication.class, args);
	}

}
