package com.Hindol.Uber.Service.Implementation;

import com.Hindol.Uber.Service.DistanceService;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class DistanceServiceOSRMImplementation implements DistanceService {
    private static final String OSRM_API_BASE_URL="https://router.project-osrm.org/route/v1/driving/";
    @Override
    public Double calculateDistance(Point src, Point dest) {
        try {
            String uri = src.getX()+","+src.getY()+";"+dest.getX()+","+dest.getY();
            OSRMResponseDTO responseDTO = RestClient.builder().baseUrl(OSRM_API_BASE_URL)
                    .build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(OSRMResponseDTO.class);
            return responseDTO.getRoutes().get(0).getDistance() / 1000.0;
        }
        catch (Exception e) {
            throw new RuntimeException("OSRM Error - " + e.getMessage());
        }
    }
}

@Data
class OSRMResponseDTO {
    private List<OSRMRoute> routes;
}

@Data
class OSRMRoute {
    private Double distance;
}
