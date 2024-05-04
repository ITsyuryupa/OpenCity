package tsurupa.opencity.service;



import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tsurupa.opencity.model.Photo;
import tsurupa.opencity.model.User;
import tsurupa.opencity.model.utils.EntityType;
import tsurupa.opencity.repository.EventRepository;
import tsurupa.opencity.repository.PhotoRepository;
import tsurupa.opencity.model.Event;
import tsurupa.opencity.repository.UserRepository;


@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private EventRepository eventRepository;

    public Photo storePhotoEvent(MultipartFile file, Long id, User user) throws IOException {

        Optional<Event> event = eventRepository.findById(id);
        Photo photo = new Photo();
        photo.setData(file.getBytes());
        photo.setEntityId(id);
        photo.setType(EntityType.event);
        photo.setUser(user);

        return photoRepository.save(photo);
    }

    public Photo storePhotoCommunity(MultipartFile file, Long id, User user) throws IOException {

        Optional<Event> event = eventRepository.findById(id);
        Photo photo = new Photo();
        photo.setData(file.getBytes());
        photo.setEntityId(id);
        photo.setType(EntityType.community);
        photo.setUser(user);

        return photoRepository.save(photo);
    }

    public Photo getPhoto(String id) {
        return photoRepository.findById(id).get();
    }

    public List<String> getAllPhotoByEntityIdAndType(long id, int type) {
        return photoRepository.findAllByEntityIdAndType(id, EntityType.fromValue(type));
    }
    public void deletePhoto(String id) {
        photoRepository.deleteById(id);
    }
}

