package gastos.app.controlegastos.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CategoriaEnum {
    MERCADO("Mercado"),
    TRANSPORTE("Transporte"),
    ALIMENTACAO("Alimentação"),
    SAUDE("Saúde"),
    LAZER("Lazer"),
    EDUCACAO("Educação"),
    MORADIA("Moradia"),
    OUTROS("Outros");

    private final String descricao;

    CategoriaEnum(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static CategoriaEnum fromDescricao(String descricao) {
        if (descricao == null) {
            return null;
        }
        for (CategoriaEnum tipo : CategoriaEnum.values()) {
            if (tipo.descricao.equalsIgnoreCase(descricao) ||
                tipo.name().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de categoria inválido: " + descricao);
    }
}
