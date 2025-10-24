package com.micro.membresiasservice.Repository;

import com.micro.membresiasservice.Model.PlanMembresia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<PlanMembresia, Long> {
}
