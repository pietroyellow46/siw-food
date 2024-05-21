package it.uniroma3.siw.controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	
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


   @Override
   public void addResourceHandlers(ResourceHandlerRegistry registry) {
      Path ingredientUploadDir = Paths.get("./images/ingredient");
	  String ingredientUploadPath = ingredientUploadDir.toFile().getAbsolutePath();
	  registry.addResourceHandler("/images/ingredient/**").addResourceLocations("file:/"+ingredientUploadPath+"/");
	  
	  
	  Path chefUploadDir = Paths.get("./images/chef");
	  String chefUploadPath = chefUploadDir.toFile().getAbsolutePath();
	  registry.addResourceHandler("/images/chef/**").addResourceLocations("file:/"+chefUploadPath+"/");
   }
   
   
}