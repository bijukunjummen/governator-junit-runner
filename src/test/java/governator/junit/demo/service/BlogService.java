package governator.junit.demo.service;


import governator.junit.demo.model.BlogEntry;

public interface BlogService {
    public BlogEntry get(long id);
}
