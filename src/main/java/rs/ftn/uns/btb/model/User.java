package rs.ftn.uns.btb.model;

import lombok.Getter;
import lombok.Setter;
import rs.ftn.uns.btb.model.dto.UserUpdateDTO;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("user")
public class User extends Person{

    @Column(name = "profession", nullable = true)
    @Getter @Setter
    private String profession;

    @Column(name = "job", nullable = true)
    @Getter @Setter
    private String job;

    @Column(name = "penalty", nullable = true)
    @Getter @Setter
    private Integer penalty;

    public User() {}



    public void copyValuesFromDTO(UserUpdateDTO userUpdateDTO) {
        this.setJmbg((userUpdateDTO.getJmbg()));
        this.setFirstName(userUpdateDTO.getFirstName());
        this.setLastName(userUpdateDTO.getLastName());
        this.setPassword(userUpdateDTO.getPassword());
        //this.setEmail(userUpdateDTO.getEmail());
        this.setGender(userUpdateDTO.getGender());
        this.setPhone(userUpdateDTO.getPhone());
        this.setAddress(userUpdateDTO.getAddress());
        this.setCity(userUpdateDTO.getCity());
        this.setCountry(userUpdateDTO.getCountry());
    }

}
