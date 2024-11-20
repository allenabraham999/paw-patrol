package com.simplogics.base.specifications;

import com.simplogics.base.entity.Image;
import com.simplogics.base.entity.Location;
import com.simplogics.base.entity.Report;
import com.simplogics.base.entity.User;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReportSpecification {

    //Haversine formula
    public static Specification<Report> filterReport(Float latitude, Float longitude, Float distance, List<String>dogStates, List<String> dogMentalStates, Long userId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Report, Location> locationJoin = root.join("location", JoinType.LEFT);
            Join<Report, Image> imageJoin = root.join("image", JoinType.LEFT);
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));  // Fixed: using user.id
            }
            // Add distance calculation if coordinates are provided
            if (latitude != null && longitude != null && distance != null) {
                // Convert the SQL distance formula to Criteria API
                Expression<Double> latRadians = criteriaBuilder.function("radians", Double.class, locationJoin.get("latitude"));
                Expression<Double> longRadians = criteriaBuilder.function("radians", Double.class, locationJoin.get("longitude"));
                Expression<Double> providedLatRadians = criteriaBuilder.function("radians", Double.class, criteriaBuilder.literal(latitude));
                Expression<Double> providedLongRadians = criteriaBuilder.function("radians", Double.class, criteriaBuilder.literal(longitude));

                // Calculate the differences
                Expression<Double> longDiff = criteriaBuilder.diff(longRadians, providedLongRadians);

                // Build the haversine formula components
                Expression<Double> cos1 = criteriaBuilder.function("cos", Double.class, providedLatRadians);
                Expression<Double> cos2 = criteriaBuilder.function("cos", Double.class, latRadians);
                Expression<Double> cosLongDiff = criteriaBuilder.function("cos", Double.class, longDiff);
                Expression<Double> sin1 = criteriaBuilder.function("sin", Double.class, providedLatRadians);
                Expression<Double> sin2 = criteriaBuilder.function("sin", Double.class, latRadians);

                // Multiply components
                Expression<Double> cosProduct = criteriaBuilder.prod(cos1, cos2);
                Expression<Double> cosLongProduct = criteriaBuilder.prod(cosProduct, cosLongDiff);
                Expression<Double> sinProduct = criteriaBuilder.prod(sin1, sin2);

                // Add the products
                Expression<Double> sum = criteriaBuilder.sum(cosLongProduct, sinProduct);

                // Calculate acos and multiply by Earth's radius (6371 km)
                Expression<Double> acos = criteriaBuilder.function("acos", Double.class, sum);
                Expression<Double> distanceCalc = criteriaBuilder.prod(6371.0, acos);

                // Convert the Float distance to a Double expression
                Expression<Double> distanceThreshold = criteriaBuilder.literal(distance.doubleValue());
                predicates.add(criteriaBuilder .lessThan(distanceCalc, distanceThreshold));
            }

            // Add dog states filter if provided
            if (dogStates != null && !dogStates.isEmpty()) {
                predicates.add(root.get("dogState").in(dogStates));
            }

            // Add dog mental states filter if provided
            if (dogMentalStates != null && !dogMentalStates.isEmpty()) {
                predicates.add(root.get("dogMentalState").in(dogMentalStates));
            }

            // Ensure we're selecting the Report entity
            query.distinct(true);

            // If you need to select specific fields
            if (query.getResultType() == Object[].class) {
                query.multiselect(
                        root.get("id"),
                        root.get("user").get("id"),  // Fixed: changed from userId to user.id
                        root.get("breed"),
                        root.get("description"),
                        root.get("dogSize"),
                        root.get("aggression"),      // Fixed: changed from aggressionLevel to aggression
                        root.get("image"),
                        locationJoin.get("latitude").alias("latitude"),  // Changed from "lat"
                        locationJoin.get("longitude").alias("longitude") // Changed from "long"
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
