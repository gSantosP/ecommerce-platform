package com.ecomm.auth.presentation;

import com.ecomm.auth.application.LoginUseCase;
import com.ecomm.auth.application.RegisterUserUseCase;
import com.ecomm.auth.domain.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(RegisterUserUseCase registerUseCase, LoginUseCase loginUseCase) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest req) {
        User user = registerUseCase.execute(req.email(), req.password(), req.name());
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        LoginUseCase.LoginResult result = loginUseCase.execute(req.email(), req.password());
        return ResponseEntity.ok(new LoginResponse(result.token(), UserResponse.from(result.user())));
    }

    public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6) String password,
        @NotBlank String name
    ) {}

    public record LoginRequest(
        @Email @NotBlank String email,
        @NotBlank String password
    ) {}

    public record LoginResponse(String token, UserResponse user) {}

    public record UserResponse(String id, String email, String name, String role) {
        public static UserResponse from(User u) {
            return new UserResponse(u.id().toString(), u.email(), u.name(), u.role());
        }
    }
}
