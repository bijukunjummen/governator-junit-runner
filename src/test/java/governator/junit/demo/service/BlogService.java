package governator.junit.demo.service;


import governator.junit.demo.model.BlogEntry;

public interface BlogService {
    BlogEntry get(long id);
    String getBlogServiceName();

}
