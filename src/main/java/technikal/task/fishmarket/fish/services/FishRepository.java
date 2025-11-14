package technikal.task.fishmarket.fish.services;

import org.springframework.data.jpa.repository.JpaRepository;

import technikal.task.fishmarket.fish.models.Fish;

public interface FishRepository extends JpaRepository<Fish, Integer> {

}
