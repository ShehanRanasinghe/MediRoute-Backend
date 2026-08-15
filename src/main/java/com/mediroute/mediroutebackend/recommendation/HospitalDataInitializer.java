// WHAT: Startup seeder that inserts the two Colombo sample hospitals when the hospital table is empty.

// WHY: Task 4's REST endpoint cannot recommend anything without hospital rows. Task 1 may have
//      created network nodes, but the hospital table (specialty + beds) is new. This initializer
//      makes POST /api/recommendations usable immediately without waiting for Task 2 seed scripts.

// HOW: ApplicationRunner runs once after Spring Boot starts. If HospitalRepository.count() is 0,
//      it creates City Hospital (CARDIAC) and General Hospital (TRAUMA) with GPS matching the
//      existing seed-data.sql, then links each to a NetworkNode of type HOSPITAL.

package com.mediroute.mediroutebackend.recommendation;

import com.mediroute.mediroutebackend.common.models.Hospital;
import com.mediroute.mediroutebackend.common.models.NetworkNode;
import com.mediroute.mediroutebackend.common.models.NodeType;
import com.mediroute.mediroutebackend.common.models.repository.HospitalRepository;
import com.mediroute.mediroutebackend.common.models.repository.NetworkNodeRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class HospitalDataInitializer implements ApplicationRunner {

    private final HospitalRepository hospitalRepository;
    private final NetworkNodeRepository networkNodeRepository;

    public HospitalDataInitializer(HospitalRepository hospitalRepository,
                                   NetworkNodeRepository networkNodeRepository) {
        this.hospitalRepository = hospitalRepository;
        this.networkNodeRepository = networkNodeRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (hospitalRepository.count() > 0) {
            return;
        }

        NetworkNode cityNode = networkNodeRepository.save(
                new NetworkNode("City Hospital", NodeType.HOSPITAL, 6.9271, 79.8612));
        NetworkNode generalNode = networkNodeRepository.save(
                new NetworkNode("General Hospital", NodeType.HOSPITAL, 6.9400, 79.8750));

        hospitalRepository.save(new Hospital(cityNode, "CARDIAC,GENERAL", 120, 34, 12, 3));
        hospitalRepository.save(new Hospital(generalNode, "TRAUMA,GENERAL", 200, 58, 20, 7));
    }
}
