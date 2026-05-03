package com.example.mom_birthday_site.repository;

import com.example.mom_birthday_site.entity.TasteGuess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TasteGuessRepository extends JpaRepository<TasteGuess, Long> {

}
