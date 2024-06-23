package it.uniroma3.siw.model;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Chef {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	private String surname;

	@Email
	private String email;

	@Past
	private LocalDate dateBirth;

	@Column(nullable = true)
	private String pathImage;

	@OneToMany(mappedBy = "chef")
	private List<Recipe> recipes;

	@OneToOne(cascade = CascadeType.REMOVE, mappedBy = "user")
	private Credentials credential;

	@PreRemove
	private void preRemove() {
		recipes.forEach(recipe -> recipe.setChef(null));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public LocalDate getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(LocalDate dateBirth) {
		this.dateBirth = dateBirth;
	}

	public String getPathImage() {
		return pathImage;
	}

	public void setPathImage(String pathImage) {
		this.pathImage = pathImage;
	}

	@Transient
	public String getPhotoImagePath() {
		if (pathImage == null) return null;
		return "/images/chef/"+pathImage;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}

	public Credentials getCredential() {
		return credential;
	}

	public void setCredential(Credentials credential) {
		this.credential = credential;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateBirth, id, name, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chef other = (Chef) obj;
		return Objects.equals(dateBirth, other.dateBirth) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(surname, other.surname);
	}
}
