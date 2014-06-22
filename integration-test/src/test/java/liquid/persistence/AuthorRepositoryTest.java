package liquid.persistence;

import liquid.config.JpaConfig;
import liquid.persistence.domain.Author;
import liquid.persistence.domain.Book;
import liquid.persistence.repository.AuthorRepository;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by redbrick9 on 6/1/14.
 */
public class AuthorRepositoryTest {

    @Test
    public void addAuthor() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();

        AuthorRepository authorRepository = context.getBean(AuthorRepository.class);
        Author author = new Author();
        author.setFirstName("Jack");
        author.setLastName("Bauer");
        Book book = new Book();
        book.setAuthor(author);
        book.setName("24");
        book.setPrice(70L);
        author.getBooks().add(book);
        authorRepository.save(author);
    }
}
