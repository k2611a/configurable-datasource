package lv.k2611a.app.domain;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.repository.CrudRepository;

@RefreshScope
public interface UserRepository extends CrudRepository<User, Long> {

}
