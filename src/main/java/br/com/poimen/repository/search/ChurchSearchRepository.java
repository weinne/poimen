package br.com.poimen.repository.search;

import br.com.poimen.domain.Church;
import br.com.poimen.repository.ChurchRepository;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Church} entity.
 */
public interface ChurchSearchRepository extends ElasticsearchRepository<Church, Long>, ChurchSearchRepositoryInternal {}

interface ChurchSearchRepositoryInternal {
    Stream<Church> search(String query);

    Stream<Church> search(Query query);

    @Async
    void index(Church entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ChurchSearchRepositoryInternalImpl implements ChurchSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ChurchRepository repository;

    ChurchSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ChurchRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Church> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Church> search(Query query) {
        return elasticsearchTemplate.search(query, Church.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Church entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Church.class);
    }
}
