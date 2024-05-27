package it.uniroma3.siw.model;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;


//unicita su nome e chef
//nome not blank
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"nome","chef_id"}))
public class Recipe {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String nome;

	@Column(nullable = true)
	private String mainImage;

	@Column(nullable = true)
	private String extraImage1;

	@Column(nullable = true)
	private String extraImage2;

	@Column(nullable = true)
	private String extraImage3;

	@ManyToOne
	private Chef chef;

	@OneToMany (mappedBy = "recipe", cascade = {CascadeType.REMOVE})
	private List<UsedIngredient> usedIngredients;

	public List<UsedIngredient> getUsedIngredients() {
		return usedIngredients;
	}

	public void setUsedIngredients(List<UsedIngredient> usedIngredients) {
		this.usedIngredients = usedIngredients;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Chef getChef() {
		return chef;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}



	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public String getExtraImage1() {
		return extraImage1;
	}

	public void setExtraImage1(String extraImage1) {
		this.extraImage1 = extraImage1;
	}

	public String getExtraImage2() {
		return extraImage2;
	}

	public void setExtraImage2(String extraImage2) {
		this.extraImage2 = extraImage2;
	}

	public String getExtraImage3() {
		return extraImage3;
	}

	public void setExtraImage3(String extraImage3) {
		this.extraImage3 = extraImage3;
	}
	
	@Transient
	public String getMainImagePath() {
		if (mainImage == null) return null;
		return "/images/recipe/"+id+"/"+mainImage;
	}
	
	@Transient
	public String getExtraImagePath2() {
		if (extraImage2 == null) return null;
		return "/images/recipe/"+id+"/"+extraImage2;
	}
	
	@Transient
	public String getExtraImagePath1() {
		if (extraImage1 == null) return null;
		return "/images/recipe/"+id+"/"+extraImage1;
	}
	
	@Transient
	public String getExtraImagePath3() {
		if (extraImage3 == null) return null;
		return "/images/recipe/"+id+"/"+extraImage3;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(id, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe other = (Recipe) obj;
		return Objects.equals(id, other.id) && Objects.equals(nome, other.nome);
	}

}
