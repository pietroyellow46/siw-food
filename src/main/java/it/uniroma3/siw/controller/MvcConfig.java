package it.uniroma3.siw.controller;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	//metodo statico che restituisce l'estensione di un file come stringa
	public static String getExtension(String filename) {
		int lastDotIndex = filename.lastIndexOf('.');
		return filename.substring(lastDotIndex + 1);
	}

	//metodo statico che prende il path relativo alla cartella del progetto e il nome su come salvare file passato
	public static void saveUploadFile(String uploadDir,MultipartFile multipartFile, String filename) throws IOException {

		Path uploadPath = Paths.get(uploadDir);

		//se non esiste crea directory
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try(InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = uploadPath.resolve(filename);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException("Could not save upload file: " + filename);
		}
	}


	//metodo statico che elimina file con path passato
	public static boolean deleteFile(String filePath) {
		Path path = Paths.get(filePath);
		try {
			Files.delete(path);
			return true;
		} catch (NoSuchFileException e) {
			System.err.println("Il file non esiste: " + filePath);
		} catch (IOException e) {
			System.err.println("Errore durante l'eliminazione del file: " + filePath);
			e.printStackTrace();
		}
		return false;
	}

	//metodo che permette al framework di avere visibilità delle cartelle in cui sono le immagini
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path ingredientUploadDir = Paths.get("./images/ingredient");
		String ingredientUploadPath = ingredientUploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/images/ingredient/**").addResourceLocations("file:/"+ingredientUploadPath+"/");

		Path recipeUploadDir = Paths.get("./images/recipe");
		String recipeUploadPath = recipeUploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/images/recipe/**").addResourceLocations("file:/"+recipeUploadPath+"/");

		Path chefUploadDir = Paths.get("./images/chef");
		String chefUploadPath = chefUploadDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/images/chef/**").addResourceLocations("file:/"+chefUploadPath+"/");
	}
}