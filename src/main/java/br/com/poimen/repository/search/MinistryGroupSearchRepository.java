package br.com.poimen.repository.search;

import br.com.poimen.domain.MinistryGroup;
import br.com.poimen.repository.MinistryGroupRepository;
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
 * Spring Data Elasticsearch repository for the {@link MinistryGroup} entity.
 */
public interface MinistryGroupSearchRepository
    extends ElasticsearchRepository<MinistryGroup, Long>, MinistryGroupSearchRepositoryInternal {}

interface MinistryGroupSearchRepositoryInternal {
    Page<MinistryGroup> search(String query, Pageable pageable);

    Page<MinistryGroup> search(Query query);

    @Async
    void index(MinistryGroup entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MinistryGroupSearchRepositoryInternalImpl implements MinistryGroupSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MinistryGroupRepository repository;

    MinistryGroupSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MinistryGroupRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<MinistryGroup> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<MinistryGroup> search(Query query) {
        SearchHits<MinistryGroup> searchHits = elasticsearchTemplate.search(query, MinistryGroup.class);
        List<MinistryGroup> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(MinistryGroup entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), MinistryGroup.class);
    }
}
