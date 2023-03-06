package com.mauricio.springsecurity.component.model;

import static java.lang.Boolean.FALSE;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public class Deletable extends BaseEntity {
	
	@Column(nullable = false, name = "deleted")
	protected Boolean deleted;
	
	public Deletable() {
		deleted = FALSE;
	}

}
