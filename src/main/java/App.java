import model.GenreResponse;
import model.MovieDetail;
import model.MovieResponse;
import model.VideoResponse;
import service.MovieService;
import service.MovieServiceImpl;
import utils.TableRenderer;

import java.util.Scanner;

public class App {

    private static final MovieService service = new MovieServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);
    private static int pageNumber = 1;
    private static int totalPages = 1;
    private static String query = "";

    private static void updatePageNumber(int pageNum) {
        if (pageNum < 1) {
            pageNumber = 1;
            return;
        }
        if (pageNum > totalPages) {
            pageNumber = totalPages;
            return;
        }
        pageNumber = pageNum;
    }


    public static void main(String[] args) {
            String RESET = "\u001B[0m";
            String COLOR = "\u001B[95m";
            System.out.println(COLOR + """
                ███╗   ███╗ ██████╗ ██╗   ██╗██╗███████╗     ██████╗██╗███╗   ██╗███████╗███╗   ███╗ █████╗   \s
                ████╗ ████║██╔═══██╗██║   ██║██║██╔════╝    ██╔════╝██║████╗  ██║██╔════╝████╗ ████║██╔══██╗  \s
                ██╔████╔██║██║   ██║██║   ██║██║█████╗      ██║     ██║██╔██╗ ██║█████╗  ██╔████╔██║███████║  \s
                ██║╚██╔╝██║██║   ██║╚██╗ ██╔╝██║██╔══╝      ██║     ██║██║╚██╗██║██╔══╝  ██║╚██╔╝██║██╔══██║  \s
                ██║ ╚═╝ ██║╚██████╔╝ ╚████╔╝ ██║███████╗    ╚██████╗██║██║ ╚████║███████╗██║ ╚═╝ ██║██║  ██║  \s
                ╚═╝     ╚═╝ ╚═════╝   ╚═══╝  ╚═╝╚══════╝     ╚═════╝╚═╝╚═╝  ╚═══╝╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝  \s
                """ + RESET);

            mainMenu();
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("""
                    [ 1 ]  Search Movie By Title
                    [ 2 ]  Search Movie By Genre
                    [ 3 ]  Exit
                    """);
            System.out.print("[!] Choose an option: ");
            String option = scanner.nextLine().trim();

            switch (option.toLowerCase()) {
                case "1" -> {
                    System.out.print("[!] Enter movie title: ");
                    query = scanner.nextLine().trim();
                    pageNumber = 1;
                    moviePageMenu();
                }
                case "2" -> showMoviesByGenre();
                case "3"  -> System.exit(0);
                default   -> System.out.println("[!] Invalid option.");
            }
        }
    }

    private static void moviePageMenu() {
        while (true) {
            MovieResponse response = service.searchMovies(query, pageNumber);
            totalPages = response.getTotalPages();

            TableRenderer.displayMovieTable(response, service);

            System.out.printf("Page %d / %d \n", pageNumber, totalPages);
            System.out.println("""
                    [n]  Next page
                    [p]  Previous page
                    [g]  Go to page
                    [md] Movie details
                    [b]  Back to main menu
                    [e]  Exit
                    """);
            System.out.print("[!] Choose an option: ");
            String op = scanner.nextLine().trim();

            switch (op.toLowerCase()) {
                case "n"  -> updatePageNumber(pageNumber + 1);
                case "p"  -> updatePageNumber(pageNumber - 1);
                case "g"  -> {
                    System.out.print("[!] Enter page number: ");
                    int page = Integer.parseInt(scanner.nextLine().trim());
                    updatePageNumber(page);
                }
                case "md" -> showMovieDetail();
                case "b" -> mainMenu();
                case "e"  -> System.exit(0);
                default   -> System.out.println("[!] Invalid option.");
            }
        }
    }

    private static void showMoviesByGenre() {

        GenreResponse genreResponse = service.getGenres();
        TableRenderer.displayGenreTable(genreResponse);

        System.out.print("[!] Enter genre ID: ");
        int genreId = Integer.parseInt(scanner.nextLine().trim());
        pageNumber = 1;

        while (true) {
            MovieResponse response = service.searchMoviesByGenre(genreId, pageNumber);
            totalPages = response.getTotalPages();

            TableRenderer.displayMovieTable(response, service);
            System.out.printf("Page %d / %d \n", pageNumber, totalPages);
            System.out.println("""
                    [n]  Next page
                    [p]  Previous page
                    [g]  Go to page
                    [md] Movie details
                    [b]  Back to main menu
                    [e]  Exit
                    """);
            System.out.print("Choose an option: ");
            String op = scanner.nextLine().trim();

            switch (op.toLowerCase()) {
                case "n"  -> updatePageNumber(pageNumber + 1);
                case "p"  -> updatePageNumber(pageNumber - 1);
                case "g"  -> {
                    System.out.print("[!] Enter page number: ");
                    int page = Integer.parseInt(scanner.nextLine().trim());
                    updatePageNumber(page);
                }
                case "md" -> showMovieDetail();
                case "b"  -> mainMenu();
                case "e"  -> System.exit(0);
                default   -> System.out.println("[!] Invalid option.");
            }
        }
    }

    private static void showMovieDetail() {
        System.out.print("[!] Enter movie ID: ");
        int movieId = Integer.parseInt(scanner.nextLine().trim());

        MovieDetail detail = service.getMovieDetail(movieId);

        String trailerUrl = "N/A";
        VideoResponse videoResponse = service.getMovieVideos(movieId);
        if (videoResponse.getResults() != null) {
            trailerUrl = videoResponse.getResults().stream()
                    .filter(v -> "Trailer".equals(v.getType()) && "YouTube".equals(v.getSite()))
                    .findFirst()
                    .map(v -> "https://youtube.com/watch?v=" + v.getKey())
                    .orElse("N/A");
        }

        TableRenderer.displayMovieDetail(detail, trailerUrl);

        System.out.print("\nPress Enter to go back...");
        scanner.nextLine();
    }

}

