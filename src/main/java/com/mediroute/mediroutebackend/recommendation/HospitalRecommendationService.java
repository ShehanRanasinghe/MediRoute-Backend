// WHAT: Spring service that loads Hospital rows from Supabase and runs Task 4 recommendation.

// WHY: HospitalRecommender is a plain algorithm class with no JPA knowledge. This service is the
//      bridge: it converts persistent Hospital + NetworkNode rows into HospitalProfile objects,
//      then delegates to HospitalRecommender so HTTP requests never touch the scoring math.

// HOW: Annotated @Service so Spring injects HospitalRepository. recommend() calls findAll(),
//      maps each entity (including the EAGER NetworkNode) into a profile, and returns the
//      RecommendationResult for the REST controller.

package com.mediroute.mediroutebackend.recommendation;

import com.mediroute.mediroutebackend.common.models.Hospital;
import com.mediroute.mediroutebackend.common.models.NetworkNode;
import com.mediroute.mediroutebackend.common.models.repository.HospitalRepository;
import com.mediroute.mediroutebackend.recommendation.model.HospitalProfile;
import com.mediroute.mediroutebackend.recommendation.model.RecommendationRequest;
import com.mediroute.mediroutebackend.recommendation.model.RecommendationResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalRecommendationService {

    private final HospitalRepository hospitalRepository;
    private final HospitalRecommender hospitalRecommender;

    public HospitalRecommendationService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalRecommender = new HospitalRecommender();
    }

    public RecommendationResult recommend(RecommendationRequest request) {
        List<Hospital> rows = hospitalRepository.findAll();
        List<HospitalProfile> profiles = new ArrayList<>();
        for (Hospital row : rows) {
            profiles.add(toProfile(row));
        }
        return hospitalRecommender.recommend(request, profiles);
    }

    private HospitalProfile toProfile(Hospital hospital) {
        NetworkNode node = hospital.getNode();
        String name = node != null ? node.getName() : "Hospital " + hospital.getId();
        double latitude = node != null ? node.getLatitude() : 0.0;
        double longitude = node != null ? node.getLongitude() : 0.0;
        return new HospitalProfile(
                hospital.getId(),
                name,
                hospital.getSpecialty(),
                latitude,
                longitude,
                hospital.getTotalBeds(),
                hospital.getAvailableBeds(),
                hospital.getTotalIcuBeds(),
                hospital.getAvailableIcuBeds()
        );
    }
}
