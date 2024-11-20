package com.simplogics.base.entity;

import com.simplogics.base.enums.DogAttendedState;
import com.simplogics.base.enums.DogMentalState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "reports")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Report extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //TODO: Add many to one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "breed")
    String breed;

    @Column(name = "description")
    String description;

    @Column(name = "dog_size")
    String dogSize;

    @Column(name = "aggression_level")
    String aggression;

    @Builder.Default
    @Column(name = "dog_attended_state")
    String dogState = DogAttendedState.UNATTENDED.name();

    @Builder.Default
    @Column(name = "dog_mental_state")
    String dogMentalState = DogMentalState.SAFE.name();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image")
    Image image;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location")
    Location location;
}
