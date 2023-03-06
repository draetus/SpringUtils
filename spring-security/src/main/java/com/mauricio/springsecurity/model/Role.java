package com.mauricio.springsecurity.model;

import java.util.Objects;

import com.mauricio.springsecurity.component.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role extends BaseEntity {
	
	@Id
	@Column(updatable=false, nullable=false, name = "id")
	private Long id;
	
	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}
	
	public Role(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	

}
