package com.photoalbum.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Controller
public class AppErrorController implements ErrorController {

	private ErrorAttributes errorAttributes;

	private final static String ERROR_PATH = "/error";

	public AppErrorController(ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}

	@RequestMapping(value = ERROR_PATH, produces = "text/html")
	public String errorHtml(HttpServletRequest request, Model model) {
		Map<String, Object> errorMap = getErrorAttributes(request, true);

		Object trace = errorMap.get("trace");
		if (trace != null){
			model.addAttribute("stackTrace", trace);
			errorMap.remove("trace");
		}

		model.addAttribute("errorMap", errorMap);

		return "errorView";

	}

	@RequestMapping(value = ERROR_PATH)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request, getTraceParameter(request));
		HttpStatus status = getStatus(request);
		return new ResponseEntity<Map<String, Object>>(body, status);
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

	private boolean getTraceParameter(HttpServletRequest request) {
		String parameter = request.getParameter("trace");
		if (parameter == null) {
			return false;
		}
		return !"false".equals(parameter.toLowerCase());
	}

	private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
		WebRequest requestAttributes = new ServletWebRequest(request);
		Map<String, Object> errorMap = errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
		return errorMap;
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode != null) {
			try {
				return HttpStatus.valueOf(statusCode);
			} catch (Exception ex) {
			}
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}