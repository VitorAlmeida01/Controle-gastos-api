package gastos.app.controlegastos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gastos.app.controlegastos.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleEnum nome;

    // Remover mapeamento bidirecional para evitar loop infinito
    // @ManyToMany(mappedBy = "roles")
    // @JsonIgnore
    // @ToString.Exclude
    // private Set<Usuario> usuarios = new HashSet<>();

    public Role(RoleEnum nome) {
        this.nome = nome;
    }
}

