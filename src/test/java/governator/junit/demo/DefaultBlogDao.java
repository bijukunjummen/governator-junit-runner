package governator.junit.demo;

import com.netflix.governator.annotations.AutoBindSingleton;
import governator.junit.demo.dao.BlogDao;
import governator.junit.demo.model.BlogEntry;

@AutoBindSingleton(baseClass = BlogDao.class)
public class DefaultBlogDao implements BlogDao {

    @Override
    public BlogEntry findById(long id) {
        return new BlogEntry(1l, "Test");
    }
}
