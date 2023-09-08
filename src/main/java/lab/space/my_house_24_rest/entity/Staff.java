package lab.space.my_house_24_rest.entity;

import jakarta.persistence.*;
import lab.space.my_house_24_rest.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "staff")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 25, nullable = false)
    private String firstname;

    @Column(length = 25, nullable = false)
    private String lastname;
    
    @Column(length = 25, nullable = false)
    private String phone;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 250)
    private String token;

    @Column(length = 250)
    private String forgotToken;

    @Column
    private Boolean theme;

    @Column
    private Boolean tokenUsage;

    @Column
    private Boolean forgotTokenUsage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private UserStatus staffStatus;

    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "staff")
    private List<CashBox> cashBoxList = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "staff_house",
            joinColumns = @JoinColumn(name = "staff_id"),
            inverseJoinColumns = @JoinColumn(name = "house_id")
    )
    private List<House> houseList = new ArrayList<>();

    @OneToMany(mappedBy = "staff")
    private List<MastersApplication> applicationList = new ArrayList<>();

}
