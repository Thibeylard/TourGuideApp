package rewards.services;

import gps.services.GpsUtilService;
import models.dto.AttractionDTO;
import models.dto.VisitedLocationDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilServiceHttpImpl implements GpsUtilService {
    @Override
    public VisitedLocationDTO getUserLocation(UUID userId) {
        return null;
    }

    @Override
    public List<AttractionDTO> getAttractions() {
        return null;
    }
}
