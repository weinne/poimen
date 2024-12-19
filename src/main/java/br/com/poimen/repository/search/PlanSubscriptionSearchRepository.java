package br.com.poimen.repository.search;

import br.com.poimen.domain.PlanSubscription;
import br.com.poimen.repository.PlanSubscriptionRepository;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link PlanSubscription} entity.
 */
public interface PlanSubscriptionSearchRepository
    extends ElasticsearchRepository<PlanSubscription, Long>, PlanSubscriptionSearchRepositoryInternal {}

interface PlanSubscriptionSearchRepositoryInternal {
    Stream<PlanSubscription> search(String query);

    Stream<PlanSubscription> search(Query query);

    @Async
    void index(PlanSubscription entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PlanSubscriptionSearchRepositoryInternalImpl implements PlanSubscriptionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PlanSubscriptionRepository repository;

    PlanSubscriptionSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PlanSubscriptionRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<PlanSubscription> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<PlanSubscription> search(Query query) {
        return elasticsearchTemplate.search(query, PlanSubscription.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(PlanSubscription entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), PlanSubscription.class);
    }
}
