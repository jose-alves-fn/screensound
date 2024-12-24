package br.com.alura.screensound.principal;

import br.com.alura.screensound.model.Artista;
import br.com.alura.screensound.model.Musica;
import br.com.alura.screensound.model.TipoArtista;
import br.com.alura.screensound.repository.SoundRepository;
import br.com.alura.screensound.service.ConsultaChatGPT;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private SoundRepository repositorio;

    public Principal(SoundRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {

        var opcao = -1;

        while (opcao!= 9) {
            var menu = """
                    *** Screen Sound Músicas ***
              
                    1- Cadastrar artistas
                    2- Cadastrar músicas
                    3- Listar músicas
                    4- Buscar músicas por artistas
                    5- Pesquisar dados sobre um artista
                   
                    9 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtistas();
                    break;
                case 2:
                    cadastrarMusicas();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4:
                    buscarMusicasPorArtista();
                    break;
                case 5:
                    pesquisarDadosDoArtista();
                    break;
                case 9:
                    System.out.println("Encerrando a aplicação!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    // Método não funcionará porque não temos uma APIKey do ChatGPT
    private void pesquisarDadosDoArtista() {
        System.out.println("Pesquisar dados sobre qual artista? ");
        var nomeArtista = leitura.nextLine();
        var resposta = ConsultaChatGPT.obterInformacao(nomeArtista);
        System.out.println(resposta.trim());
    }

    // É ótimo pegar a prática de validar usando um Optional, dessa forma já verifica se o artista consta no banco
    private void buscarMusicasPorArtista() {
        System.out.println("Qual artista você deseja pesquisar as músicas? ");
        var nomeArtista = leitura.nextLine();
        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nomeArtista);

        if (artista.isPresent()) {
            var artistaEncontrado = artista.get().getNome();
            List<Musica> musicasEncontradas = repositorio.buscaMusicaPorArtista(artistaEncontrado);
            musicasEncontradas.forEach(System.out::println);
        } else {
            System.out.println("Artista não encontrado!");
        }
    }

    private void listarMusicas() {
        List<Artista> artistas = repositorio.findAll();
        artistas.forEach(a ->
                a.getMusicas().forEach(System.out::println));
    }

    private void cadastrarMusicas() {
        System.out.println("Cadastrar música para qual artista? ");
        var nomeArtista = leitura.nextLine();
        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nomeArtista);
        if (artista.isPresent()) {
            System.out.println("Qual o título da música? ");
            var nomeMusica = leitura.nextLine();

            // Criando um objeto musica, pegando os objetos e setando os dados para persistencia
            Musica musica = new Musica(nomeMusica);
            musica.setArtista(artista.get());
            artista.get().getMusicas().add(musica);
            repositorio.save(artista.get());

        } else {
            System.out.println("Artista não encontrado(a).");
        }
    }

    private void cadastrarArtistas() {

        var cadastrarNovo = "S";

        while(cadastrarNovo.equalsIgnoreCase("S")) {
            System.out.println("Qual artista você deseja cadastrar? ");
            var nome = leitura.nextLine();
            System.out.println("Qual o tipo do artista? (solo, dupla ou banda)");
            var tipo = leitura.nextLine();

            TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase()); // Forma simples de usar o Enum
            Artista artista = new Artista(nome, tipoArtista);
            repositorio.save(artista);

            System.out.println("Cadastrar outro artista? [S/N]");
            cadastrarNovo = leitura.nextLine();
        }
    }
}
