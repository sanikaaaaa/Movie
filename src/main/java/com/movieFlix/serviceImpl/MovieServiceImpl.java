package com.movieFlix.serviceImpl;


import com.movieFlix.dto.MovieDto;
import com.movieFlix.entity.MovieEntity;
import com.movieFlix.repository.MovieRepository;
import com.movieFlix.service.FileService;
import com.movieFlix.service.MovieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;


    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }


    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        // 1.Upload the file
        String uploadedFileName = fileService.uploadFile(path, file);

        //2.set the value of field 'poster' as filename
        movieDto.setPoster(uploadedFileName);


        //map dto to movie object
        MovieEntity movie = new MovieEntity(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        //4.save the movie object -> saved Movie object
        MovieEntity savedMovie = movieRepository.save(movie);

        //5.generate the poster url
        String posterUrl = baseUrl + "/file/" + uploadedFileName;

        //6.map movie object to dto object and return it

        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl


        );

        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        return null;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        return List.of();
    }
}