package com.example.mom_birthday_site.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="solution")
public class Solution {
    @Id
    public Long id;

    public String siteSolution;
    public boolean used;

    public Solution(Long id, String siteSolution, boolean used) {
        this.id = id;
        this.siteSolution = siteSolution;
        this.used = used;
    }

}
