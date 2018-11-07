package com.prova.nexxera.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prova.nexxera.api.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long>{

	@Query(value="SELECT * FROM conta WHERE filial_id =?1 ORDER BY data_lancamento ASC", nativeQuery = true)
	List<Conta> findContaByFilialId(long idFilial);
	
}
