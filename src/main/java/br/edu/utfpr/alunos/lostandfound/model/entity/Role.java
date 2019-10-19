package br.edu.utfpr.alunos.lostandfound.model.entity;

public enum Role {
  
	ADMIN(1, "ROLE_ADMIN"), USER(2, "ROLE_USER"), MANAGER(3, "ROLE_MANAGER");

    private Integer code;
    private String description;
    
    Role(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static Role toEnum(Integer code) {
        if (code != null) {
            for (Role profile : Role.values()) {
                if (code.equals(profile.code)) {
                    return profile;
                }
            }
        }
        throw new IllegalArgumentException("Código de perfil invalido");
    }
}
