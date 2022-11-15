package rs.ftn.uns.btb.model.dto;

import lombok.Getter;
import lombok.Setter;

public class UserLoginDTO {
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String password;

    public UserLoginDTO() {}

    public UserLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
