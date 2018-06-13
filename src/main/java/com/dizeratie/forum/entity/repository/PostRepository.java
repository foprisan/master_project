kage com.dizeratie.forum.entity.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dizeratie.forum.entity.Post;
import com.dizeratie.forum.entity.Topic;
import com.dizeratie.forum.entity.User;


public interface PostRepository extends JpaRepository<Post, Integer> {
    
    Set<Post> findByUser(User user);
    
    Set<Post> findByTopic(Topic topic);
    
    Set<Post> findAllByOrderByCreationDateDesc();
    
    Set<Post> findTop5ByOrderByCreationDateDesc();
}
