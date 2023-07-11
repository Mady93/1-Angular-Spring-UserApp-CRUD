package com.springcrud.user.controllersDue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.EntityLinks;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import com.springcrud.user.repositories.UserRepository;
import com.springcrud.user.apiDetails.ApiResponse;
import com.springcrud.user.apiDetails.ApiResponseDelete;
import com.springcrud.user.entities.User;
import com.springcrud.user.exceptionsDue.ResourceNotFoundExceptionDue;

import javax.validation.Valid;
import org.springframework.web.bind.MethodArgumentNotValidException;

// page
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping(path = "/spring-rest-api", produces = "application/json")
@CrossOrigin(origins = "http://localhost:4200") // angular home page
public class UserControllerDue {

	private final UserRepository userRepo;

	@Autowired
	EntityLinks entityLinks;

	@Autowired
	public UserControllerDue(UserRepository userRepo) {
		this.userRepo = userRepo;
	}




	@PostMapping(path = "/users/create", consumes = "application/json")
	public ResponseEntity<Object> postUser(@RequestBody @Valid User user)
			throws MethodArgumentNotValidException, IllegalArgumentException {
		User savedUser = userRepo.save(user);
		String message = "User created successfully";
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse(HttpStatus.CREATED.value(), message, savedUser));
	}




	@GetMapping("/users/list")
	public ResponseEntity<List<User>> getUsers() throws ResourceNotFoundExceptionDue, IllegalArgumentException {

		List<User> userList = (List<User>) userRepo.findAll();
		return Optional.of(userList)
				.filter(list -> !list.isEmpty())
				.map(list -> new ResponseEntity<>(list, HttpStatus.OK))
				.orElseThrow(() -> new ResourceNotFoundExceptionDue(
						"Unable to retrieve the list. No users resource was found in the database"));
	}




	@GetMapping("/users/count")
	public ResponseEntity<Integer> countUser() throws ResourceNotFoundExceptionDue, IllegalArgumentException {
		Long count = userRepo.count();
		if (count == 0) {
			throw new ResourceNotFoundExceptionDue(
					"Unable to perform the count. No users resource was found in the database");
		} else {
			return new ResponseEntity<>(count.intValue(), HttpStatus.OK);
		}
	}




	@GetMapping(path = "/users/all")

	public ResponseEntity<List<User>> getUsers(@RequestParam("page") int page, @RequestParam("size") int size)
			throws ResourceNotFoundExceptionDue, IllegalArgumentException {
		Pageable pageable = PageRequest.of(page, size, Sort.by("name").descending());
		Page<User> pagedResult = userRepo.findAll(pageable);
		if (pagedResult.hasContent()) {
			return new ResponseEntity<>(pagedResult.getContent(), HttpStatus.OK);
		} else {
			throw new ResourceNotFoundExceptionDue(
					"Unable to retrieve the page: "+page+". No users resource was found in the database");
		}
	}




	@GetMapping(path = "/users/{id:\\d+}/one")
	public ResponseEntity<User> getUserById(@PathVariable("id") Long id)
			throws ResourceNotFoundExceptionDue, IllegalArgumentException {

		if (id == null || (id.equals(""))) {
			throw new IllegalArgumentException("Invalid URL");
		}

		Optional<User> optionalUser = userRepo.findById(id);
		User user = optionalUser.orElseThrow(
				() -> new ResourceNotFoundExceptionDue("Unable to retrieve the resource. The user resource with ID: " + id
						+ " was not found in the database"));
		return ResponseEntity.ok().body(user);
	}




	@PutMapping(path = "/users/{id:\\d+}/put", consumes = "application/json")
	public ResponseEntity<Object> putUser(@PathVariable("id") Long id, @RequestBody @Valid User user)
			throws ResourceNotFoundExceptionDue, MethodArgumentNotValidException, IllegalArgumentException {

		if (id == null || (id.equals(""))) {
			throw new IllegalArgumentException("Invalid URL");
		}

		Optional<User> optionalUser = userRepo.findById(id);
		if (optionalUser.isPresent()) {
			User existingUser = optionalUser.get();
			existingUser.setName(user.getName());
			existingUser.setEmail(user.getEmail());

			User userUpdated = userRepo.save(existingUser);

			String message = "User updated successfully";
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ApiResponse(HttpStatus.OK.value(), message, userUpdated));
		} else {
			throw new ResourceNotFoundExceptionDue("Unable to perform the modification. The user resource with ID: " + id
					+ " was not found in the database");
		}
	}




	@PatchMapping(path = "/users/{id:\\d+}/patch", consumes = "application/json")
	public ResponseEntity<Object> patchUser(@PathVariable("id") Long id, @RequestBody User patch)
			throws ResourceNotFoundExceptionDue, IllegalArgumentException, MethodArgumentNotValidException {

		if (id == null || (id.equals(""))) {
			throw new IllegalArgumentException("Invalid URL");
		}

		Optional<User> optionalUser = userRepo.findById(id);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();

			if (patch.getName() != null && patch.getName() != "") {
				user.setName(patch.getName());
			}
			if (patch.getEmail() != null && patch.getEmail() != "") {
				user.setEmail(patch.getEmail());
			}

			User updatedUser = userRepo.save(user);
			String message = "User patching successfully";
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ApiResponse(HttpStatus.OK.value(), message, updatedUser));
		} else {
			throw new ResourceNotFoundExceptionDue("Unable to perform the modification. The user resource with ID: " + id
					+ " was not found in the database");
		}
	}




	@DeleteMapping(path = "/users/{id:\\d+}/delete")
	public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id)
			throws ResourceNotFoundExceptionDue, IllegalArgumentException {

		if (id == null || (id.equals(""))) {
			throw new IllegalArgumentException("Invalid URL");
		}

		Optional<User> userOptional = userRepo.findById(id);
		if (userOptional.isPresent()) {
			userRepo.deleteById(id);

			String message = "User deleted successfully";
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ApiResponseDelete(HttpStatus.OK.value(), message));

		} else {
			throw new ResourceNotFoundExceptionDue("Unable to perform the deletion. The user resource with ID: " + id
					+ " was not found in the database");
		}
	}


	

}
