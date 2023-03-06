package com.mauricio.springsecurity.model;

import com.mauricio.springsecurity.component.model.Deletable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "login")
public class Login extends Deletable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "login_generator")
	@SequenceGenerator(name="login_generator", sequenceName="login_id_seq", allocationSize=1)
	@Column(updatable=false, nullable=false, name = "id")
	private Long id;
	
	@Column(name = "username", unique = true, nullable = false, updatable = false)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@OneToOne
	@JoinColumn(nullable = false, name="role_id")
	private Role role; 
	
	@OneToOne(mappedBy = "login", fetch = FetchType.LAZY)
	private User user;
	
	public Login(String username, String password, Role role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
}