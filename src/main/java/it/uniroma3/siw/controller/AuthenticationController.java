package it.uniroma3.siw.controller;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.validator.CredentialsValidator;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private ChefService chefService;

	@Autowired 
	private CredentialsValidator credentialsValidator;

	//ritorna a pagina register
	@GetMapping("/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("chef", new Chef());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser.html";
	}

	//ritorna a pagina login
	@GetMapping("/login") 
	public String showLoginForm (Model model) {
		return "formLogin.html";
	}


	//ritorna a index se non loggato, indexChef se sei loggato non admin, indexAdmin se sei admin
	@GetMapping("/") 
	public String index(Model model) {
		return "redirect:/recipe";
		
	}

	//pagina di ritorno al login andato bene, ritorna a / che ti restituisce pagine diverse in base al ruolo
	@GetMapping("/success")
	public String defaultAfterLogin(Model model) {
		return "redirect:/";
	}

	//post per la registrazione con chef, credentials, immagine
	@PostMapping("/register" )
	public String registerUser(@Valid @ModelAttribute("chef") Chef chef, BindingResult userBindingResult,
			@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
			Model model, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

		// se user e credential hanno entrambi contenuti validi, memorizza User e Credentials nel DB
		this.credentialsValidator.validate(credentials, credentialsBindingResult);
		if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {

			if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				this.chefService.save(chef);

				String newFileName = "chef"+chef.getId()+"."+MvcConfig.getExtension(fileName);
				chef.setPathImage(newFileName);
				this.chefService.save(chef);

				String uploadDir="./images/chef";

				MvcConfig.saveUploadFile(uploadDir, multipartFile, newFileName);
			}

			credentials.setUser(chef);
			credentialsService.saveCredentials(credentials);
			model.addAttribute("chef", chef);
			return "redirect:/login?success=true";
		}
		return "formRegisterUser.html";
	}
}