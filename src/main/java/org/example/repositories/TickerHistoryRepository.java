package org.example.repositories;

import org.example.domain.TickerHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "tickerhistories", path = "tickerhistories")
public interface TickerHistoryRepository extends PagingAndSortingRepository<TickerHistory, Long> {

	Page<TickerHistory> findAll(Pageable pageable);

	@RestResource(exported = false)
	TickerHistory findById(Long id);
	
	 @RestResource(path = "stockTicker", rel = "stockTicker")
	 Page<TickerHistory> findByTickerIgnoreCase(@Param("q") String ticker, Pageable pageable);
}