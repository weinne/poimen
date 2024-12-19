package br.com.poimen.repository.search;

import br.com.poimen.domain.WorshipEvent;
import br.com.poimen.repository.WorshipEventRepository;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link WorshipEvent} entity.
 */
public interface WorshipEventSearchRepository extends ElasticsearchRepository<WorshipEvent, Long>, WorshipEventSearchRepositoryInternal {}

interface WorshipEventSearchRepositoryInternal {
    Page<WorshipEvent> search(String query, Pageable pageable);

    Page<WorshipEvent> search(Query query);

    @Async
    void index(WorshipEvent entity);

    @Async
    void deleteFromIndexById(Long id);
}

class WorshipEventSearchRepositoryInternalImpl implements WorshipEventSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final WorshipEventRepository repository;

    WorshipEventSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, WorshipEventRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<WorshipEvent> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<WorshipEvent> search(Query query) {
        SearchHits<WorshipEvent> searchHits = elasticsearchTemplate.search(query, WorshipEvent.class);
        List<WorshipEvent> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(WorshipEvent entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), WorshipEvent.class);
    }
}
