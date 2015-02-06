package governator.junit.demo;

import com.google.inject.Inject;
import com.netflix.governator.annotations.AutoBindSingleton;
import com.netflix.governator.annotations.Configuration;
import governator.junit.demo.dao.BlogDao;
import governator.junit.demo.model.BlogEntry;
import governator.junit.demo.service.BlogService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@AutoBindSingleton(baseClass = BlogService.class)
public class DefaultBlogService implements BlogService {
    private final BlogDao blogDao;

    @Configuration("blogservice.name")
    private String blogServiceName;

    @Inject
    public DefaultBlogService(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    @Override
    public BlogEntry get(long id) {
        return this.blogDao.findById(id);
    }

    public String getBlogServiceName() {
        return this.blogServiceName;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Post-construct called!!");
    }
    @PreDestroy
    public void preDestroy() {
        System.out.println("Pre-destroy called!!");
    }
}
