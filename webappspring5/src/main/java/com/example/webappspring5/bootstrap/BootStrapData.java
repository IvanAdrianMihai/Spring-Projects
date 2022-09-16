package com.example.webappspring5.bootstrap;

import com.example.webappspring5.domain.Author;
import com.example.webappspring5.domain.Book;
import com.example.webappspring5.domain.Publisher;
import com.example.webappspring5.repositories.AuthorRepository;
import com.example.webappspring5.repositories.BookRepository;
import com.example.webappspring5.repositories.PublisherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    public BootStrapData(AuthorRepository authorRepository, BookRepository bookRepository, PublisherRepository publisherRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Start in Bootstrap");

        Publisher publisher = new Publisher();
        publisher.setName("SFG Publishing");
        publisher.setCity("St Petersburg");
        publisher.setState("FL");

        publisher = publisherRepository.save(publisher);

        System.out.println("Publisher Count:" + publisherRepository.count());

        Author eric = new Author("Eric", "Evans");
        Book book = new Book("Domain Driven Design", "12141");
        eric.getBooks().add(book);
        book.getAuthors().add(eric);

        book.setPublisher(publisher);
        publisher.getBooks().add(book);

        authorRepository.save(eric);
        bookRepository.save(book);
        publisherRepository.save(publisher);

        Author rod = new Author("Rod", "Johnson");
        Book book2 = new Book("My Book 2", "1561");
        rod.getBooks().add(book2);
        book2.getAuthors().add(rod);

        book2.setPublisher(publisher);
        publisher.getBooks().add(book2);

        authorRepository.save(rod);
        bookRepository.save(book2);
        publisherRepository.save(publisher);

        System.out.println("Number of Books: " + bookRepository.count());
        System.out.println("Number of Authors: " + authorRepository.count());
        System.out.println("Publisher Number of Books: " + publisher.getBooks().size());
    }
}
