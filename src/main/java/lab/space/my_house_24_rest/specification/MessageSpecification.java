package lab.space.my_house_24_rest.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lab.space.my_house_24_rest.entity.Message;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;


@Builder
@EqualsAndHashCode
public class MessageSpecification implements Specification<Message> {
    private Long id;


    @Override
    public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        query.distinct(true);
        Predicate predicate =
                criteriaBuilder.equal(root.get("users").get("id"), id);
        query.orderBy(criteriaBuilder.desc(root.get("id")));
        return predicate;


    }
}
