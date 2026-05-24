package com.example.mom_birthday_site.controller;

import com.example.mom_birthday_site.DTO.GuessRequest;
import com.example.mom_birthday_site.entity.Solution;
import com.example.mom_birthday_site.entity.Song;
import com.example.mom_birthday_site.entity.TasteGuess;
import com.example.mom_birthday_site.repository.SongRepository;
import com.example.mom_birthday_site.repository.TasteGuessRepository;
import com.example.mom_birthday_site.repository.SolutionRepository;
import com.example.mom_birthday_site.response.SongGuessResponse;
import com.example.mom_birthday_site.response.TasteGuessResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:3001", "https://happy-birthday-qj6e.onrender.com/","https://mom-birthday-song.onrender.com/"})
@RestController
@RequestMapping("/api")
public class MainController {
    public final SongRepository songRepository;
    public final TasteGuessRepository tasteGuessRepository;
    public final SolutionRepository solutionRepository;

    private final String ADMIN_PASSWORD = "Dmr31301356";

    @PostConstruct
    public void init() {
        LocalDateTime now = LocalDateTime.now().minusMinutes(7);
        TasteGuess tasteGuess = new TasteGuess(1L, "קבלי", now);
        tasteGuessRepository.save(tasteGuess);
        Solution solution = new Solution(1L, "546", false);
        solutionRepository.save(solution);

        // הוספת 3 השירים ל-DB
        songRepository.save(new Song("גוףונשמה", false));
        songRepository.save(new Song("תןליתפילה", false));
        songRepository.save(new Song("ואניתפילה", false));

        System.out.println(" מוכן לעבודה!");
    }

    public MainController(TasteGuessRepository tasteGuessRepository, SolutionRepository solutionRepository, SongRepository songRepository) {
        this.tasteGuessRepository = tasteGuessRepository;
        this.solutionRepository = solutionRepository;
        this.songRepository = songRepository;
    }

    @GetMapping("/reset-db")
    public String resetDatabase(@RequestParam String password) {
        // בדיקת אבטחה בסיסית
        if (!ADMIN_PASSWORD.equals(password)) {
            return "גישה נדחתה: סיסמה שגויה!";
        }

        try {
            // 1. איפוס הדאטה בייס (מחיקת כל הרשומות הקיימות)
            tasteGuessRepository.deleteAll();
            solutionRepository.deleteAll();
            songRepository.deleteAll();

            // 2. הזנת הנתונים מחדש
            LocalDateTime now = LocalDateTime.now().minusMinutes(7);

            TasteGuess tasteGuess = new TasteGuess(1L, "קבלי", now);
            tasteGuessRepository.save(tasteGuess);

            Solution solution = new Solution(1L, "546", false);
            solutionRepository.save(solution);

            songRepository.save(new Song("גוףונשמה", false));
            songRepository.save(new Song("תןליתפילה", false));
            songRepository.save(new Song("ואניתפילה", false));

            return "הדאטה בייס אותחל בהצלחה!";

        } catch (Exception e) {
            return "שגיאה במהלך האיפוס: " + e.getMessage();
        }
    }

    @GetMapping("/ping")
    public String ping() {
        return "OK";
    }

    @PatchMapping("/guess/{guess}")
    public ResponseEntity<TasteGuessResponse> sendGuess(@PathVariable String guess) {
        TasteGuess dbGuess = tasteGuessRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        LocalDateTime lastGuessTime = dbGuess.getLastGuessTime();
        LocalDateTime now = LocalDateTime.now();

        if (lastGuessTime != null && lastGuessTime.plusMinutes(5).isAfter(now)) {
            long secondsLeft = java.time.Duration.between(now, lastGuessTime.plusMinutes(5)).getSeconds();
            return ResponseEntity.ok().body(new TasteGuessResponse(secondsLeft, "U HAVE TO WAIT 5 MINUTES BETWEEN EVERY GUESS"));
        } else {
            if (dbGuess.getGuess().equalsIgnoreCase(guess.trim().replace('כ', 'ק'))) {
                return ResponseEntity.ok().body(new TasteGuessResponse("601", "CONGRATS!! UR GUESS IS RIGHT."));
            } else {
                dbGuess.setLastGuessTime(now);
                tasteGuessRepository.save(dbGuess);
                LocalDateTime updatedTime = LocalDateTime.now();
                long secondsLeft = java.time.Duration.between(updatedTime, now.plusMinutes(5)).getSeconds();
                return ResponseEntity.ok().body(new TasteGuessResponse(secondsLeft, "BAD GUESS. WAIT 5 MINUTES BEFORE THE NEXT ONE"));
            }
        }
    }

