package holge.shopping.loginservice.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import commons.dto.ApiResponse;
import commons.dto.UserDTO;
import commons.enums.ERol;
import holge.shopping.loginservice.exception.CustomGenericalException;
import holge.shopping.loginservice.payload.LoginResponse;
import holge.shopping.loginservice.security.JwtUtils;

@Service
public class LoginService {
	private static Logger log = LoggerFactory.getLogger(LoginService.class);
	
	private Map<String, CompletableFuture<ResponseEntity<String>>> futures = new HashMap<>();
	
	JwtUtils jwtUtils;
	RestTemplate restTemplate;
	Gson gson;
	
	public LoginService (JwtUtils jwtUtils, RestTemplate restTemplate, Gson gson) {
		this.jwtUtils = jwtUtils;
		this.restTemplate = restTemplate;
		this.gson = gson;
	}
	
	
	/**
	 * Validate one JWT
	 * @param jwt
	 * @return
	 */
	public boolean validJwt(String jwt) {
		return jwtUtils.validateJwtToken(jwt);		
	}

	/**
	 * Get the user of the JWT
	 * @param jwt
	 * @return
	 */
	public UserDTO getJwtDetails(String jwt) {
		UserDTO user = jwtUtils.getUserFromJwt(jwt);
		
		return user;
	}
	
	public ApiResponse login(String email, String password) {
		
		HashMap<String, String> user = new HashMap<>();
		user.put("email", email);
		user.put("password", password);
		
		ResponseEntity<String> responseFromUser;
		responseFromUser = restTemplate.postForEntity("http://user-service/api/user/login", user, String.class);
		
		ApiResponse apiResponse = gson.fromJson(responseFromUser.getBody(), ApiResponse.class);
		
		if (apiResponse.isError()) {
			log.error(apiResponse.getMsg());
			throw new CustomGenericalException(apiResponse.getMsg(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// We need parse the data becouse comes as LinkedHashMap
		UserDTO userDTO = gson.fromJson(gson.toJson(apiResponse.getData()), UserDTO.class);

		String tokenString = jwtUtils.generateJwtToken(userDTO);
		
		LoginResponse loginResponse = new LoginResponse(tokenString, userDTO.getName(), userDTO.getEmail(), userDTO.getRol()); 
		return new ApiResponse(false, "", loginResponse);
	}
	
}