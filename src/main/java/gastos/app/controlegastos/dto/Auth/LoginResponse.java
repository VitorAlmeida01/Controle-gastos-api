package gastos.app.controlegastos.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tipo = "Bearer";
    private String email;
    private String nome;

    public LoginResponse(String token, String email, String nome) {
        this.token = token;
        this.email = email;
        this.nome = nome;
    }
}

