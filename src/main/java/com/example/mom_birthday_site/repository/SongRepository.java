package com.example.mom_birthday_site.repository;

import com.example.mom_birthday_site.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByIsFoundFalse(); // מחזיר רק את השירים שעוד לא זוהו
    long countByIsFoundTrue(); // סופר כמה שירים כבר זוהו
}