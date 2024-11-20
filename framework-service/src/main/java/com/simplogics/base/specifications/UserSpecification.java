package com.simplogics.base.specifications;

import com.simplogics.base.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filterUsers(String search, List<Long> roleIds, Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
                Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern);
                predicates.add(criteriaBuilder.or(namePredicate, emailPredicate));
            }

            if (roleIds != null && !roleIds.isEmpty()) {
                predicates.add(root.join("userRoles").get("id").in(roleIds));
            }

            if(isActive != null){
                predicates.add(criteriaBuilder.equal(root.get("isActive"), isActive));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