    @PatchMapping("/solution/{solution}")
    public ResponseEntity<TasteGuessResponse> sendSolution(@PathVariable String solution) {
        Solution dbSolution = solutionRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        if (dbSolution.isUsed()) {
            return ResponseEntity.ok().body(new TasteGuessResponse("BONUS ALREADY USED."));
        } else {
            if (solution != null && dbSolution.getSiteSolution().equalsIgnoreCase(solution.trim())) {
                TasteGuess dbGuess = tasteGuessRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Record not found"));

                dbGuess.setLastGuessTime(LocalDateTime.now().minusMinutes(5));
                dbSolution.setUsed(true);
                tasteGuessRepository.save(dbGuess);
                solutionRepository.save(dbSolution);
                return ResponseEntity.ok().body(new TasteGuessResponse("PROBLEM SOLVED!! U GOT ONE MORE GUESS."));
            } else return ResponseEntity.ok().body(new TasteGuessResponse("BAD SOLUTION. TRY AGAIN."));
        }
    }

    @GetMapping("/timeCheck")
    public ResponseEntity<TasteGuessResponse> checkTheTime() {
        TasteGuess dbGuess = tasteGuessRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dbLastGuess = dbGuess.getLastGuessTime();
        boolean allowed = false;
        long secondsLeft = 100000;

        if (now.isAfter(dbGuess.getLastGuessTime().plusMinutes(5))) {
            allowed = true;
            secondsLeft = 0;
        } else {
            secondsLeft = java.time.Duration.between(now, dbLastGuess.plusMinutes(5)).getSeconds();
        }
        return ResponseEntity.ok().body(new TasteGuessResponse(allowed, secondsLeft));
    }

    @PatchMapping("/song")
    public ResponseEntity<SongGuessResponse> guessSong(@RequestBody GuessRequest request) {
        String songName = request.getGuess();

        // שלב 1: שולף מה-DB את כל השירים שעדיין לא נמצאו
        List<Song> unreadSongs = songRepository.findByIsFoundFalse();
        System.out.println(songName);
        System.out.println(unreadSongs.size());
        for (Song song : unreadSongs) {
            System.out.println(song.getSongName());
        }

        // אם המערך ריק, זה אומר שכל השירים כבר נמצאו קודם לכן
        if (unreadSongs.isEmpty()) {
            return ResponseEntity.ok().body(new SongGuessResponse(false, "ALL SONGS ALREADY FOUND."));
        }

        // שלב 2: בודק בין אלה שלא נקראו האם זה אותו השם
        Song matchedSong = null;
        for (Song song : unreadSongs) {
            if (song.getSongName().equalsIgnoreCase(songName.trim())) {
                matchedSong = song;
                break;
            }
        }

        // שלב 3: אם לא נמצאה התאמה
        if (matchedSong == null) {
            return ResponseEntity.ok().body(new SongGuessResponse(false, "WRONG SONG OR ALREADY GUESSED."));
        }

        // שלב 4: במידה ויש התאמה, מסמנים אותו כזוהה ושומרים ב-DB
        matchedSong.setFound(true);
        songRepository.save(matchedSong);

        // בודקים כמה שירים זוהו בסך הכל עד עכשיו ישירות מה-DB
        long totalFoundCount = songRepository.countByIsFoundTrue();

        // שלב 5: אם מצאנו את כל 3 השירים
        if (totalFoundCount == 3) {
            return ResponseEntity.ok().body(new SongGuessResponse(
                    true,
                    "AMAZING! YOU FOUND ALL 3 SONGS.",
                    totalFoundCount,
                    "975" // הקוד הסודי
            ));
        }

        // אם נמצא שיר אבל עדיין לא הגיעו ל-3
        return ResponseEntity.ok().body(new SongGuessResponse(
                true,
                "GREAT GUESS! SONG FOUND.",
                totalFoundCount
        ));
    }

    @GetMapping("/songs-left")
    public ResponseEntity<Integer> getSongsLeft() {
        // אנחנו משתמשים במתודה שקיימת בטוח ומחסירים מ-3
        int foundCount = (int) songRepository.countByIsFoundTrue();
        int unreadCount = 3 - foundCount;

        return ResponseEntity.ok(unreadCount);
    }
}
/*

PUT (החלפה מלאה): משתמשים בו כשרוצים לשלוח את כל האובייקט מחדש. אם שלחת רק שם והשמטת את הטלפון, הטלפון עלול להימחק או להפוך ל-null (תלוי במימוש).
PATCH (עדכון חלקי): זו הבחירה הנפוצה והנכונה ביותר לעדכון פרופיל. אתה שולח רק את השדות שתרצה לשנות (למשל: רק את השם), ושאר השדות נשארים ללא שינוי.

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

   @GetMapping("/{id}") // שליפה (Read)
   public Task getTask(@PathVariable Long id) { ... }

   @PostMapping // יצירה (Create)
   public Task create(@RequestBody Task task) { ... }

   @PutMapping("/{id}") // עדכון מלא (Update/Replace)
   // מחליף את כל האובייקט בנתונים החדשים שנשלחו
   public Task update(@PathVariable Long id, @RequestBody Task task) { ... }

   @PatchMapping("/{id}") // עדכון חלקי (Partial Update)
   // מעדכן רק שדות ספציפיים (למשל רק את ה-Role או הסטטוס)
   public Task patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) { ... }

   @DeleteMapping("/{id}") // מחיקה (Delete)
   public void delete(@PathVariable Long id) { ... }
 */
