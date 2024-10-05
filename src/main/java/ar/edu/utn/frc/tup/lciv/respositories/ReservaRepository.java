package ar.edu.utn.frc.tup.lciv.respositories;

import ar.edu.utn.frc.tup.lciv.entities.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {
}