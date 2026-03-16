package gastos.app.controlegastos.enums;

public enum RoleEnum {
    ROLE_USER("User"),
    ROLE_ADMIN("Admin");

    private final String descricao;

    RoleEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

