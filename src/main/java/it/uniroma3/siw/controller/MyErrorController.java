package it.uniroma3.siw.controller;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController  {

	@GetMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
			System.out.println(statusCode);
			model.addAttribute("errorCode", statusCode);
			if(statusCode == HttpStatus.NOT_FOUND.value()) {
				return "error-404";
			}
			else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return "error-500";
			}
			else if(statusCode == HttpStatus.UNAUTHORIZED.value()) {
				return "error-500";
			}
		}
		return "error";
	}

}