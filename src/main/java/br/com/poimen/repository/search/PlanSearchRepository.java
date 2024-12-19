package br.com.poimen.repository.search;

import br.com.poimen.domain.Plan;
import br.com.poimen.repository.PlanRepository;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Plan} entity.
 */
public interface PlanSearchRepository extends ElasticsearchRepository<Plan, Long>, PlanSearchRepositoryInternal {}

interface PlanSearchRepositoryInternal {
    Stream<Plan> search(String query);

    Stream<Plan> search(Query query);

    @Async
    void index(Plan entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PlanSearchRepositoryInternalImpl implements PlanSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PlanRepository repository;

    PlanSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PlanRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Plan> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Plan> search(Query query) {
        return elasticsearchTemplate.search(query, Plan.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Plan entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Plan.class);
    }
}
