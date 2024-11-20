package com.simplogics.base.entity;

import java.time.Instant;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@MappedSuperclass
@Data
public class BaseEntity {
	
	@Column(name = "created_at", updatable = false)
	protected Instant createdAt;

	@Column(name = "updated_at")
	protected Instant updatedAt;
	
	@OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "created_by")
	protected User createdBy;

	@JoinColumn(name = "updated_by")
	@OneToOne(targetEntity= User.class, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	protected User updatedBy;
}
