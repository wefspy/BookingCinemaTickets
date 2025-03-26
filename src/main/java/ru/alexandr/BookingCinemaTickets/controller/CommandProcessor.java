package ru.alexandr.BookingCinemaTickets.controller;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alexandr.BookingCinemaTickets.controller.exception.StringDeserializeException;
import ru.alexandr.BookingCinemaTickets.controller.mapper.MovieDeserializer;
import ru.alexandr.BookingCinemaTickets.domain.Movie;
import ru.alexandr.BookingCinemaTickets.service.MovieService;
import ru.alexandr.BookingCinemaTickets.service.dto.MovieDto;
import ru.alexandr.BookingCinemaTickets.service.exception.EntityNotFoundException;

import java.util.List;

@Component
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class CommandProcessor {
    private final MovieService movieService;

    public CommandProcessor(MovieService movieService) {
        this.movieService = movieService;
    }

    public void processCommand(String input)
    {
        String[] cmd = input.split(" ");
        switch (cmd[0])
        {
            case "/help" -> System.out.println("""
                    Доступыне команды:
                    /create [title:String] [description:String] [durationInMinutes:Integer] [releaseDate:LocalDateTime] [rating:Rating] [posterUrl:URL]
                    /find [id:Long]
                    /findAll
                    /delete [id:Long]
                    /update [id:Long] [title:String] [description:String] [durationInMinutes:Integer] [releaseDate:LocalDateTime] [rating:Rating] [posterUrl:URL]
                    """);
            case "/create" ->
            {
                try {
                    MovieDto movie = MovieDeserializer.toDto(cmd);
                    movieService.create(movie);

                    System.out.println("Фильм успешно создан...");
                } catch (StringDeserializeException e) {
                    System.err.println("Ошибка в обработке введенных данных " + e.getMessage());
                }
            }
            case "/find" -> {
                try {
                    Long movieId = Long.parseLong(cmd[1]);
                    Movie movie = movieService.findById(movieId);

                    System.out.println(movie);
                } catch (EntityNotFoundException e) {
                    System.err.println(e.getMessage());
                }
            }
            case "/findAll" -> {
                List<Movie> movies = movieService.findAll();

                movies.forEach(System.out::println);
            }
            case "/delete" -> {
                try {
                    Long movieId = Long.parseLong(cmd[1]);
                    movieService.delete(movieId);

                    System.out.println("Фильм успешно удален...");
                } catch (EntityNotFoundException e) {
                    System.err.println(e.getMessage());
                }
            }
            case "/update" -> {
                try {
                    Movie movie = MovieDeserializer.toMovie(cmd);
                    movieService.update(movie);

                    System.out.println("Фильм успешно обновлен...");
                } catch (StringDeserializeException e) {
                    System.err.println("Ошибка в обработке введенных данных " + e.getMessage());
                } catch (EntityNotFoundException e) {
                    System.err.println(e.getMessage());
                }
            }
            default -> System.out.println("Введена неизвестная команда...");
        }
    }

}
