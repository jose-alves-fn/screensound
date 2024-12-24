package br.com.alura.screensound;

import br.com.alura.screensound.principal.Principal;
import br.com.alura.screensound.repository.SoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreensoundApplication implements CommandLineRunner {

	// A interface repositorio precisa ser gerenciada pelo Spring, portanto anotamos um @Autowired
	@Autowired
	private SoundRepository repositorio;

	// Criando uma instância de classe por meio de reflexão (ScreenmatchApplication.class)
	public static void main(String[] args) {
		SpringApplication.run(ScreensoundApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorio);
		principal.exibeMenu();
	}
}
