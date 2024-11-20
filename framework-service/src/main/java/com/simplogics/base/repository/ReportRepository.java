package com.simplogics.base.repository;

import com.simplogics.base.dto.ReportResponse;
import com.simplogics.base.entity.Report;
import com.simplogics.base.projections.ReportResponseView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> , JpaSpecificationExecutor<Report> {
    @Query(value = "select \n" +
            "    r.id as is,\n" +
            "    r.user_id as userId,\n" +
            "    r.breed as breed,\n" +
            "    r.description as description,\n" +
            "    r.dog_size as dogSize,\n" +
            "    r.agression_level as agressionLevel,\n" +
            "    r.image as image,\n" +
            "    l.lat AS latitude, \n" +
            "    l.long AS longitude\n" +
            "from \n" +
            "reports r \n" +
            "left join \"location\" l\n" +
            "on r.\"location\" = l.id \n" +
            "left join image i \n" +
            "on r.image  = i.id\n" +
            "where r.user_id = :userid\n" +
            "and \n" +
            "    6371 * acos(\n" +
            "        cos(radians(:latitude)) * cos(radians(l.lat)) *\n" +
            "        cos(radians(l.long) - radians(:longitude)) +\n" +
            "        sin(radians(:latitude)) * sin(radians(l.lat))\n" +
            "    ) < 2000000.0", nativeQuery = true)
    List<ReportResponseView> getReportsFilteredByLocation(@Param("userid") Long userId, @Param("longitude") Float longitude, @Param("latitude") Float latitude);


    @Query(value = "\n" +
            "select COUNT(*)\n" +
            "FROM\n" +
            "    reports r\n" +
            "LEFT JOIN \"location\" l ON r.location = l.id\n" +
            "LEFT JOIN image i ON r.image = i.id\n" +
            "WHERE\n" +
            "    r.user_id = :userid\n" +
            "AND 6371 * ACOS(\n" +
            "    COS(RADIANS(:latitude)) * COS(RADIANS(l.lat)) *\n" +
            "    COS(RADIANS(l.long) - RADIANS(:longitude)) +\n" +
            "    SIN(RADIANS(:latitude)) * SIN(RADIANS(l.lat))\n" +
            ") < :dist", nativeQuery = true)
    Long getCountOfReportsForAnAreaCustomDist(@Param("userid") Long userId, @Param("longitude") Float longitude, @Param("latitude") Float latitude, @Param("dist")Float dist);
}
