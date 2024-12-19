package br.com.poimen.repository.search;

import br.com.poimen.domain.Hymn;
import br.com.poimen.repository.HymnRepository;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Hymn} entity.
 */
public interface HymnSearchRepository extends ElasticsearchRepository<Hymn, Long>, HymnSearchRepositoryInternal {}

interface HymnSearchRepositoryInternal {
    Stream<Hymn> search(String query);

    Stream<Hymn> search(Query query);

    @Async
    void index(Hymn entity);

    @Async
    void deleteFromIndexById(Long id);
}

class HymnSearchRepositoryInternalImpl implements HymnSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final HymnRepository repository;

    HymnSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, HymnRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Hymn> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Hymn> search(Query query) {
        return elasticsearchTemplate.search(query, Hymn.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Hymn entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Hymn.class);
    }
}
