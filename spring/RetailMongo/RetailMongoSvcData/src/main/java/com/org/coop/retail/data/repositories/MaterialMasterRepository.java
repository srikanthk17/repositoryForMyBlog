package com.org.coop.retail.data.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.coop.retail.data.entities.MaterialMaster;

@Repository
public interface MaterialMasterRepository extends MongoRepository<MaterialMaster, Integer> {
}
