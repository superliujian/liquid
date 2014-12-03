package liquid.facade;

import liquid.domain.LocationForm;
import liquid.domain.LocationType;
import liquid.persistence.domain.LocationEntity;
import liquid.pinyin4j.PinyinHelper;
import liquid.service.LocationService;
import liquid.validation.FormValidationResult;
import liquid.web.domain.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by redbrick9 on 7/9/14.
 */
@Service
public class LocationFacade {

    @Autowired
    private LocationService locationService;

    @Autowired
    protected MessageSource messageSource;

    public Iterable<LocationEntity> save(LocationForm location) {
        List<LocationEntity> entities = new ArrayList<>();
        for (Integer type : location.getTypes()) {
            LocationEntity entity = new LocationEntity(location.getName(), type);
            String queryName = PinyinHelper.converterToFirstSpell(location.getName()) + ";" + location.getName();
            entity.setQueryName(queryName);
            entities.add(entity);
        }
        return locationService.save(entities);
    }

    public Iterable<Location> findByNameLike(String name) {
        Iterable<LocationEntity> entities = locationService.findByNameLike(name);
        List<Location> locations = new ArrayList<>();
        for (LocationEntity entity : entities) {
            Location location = convert(entity);
            locations.add(location);
        }
        return locations;
    }

    private Location convert(LocationEntity entity) {
        Location location = new Location();
        location.setId(entity.getId());
        location.setName(entity.getName());
        location.setType(entity.getType());
        location.setTypeText(messageSource.getMessage(LocationType.valueOf(location.getType()).getI18nKey(), new Object[0], Locale.CHINA));
        return location;
    }

    public Iterable<Location> findByTypeAndNameLike(Integer type, String name) {
        Iterable<LocationEntity> entities = locationService.findByTypeAndNameLike(type, name);
        List<Location> locations = new ArrayList<>();
        for (LocationEntity entity : entities) {
            Location location = convert(entity);
            locations.add(location);
        }
        return locations;
    }

    public FormValidationResult validateLocation(Long id, String name, Integer type) {
        if (null == id) {
            LocationEntity location = locationService.findByTypeAndName(type, name);
            if (null == location) return FormValidationResult.newFailure("invalid.location");
        }
        return FormValidationResult.newSuccess();
    }
}
