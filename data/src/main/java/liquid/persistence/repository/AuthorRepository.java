package liquid.persistence.repository;

import liquid.persistence.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 6/1/14.
 */
public interface AuthorRepository extends CrudRepository<Author, Long>, JpaRepository<Author, Long> {}
