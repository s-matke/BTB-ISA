package rs.ftn.uns.btb.model.dto;

import lombok.Getter;
import lombok.Setter;

public class CenterUpdateDTO {

    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String address;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private Double grade;

    public CenterUpdateDTO() {}

    public CenterUpdateDTO(String name, String address, String description, Double grade) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.grade = grade;
    }
}
