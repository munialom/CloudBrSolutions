package com.ctecx.brsuite.branch;


import com.ctecx.brsuite.util.AuditableBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "branches")
public class Branch  extends AuditableBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String branchName;
    private String branchLocation;
    private String branchCode;
    private boolean isHeadQuarter;


}

