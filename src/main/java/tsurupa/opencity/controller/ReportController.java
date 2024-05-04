package tsurupa.opencity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsurupa.opencity.model.Event;
import tsurupa.opencity.model.User;
import tsurupa.opencity.model.Report;
import tsurupa.opencity.model.utils.EntityType;
import tsurupa.opencity.repository.UserRepository;
import tsurupa.opencity.repository.ReportRepository;
import tsurupa.opencity.repository.EventRepository;
import tsurupa.opencity.service.CheckPermission;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/add/event/{id}")
    public ResponseEntity<?> addReport(@PathVariable("id") long id, @RequestBody Report report, @RequestHeader String token) {
        try {
            // Проверяем существование пользователя
            User user = userRepository.findByEmail(CheckPermission.tokenDecryption(token)[0]).orElse(null);
            if (user == null || !CheckPermission.auth(userRepository, token)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Отказано в доступе");
            }

            // Создаем новый отчет
            Report newReport = new Report();
            newReport.setDescription(report.getDescription());
            newReport.setUser(user);
            newReport.setType(EntityType.event);

            // Добавляем связанные события, если они указаны
            Event event = eventRepository.findById(id).orElse(null);
            if (event != null) {
                newReport.setEntityId(id);
            }else{
                return ResponseEntity.status(HttpStatus.CREATED).body("event не найден");
            }



            // Сохраняем отчет
            Report savedReport = reportRepository.save(newReport);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при добавлении отчета: " + e.getMessage());
        }
    }

    @GetMapping("/getall/event/{eventId}")
    public ResponseEntity<?> getReportsByEventId(@PathVariable("eventId") Long eventId, @RequestHeader String token) {
        try {
            // Проверяем существование события
            Event event = eventRepository.findById(eventId).orElse(null);
            if (event == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Событие с указанным ID не найдено");
            }
            if(!CheckPermission.himself_moderator(userRepository, event.getUser(), token)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Отказано в доступе");
            }
            // Получаем все отчеты для данного события
            List<Report> reports = reportRepository.findAllByType(EntityType.event);
            for (int i = reports.size() - 1; i >= 0; i--) {
                if (reports.get(i).getEntityId() != eventId) {
                    reports.remove(i);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при поиске отчетов для события: " + e.getMessage());
        }
    }

    @DeleteMapping("/event/{reportId}")
    public ResponseEntity<?> deleteReportById(@PathVariable("reportId") Long reportId, @RequestHeader("token") String token) {
        try {
            // Получаем отчет по его идентификатору
            Report report = reportRepository.findById(reportId).orElse(null);
            if (report == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Отчет с указанным ID не найден");
            }

            // Получаем пользователя из токена
            User user = userRepository.findByEmail(CheckPermission.tokenDecryption(token)[0]).orElse(null);

            Optional<Event> event = eventRepository.findById(report.getEntityId());
            // Проверяем, является ли пользователь владельцем события, к которому привязан отчет
            if (!CheckPermission.himself_moderator(userRepository, event.get().getUser(), token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Недостаточно прав для удаления отчета");
            }

            // Удаляем отчет
            reportRepository.delete(report);
            return ResponseEntity.status(HttpStatus.OK).body("Отчет успешно удален");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении отчета: " + e.getMessage());
        }
    }
}
