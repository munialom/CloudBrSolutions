package com.ctecx.brsuite.users;


import com.ctecx.brsuite.branch.Branch;
import com.ctecx.brsuite.userroles.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name ="User")
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128, nullable = false , unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    public String phone;
    @Column(length = 6)
    private String pin;
    private String verificationCode;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    private boolean enabled;
    private boolean firstLogin;
    private LocalDateTime lastLogin;

    @ManyToMany(fetch =FetchType.EAGER)
    @JoinTable(name="user_roles",
            joinColumns =@JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name ="role_id"))
    private Set<Role> roles= new HashSet<>();


    public boolean hasRole(String roleName){

        Iterator<Role> roleIterator=roles.iterator();
        while(roleIterator.hasNext()){
            Role role=roleIterator.next();
            if(role.getRoleName().equals(roleName)){
                return true;
            }

        }
       return false;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
