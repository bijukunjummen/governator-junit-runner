package governator.junit.demo.dao;

import governator.junit.demo.model.BlogEntry;

public interface BlogDao {
    BlogEntry findById(long id);
}
