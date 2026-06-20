import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sn.fouadi.entities.Book;
import sn.fouadi.entities.Category;
import sn.fouadi.repositories.BookRepository;
import sn.fouadi.repositories.CategoryRepository;
import sn.fouadi.repositories.JpaUtil;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryTest {

    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final BookRepository bookRepository = new BookRepository();

    @BeforeEach
    void setUp() {
        JpaUtil.init();
    }

    @AfterEach
    void tearDown() {
        JpaUtil.close();
    }

    @Test
    void shouldSaveAndFindBook() {
        Category category = Category.builder()
                .name("Informatique")
                .state(true)
                .build();

        Category savedCategory = categoryRepository.save(category);

        Book book = Book.builder()
                .isbn("978-1-234")
                .title("Java pour débutants")
                .author("Fouad")
                .publicationYear(2024)
                .countPages(320)
                .category(savedCategory)
                .build();

        Book savedBook = bookRepository.save(book);

        assertNotNull(savedBook.getId());
        assertEquals("978-1-234", bookRepository.findByIsbn("978-1-234").getIsbn());
    }
}
