package lab.space.my_house_24_rest.repository;

import lab.space.my_house_24_rest.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long>, JpaSpecificationExecutor<Message> {

}
